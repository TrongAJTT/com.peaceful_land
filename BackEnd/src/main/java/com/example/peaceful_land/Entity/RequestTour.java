package com.example.peaceful_land.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity @Table(name = "requests_tour")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class RequestTour extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "property_id")
    private Property propertyId;

    @Column(name = "tour_type")
    private String tourType;

    @Column(name = "expected_time")
    private LocalDateTime expectedTime;

    @Column
    private String name;

    @Column
    private String phone;

    @Column
    private String email;

    @Column(name = "interest_level")
    private String interestLevel;
    
}
