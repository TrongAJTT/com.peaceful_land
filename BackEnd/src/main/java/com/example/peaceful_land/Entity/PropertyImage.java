package com.example.peaceful_land.Entity;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @Builder
@Entity @Table(name = "property_images")
@NoArgsConstructor @AllArgsConstructor
public class PropertyImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "property_id")
    private Property property;

    @Column(name = "file_url")
    private String fileUrl;

}