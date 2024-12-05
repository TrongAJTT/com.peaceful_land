package com.example.peaceful_land.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity @Table(name = "discounts")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Discount extends BaseEntity {

    @Id @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "property_id")
    private Property propertyId;

    @Column(name = "orignal_price")
    private Long origionalPrice;

    @Column(name = "discount_price")
    private Long discountPrice;

    @Column
    private LocalDateTime expiration;

    @Override
    public String toString() {
        return "Discount{" +
                "id=" + id +
                ", propertyId=" + propertyId +
                ", origionalPrice=" + origionalPrice +
                ", discountPrice=" + discountPrice +
                ", expiration=" + expiration +
                '}';
    }
}
