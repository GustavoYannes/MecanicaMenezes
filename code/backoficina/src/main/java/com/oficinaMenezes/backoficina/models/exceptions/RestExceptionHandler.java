package com.oficinaMenezes.backoficina.models.exceptions;

import com.oficinaMenezes.backoficina.models.dtos.exception.ErrorMessageDTO;
import com.oficinaMenezes.backoficina.models.dtos.exception.ListErrorMessageDTO;
import com.oficinaMenezes.backoficina.models.exceptions.auth.UsuarioJaExisteException;
import com.oficinaMenezes.backoficina.models.exceptions.auth.UsuarioNaoCadastrado;
import com.oficinaMenezes.backoficina.models.exceptions.entrada.EntradaJaFinalizada;
import com.oficinaMenezes.backoficina.models.exceptions.entrada.EntradaNaoExisteException;
import com.oficinaMenezes.backoficina.models.exceptions.entrada.EntradaNaoFoiFechada;
import com.oficinaMenezes.backoficina.models.exceptions.entrada.VeiculoEmAtendimentoException;

import com.oficinaMenezes.backoficina.models.exceptions.funcionario.FuncionarioNaoExiste;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

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

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList();

        ListErrorMessageDTO newError = new ListErrorMessageDTO(
                errors,
                LocalDateTime.now().toString()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(newError);

    }

    @ExceptionHandler(VeiculoEmAtendimentoException.class)
    private ResponseEntity<ErrorMessageDTO> veiculoEmAtendimentoHandler(VeiculoEmAtendimentoException ex) {
        ErrorMessageDTO newError = new ErrorMessageDTO(
                ex.getMessage(),
                LocalDateTime.now().toString()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(newError);
    }

    @ExceptionHandler(EntradaJaFinalizada.class)
    private ResponseEntity<ErrorMessageDTO> entradaJaFinalizadaHandler(EntradaJaFinalizada ex) {
        ErrorMessageDTO newError = new ErrorMessageDTO(
                ex.getMessage(),
                LocalDateTime.now().toString()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(newError);
    }

    @ExceptionHandler(EntradaNaoExisteException.class)
    private ResponseEntity<ErrorMessageDTO> entradaNaoExisteHandler(EntradaNaoExisteException ex) {
        ErrorMessageDTO newError = new ErrorMessageDTO(
                ex.getMessage(),
                LocalDateTime.now().toString()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(newError);
    }

    @ExceptionHandler(FuncionarioNaoExiste.class)
    private ResponseEntity<ErrorMessageDTO> funcionarioNaoExiste(FuncionarioNaoExiste ex){
        ErrorMessageDTO newError = new ErrorMessageDTO(
                ex.getMessage(),
                LocalDateTime.now().toString()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(newError);
    }

    @ExceptionHandler(EntradaNaoFoiFechada.class)
    private ResponseEntity<ErrorMessageDTO> funcionarioNaoExiste(EntradaNaoFoiFechada ex){
        ErrorMessageDTO newError = new ErrorMessageDTO(
                ex.getMessage(),
                LocalDateTime.now().toString()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(newError);
    }

}