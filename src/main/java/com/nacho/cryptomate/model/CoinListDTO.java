package com.nacho.cryptomate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
@JsonDeserialize(builder = CoinListDTO.CoinListDTOBuilder.class)
public class CoinListDTO {
    @JsonProperty("coins")
    public List<CoinDTO> coinDTOS;
}
