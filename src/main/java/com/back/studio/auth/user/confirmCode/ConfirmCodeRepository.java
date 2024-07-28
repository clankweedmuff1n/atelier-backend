package com.back.studio.auth.user.confirmCode;

import com.back.studio.auth.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfirmCodeRepository extends JpaRepository<ConfirmCode, Integer> {
    Optional<ConfirmCode> findByUser(User user);
    Optional<ConfirmCode> findByCode(String code);
}