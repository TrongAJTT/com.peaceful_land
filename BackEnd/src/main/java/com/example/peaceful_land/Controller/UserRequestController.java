package com.example.peaceful_land.Controller;

import com.example.peaceful_land.Repository.RequestPostRepository;
import com.example.peaceful_land.Service.IPostRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/user-requests")
public class UserRequestController {

    private final RequestPostRepository requestPostRepository;
    private final IPostRequestService postRequestService;

    @GetMapping("/posts")
    public ResponseEntity<?> getAllPostRequests() {
        return ResponseEntity.ok(postRequestService.getAllPostRequests());
    }

    @GetMapping("/posts-pending")
    public ResponseEntity<?> getAllPendingPostRequests() {
        return ResponseEntity.ok(requestPostRepository.findAll());
    }

}
