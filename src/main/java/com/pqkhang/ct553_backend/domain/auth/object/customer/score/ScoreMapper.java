package com.pqkhang.ct553_backend.domain.auth.object.customer.score;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.domain.auth.object.role.RoleMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface ScoreMapper {
    ScoreDTO toScoreDTO(Score Score);
    Score toScore(ScoreDTO ScoreDTO);

}
