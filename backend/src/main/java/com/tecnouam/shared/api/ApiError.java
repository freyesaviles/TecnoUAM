
package com.tecnouam.shared.api;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiError(
        LocalDateTime timestamp,
        int status,
        String error,
        Map<String, String> detalles
) {}
