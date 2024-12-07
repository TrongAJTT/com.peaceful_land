package com.example.peaceful_land.Service;

import com.example.peaceful_land.DTO.*;
import com.example.peaceful_land.Entity.Post;
import com.example.peaceful_land.Entity.RequestPost;

public interface IPostService {
    Post createPost(PostRequest request);
    String changeThumbnail(ChangePostThumbnailRequest request);
    RequestPost createUserPostRequestApproval(IdRequest postRequest);
    PostResponse getPostInformation(IdRequest request);
    String interestPost(InterestPostRequest request);
}
