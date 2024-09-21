package com.back.studio.auth;

import com.back.studio.auth.requests.RegisterRequest;
import com.back.studio.auth.responses.AuthenticationResponse;
import com.back.studio.auth.user.Role;
import com.back.studio.auth.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    private RegisterRequest request;
    private AuthenticationResponse response;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        request = new RegisterRequest(
                "John",
                "Doe",
                "john.doe@mail.com",
                "password"
        );

        response = new AuthenticationResponse(
                "accessToken",
                "resfreshToken",
                User.builder()
                        .firstname("John")
                        .lastname("Doe")
                        .password("password")
                        .email("john.doe@mail.com")
                        .role(Role.USER)
                        .build()
        );
    }

    @Test
    void register() throws Exception {
        when(userService.register(any()))
                .thenReturn(response);

        mvc.perform(post("/api/v1/auth/register")
                        .content(mapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.email", is(response.getUser().getEmail())))
                .andExpect(jsonPath("$.user.first_name", is(response.getUser().getFirstname())))
                .andExpect(jsonPath("$.user.last_name", is(response.getUser().getLastname())))
                .andExpect(jsonPath("$.user.role", is(response.getUser().getRole().toString())))
                .andExpect(jsonPath("$.access_token", is(response.getAccessToken())))
                .andExpect(jsonPath("$.refresh_token", is(response.getRefreshToken())));
    }
}