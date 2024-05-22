package com.nucleus.picpay.dto;

import com.nucleus.picpay.domain.user.UserType;

import java.math.BigDecimal;

public record UserDTO(String fullName, String email, String document, String password, BigDecimal balance, UserType userType) { }
