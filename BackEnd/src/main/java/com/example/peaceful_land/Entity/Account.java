package com.example.peaceful_land.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity @Data @Table(name = "account") @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Account {
    @Id @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column private long id;
    @Column private byte role;
    @Column private String email;
    @Column private String password;
    @Column private long accountBalance;
    @Column private String name;
    @Column private LocalDateTime birthDate;
    @Column private String phone;
    @Column private boolean gender;
    @Column private String avatar;
    @Column private boolean status;
    @Column private String meta;
    @Column private boolean hide;
    @Column private int order;
    @Column private LocalDateTime dateBegin;
}
