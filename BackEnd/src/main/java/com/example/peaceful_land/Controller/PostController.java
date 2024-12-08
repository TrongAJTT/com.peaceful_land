package com.example.peaceful_land.Controller;

import com.example.peaceful_land.DTO.*;
import com.example.peaceful_land.Entity.Post;
import com.example.peaceful_land.Entity.RequestPost;
import com.example.peaceful_land.Service.IPostService;
import com.example.peaceful_land.Utils.VariableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/posts")
public class PostController {

    private final IPostService postService;

    @PostMapping("/create-post")
    public ResponseEntity<?> createPost(@RequestBody PostRequest request) {
        try {
            Post newPost = postService.createPost(request);
            return ResponseEntity.ok("Thêm bất động sản thành công:\n" + newPost.toString());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/change-thumbnail", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> changeThumbnail(@ModelAttribute ChangePostThumbnailRequest request) {
        try {
            String result = postService.changeThumbnail(request);
            return ResponseEntity.ok(result);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/request-approve")
    public ResponseEntity<?> requestApprove(@RequestBody IdRequest request) {
        try {
            RequestPost newRequest = postService.createUserPostRequestApproval(request);
            return ResponseEntity.ok("Yêu cầu duyệt bài rao thành công:\n" + newRequest.toString());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostInformation(@PathVariable Long id, @RequestBody IdRequest request) {
        try {
            request.setPostId(id);
            ViewPostResponse post = postService.getPostInformation(request);
            return ResponseEntity.ok(post);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/interest")
    public ResponseEntity<?> interestPost(@PathVariable Long id, @RequestBody InterestPostRequest request) {
        try {
            request.setPostId(id);
            String msg = postService.interestPost(request);
            return ResponseEntity.ok(msg);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/update-permission")
    public ResponseEntity<?> getUpdatePostPermission(@PathVariable Long id, @RequestBody IdRequest request) {
        try {
            request.setPostId(id);
            ResponsePostUpdatePermission permission = postService.getUpdatePostPermission(request);
            return ResponseEntity.ok(permission);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/{id}/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestParam String action, @ModelAttribute UpdatePropertyPostRequest request) {
        try {
            // Lấy thông tin bài rao
            Post updatePost = postService.checkPostExists(id);
            // Kiểm tra quyền cập nhật
            if (updatePost.getProperty().getUser().getId() != request.getUser_id()) {
                return ResponseEntity.badRequest().body("Bạn không có quyền cập nhật bài rao này");
            }
            // Cập nhật thông tin hành động cần thực hiện
            request.setAction(VariableUtils.getUpdateActionString(action));
            // Thực hiện cập nhật
            switch (action) {
                case VariableUtils.UPDATE_TYPE_SOLD -> {
                    // Xử lý trường hợp đã bán
                    postService.updatePost_SoldOrRented(updatePost, request, true);
                    return ResponseEntity.ok("Cập nhật trạng thái đã bán bất động sản thành công");
                }
                case VariableUtils.UPDATE_TYPE_RENTED -> {
                    // Xử lý trường hợp đã cho thuê
                    postService.updatePost_SoldOrRented(updatePost, request, false);
                    return ResponseEntity.ok("Cập nhật trạng thái đã cho thuê bất động sản thành công");
                }
                case VariableUtils.UPDATE_TYPE_RESALE -> {
                    // Xử lý trường hợp mua bán lại
                    postService.updatePost_ReSaleOrReRent(updatePost, request, true);
                    return ResponseEntity.ok("Cập nhật trạng thái mua bán lại bất động sản thành công");
                }
                case VariableUtils.UPDATE_TYPE_RERENT -> {
                    // Xử lý trường hợp cho thuê lại
                    postService.updatePost_ReSaleOrReRent(updatePost, request, false);
                    return ResponseEntity.ok("Cập nhật trạng thái cho thuê lại bất động sản thành công");
                }
                case VariableUtils.UPDATE_TYPE_PRICE -> {
                    // Xử lý trường hợp cập nhật giá
                    postService.updatePost_Price(updatePost, request);
                    return ResponseEntity.ok("Cập nhật giá bất động sản thành công");
                }
                case VariableUtils.UPDATE_TYPE_OFFER -> {
                    // Xử lý trường hợp thay đổi hình thức
                    postService.updatePost_Offer(updatePost, request);
                }
                case VariableUtils.UPDATE_TYPE_RENTAL_PERIOD -> {
                    // Xử lý trường hợp thay đổi hạn cho thuê
                    postService.updatePost_RentalPeriod(updatePost, request);
                }
                case VariableUtils.UPDATE_TYPE_DISCOUNT -> {
                    // TODO: Xử lý trường hợp giảm giá
                }
                default -> {
                    // TODO: Xử lý trường hợp cập nhật thông tin bất động sản
                }
            }
            // Gửi email thông báo cho người theo dõi

            // Xóa cái này
            return ResponseEntity.ok("Cập nhật bất động sản thành công:\n");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
