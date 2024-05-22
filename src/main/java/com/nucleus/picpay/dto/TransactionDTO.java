package com.nucleus.picpay.dto;

import java.math.BigDecimal;

public record TransactionDTO(String senderDocument, String receiverDocument, BigDecimal amount) { }
