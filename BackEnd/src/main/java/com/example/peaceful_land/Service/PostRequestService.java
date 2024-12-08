package com.example.peaceful_land.Service;

import com.example.peaceful_land.DTO.PostApprovalResponse;
import com.example.peaceful_land.DTO.ResponseUserPostReqView;
import com.example.peaceful_land.Entity.*;
import com.example.peaceful_land.Repository.PostRepository;
import com.example.peaceful_land.Repository.PropertyRepository;
import com.example.peaceful_land.Repository.RequestPostRepository;
import com.example.peaceful_land.Repository.UserInterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
public class PostRequestService implements IPostRequestService {

    private final PropertyRepository propertyRepository;
    private final PostRepository postRepository;
    private final RequestPostRepository requestPostRepository;
    private final UserInterestRepository userInterestRepository;
    private final IEmailService emailService;


    @Override
    public List<PostApprovalResponse> getAllPostRequests() {
        List<RequestPost> postRequests = requestPostRepository.findAllByOrderByIdDesc();
        return postRequests.stream()
                .map(RequestPost::parsePostApprovalResponse)
                .toList();
    }

    @Override
    public List<PostApprovalResponse> getPendingPostRequests() {
        List<RequestPost> postRequests = requestPostRepository.findAllByApprovedEqualsOrderByExpirationDesc(null);
        return postRequests.stream()
                .map(RequestPost::parsePostApprovalResponse)
                .toList();
    }

    @Override
    public List<PostApprovalResponse> getApprovedPostRequests() {
        List<RequestPost> postRequests = requestPostRepository.findAllByApprovedEqualsOrderByExpirationDesc(true);
        return postRequests.stream()
                .map(RequestPost::parsePostApprovalResponse)
                .toList();
    }

    @Override
    public List<PostApprovalResponse> getRejectedPostRequests() {
        List<RequestPost> postRequests = requestPostRepository.findAllByApprovedEqualsOrderByExpirationDesc(false);
        return postRequests.stream()
                .map(RequestPost::parsePostApprovalResponse)
                .toList();
    }

    @Override
    public ResponseUserPostReqView getPostRequestById(Long id) {
        RequestPost postRequest = requestPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu"));
        return postRequest.parseResponseUserPostReqView();
    }

    @Override
    public void approvePostRequest(Long id) {
        // Lấy ra yêu cầu
        RequestPost postRequest = requestPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu"));
        // Thay đổi trạng thái yêu cầu và lưu vào database
        postRequest.setApproved(true);
        requestPostRepository.save(postRequest);
        // Thay đổi trạng thái bài rao và lưu vào database nếu có
        Post approvedPost = postRequest.getPost();
        if (postRequest.getHide()){
            approvedPost.setHide(false);
            postRepository.save(approvedPost);
            // Thay đổi trạng thái bất động sản và lưu vào database
            Property property = approvedPost.getProperty();
            property.setHide(false);
            propertyRepository.save(property);
        }
        // Gửi email thông báo cho người đăng bài
        new Thread(() ->
            emailService.sendPostApprovedEmailToOwner(
                    approvedPost.getProperty().getUser().getEmail(),
                    approvedPost.getId(),
                    approvedPost.getDateBegin()
            )
        ).start();
        // Gửi email đến người quan tâm
        List<UserInterest> listUserInterested = userInterestRepository.findByPropertyEqualsAndNotificationEquals(approvedPost.getProperty(), true);
        if (!listUserInterested.isEmpty()){
            new Thread(() -> {
                for(UserInterest userInterest : listUserInterested) {
                    emailService.sendPostApprovedEmailToWhoInterested(
                            userInterest.getUser().getEmail(),
                            approvedPost.getId(),
                            userInterest.getDateBegin()
                    );
                }
            }).start();
        }
    }

    @Override
    public void rejectPostRequest(Long id, String denyMessage) {
        // Lấy ra yêu cầu
        RequestPost postRequest = requestPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu"));
        // Thay đổi trạng thái yêu cầu và lưu vào database
        postRequest.setApproved(false);
        postRequest.setDenyMessage(denyMessage);
        requestPostRepository.save(postRequest);
        // Thay đổi trạng thái bài rao và lưu vào database nếu có
        Post approvedPost = postRequest.getPost();
        if (postRequest.getHide()){
            approvedPost.setHide(false);
            postRepository.save(approvedPost);
            // Thay đổi trạng thái bất động sản và lưu vào database
            Property property = approvedPost.getProperty();
            property.setHide(false);
            propertyRepository.save(property);
        }
        // Gửi email thông báo cho người đăng bài
        new Thread(() ->
            emailService.sendPostRejectedEmailToOwner(
                    approvedPost.getProperty().getUser().getEmail(),
                    approvedPost.getId(),
                    approvedPost.getDateBegin(),
                    denyMessage
            )
        ).start();
    }
}
