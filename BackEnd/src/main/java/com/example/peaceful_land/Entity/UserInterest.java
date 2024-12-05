package com.example.peaceful_land.Entity;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @Builder
@Entity @Table(name = "user_interests")
@NoArgsConstructor @AllArgsConstructor
public class UserInterest extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "user_id")
    private Account userId;

    @ManyToOne @JoinColumn(name = "property_id")
    private Property propertyId;

    @Column
    private Boolean type;

}
