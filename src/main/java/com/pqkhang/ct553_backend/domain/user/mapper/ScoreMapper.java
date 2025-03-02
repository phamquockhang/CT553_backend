package com.pqkhang.ct553_backend.domain.user.mapper;

import com.pqkhang.ct553_backend.domain.user.dto.ScoreDTO;
import com.pqkhang.ct553_backend.domain.user.entity.Score;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ScoreMapper {
    ScoreDTO toScoreDTO(Score Score);

    Score toScore(ScoreDTO ScoreDTO);
}
