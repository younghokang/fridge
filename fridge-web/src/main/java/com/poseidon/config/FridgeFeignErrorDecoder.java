package com.poseidon.config;

import static feign.FeignException.errorStatus;

import com.poseidon.fridge.service.NotFoundException;

import feign.Response;
import feign.codec.ErrorDecoder;

public class FridgeFeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        if(response.status() == 404) {
            return new NotFoundException(response.status(), response.reason());
        }
        return errorStatus(methodKey, response);
    }

}
