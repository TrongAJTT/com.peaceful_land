package com.example.peaceful_land.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchPostRequest {
    private Long userId;
    private Boolean offer;
    private Boolean status;
    private String location;
    private String category;
    private Long price;
    private Integer area;
    private Integer bedrooms;
    private Integer toilets;
    private Byte entrance;
    private Byte frontage;
    private String houseOrientation;
    private String balconyOrientation;
}
