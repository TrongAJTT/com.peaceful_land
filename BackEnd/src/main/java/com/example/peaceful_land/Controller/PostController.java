package com.example.peaceful_land.Controller;

import com.example.peaceful_land.DTO.ChangePostThumbnailRequest;
import com.example.peaceful_land.DTO.IdRequest;
import com.example.peaceful_land.DTO.PostRequest;
import com.example.peaceful_land.DTO.PostResponse;
import com.example.peaceful_land.Entity.Post;
import com.example.peaceful_land.Entity.RequestPost;
import com.example.peaceful_land.Service.IPostService;
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
            PostResponse post = postService.getPostInformation(request);
            return ResponseEntity.ok(post);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
