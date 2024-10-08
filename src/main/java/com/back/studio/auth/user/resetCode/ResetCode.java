package com.back.studio.auth.user.resetCode;

import com.back.studio.auth.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_res_code")
public class ResetCode {
    @Id
    @GeneratedValue
    private Integer id;
    private String code;
    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}