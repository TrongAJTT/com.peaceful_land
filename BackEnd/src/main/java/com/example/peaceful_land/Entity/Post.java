package com.example.peaceful_land.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity @Table(name = "posts")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Post extends BaseEntity {

    @Id @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "property_id")
    private Property property;

    @Column
    private String title;

    @Column
    private Boolean status;

    @Column
    private String description;

    @Column(name = "thumbn_url")
    private String thumbnUrl;

    @Column
    private LocalDate expiration;

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", property=" + property +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", thumbnUrl='" + thumbnUrl + '\'' +
                ", expiration=" + expiration +
                '}';
    }

    public PostLog parsePostLog() {
        return PostLog.builder()
                .post(this)
                .title(this.title)
                .status(this.status)
                .description(this.description)
                .thumbnUrl(this.thumbnUrl)
                .expiration(this.expiration)
                .build();
    }
}