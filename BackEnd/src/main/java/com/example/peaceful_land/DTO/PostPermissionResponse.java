package com.example.peaceful_land.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data @Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class PostPermissionResponse {

    // Thời gian sống tối đa của bài rao (ngày)
    @JsonProperty("max_live_time")
    private Integer maxLiveTime;

    // Có quyền chọn toàn bộ danh mục bất động sản hay chỉ "Nhà riêng"
    @JsonProperty("full_category")
    private Boolean fullCategory;

}
