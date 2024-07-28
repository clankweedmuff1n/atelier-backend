package com.back.studio.auth.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @JsonProperty(value = "first_name")
    private String firstname;
    @JsonProperty(value = "last_name")
    private String lastname;
    private String email;
    private String password;
}