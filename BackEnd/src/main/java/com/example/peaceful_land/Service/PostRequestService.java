package com.example.peaceful_land.Service;

import com.example.peaceful_land.Repository.PostRepository;
import com.example.peaceful_land.Repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class PostRequestService implements IPostRequestService {

    private final PropertyRepository propertyRepository;
    private final PostRepository postRepository;


}
