package com.oficinaMenezes.backoficina.models.specifications;

import com.oficinaMenezes.backoficina.models.entities.Entrada;
import com.oficinaMenezes.backoficina.models.entities.Funcionario;
import com.oficinaMenezes.backoficina.models.entities.Servico;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;

public class ServicoSpec {

    public static Specification<Servico> dataMaiorOuIgual(LocalDate inicio) {
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(inicio)) {
                return null;
            }

            return builder.greaterThanOrEqualTo(root.get("data"), inicio);
        };
    }

    public static Specification<Servico> dataMenorOuIgual(LocalDate fim) {
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(fim)) {
                return null;
            }

            return builder.lessThanOrEqualTo(root.get("data"), fim);
        };
    }

    public static Specification<Servico> funcionarioIgual(Funcionario funcionario) {
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(funcionario)) {
                return null;
            }

            return builder.equal(root.get("funcionario"), funcionario);
        };
    }

    public static Specification<Servico> entradaIgual(Entrada entrada) {
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(entrada)) {
                return null;
            }

            return builder.equal(root.get("entrada"), entrada);
        };
    }

}
