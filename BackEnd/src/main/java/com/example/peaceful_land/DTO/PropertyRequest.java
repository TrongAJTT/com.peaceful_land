package com.example.peaceful_land.DTO;

import com.example.peaceful_land.Entity.Property;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data @Getter @Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class PropertyRequest {

    @JsonProperty("user_id")
    private Long userId;

    private Integer offer; // Hình thức: 0 - Mua bán, 1 - Cho thuê

    @JsonProperty("rental_period")
    private LocalDate rentalPeriod; // Thời gian thuê

    private String location;

    @JsonProperty("location_detail")
    private String locationDetail;

    @JsonProperty("map_url")
    private String mapUrl;

    // Nhận 1 trong các giá trị:
    // 'Nhà riêng', 'Nhà biệt thự, liền kề', 'Nhà mặt phố', 'Shophouse, nhà phố thương mại', 'Chung cư mini, căn hộ dịch vụ'
    // 'Condotel', 'Đất nền dự án', 'Bán đất', 'Trang trại, khu nghỉ dưỡng', 'Kho, nhà xưởng', 'Bất động sản khác'
    private String category;

    private Long price;

    private Integer area;

    private String legal;

    private Integer bedrooms;

    private Integer toilets;

    private Byte entrance;

    private Byte frontage;

    @Column(name = "house_orientation")
    private String houseOrientation;

    @Column(name = "balcony_orientation")
    private String balconyOrientation;
    
    // Chuyển đổi sang đối tượng Property

    public Property parsePropertyWithoutAccount(){
        try {
            return Property.builder()
                    .offer(getOffer() != 0)
                    .status(true)
                    .rentalPeriod(getRentalPeriod())
                    .location(getLocation())
                    .locationDetail(getLocationDetail())
                    .mapUrl(getMapUrl())
                    .category(getCategory())
                    .price(getPrice())
                    .area(getArea())
                    .legal(getLegal())
                    .bedrooms(getBedrooms())
                    .toilets(getToilets())
                    .entrance(getEntrance())
                    .frontage(getFrontage())
                    .houseOrientation(getHouseOrientation())
                    .balconyOrientation(getBalconyOrientation())
                    .build();
        }
        catch (Exception e) {
            throw new RuntimeException("Error creating property: ", e.getCause());
        }
    }

}
