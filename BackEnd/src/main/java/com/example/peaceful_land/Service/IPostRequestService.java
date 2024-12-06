package com.example.peaceful_land.Service;

import com.example.peaceful_land.DTO.PostApprovalResponse;

import java.util.List;

public interface IPostRequestService {
    List<PostApprovalResponse> getAllPostRequests();
    List<PostApprovalResponse> getPendingPostRequests();
    List<PostApprovalResponse> getApprovedPostRequests();
}