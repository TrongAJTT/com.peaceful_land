package com.example.peaceful_land.Entity;

import com.example.peaceful_land.DTO.ResponsePropertyLog;
import com.example.peaceful_land.Utils.VariableUtils;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity @Table(name = "property_logs")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class PropertyLog extends BaseEntity {

    @Id @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "property_id")
    private Property property;

    @Column
    private String action;

    @Column
    private Boolean offer;   // Hình thức: 0 - Mua bán, 1 - Cho thuê

    @Column
    private Boolean status; // Trạng thái: 0 - Đã bán hoặc cho thuê, 1 - sẵn sàng

    @Column(name = "rental_period")
    private LocalDate rentalPeriod;

    @Column
    private Long price;

    public ResponsePropertyLog toResponsePropertyLog() {
        return ResponsePropertyLog.builder()
                .action(getAction())
                .date(getDateBegin().format(VariableUtils.FORMATTER_DATE_TIME))
                .price(getPrice())
                .build();
    }

}
