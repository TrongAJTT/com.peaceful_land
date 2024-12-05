package com.example.peaceful_land.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity @Table(name = "requests_post")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class RequestPost {

    @Id @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "post_id")
    private Post postId;

    @Column
    private LocalDate expiration;

    @Column
    private Boolean approved;

    @Column(name = "deny_message")
    private String denyMessage;

}