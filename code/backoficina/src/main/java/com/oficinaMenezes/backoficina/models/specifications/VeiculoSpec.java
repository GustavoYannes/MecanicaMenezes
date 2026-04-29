package com.oficinaMenezes.backoficina.models.specifications;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import com.oficinaMenezes.backoficina.models.entities.Veiculo;
import com.oficinaMenezes.backoficina.models.entities.enums.EStatusVeiculo;

public class VeiculoSpec {
    
    public static Specification<Veiculo> statusConstains( List<EStatusVeiculo> statusVeiculo){
        return (root, query, builder) -> {
            if(ObjectUtils.isEmpty(statusVeiculo)){
                     return null;
            }
            return root.get("status").in(statusVeiculo);
        };
    }
}
