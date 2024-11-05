package com.example.peaceful_land.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    private Long id;                      // id: Long, A_I, PK
    private byte role;                    // role: TINYINT, byte
    private String email;                 // email: TEXT, String
    private String password;              // password: TEXT, String
    private Long accountBalance;          // accountBalance: BIGINT, Long
    private String name;                  // name: TEXT, String
    private Date birthDate;      // birthDate: DATETIME, LocalDateTime
    private String phone;                 // phone: TEXT, String
    private boolean gender;               // gender: BIT, boolean
    private Long avatar;                  // avatar: BIGINT, Long, FK_Files
    private boolean status;               // status: BIT, boolean
    private String meta;                  // meta: TEXT(MAX), String
    private boolean hide;
    @Column(name = "order_index")// hide: BIT, boolean
    private int order;                    // order: INTEGER, int, A_I
    private Date dateBegin;      // dateBegin: DATETIME, LocalDateTime
}
