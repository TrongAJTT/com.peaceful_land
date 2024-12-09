package com.example.peaceful_land.Entity;

import com.example.peaceful_land.DTO.ReportRequest;
import com.example.peaceful_land.Utils.VariableUtils;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "requests_report")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class RequestReport extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "property_id")
    private Property property;

    @Column
    private String object;

    @Column
    private String reasons;

    @Column
    private String description;

    @Column
    private String name;

    @Column
    private String phone;

    @Column
    private String email;

    public static RequestReport fromReportRequestWithoutProperty(ReportRequest request) {
        return RequestReport.builder()
                .object(request.getObject() == VariableUtils.REQUEST_REPORT_OBJECT_ID_POST ?
                        VariableUtils.REQUEST_REPORT_OBJECT_VI_POST :
                        VariableUtils.REQUEST_REPORT_OBJECT_VI_MAP)
                .reasons(request.getReason())
                .description(request.getDescription())
                .name(request.getName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .build();
    }

}
