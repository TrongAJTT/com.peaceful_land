package com.example.peaceful_land.Service;

import com.example.peaceful_land.DTO.*;
import com.example.peaceful_land.Entity.*;
import com.example.peaceful_land.Exception.PropertyNotFoundException;
import com.example.peaceful_land.Repository.*;
import com.example.peaceful_land.Utils.ImageUtils;
import com.example.peaceful_land.Utils.VariableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.example.peaceful_land.Utils.VariableUtils.TYPE_UPLOAD_POST_THUMBNAIL;

@Service @RequiredArgsConstructor
public class PostService implements IPostService {

    private final AccountRepository accountRepository;
    private final RequestPostRepository requestPostRepository;
    private final PropertyLogRepository propertyLogRepository;
    private final PostRepository postRepository;
    private final PostLogRepository postLogRepository;
    private final UserInterestRepository userInterestRepository;
    private final PropertyRepository propertyRepository;
    private final IEmailService emailService;

    @Override
    public Post createPost(PostRequest request) {
        try{
            // Lấy ra bất động sản từ request
            Property property = propertyRepository.findById(request.getPropertyId()).orElse(null);
            if (property == null) throw new PropertyNotFoundException();
            // Ném ngoại lệ nếu bất động sản đã có bài rao
            if (postRepository.existsByProperty(property)) {
                throw new RuntimeException("Bất động sản đã có bài rao");
            }
            // Tạo post mới từ request và lưu vào database
            Post newPost = request.parsePostWithoutProperty();
            newPost.setProperty(property);
            newPost = postRepository.save(newPost);
            // Chuyển sang trạng thái ẩn (chờ duyệt)
            newPost.setHide(true);
            postRepository.save(newPost);
            // Lưu vào nhật ký thay đổi
            postLogRepository.save(newPost.toPostLog());
            // Trả về post mới
            return newPost;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Post checkPostExists(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new RuntimeException("Bài rao không tồn tại"));
    }

    @Override
    public String changeThumbnail(ChangePostThumbnailRequest request) {
        // Kiểm tra tài khoản tồn tại
        Post post = postRepository.findById(request.getPost_id()).orElse(null);
        if (post == null) throw new RuntimeException("Bài rao không tồn tại");
        // Kiểm tra file hợp lệ
        MultipartFile file = request.getImage();
        ImageUtils.checkImageFile(file);
        // Lấy tên file cũ
        String oldThumbnail = post.getThumbnUrl();
        // Thực hiện thay đổi thumbnail
        try {
            // Lưu file vào server
            String fileName = ImageUtils.saveFileServer(file, TYPE_UPLOAD_POST_THUMBNAIL);
            // Cập nhật đường dẫn file mới vào database
            post.setThumbnUrl(fileName);
            postRepository.save(post);
            // Cập nhật vào bản ghi nhật ký mới nhất của bài rao
            PostLog postLog = postLogRepository.findTopByPostEqualsOrderByDateBeginDesc(post);
            postLog.setThumbnUrl(fileName);
            postLogRepository.save(postLog);
            // Xóa file cũ nếu không có postLog nào sử dụng
            if (!oldThumbnail.equals(VariableUtils.IMAGE_NA)){
                if (!postLogRepository.existsByThumbnUrl(oldThumbnail)) {
                    ImageUtils.deleteFileServer(oldThumbnail);
                }
            }
            // Trả về thông báo thành công
            return "Đổi ảnh bìa của bài rao thành công. Đường dẫn mới: " + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi lưu tập tin: " + e.getMessage());
        }
    }

    @Override
    public RequestPost createUserPostRequestApproval(IdRequest postRequest) {
        // Kiểm tra nếu bài rao tồn tại
        Post post = postRepository.findById(postRequest.getPostId()).orElse(null);
        if (post == null) {
            throw new RuntimeException("Bài đăng không tồn tại");
        }
        // Lấy vai trò người dùng
        Byte role = post.getProperty().getUser().getRole();
        // Nếu role là môi giới VIP thì hiện bài đăng và bất động sản ngay (nhưng vẫn chờ duyệt)
        if (role.equals(VariableUtils.ROLE_BROKER_VIP)){
            post.getProperty().setHide(false);
            propertyRepository.save(post.getProperty());
            post.setHide(false);
            postRepository.save(post);
        }
        // Lấy số ngày duyệt bài tối đa
        int noDayApprove = VariableUtils.getApprovalDayRange(role);
        // Tạo yêu cầu duyệt bài
        RequestPost requestPost = RequestPost.builder()
                .post(post)
                .expiration(LocalDate.now().plusDays(noDayApprove))
                .build();
        // Lưu yêu cầu
        return requestPostRepository.save(requestPost);
    }

    @Override
    public ViewPostResponse getPostInformation(IdRequest request) {
        long userId = request.getUserId() == null ? -1 : request.getUserId();
        // Kiểm tra nếu người dùng tồn tại
        Optional<Account> account = accountRepository.findById(userId);
        // Kiểm tra nếu bài rao tồn tại
        Post post = postRepository.findById(request.getPostId()).orElse(null);
        if (post == null) {
            throw new RuntimeException("Bài rao không tồn tại");
        }
        // Kiểm tra nếu bài rao đã bị ẩn
        if (post.getHide()) {
            throw new RuntimeException("Bài rao đã bị ẩn");
        }
        // Lấy bài duyệt của bài rao này
        RequestPost requestPost = requestPostRepository.findByPostEquals(post);
        // Trả kết quả nếu người dùng không tồn tại
        if (account.isEmpty()) {
            return ViewPostResponse.builder()
                    .data(ResponsePost.fromPost(post))
                    .isPendingApproval(requestPost.getApproved())
                    .build();
        }
        // Lấy thông tin quan tâm của người dùng
        Optional<UserInterest> userInterest = userInterestRepository
                .findByUserEqualsAndPropertyEquals(account.get(), post.getProperty());
        // Trả về thông tin phản hồi
        if (userInterest.isPresent()) {
            return ViewPostResponse.builder()
                    .data(ResponsePost.fromPost(post))
                    .isPendingApproval(requestPost.getApproved())
                    .interested(userInterest.get().getInterested())
                    .build();
        }
        else {
            return ViewPostResponse.builder()
                    .data(ResponsePost.fromPost(post))
                    .isPendingApproval(requestPost.getApproved())
                    .build();
        }
    }

    @Override
    public String interestPost(InterestPostRequest request) {
        Account account = accountRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new RuntimeException("Bài rao không tồn tại"));
        // Kiểm tra nếu bài rao đã bị ẩn
        if (post.getHide()) {
            throw new RuntimeException("Bài rao đã bị ẩn");
        }
        // Lưu thông tin quan tâm
        UserInterest userInterest = userInterestRepository.findByUserEqualsAndPropertyEquals(account, post.getProperty())
                .orElse(UserInterest.builder().build());
        // Cập nhật thông tin quan tâm, nếu như đã quan tâm (hoặc không quan tâm), mà nhấn lần nữa là xóa đi
        if (userInterest.getId() != null && userInterest.getInterested() == request.isInterested()) {
            userInterestRepository.delete(userInterest);
            return userInterest.getInterested() ? "Đã xóa dữ liệu quan tâm" : "Đã xóa dữ liệu không quan tâm";
        }
        // Cập nhật thông tin quan tâm mới (hoặc tạo mới nếu chưa có) và lưu vào database
        userInterest.setUser(account);
        userInterest.setProperty(post.getProperty());
        userInterest.setInterested(request.isInterested());
        userInterest.setNotification(request.isNotification());
        userInterestRepository.save(userInterest);
        return userInterest.getInterested() ? "Đã quan tâm bài đăng" : "Đã không quan tâm bài đăng";
    }

    @Override
    public void sendNotificationToInterestedUsers(Property property, String contentUpdate) {
        List<UserInterest> listUserInterested = userInterestRepository.findByPropertyEqualsAndNotificationEquals(property, true);
        if (!listUserInterested.isEmpty()){
            new Thread(() -> {
                for(UserInterest userInterest : listUserInterested) {
                    emailService.sendPostUpdatedEmailToWhoInterested(
                            userInterest.getUser().getEmail(),
                            property.getId(),
                            userInterest.getDateBegin(),
                            contentUpdate
                    );
                }
            }).start();
        }
    }

    @Override
    public void updatePost_Sold(Post post, UpdatePropertyPostRequest request) {
        // Cập nhật thông tin tình trạng bất động sản
        post.getProperty().setStatus(false);
        propertyRepository.save(post.getProperty());
        // Cập nhật vào bản ghi nhật ký mới nhất của bất động sản
        PropertyLog propertyLog = post.getProperty().toPropertyLog();
        propertyLog.setAction(request.getAction());
        propertyLogRepository.save(propertyLog);
        // Thông báo cho người quan tâm
        sendNotificationToInterestedUsers(post.getProperty(), "Bất động sản đã được bán");
    }

}
