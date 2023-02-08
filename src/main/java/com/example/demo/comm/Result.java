package com.example.demo.comm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    @Builder.Default
    private int code = HttpStatus.OK.value();

    private String message;

    private Object data;
}
