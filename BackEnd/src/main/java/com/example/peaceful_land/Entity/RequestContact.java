package com.example.peaceful_land.Entity;

import com.example.peaceful_land.DTO.ContactRequest;
import com.example.peaceful_land.DTO.ResponseReqContact;
import com.example.peaceful_land.Utils.VariableUtils;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "requests_contact")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class RequestContact extends BaseEntity {

    @Id @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "property_id")
    private Property property;

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

    public static RequestContact fromContactRequestWithoutProperty(ContactRequest request) {
        return RequestContact.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .interestLevel(request.getInterestLevel() == 0 ? VariableUtils.REQUEST_INTEREST_INFO : VariableUtils.REQUEST_INTEREST_BUY)
                .message(request.getMessage())
                .build();
    }

    public ResponseReqContact toContactRequest() {
        return ResponseReqContact.builder()
                .name(getName())
                .phone(getPhone())
                .email(getEmail())
                .interestLevel(getInterestLevel())
                .message(getMessage())
                .createdAt(getDateBegin())
                .build();
    }

}
