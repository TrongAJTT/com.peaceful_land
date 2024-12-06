package com.example.peaceful_land.Service;

import com.example.peaceful_land.DTO.ChangePostThumbnailRequest;
import com.example.peaceful_land.DTO.PostApprovalRequest;
import com.example.peaceful_land.DTO.PostRequest;
import com.example.peaceful_land.DTO.PostResponse;
import com.example.peaceful_land.Entity.Post;
import com.example.peaceful_land.Entity.RequestPost;

public interface IPostService {
    Post createPost(PostRequest request);
    String changeThumbnail(ChangePostThumbnailRequest request);
    RequestPost createUserPostRequestApproval(PostApprovalRequest postRequest);
    PostResponse getPostInformation(Long id);
}
