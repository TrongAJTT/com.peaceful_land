package com.example.peaceful_land.Entity;

import com.example.peaceful_land.DTO.ResponsePostLog;
import com.example.peaceful_land.Utils.VariableUtils;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity @Table(name = "post_logs")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class PostLog extends BaseEntity {

    @Id @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "post_id")
    private Post post;

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

    public ResponsePostLog toResponsePostLog() {
        return ResponsePostLog.builder()
                .createdAt(this.getDateBegin().format(VariableUtils.FORMATTER_DATE_TIME))
                .title(this.title)
                .description(this.description)
                .thumbnail_url(this.thumbnUrl)
                .build();
    }

}