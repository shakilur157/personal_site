package com.sazzadur.payload.response.commonResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CommonResponse <T>{
    private int statusCode;
    private String message;
    private T body;



    public CommonResponse(int statusCode, String message, T userInformation) {
        this.statusCode = statusCode;
        this.message = message;
        this.body = userInformation;
    }
}
