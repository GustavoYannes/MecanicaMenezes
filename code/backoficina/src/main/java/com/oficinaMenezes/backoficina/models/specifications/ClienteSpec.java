package com.oficinaMenezes.backoficina.models.specifications;

import com.oficinaMenezes.backoficina.models.entities.Cliente;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.jpa.domain.Specification;

public class ClienteSpec {

    public static Specification<Cliente> nomeCompletoContains(String nomeCompleto) {
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(nomeCompleto)) {
                return null;
            }
            return builder.like(root.get("nomeCompleto"), "%" + nomeCompleto + "%");
        };
    }

}
