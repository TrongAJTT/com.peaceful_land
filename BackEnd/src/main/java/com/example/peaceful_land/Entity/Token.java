package com.example.peaceful_land.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity @Table(name = "requests_withdraw")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Token {

    @Id @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column
    private String token;

    @Column(name = "token_type")
    private String tokenType;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @Column
    private Boolean revoked;

    @Column
    private Boolean expired;

    @ManyToOne @JoinColumn(name = "user_id")
    private Account userId;

}
