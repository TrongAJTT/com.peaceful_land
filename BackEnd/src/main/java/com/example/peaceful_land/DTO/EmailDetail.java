package com.example.peaceful_land.DTO;

import lombok.*;

@Data @Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class EmailDetail {
    // Class data members
    private String toEmail;
    private String subject;
    private String msgBody;
    private String attachment;
}
