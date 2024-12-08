package com.example.peaceful_land.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "purchases")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Purchase extends BaseEntity {

    @Id @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "user_id")
    private Account user;

    @Column
    private Long amount;

    @Column
    private String action;

}
