package com.example.peaceful_land.Service;

import com.example.peaceful_land.DTO.*;
import com.example.peaceful_land.Entity.Post;
import com.example.peaceful_land.Entity.Property;
import com.example.peaceful_land.Entity.RequestPost;

public interface IPostService {
    // Tạo bài rao
    Post createPost(PostRequest request);
    // Kiểm tra bài rao tồn tại hay không
    Post checkPostExists(Long id);
    // Thay đổi ảnh đại diện bài rao
    String changeThumbnail(ChangePostThumbnailRequest request);
    // Yêu cầu phê duyệt bài rao
    RequestPost createUserPostRequestApproval(IdRequest postRequest);
    // Xem thông tin
    ViewPostResponse getPostInformationFromPostId(IdRequest request);
    // Quan tâm một bài rao
    String interestPost(InterestPostRequest request);
    void sendNotificationToInterestedUsers(Property property, String contentUpdate);
    // Xem trạng thái cập nhật bài rao
    ResponsePostUpdatePermission getUpdatePostPermission(IdRequest request);
    // Cập nhật một bài rao thành đã bán hoặc đã cho thuê
    void updatePost_SoldOrRented(Post post, UpdatePropertyPostRequest request, boolean isSold);
    // Cập nhật một bài rao thành đã được bán lại hoặc cho thuê lại
    void updatePost_ReSaleOrReRent(Post post, UpdatePropertyPostRequest request, boolean isReSale);
    // Cập nhật giá một bài rao
    void updatePost_Price(Post post, UpdatePropertyPostRequest request);
    // Cập nhật hình thức một bài rao
    void updatePost_Offer(Post post, UpdatePropertyPostRequest request);
    // Cập nhật hạn cho thuê một bài rao
    void updatePost_RentalPeriod(Post post, UpdatePropertyPostRequest request);
    // Cập nhật giảm giá một bài rao
    void updatePost_Discount(Post post, UpdatePropertyPostRequest request);
    // Cập nhật thông tin bài rao
    String updatePost_Information(Post post, UpdatePropertyPostRequest request);
    // Truy vấn bài rao theo điều kiện tìm kiếm
    Object searchPost(SearchPostRequest request, int page, int size);
    // Tìm các bài rao gần nhất
    Object findNearestPosts(NearestPostsRequest request);
    // Lịch sử cập nhật bất động sản
    Object getPropertyUpdateHistory(IdRequest request);
    // Lịch sử cập nhật bài rao
    Object getPostUpdateHistory(IdRequest request);
    // Kiểm tra có quyền yêu cầu xem nhà hay liên hệ hay không
    Object requestPermissionToContactAndTour(IdRequest request);
    // Yêu cầu xem nhà
    Object requestTour(Long postId, TourRequest request);
    // Yêu cầu liên hệ lại
    Object requestContact(Long postId, ContactRequest request);
    // Xem yêu cầu người dùng trên 1 bài đăng
    Object viewUserRequestOnPost(Long postId, Long userId, String type);
    // Xem yêu cầu người dùng trên tất cả bài đăng
    Object viewUserRequestOnAllPosts(Long userId, String type);
    // Gửi yêu cầu báo cáo
    Object sendReportRequest(Long postId, ReportRequest request);
    // Gia hạn bài đăng
    Object extendPost(Long postId, ExtendPostRequest request);
}
