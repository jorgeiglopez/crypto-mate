package com.nacho.cryptomate.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nacho.cryptomate.helper.PriceFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crypto")
public class PriceHandler {

    @Autowired
    PriceFetcher priceFetcher;

    @RequestMapping(name = "/")
    public String getPrice(@RequestParam(name = "currency", defaultValue = "USD") String currency) throws JsonProcessingException {
        return priceFetcher.getAllPrices(currency);
    }

}
