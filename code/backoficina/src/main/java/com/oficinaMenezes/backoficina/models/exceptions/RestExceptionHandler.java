package com.oficinaMenezes.backoficina.models.exceptions;

import com.oficinaMenezes.backoficina.models.dtos.exception.ErrorMessageDTO;
import com.oficinaMenezes.backoficina.models.exceptions.auth.UsuarioJaExisteException;
import com.oficinaMenezes.backoficina.models.exceptions.auth.UsuarioNaoCadastrado;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UsuarioJaExisteException.class)
    private ResponseEntity<ErrorMessageDTO> usuarioJaExisteHandler(UsuarioJaExisteException usuarioJaExisteException) {
        ErrorMessageDTO newError = new ErrorMessageDTO(
                usuarioJaExisteException.getMessage(),
                LocalDateTime.now().toString()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(newError);
    }

    @ExceptionHandler(UsuarioNaoCadastrado.class)
    private ResponseEntity<ErrorMessageDTO> usuarioNaoCadastradoHandler(UsuarioNaoCadastrado usuarioNaoCadastrado) {
        ErrorMessageDTO newError = new ErrorMessageDTO(
                usuarioNaoCadastrado.getMessage(),
                LocalDateTime.now().toString()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(newError);
    }

    @ExceptionHandler(BadCredentialsException.class)
    private ResponseEntity<ErrorMessageDTO> badCredentialsHandler(BadCredentialsException ex) {
        ErrorMessageDTO newError = new ErrorMessageDTO(
                "CPF ou senha inválidos",
                LocalDateTime.now().toString()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(newError);
    }

}
