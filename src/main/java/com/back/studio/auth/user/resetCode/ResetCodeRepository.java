package com.back.studio.auth.user.resetCode;

import com.back.studio.auth.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResetCodeRepository extends JpaRepository<ResetCode, Integer> {
    Optional<ResetCode> findByUser(User user);
    Optional<ResetCode> findByCode(String code);
}