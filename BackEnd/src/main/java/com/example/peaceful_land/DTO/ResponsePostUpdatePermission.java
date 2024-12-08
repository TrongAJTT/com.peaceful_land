package com.example.peaceful_land.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data @Getter @Setter @Builder
public class ResponsePostUpdatePermission {
    private boolean can_update;
    private List<String> actions;
}
