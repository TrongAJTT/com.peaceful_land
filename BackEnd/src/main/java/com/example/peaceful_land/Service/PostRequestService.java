package com.example.peaceful_land.Service;

import com.example.peaceful_land.DTO.PostApprovalResponse;
import com.example.peaceful_land.Entity.RequestPost;
import com.example.peaceful_land.Repository.PostRepository;
import com.example.peaceful_land.Repository.PropertyRepository;
import com.example.peaceful_land.Repository.RequestPostRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
public class PostRequestService implements IPostRequestService {

    private final PropertyRepository propertyRepository;
    private final PostRepository postRepository;
    private final RequestPostRepository requestPostRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<PostApprovalResponse> getAllPostRequests() {
        List<RequestPost> postRequests = requestPostRepository.findAll();
        return postRequests.stream()
                .map(RequestPost::parsePostApprovalResponse)
                .toList();
    }
}
