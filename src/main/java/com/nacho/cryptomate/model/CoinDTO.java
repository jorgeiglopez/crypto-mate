package com.nacho.cryptomate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
@JsonDeserialize(builder = CoinDTO.CoinDTOBuilder.class)
public class CoinDTO {

    @JsonProperty String id;

    @JsonProperty String icon;

    @JsonProperty String name;

    @JsonProperty String symbol;

    @JsonProperty int rank;

    @JsonProperty double price;

    @JsonProperty double priceBtc;

    @JsonProperty double volume;

    @JsonProperty double marketCap;

    @JsonProperty double availableSupply;

    @JsonProperty double totalSupply;

    @JsonProperty double priceChange1h;

    @JsonProperty double priceChange1d;

    @JsonProperty double priceChange1w;

    @JsonProperty String websiteUrl;

    @JsonProperty String twitterUrl;

    @JsonProperty String redditUrl;

    @JsonProperty List<String> exp;

    @JsonProperty String contractAddress;

    @JsonProperty int decimals;
}

