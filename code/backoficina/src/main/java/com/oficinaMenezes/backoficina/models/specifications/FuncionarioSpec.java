package com.oficinaMenezes.backoficina.models.specifications;

import com.oficinaMenezes.backoficina.models.entities.Funcionario;
import com.oficinaMenezes.backoficina.models.entities.enums.ERole;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class FuncionarioSpec {

    public static Specification<Funcionario> nomeContains(String nome) {
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(nome)) {
                return null;
            }
            return builder.like(root.get("nome"), "%" + nome + "%");
        };
    }

    public static Specification<Funcionario> roleConstains(ERole role){
        return (root, query, builder) -> {
            if(org.springframework.util.ObjectUtils.isEmpty(role)){
                return null;
            }
            return root.get("role").in(role);
        };
    }

}
