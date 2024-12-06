package com.example.peaceful_land.DTO;

import com.example.peaceful_land.Entity.Post;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data @Getter @Setter @Builder
public class PostResponse {

    private Post data;

    // Nếu bài rao được đăng bởi môi giới VIP thì vẫn hiện trong khi duyệt.
    // Trong lúc duyệt, bài rao vẫn hiện, nhưng sẽ có một số hạn chế:
    // + Người dùng sẽ không thể tạo yêu cầu liên hệ hoặc tham quan.
    // + Bài rao sẽ không hiện thông tin về địa chỉ.
    // + Bài rao sẽ không hiện thông tin về môi giới.
    // + tên bài rao sẽ là "Bài rao chờ duyệt #<mã bài rao>".
    @JsonProperty("is_pending_approval")
    private Boolean isPendingApproval;

}