package com.example.peaceful_land.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "requests_withdraw")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class RequestWithdraw extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "user_id")
    private Account account;

    @ManyToOne @JoinColumn(name = "id_pay_method")
    private PaymentMethod payment;

    @Column
    private Long amount;    // Lượng tiền muốn rút (bội số 50000)

    @Column
    private Byte status;    // Trạng thái: 0 - Đang chờ xử lý, 1 - Thành công, 2 - Thất bại

    @Column(name = "result_message")
    private String resultMessage;

}
