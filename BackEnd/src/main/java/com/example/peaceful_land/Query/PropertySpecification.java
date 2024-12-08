package com.example.peaceful_land.Query;

import com.example.peaceful_land.Entity.Property;
import org.springframework.data.jpa.domain.Specification;



public class PropertySpecification {
    public static Specification<Property> hasOffer(Boolean offer) {
        return (root, query, criteriaBuilder) -> {
            if (offer == null) {
                return null; // không thêm điều kiện nếu offer không có
            }
            return criteriaBuilder.equal(root.get("offer"), offer);
        };
    }

    public static Specification<Property> hasStatus(Boolean status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return null; // không thêm điều kiện nếu status không có
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    public static Specification<Property> hasLocation(String location) {
        return (root, query, criteriaBuilder) -> {
            if (location == null) {
                return null; // không thêm điều kiện nếu location không có
            }
            return criteriaBuilder.like(root.get("location"), "%" + location + "%");
        };
    }

    public static Specification<Property> hasCategory(String category) {
        return (root, query, criteriaBuilder) -> {
            if (category == null) {
                return null; // không thêm điều kiện nếu category không có
            }
            return criteriaBuilder.like(root.get("category"), "%" + category + "%");
        };
    }

    public static Specification<Property> hasPriceGreaterThan(Long price) {
        return (root, query, criteriaBuilder) -> {
            if (price == null) {
                return null; // không thêm điều kiện nếu price không có
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), price);
        };
    }

    public static Specification<Property> hasAreaGreaterThan(Integer area) {
        return (root, query, criteriaBuilder) -> {
            if (area == null) {
                return null; // không thêm điều kiện nếu area không có
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("area"), area);
        };
    }

    public static Specification<Property> hasBedroomsGreaterThan(Integer bedrooms) {
        return (root, query, criteriaBuilder) -> {
            if (bedrooms == null) {
                return null; // không thêm điều kiện nếu bedrooms không có
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("bedrooms"), bedrooms);
        };
    }

    public static Specification<Property> hasToiletsGreaterThan(Integer toilets) {
        return (root, query, criteriaBuilder) -> {
            if (toilets == null) {
                return null; // không thêm điều kiện nếu toilets không có
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("toilets"), toilets);
        };
    }

    public static Specification<Property> hasEntrance(Byte entrance) {
        return (root, query, criteriaBuilder) -> {
            if (entrance == null) {
                return null; // không thêm điều kiện nếu entrance không có
            }
            return criteriaBuilder.equal(root.get("entrance"), entrance);
        };
    }

    public static Specification<Property> hasFrontage(Byte frontage) {
        return (root, query, criteriaBuilder) -> {
            if (frontage == null) {
                return null; // không thêm điều kiện nếu frontage không có
            }
            return criteriaBuilder.equal(root.get("frontage"), frontage);
        };
    }

    public static Specification<Property> hasHouseOrientation(String houseOrientation) {
        return (root, query, criteriaBuilder) -> {
            if (houseOrientation == null) {
                return null; // không thêm điều kiện nếu houseOrientation không có
            }
            return criteriaBuilder.like(root.get("houseOrientation"), "%" + houseOrientation + "%");
        };
    }

    public static Specification<Property> hasBalconyOrientation(String balconyOrientation) {
        return (root, query, criteriaBuilder) -> {
            if (balconyOrientation == null) {
                return null; // không thêm điều kiện nếu balconyOrientation không có
            }
            return criteriaBuilder.like(root.get("balconyOrientation"), "%" + balconyOrientation + "%");
        };
    }

    public static Specification<Property> hasHideFalse() {
        return (root, query, builder) -> builder.equal(root.get("hide"), false);
    }
}