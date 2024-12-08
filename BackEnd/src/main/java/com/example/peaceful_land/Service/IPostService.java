package com.example.peaceful_land.Service;

import com.example.peaceful_land.DTO.*;
import com.example.peaceful_land.Entity.Post;
import com.example.peaceful_land.Entity.Property;
import com.example.peaceful_land.Entity.RequestPost;

public interface IPostService {
    Post createPost(PostRequest request);
    Post checkPostExists(Long id);
    String changeThumbnail(ChangePostThumbnailRequest request);
    RequestPost createUserPostRequestApproval(IdRequest postRequest);
    ViewPostResponse getPostInformation(IdRequest request);
    String interestPost(InterestPostRequest request);
    void sendNotificationToInterestedUsers(Property property, String contentUpdate);
    void updatePost_Sold(Post post, UpdatePropertyPostRequest request);
}
