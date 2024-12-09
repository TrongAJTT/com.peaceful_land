package com.example.peaceful_land.Entity;

import com.example.peaceful_land.DTO.TourRequest;
import com.example.peaceful_land.Utils.VariableUtils;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity @Table(name = "requests_tour")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class RequestTour extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "property_id")
    private Property property;

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

    public static RequestTour fromTourRequestWithoutProperty(TourRequest request) {
        return RequestTour.builder()
                .tourType(request.getType() == 0 ? VariableUtils.REQUEST_TOUR_HOMESTAY : VariableUtils.REQUEST_TOUR_DIRECT)
                .expectedTime(LocalDateTime.of(
                        request.getExpectedDate(),
                        LocalTime.of(request.getHour(), 0, 0)
                ))
                .name(request.getName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .interestLevel(request.getInterestLevel() == 0 ? VariableUtils.REQUEST_INTEREST_INFO : VariableUtils.REQUEST_INTEREST_BUY)
                .build();
    }
}