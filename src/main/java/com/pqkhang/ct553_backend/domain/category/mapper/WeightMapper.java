package com.pqkhang.ct553_backend.domain.category.mapper;

import com.pqkhang.ct553_backend.domain.category.dto.WeightDTO;
import com.pqkhang.ct553_backend.domain.category.entity.Weight;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WeightMapper {
    WeightDTO toWeightDTO(Weight weight);

    Weight toWeight(WeightDTO weightDTO);
}
