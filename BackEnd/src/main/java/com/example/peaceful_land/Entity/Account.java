package com.example.peaceful_land.Entity;

import com.example.peaceful_land.Utils.VariableUtils;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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
    private LocalDate birthDate;

    @Column
    private String phone;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column
    private boolean status;

    @Column(name = "role_expiration")
    private LocalDate roleExpiration;

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
                ", avatar=" + avatarUrl +
                ", status=" + status +
                ", roleExpiration=" + roleExpiration +
                '}';
    }

    public String getRoleString(){
        return Account.getRoleString(role);
    }

    public static String getRoleString(Byte role){
        return switch (role) {
            case 0 -> VariableUtils.ROLE_NORMAL_STR;
            case 1 -> VariableUtils.ROLE_BROKER_STR;
            case 2 -> VariableUtils.ROLE_BROKER_VIP_STR;
            default -> VariableUtils.ROLE_ADMIN_STR;
        };
    }

}
