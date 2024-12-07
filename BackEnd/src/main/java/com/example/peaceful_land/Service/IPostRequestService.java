package com.example.peaceful_land.Service;

import com.example.peaceful_land.DTO.PostApprovalResponse;
import com.example.peaceful_land.DTO.ResponseUserPostReqView;

import java.util.List;

public interface IPostRequestService {
    List<PostApprovalResponse> getAllPostRequests();
    List<PostApprovalResponse> getPendingPostRequests();
    List<PostApprovalResponse> getApprovedPostRequests();
    List<PostApprovalResponse> getRejectedPostRequests();
    ResponseUserPostReqView getPostRequestById(Long id);
    void approvePostRequest(Long id);
    void rejectPostRequest(Long id, String denyMessage);
}