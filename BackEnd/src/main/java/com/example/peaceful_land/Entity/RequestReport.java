package com.example.peaceful_land.Entity;

import com.example.peaceful_land.DTO.ReportRequest;
import com.example.peaceful_land.DTO.ResponseReport;
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

    public boolean isHandled() {
        return this.getHide();
    }

    public void setHandled(boolean handled) {
        this.setHide(handled);
    }

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

    public ResponseReport toLiteResponseReport() {
        return ResponseReport.builder()
                .id(this.id)
                .propertyId(this.property.getId())
                .object(this.object)
                .reasons(this.reasons)
                .name(this.name)
                .isHandled(this.isHandled())
                .build();
    }

    public ResponseReport toResponseReport() {
        return ResponseReport.builder()
                .id(this.id)
                .propertyId(this.property.getId())
                .object(this.object)
                .reasons(this.reasons)
                .description(this.description)
                .name(this.name)
                .phone(this.phone)
                .email(this.email)
                .isHandled(this.isHandled())
                .build();
    }

}
