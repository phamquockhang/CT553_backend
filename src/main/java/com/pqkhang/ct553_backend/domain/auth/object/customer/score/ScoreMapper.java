package com.pqkhang.ct553_backend.domain.auth.object.customer.score;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ScoreMapper {
    ScoreDTO toScoreDTO(Score Score);
    Score toScore(ScoreDTO ScoreDTO);

}
