package com.back.studio.auth.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class ApiResponse {
    private final Date timestamp;
    private final HttpStatus status;
    private final String message;
    private final String path;
    public static ApiResponse of(Date timestamp, HttpStatus status, String message, String path) {
        return new ApiResponse(timestamp, status, message, path);
    }
}
