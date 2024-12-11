package com.example.peaceful_land.Controller;

import com.example.peaceful_land.DTO.*;
import com.example.peaceful_land.Entity.Post;
import com.example.peaceful_land.Entity.RequestPost;
import com.example.peaceful_land.Exception.PropertyNotFoundException;
import com.example.peaceful_land.Exception.RequestInvalidException;
import com.example.peaceful_land.Repository.PostRepository;
import com.example.peaceful_land.Repository.PropertyRepository;
import com.example.peaceful_land.Service.IPostService;
import com.example.peaceful_land.Utils.VariableUtils;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/posts")
public class PostController {

    private final IPostService postService;
    private final Gson gson;
    private final PostRepository postRepository;
    private final PropertyRepository propertyRepository;

    @PostMapping("/create-post")
    public ResponseEntity<?> createPost(@RequestBody PostRequest request) {
        Post newPost = postService.createPost(request);
        return ok(newPost);
    }

    @PostMapping(value = "/change-thumbnail", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> changeThumbnail(@ModelAttribute ChangePostThumbnailRequest request) {
        String result = postService.changeThumbnail(request);
        return ok(gson.toJson(result));
    }

    @PostMapping("/request-approve")
    public ResponseEntity<?> requestApprove(@RequestBody IdRequest request) {
        RequestPost newRequest = postService.createUserPostRequestApproval(request);
        return ok(newRequest);
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> getPostInformation(@PathVariable Long id, @RequestBody IdRequest request) {
        request.setPostId(id);
        ViewPostResponse post = postService.getPostInformationFromPostId(request);
        return ok(post);
    }

    @PostMapping("/{id}/interest")
    public ResponseEntity<?> interestPost(@PathVariable Long id, @RequestBody InterestPostRequest request) {
        request.setPostId(id);
        return ResponseEntity.ok(gson.toJson(postService.interestPost(request)));
    }

    @PostMapping("/{id}/update-permission")
    public ResponseEntity<?> getUpdatePostPermission(@PathVariable Long id, @RequestBody IdRequest request) {
        request.setPostId(id);
        ResponsePostUpdatePermission permission = postService.getUpdatePostPermission(request);
        return ok(permission);
    }

    @PostMapping(value = "/{id}/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestParam String action, @ModelAttribute UpdatePropertyPostRequest request) {
        // Lấy thông tin bài rao
        Post updatePost = postService.checkPostExists(id);
        // Kiểm tra quyền cập nhật
        if (updatePost.getProperty().getUser().getId() != request.getUser_id()) {
            throw new DataIntegrityViolationException("Bạn không có quyền cập nhật bài rao này");
        }
        // Cập nhật thông tin hành động cần thực hiện
        request.setAction(VariableUtils.getUpdateActionString(action));
        // Thực hiện cập nhật
        switch (action) {
            case VariableUtils.UPDATE_TYPE_SOLD -> {
                // Xử lý trường hợp đã bán
                postService.updatePost_SoldOrRented(updatePost, request, true);
                return ok(gson.toJson("Cập nhật trạng thái đã bán bất động sản thành công"));
            }
            case VariableUtils.UPDATE_TYPE_RENTED -> {
                // Xử lý trường hợp đã cho thuê
                postService.updatePost_SoldOrRented(updatePost, request, false);
                return ok(gson.toJson("Cập nhật trạng thái đã cho thuê bất động sản thành công"));
            }
            case VariableUtils.UPDATE_TYPE_RESALE -> {
                // Xử lý trường hợp mua bán lại
                postService.updatePost_ReSaleOrReRent(updatePost, request, true);
                return ok(gson.toJson("Cập nhật trạng thái mua bán lại bất động sản thành công"));
            }
            case VariableUtils.UPDATE_TYPE_RERENT -> {
                // Xử lý trường hợp cho thuê lại
                postService.updatePost_ReSaleOrReRent(updatePost, request, false);
                return ok(gson.toJson("Cập nhật trạng thái cho thuê lại bất động sản thành công"));
            }
            case VariableUtils.UPDATE_TYPE_PRICE -> {
                // Xử lý trường hợp cập nhật giá
                postService.updatePost_Price(updatePost, request);
                return ok(gson.toJson("Cập nhật giá bất động sản thành công"));
            }
            case VariableUtils.UPDATE_TYPE_OFFER -> {
                // Xử lý trường hợp thay đổi hình thức
                postService.updatePost_Offer(updatePost, request);
                return ok(gson.toJson("Cập nhật hình thức bất động sản thành công"));
            }
            case VariableUtils.UPDATE_TYPE_RENTAL_PERIOD -> {
                // Xử lý trường hợp thay đổi hạn cho thuê
                postService.updatePost_RentalPeriod(updatePost, request);
                return ok(gson.toJson("Cập nhật hạn cho thuê bất động sản thành công"));
            }
            case VariableUtils.UPDATE_TYPE_DISCOUNT -> {
                // TODO: Xử lý trường hợp giảm giá
                postService.updatePost_Discount(updatePost, request);
                return ok(gson.toJson("Cập nhật giảm giá bất động sản thành công"));
            }
            default -> {
                // Xử lý trường hợp cập nhật thông tin bất động sản
                String result = postService.updatePost_Information(updatePost, request);
                return ok(gson.toJson("Cập nhật thông tin bài rao bất động sản: " + result));
            }
        }
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchPost (
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestBody SearchPostRequest request ){
        return ok(postService.searchPost(request, page, size));
    }

    @PostMapping("/find-nearest-{number}")
    public ResponseEntity<?> searchNearestTopK (@PathVariable int number, @RequestBody NearestPostsRequest request) {
        request.setNumber(number);
        return ok(postService.findNearestPosts(request));
    }

    @GetMapping("/{id}/property-logs")
    public ResponseEntity<?> getPropertyUpdateHistory(@PathVariable Long id) {
        return ok(postService.getPropertyUpdateHistory(IdRequest.builder().postId(id).build()));
    }

    @GetMapping("/{id}/post-logs")
    public ResponseEntity<?> getPostUpdateHistory(@PathVariable Long id) {
        return ok(postService.getPostUpdateHistory(IdRequest.builder().postId(id).build()));
    }

    @PostMapping("/{id}/request-tour")
    public ResponseEntity<?> requestTour(@PathVariable Long id, @RequestBody TourRequest request) {
        // Lấy thông tin bài rao
        Post tourPost = postService.checkPostExists(id);
        return ResponseEntity.ok(gson.toJson(postService.requestTour(tourPost.getId(), request)));
    }

    @PostMapping("/{id}/request-contact")
    public ResponseEntity<?> requestContact(@PathVariable Long id, @RequestBody ContactRequest request) {
        // Lấy thông tin bài rao
        Post contactPost = postService.checkPostExists(id);
        return ResponseEntity.ok(gson.toJson(postService.requestContact(contactPost.getId(), request)));
    }

    @PostMapping("/{id}/request-permission")
    public ResponseEntity<?> getRequestPermission(@PathVariable Long id, @RequestBody IdRequest request) {
        request.setPostId(id);
        return ok(gson.toJson(postService.requestPermissionToContactAndTour(request)));
    }

    @PostMapping("/{id}/manager/view-request")
    public ResponseEntity<?> viewRequest(@PathVariable Long id, @RequestParam String type, @RequestBody IdRequest request) {
        if (!type.equals(VariableUtils.REQUEST_TYPE_TOUR) && !type.equals(VariableUtils.REQUEST_TYPE_CONTACT)) {
            throw new RequestInvalidException();
        }
        if (request.getUserId() == null) {
            throw new DataIntegrityViolationException("Yêu cầu này cần id người dùng");
        }
        return ok(postService.viewUserRequestOnPost(id, request.getUserId(), type));
    }

    @PostMapping("/manager/view-request")
    public ResponseEntity<?> viewRequest(@RequestParam String type, @RequestBody IdRequest request) {
        if (!type.equals(VariableUtils.REQUEST_TYPE_TOUR) && !type.equals(VariableUtils.REQUEST_TYPE_CONTACT)) {
            throw new RequestInvalidException();
        }
        if (request.getUserId() == null) {
            throw new DataIntegrityViolationException("Yêu cầu này cần id người dùng");
        }
        return ok(postService.viewUserRequestOnAllPosts(request.getUserId(), type));
    }

    @PostMapping("/{id}/request-report")
    public ResponseEntity<?> sendReportRequest(@PathVariable Long id, @RequestBody ReportRequest request) {
        return ok(gson.toJson(postService.sendReportRequest(id, request)));
    }

    @PostMapping("/{id}/manager/extend-expiration")
    public ResponseEntity<?> extendPost(@PathVariable Long id, @RequestBody ExtendPostRequest request) {
        return ok(gson.toJson(postService.extendPost(id, request)));
    }

    @PostMapping("/my-posts")
    public ResponseEntity<?> getMyPosts(@RequestBody IdRequest request) {
        return ok(postService.viewUserPosts(request.getUserId()));
    }

    @GetMapping("/from-property/{id}")
    public ResponseEntity<?> getPostsFromProperty(@PathVariable Long id) {
        return ok(gson.toJson(
                postRepository.findByProperty(
                        propertyRepository.findById(id).orElseThrow(PropertyNotFoundException::new)
                ).getId()
        ));
    }

}
