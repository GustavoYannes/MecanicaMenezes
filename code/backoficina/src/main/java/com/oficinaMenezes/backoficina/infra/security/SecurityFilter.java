package com.oficinaMenezes.backoficina.infra.security;

import com.oficinaMenezes.backoficina.repositories.FuncionarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private TokenService tokenService;

    private FuncionarioRepository funcionarioRepository;

    public SecurityFilter(TokenService tokenService, FuncionarioRepository funcionarioRepository) {
        this.tokenService = tokenService;
        this.funcionarioRepository = funcionarioRepository;
    }


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        var token = this.recoverToken(request);

        if (token != null) {
            var funcionario = tokenService.validarToken(token);

            if (funcionario == null || funcionario.isBlank()) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                String json = """
                    {
                        "message": "Token inválido ou expirado",
                        "timestamp": "%s"
                    }
                    """.formatted(LocalDateTime.now().toString());

                response.getWriter().write(json);
                return;
            }

            UUID userUuid;

            try {
                userUuid = UUID.fromString(funcionario);
            } catch (IllegalArgumentException ex) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                String json = """
                    {
                        "message": "Token inválido: UUID do funcionário inválido",
                        "timestamp": "%s"
                    }
                    """.formatted(LocalDateTime.now().toString());

                response.getWriter().write(json);
                return;
            }

            UserDetails user = funcionarioRepository.findByUuid(userUuid);

            if (user == null) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                String json = """
                    {
                        "message": "Funcionário não encontrado",
                        "timestamp": "%s"
                    }
                    """.formatted(LocalDateTime.now().toString());

                response.getWriter().write(json);
                return;
            }

            var authentication = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    user.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
