package com.example.peaceful_land.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity @Table(name = "post_logs")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class PostLog extends BaseEntity {

    @Id @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "post_id")
    private Post postId;

    @Column
    private String title;

    @Column
    private Boolean status;

    @Column
    private String description;

    @OneToOne @JoinColumn(name = "thumbn_id")
    private File thumbn;

    @Column
    private LocalDateTime expiration;

}