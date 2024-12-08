package com.example.peaceful_land.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "requests_contact")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class RequestContact extends BaseEntity {

    @Id @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "property_id")
    private Property propertyId;

    @Column
    private String name;

    @Column
    private String phone;

    @Column
    private String email;

    @Column(name = "interest_level")
    private String interestLevel;

    @Column
    private String message;

}
