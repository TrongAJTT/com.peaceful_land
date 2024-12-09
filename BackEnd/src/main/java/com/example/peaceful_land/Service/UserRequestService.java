package com.example.peaceful_land.Service;

import com.example.peaceful_land.DTO.PostApprovalResponse;
import com.example.peaceful_land.DTO.ResponseUserPostReqView;
import com.example.peaceful_land.DTO.ResponseWithdrawRequest;
import com.example.peaceful_land.Entity.*;
import com.example.peaceful_land.Repository.*;
import com.example.peaceful_land.Utils.VariableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
public class UserRequestService implements IUserRequestService {

    private final PropertyRepository propertyRepository;
    private final RequestWithdrawRepository requestWithdrawRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final PostRepository postRepository;
    private final RequestPostRepository requestPostRepository;
    private final UserInterestRepository userInterestRepository;
    private final IEmailService emailService;
    private final AccountRepository accountRepository;

    @Override
    public List<PostApprovalResponse> getPostRequestBaseOn(String requestState) {
        List<RequestPost> postRequests = switch(requestState){
            case VariableUtils.REQUEST_STATE_ALL -> requestPostRepository.findAllByOrderByIdDesc();
            case VariableUtils.REQUEST_STATE_PENDING -> requestPostRepository.findAllByApprovedEqualsOrderByExpirationDesc(null);
            case VariableUtils.REQUEST_STATE_APPROVED -> requestPostRepository.findAllByApprovedEqualsOrderByExpirationDesc(true);
            case VariableUtils.REQUEST_STATE_REJECTED -> requestPostRepository.findAllByApprovedEqualsOrderByExpirationDesc(false);
            default -> throw new IllegalStateException("Trạng thái yêu cầu không hợp lệ");
        };
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

    @Override
    public List<ResponseWithdrawRequest> getWithdrawRequestBaseOn(String requestState) {
        List<RequestWithdraw> withdrawRequests = switch(requestState){
            case VariableUtils.REQUEST_STATE_ALL -> requestWithdrawRepository.findAllByOrderByDateBeginDesc();
            case VariableUtils.REQUEST_STATE_PENDING -> requestWithdrawRepository.findAllByStatusEqualsOrderByDateBeginDesc(RequestWithdraw.STATUS_PENDING);
            case VariableUtils.REQUEST_STATE_APPROVED -> requestWithdrawRepository.findAllByStatusEqualsOrderByDateBeginDesc(RequestWithdraw.STATUS_APPROVED);
            case VariableUtils.REQUEST_STATE_REJECTED -> requestWithdrawRepository.findAllByStatusEqualsOrderByDateBeginDesc(RequestWithdraw.STATUS_REJECTED);
            default -> throw new IllegalStateException("Trạng thái yêu cầu không hợp lệ");
        };
        return withdrawRequests.stream()
                .map(RequestWithdraw::toResponseWithdrawRequest)
                .toList();
    }

    @Override
    public void approveOrRejectWithdrawRequest(Long id, Boolean isApprove, String denyMessageIfFalse) {
        // Lấy ra yêu cầu
        RequestWithdraw withdrawRequest = requestWithdrawRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy yêu cầu"));
        if (withdrawRequest.getStatus() != RequestWithdraw.STATUS_PENDING){
            throw new IllegalArgumentException("Yêu cầu đã được xử lý");
        }
        // Thay đổi trạng thái yêu cầu và số dư tài khoản (nếu có) và lưu vào database
        Account account = withdrawRequest.getAccount();
        if (isApprove){
            withdrawRequest.setStatus(RequestWithdraw.STATUS_APPROVED);
            // Trừ tiền và lưu vào database
            account.setAccountBalance(account.getAccountBalance() - withdrawRequest.getAmount() - 3000);
            accountRepository.save(account);
        }
        else {
            withdrawRequest.setStatus(RequestWithdraw.STATUS_REJECTED);
            withdrawRequest.setResultMessage(denyMessageIfFalse);
        }
        requestWithdrawRepository.save(withdrawRequest);
        // Gửi email thông báo cho người yêu cầu
        new Thread(() ->
            emailService.sendWithdrawResponse(
                    account.getEmail(),
                    withdrawRequest.getId(),
                    withdrawRequest.getDateBegin(),
                    withdrawRequest.getAmount(),
                    withdrawRequest.getPayment(),
                    isApprove, isApprove ? null : denyMessageIfFalse)
        ).start();
    }
}
