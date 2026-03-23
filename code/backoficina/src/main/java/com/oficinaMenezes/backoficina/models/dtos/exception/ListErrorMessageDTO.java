package com.oficinaMenezes.backoficina.models.dtos.exception;

import java.util.List;

public record ListErrorMessageDTO(
        List<String> errors,
        String timestamp
) {
}
