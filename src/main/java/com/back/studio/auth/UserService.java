package com.back.studio.auth;

import com.back.studio.auth.requests.AuthenticationRequest;
import com.back.studio.auth.responses.AuthenticationResponse;
import com.back.studio.auth.requests.RegisterRequest;
import com.back.studio.auth.user.User;
import com.back.studio.auth.user.UserRepository;
import com.back.studio.auth.user.confirmCode.ConfirmCode;
import com.back.studio.auth.user.confirmCode.ConfirmCodeRepository;
import com.back.studio.auth.user.resetCode.ResetCode;
import com.back.studio.auth.user.resetCode.ResetCodeRepository;
import com.back.studio.auth.user.token.Token;
import com.back.studio.auth.user.token.TokenRepository;
import com.back.studio.auth.user.token.TokenType;
import com.back.studio.configuration.JwtService;
import com.back.studio.email.EmailServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static com.back.studio.auth.user.Role.USER;

@Service
@RequiredArgsConstructor
public class UserService {

    private final EmailServiceImpl emailService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    private final ConfirmCodeRepository confirmCodeRepository;
    private final ResetCodeRepository resetCodeRepository;
    private final RestTemplate restTemplate;
    private final Random random = new Random();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AuthenticationResponse register(RegisterRequest request) {
        User user = createUser(request);
        ConfirmCode confirmCode = createConfirmCode(user);
        sendConfirmationEmail(user, confirmCode.getCode());
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .user(user)
                .refreshToken(refreshToken)
                .build();
    }

    private User createUser(RegisterRequest request) {
        User user = User.builder()
                .firstname(request.getFirstname())
                .confirmed(false)
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(USER)
                .build();
        return userRepository.save(user);
    }

    private ConfirmCode createConfirmCode(User user) {
        ConfirmCode confirmCode = ConfirmCode.builder()
                .code(String.valueOf(random.nextInt(900000) + 100000))
                .user(user)
                .build();
        return confirmCodeRepository.save(confirmCode);
    }

    private void sendConfirmationEmail(User user, String code) {
        String emailContent = String.format("""
                <div style="width: 100%%; display: flex; margin: 0 auto; justify-content: center; justify-items: center;">
                    <div style="max-width: 600px;">
                        <div style="margin: 0 auto;text-align: center;">
                            <img width="auto" height="auto" style="margin-top:0;margin-right:0;margin-bottom: 0px;margin-left:0px" src="https://x-lines.ru/letters/i/cyrillicscript/0444/030326/20/0/q3zsa5uxq3o1hau1cfzge.png" alt="logo" />
                            <h1>Подтвердите ваш адрес электронной почты</h1>
                            <p style="font-size:20px;line-height:28px;letter-spacing:-0.2px;margin-bottom:28px;word-break:break-word"> Для подтверждения адреса, введите код указанный ниже в окне подтверждения </p>
                        </div>
                        <div style="background:#f5f4f5;border-radius:4px;padding:23px 13px;margin-left:50px;margin-right:50px;margin-bottom:72px;margin-bottom:30px">
                            <div style="color: black; text-align:center;vertical-align:middle;font-size:30px"> %s </div>
                        </div>
                        <div style="margin-left:50px;margin-right:50px;margin-bottom:72px;margin-bottom:30px">
                            <p style="font-size:16px;line-height:24px;letter-spacing:-0.2px;margin-bottom:28px"></p>
                            <p style="font-size:16px;line-height:24px;letter-spacing:-0.2px;margin-bottom:28px"> Если вы не запрашивали подтверждение, не беспокойтесь, вы можете спокойно проигнорировать это письмо. </p>
                            <div>©2024 Вольнова.рф <br>
                            </div>
                            <br>Все права защищены.
                        </div>
                    </div>
                </div>
                """, code);
        emailService.sendSimpleMessage(user.getEmail(), "Account confirmation", emailContent);
    }

    public User confirmAccount(String id) {
        ConfirmCode confirmCode = confirmCodeRepository.findByCode(id)
                .orElseThrow(() -> new UsernameNotFoundException("Link not found"));
        User user = confirmCode.getUser();
        if (user.getConfirmed()) throw new UsernameNotFoundException("User is already confirmed");
        user.setConfirmed(true);
        return userRepository.save(user);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticateUser(request.getEmail(), request.getPassword());
        User user = getUserByEmail(request.getEmail());
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .user(user)
                .refreshToken(refreshToken)
                .build();
    }

    private void authenticateUser(String email, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (Exception e) {
            throw new IllegalStateException("Invalid credentials", e);
        }
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User updateUser(User user) {
        userRepository.findById(user.getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return userRepository.save(user);
    }

    public List<User> getUsers(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String refreshToken = extractToken(request);
        String userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            User user = getUserByEmail(userEmail);
            if (jwtService.isTokenValid(refreshToken, user)) {
                return userRepository.findAll();
            }
        }
        throw new UsernameNotFoundException("Don't have access");
    }

    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UsernameNotFoundException("Don't have access");
        }
        return authHeader.substring(7);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String refreshToken = extractToken(request);
        String userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            User user = getUserByEmail(userEmail);
            if (jwtService.isTokenValid(refreshToken, user)) {
                String accessToken = jwtService.generateToken(user);
                AuthenticationResponse authResponse = AuthenticationResponse.builder()
                        .user(user)
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                objectMapper.writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        validUserTokens.forEach(token -> {
            token.setRevoked(true);
            token.setExpired(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void sendResetEmail(String emailAddress) {
        User user = getUserByEmail(emailAddress);
        String resetLink = createResetLink(user);
        sendResetEmail(user, resetLink);
    }

    public User getById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private String createResetLink(User user) {
        ResetCode resetCode = ResetCode.builder()
                .code(UUID.randomUUID().toString())
                .user(user)
                .build();
        resetCodeRepository.save(resetCode);
        return "https://engelrealestate.us/reset/" + resetCode.getCode();
    }

    private void sendResetEmail(User user, String resetLink) {
        String emailContent = String.format("""
                <div style="max-width: 600px;">
                   <div style="margin: 0 auto;text-align: center;">
                      <img width="auto" height="auto"
                         style="margin-top:0;margin-right:0;margin-bottom:32px;margin-left:0px"
                         src="https://i.imgur.com/gCjUQw7.png"
                         alt="logo"/>
                      <h1>Confirm your email address</h1>
                      <p style="font-size:20px;line-height:28px;letter-spacing:-0.2px;margin-bottom:28px;word-break:break-word">
                         To reset your password click on the button below
                      </p>
                   </div>
                   <div
                      style="background:#f5f4f5;border-radius:4px;padding:23px 13px;margin-left:50px;margin-right:50px;margin-bottom:72px;margin-bottom:30px">
                      <a style="cursor: pointer; user-select: none; text-decoration: none" href="%s">
                         <div style="color: black; text-align:center;vertical-align:middle;font-size:30px">
                            Reset Password
                         </div>
                      </a>
                   </div>
                   <div style="margin-left:50px;margin-right:50px;margin-bottom:72px;margin-bottom:30px">
                      <p style="font-size:16px;line-height:24px;letter-spacing:-0.2px;margin-bottom:28px"></p>
                      <p style="font-size:16px;line-height:24px;letter-spacing:-0.2px;margin-bottom:28px">
                         If you didn’t request this email, there’s nothing to worry about — you
                         can safely ignore it.
                      </p>
                      <div>©2024 EngelRealEstate LLC<br>
                      </div>
                      <br>All rights reserved.
                   </div>
                </div>
                """, resetLink);
        emailService.sendSimpleMessage(user.getEmail(), "Reset your password", emailContent);
    }
}
