package com.example.UserAuthService.dtos;

import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.common.protocol.types.Field;

@Getter
@Setter
public class SendEmailEventDto {

    private String toEmail;
    private String subject;
    private String body;
}
