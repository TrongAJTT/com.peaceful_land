package com.example.peaceful_land.Entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Data @Getter @Setter
public class UserId implements Serializable {
    private Long userId;
}

