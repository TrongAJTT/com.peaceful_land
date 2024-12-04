package com.example.peaceful_land.Entity;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @Builder
@Entity @Table(name = "user_uninterests")
@NoArgsConstructor @AllArgsConstructor
public class UserUninterest extends BaseEntity {

    @Id
    private Long userId;

    @OneToOne
    @MapsId // Sử dụng userId từ thực thể liên quan làm khóa chính.
    @JoinColumn(name = "user_id")
    private Account user;

    @Column(name = "property_list_id")
    private String propertyListId;

    @Column
    private Integer count;

}
