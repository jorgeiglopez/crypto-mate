package com.nacho.cryptomate.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nacho.cryptomate.model.CoinListDTO;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.stream.Collectors;

@Service
public class PriceFetcher {

    RestTemplate restTemplate = new RestTemplate();

    ObjectMapper objectMapper = new ObjectMapper();

    public String getAllPrices(String currency) {
        final String URL = "https://api.coinstats.app/public/v1/coins?skip=0&limit=10&currency=" + currency;

        String response = restTemplate.getForEntity(URL, String.class).getBody();
        CoinListDTO root = null;
        try {
            root = objectMapper.readValue(response, CoinListDTO.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Error occurred";
        }
        return root.getCoinDTOS().stream()
                .map(coin -> String.format("%s(%s): $%.2f %s", coin.getName(), coin.getSymbol(), coin.getPrice(), currency))
                .collect(Collectors.joining("\n"));
    }
}
