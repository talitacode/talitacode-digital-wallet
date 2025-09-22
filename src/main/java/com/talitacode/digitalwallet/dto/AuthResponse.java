package com.talitacode.digitalwallet.dto;

import java.util.UUID;

public record AuthResponse(String token, UUID accountId) {}
