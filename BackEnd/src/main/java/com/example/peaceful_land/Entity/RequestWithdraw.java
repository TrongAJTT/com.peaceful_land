package com.example.peaceful_land.Entity;

import com.example.peaceful_land.DTO.ResponseWithdrawRequest;
import com.example.peaceful_land.Utils.VariableUtils;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "requests_withdraw")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class RequestWithdraw extends BaseEntity {

    public static final byte STATUS_PENDING = 0;
    public static final byte STATUS_APPROVED = 1;
    public static final byte STATUS_REJECTED = 2;

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

    public ResponseWithdrawRequest toResponseWithdrawRequest(){
        return ResponseWithdrawRequest.builder()
                .id(this.id)
                .userId(account.getId())
                .status(status == STATUS_PENDING ? "Đang chờ xử lý" : this.status == STATUS_APPROVED ? "Đã duyệt" : "Đã từ chối")
                .denyMessage(resultMessage)
                .requestDate(getDateBegin().format(VariableUtils.FORMATTER_DATE_TIME))
                .build();
    }

}
