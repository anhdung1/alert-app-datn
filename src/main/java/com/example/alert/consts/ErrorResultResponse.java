package com.example.alert.consts;

import com.example.alert.service.Result;

public class ErrorResultResponse {
    public static final Result<?> unauthorizedResult=new Result<>(null, ErrorMessage.unauthorized,403);
    public static final Result<?> tokenExpirationResult =new Result<>(null, ErrorMessage.tokenExpiration, 403);
    public static Result<Object> successResult(Object data){
        return new Result<>(data,ErrorMessage.success,200);
    }
}
