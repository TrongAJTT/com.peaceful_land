package com.example.peaceful_land.Entity;

import com.example.peaceful_land.DTO.FileDTO;
import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @Builder
@Entity @Table(name = "files")
@NoArgsConstructor @AllArgsConstructor
public class File extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_url")
    private String fileUrl;

}