/*
package com.back.studio.auth;

import com.back.studio.auth.requests.RegisterRequest;
import com.back.studio.auth.responses.AuthenticationResponse;
import com.back.studio.auth.user.User;
import com.back.studio.auth.user.UserRepository;
import com.back.studio.auth.user.confirmCode.ConfirmCode;
import com.back.studio.auth.user.confirmCode.ConfirmCodeRepository;
import com.back.studio.auth.user.token.TokenRepository;
import com.back.studio.configuration.JwtService;
import com.back.studio.email.EmailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private EmailServiceImpl emailService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private ConfirmCodeRepository confirmCodeRepository;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setFirstname("John");
        request.setLastname("Doe");
        request.setEmail("john@example.com");
        request.setPassword("password");

        User mockUser = new User();
        mockUser.setFirstname("John");
        mockUser.setLastname("Doe");
        mockUser.setEmail("john@example.com");
        mockUser.setConfirmed(false);
        mockUser.setPassword("encodedPassword");

        ConfirmCode mockConfirmCode = new ConfirmCode();
        mockConfirmCode.setCode("123456");  // Mock confirmation code
        mockConfirmCode.setUser(mockUser);

        String mockJwtToken = "mockJwtToken";
        String mockRefreshToken = "mockRefreshToken";

        // Mock behavior for dependencies
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        when(confirmCodeRepository.save(any(ConfirmCode.class))).thenReturn(mockConfirmCode);  // Ensure the confirmCode is mocked
        when(jwtService.generateToken(any(User.class))).thenReturn(mockJwtToken);
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn(mockRefreshToken);

        // Act
        AuthenticationResponse response = userService.register(request);

        // Assert
        assertEquals(mockJwtToken, response.getAccessToken());
        assertEquals(mockRefreshToken, response.getRefreshToken());
        assertEquals(mockUser, response.getUser());

        // Verify that certain methods were called
        verify(userRepository, times(1)).save(any(User.class));
        verify(confirmCodeRepository, times(1)).save(any(ConfirmCode.class));
        verify(emailService, times(1)).sendSimpleMessage(anyString(), anyString(), anyString());
        verify(tokenRepository, times(1)).save(any());
    }

    @Test
    void confirmAccount() {
    }

    @Test
    void authenticate() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void getUsers() {
    }

    @Test
    void refreshToken() {
    }

    @Test
    void sendResetEmail() {
    }

    @Test
    void getById() {
    }
}*/
