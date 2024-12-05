package com.example.peaceful_land.Service;

import com.example.peaceful_land.DTO.ChangePostThumbnailRequest;
import com.example.peaceful_land.DTO.PostRequest;
import com.example.peaceful_land.Entity.Post;
import com.example.peaceful_land.Entity.PostLog;
import com.example.peaceful_land.Entity.Property;
import com.example.peaceful_land.Exception.PropertyNotFoundException;
import com.example.peaceful_land.Repository.PostLogRepository;
import com.example.peaceful_land.Repository.PostRepository;
import com.example.peaceful_land.Repository.PropertyRepository;
import com.example.peaceful_land.Utils.ImageUtils;
import com.example.peaceful_land.Utils.VariableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.example.peaceful_land.Utils.VariableUtils.TYPE_UPLOAD_POST_THUMBNAIL;

@Service @RequiredArgsConstructor
public class PostService implements IPostService {

    private final PostRepository postRepository;
    private final PostLogRepository postLogRepository;
    private final PropertyRepository propertyRepository;

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
            postLogRepository.save(newPost.parsePostLog());
            // Trả về post mới
            return newPost;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
}
