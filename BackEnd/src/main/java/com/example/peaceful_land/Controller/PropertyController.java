package com.example.peaceful_land.Controller;

import com.example.peaceful_land.DTO.PropertyImagesRequest;
import com.example.peaceful_land.DTO.PropertyRequest;
import com.example.peaceful_land.Entity.Property;
import com.example.peaceful_land.Service.IPropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/properties")
public class PropertyController {

    private final IPropertyService propertyService;

    @PostMapping("/create-property")
    public ResponseEntity<?> createProperty(@RequestBody PropertyRequest request) {
        try {
            Property newProperty = propertyService.createProperty(request);
            return ResponseEntity.ok("Property created successfully:\n" + newProperty);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/upload-images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(@ModelAttribute PropertyImagesRequest request) {
        try{
            return ResponseEntity.ok(propertyService.uploadImages(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
