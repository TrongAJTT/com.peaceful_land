package com.example.peaceful_land.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity @Table(name = "posts")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Post extends BaseEntity {

    @Id @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "property_id")
    private Property propertyId;

    @Column
    private String title;

    @Column
    private Boolean status;

    @Column
    private String description;

    @Column(name = "thumbn_url")
    private String thumbnUrl;

    @Column
    private LocalDateTime expiration;

}