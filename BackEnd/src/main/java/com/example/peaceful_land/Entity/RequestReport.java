package com.example.peaceful_land.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "requests_report")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class RequestReport extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "property_id")
    private Property propertyId;

    @Column
    private String type;

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

}
