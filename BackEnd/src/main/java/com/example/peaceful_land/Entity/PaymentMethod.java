package com.example.peaceful_land.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "payment_methods")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class PaymentMethod extends BaseEntity {

    @Id @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "user_id")
    private Account account;

    @Column(name = "is_wallet")
    private Boolean isWallet;

    @Column
    private String name;

    @Column(name = "account_number")
    private String accountNumber;

    @Override
    public String toString() {
        return "PaymentMethod{" +
                "id=" + id +
                ", userId=" + account.getId() +
                ", isWallet=" + isWallet +
                ", name='" + name + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                '}';
    }

    public String getMethodAndNameString(){
        return isWallet ? "Ví " + name : "Ngân hàng " + name;
    }
}
