package com.example.peaceful_land.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity @Table(name = "accounts")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Account extends BaseEntity {

    @Id @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private long id;

    @Column
    private byte role;

    @Column
    private String email;

    @Column
    private String password;

    @Column(name = "account_balance")
    private long accountBalance;

    @Column
    private String name;

    @Column(name = "birth_date")
    private LocalDateTime birthDate;

    @Column
    private String phone;

    @Column
    private Long avatar;

    @Column
    private boolean status;

    @Override
    public String toString(){
        return "Account{" +
                "id=" + id +
                ", role=" + role +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", accountBalance=" + accountBalance +
                ", name='" + name + '\'' +
                ", birthDate=" + birthDate +
                ", phone='" + phone + '\'' +
                ", avatar=" + avatar +
                ", status=" + status +
                '}';
    }

}
