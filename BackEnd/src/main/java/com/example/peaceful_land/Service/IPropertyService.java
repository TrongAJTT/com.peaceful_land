package com.example.peaceful_land.Service;

import com.example.peaceful_land.DTO.PropertyImagesRequest;
import com.example.peaceful_land.DTO.PropertyRequest;
import com.example.peaceful_land.Entity.Property;

public interface IPropertyService {
    Property createProperty(PropertyRequest property);
    String uploadImages(PropertyImagesRequest request);
}
