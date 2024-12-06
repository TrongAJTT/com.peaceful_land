package com.example.peaceful_land.DTO;

import lombok.*;

import java.util.List;

@Data @Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ListPendingPostResponse {

    private List<PostApprovalResponse> pendingPostsList;

}
