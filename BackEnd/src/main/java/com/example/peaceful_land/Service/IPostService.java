package com.example.peaceful_land.Service;

import com.example.peaceful_land.DTO.ChangePostThumbnailRequest;
import com.example.peaceful_land.DTO.PostRequest;
import com.example.peaceful_land.Entity.Post;

public interface IPostService {
    Post createPost(PostRequest request);
    String changeThumbnail(ChangePostThumbnailRequest request);
}
