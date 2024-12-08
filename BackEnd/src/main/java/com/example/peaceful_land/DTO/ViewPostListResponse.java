package com.example.peaceful_land.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data @Getter @Setter @Builder
public class ViewPostListResponse {

    List<ViewPostResponse> list_data;
    private int total_page;

}