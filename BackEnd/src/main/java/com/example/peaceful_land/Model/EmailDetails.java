package com.example.peaceful_land.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailDetails {
    // Class data members
    private String toEmail;
    private String subject;
    private String msgBody;
    private String attachment;
}