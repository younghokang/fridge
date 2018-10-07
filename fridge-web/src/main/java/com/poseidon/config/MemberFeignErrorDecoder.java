package com.poseidon.config;

import feign.Response;
import feign.codec.ErrorDecoder;
import static feign.FeignException.errorStatus;

import com.poseidon.member.service.MemberNotFoundException;

public class MemberFeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        if(response.status() == 404) {
            return new MemberNotFoundException(response.status(), response.reason());
        }
        return errorStatus(methodKey, response);
    }

}
