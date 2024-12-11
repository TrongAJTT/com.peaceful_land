package com.example.peaceful_land.DTO;

import com.example.peaceful_land.Entity.Property;
import com.example.peaceful_land.Utils.VariableUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data @Getter @Setter @Builder
public class ResponseProperty {
    private Long id;

    @JsonProperty("user_id")
    private Long userId;

    private Boolean offer; // Hình thức: 0 - Mua bán, 1 - Cho thuê
    private Boolean status; // Trạng thái: 0 - Đã bán hoặc cho thuê, 1 - sẵn sàng

    @JsonProperty("rental_period")
    private String rentalPeriod; // Thời gian thuê

    private String location;

    @JsonProperty("location_detail")
    private String locationDetail;

    @JsonProperty("map_url")
    private String mapUrl;

    private String category;
    private Long price;
    private Integer area;
    private String legal;
    private Integer bedrooms;
    private Integer toilets;
    private Byte entrance;
    private Byte frontage;

    @JsonProperty("house_orientation")
    private String houseOrientation;

    @JsonProperty("balcony_orientation")
    private String balconyOrientation;

    @JsonProperty("created_at")
    private String createdAt;

    public static ResponseProperty fromProperty(Property property) {
        String rentalPeriod = property.getRentalPeriod() == null ? null : property.getRentalPeriod().format(VariableUtils.FORMATTER_DATE);
        return ResponseProperty.builder()
                .id(property.getId())
                .userId(property.getUser().getId())
                .offer(property.getOffer())
                .status(property.getStatus())
                .rentalPeriod(rentalPeriod)
                .location(property.getLocation())
                .locationDetail(property.getLocationDetail())
                .mapUrl(property.getMapUrl())
                .category(property.getCategory())
                .price(property.getPrice())
                .area(property.getArea())
                .legal(property.getLegal())
                .bedrooms(property.getBedrooms())
                .toilets(property.getToilets())
                .entrance(property.getEntrance())
                .frontage(property.getFrontage())
                .houseOrientation(property.getHouseOrientation())
                .balconyOrientation(property.getBalconyOrientation())
                .createdAt(property.getDateBegin().format(VariableUtils.FORMATTER_DATE_TIME))
                .build();
    }

}
