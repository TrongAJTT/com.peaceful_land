package com.example.peaceful_land.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "transactions")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Transaction extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "user_id")
    private Account userId;

    @Column
    private Long money;

    @Column(name = "transaction_description")
    private String transactionDescription;

    @Column(name = "transaction_information")
    private String transactionInformation;

    @Column
    private Byte status;

}
