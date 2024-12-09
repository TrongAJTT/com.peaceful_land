package com.example.peaceful_land.Controller;

import com.example.peaceful_land.DTO.RejectPostRequest;
import com.example.peaceful_land.Repository.RequestPostRepository;
import com.example.peaceful_land.Service.IPostRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/user-requests")
public class UserRequestController {

    private final RequestPostRepository requestPostRepository;
    private final IPostRequestService postRequestService;

    @GetMapping("/posts")
    public ResponseEntity<?> getAllPostRequests(@RequestParam(required = false) String type) {
        if (type == null || type.equals("all")) {
            return ResponseEntity.ok(postRequestService.getAllPostRequests());
        } else if (type.equals("pending")) {
            return ResponseEntity.ok(postRequestService.getPendingPostRequests());
        } else if (type.equals("approved")) {
            return ResponseEntity.ok(postRequestService.getApprovedPostRequests());
        } else if (type.equals("rejected")) {
            return ResponseEntity.ok(postRequestService.getRejectedPostRequests());
        } else {
            return ResponseEntity.badRequest().body("Không hợp lệ");
        }
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<?> getPostRequestById(@PathVariable Long id) {
        return ResponseEntity.ok(postRequestService.getPostRequestById(id));
    }

    @PostMapping("/post/{id}/action")
    public ResponseEntity<?> getPostRequestById(@PathVariable Long id, @RequestParam String type, @RequestBody(required = false) RejectPostRequest request) {
        if (type.equals("approve")) {
            postRequestService.approvePostRequest(id);
            return ResponseEntity.ok("Duyệt bài rao thành công");
        } else if (type.equals("reject")) {
            if (request == null || request.getDenyMessage() == null || request.getDenyMessage().isEmpty()) {
                return ResponseEntity.badRequest().body("Lý do từ chối không được để trống");
            }
            postRequestService.rejectPostRequest(id, request.getDenyMessage());
            return ResponseEntity.ok("Từ chối bài rao thành công");
        } else {
            return ResponseEntity.badRequest().body("Không hợp lệ");
        }
    }

}