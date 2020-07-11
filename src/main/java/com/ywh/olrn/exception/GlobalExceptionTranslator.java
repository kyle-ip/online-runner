package com.ywh.olrn.exception;


import com.ywh.olrn.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ywh
 * @since 11/07/2020
 */
@Slf4j
@RestControllerAdvice(basePackages = {"com.ywh.olrn"})
public class GlobalExceptionTranslator {

    @ExceptionHandler(BusinessException.class)
    public Response handleError(HttpServletRequest request, HttpServletResponse response, BusinessException e) {
        log.error(ResultCode.INTERNAL_SERVER_ERROR + "：", e);
        return Response
            .builder()
            .code(e.getResultCode().getCode())
            .msg(e.getMessage())
            .timestamp(Response.currentTimestamp())
            .build();
    }


    @ExceptionHandler(Throwable.class)
    public Response handleError(HttpServletRequest request, HttpServletResponse response, Throwable e) {
        log.error(ResultCode.INTERNAL_SERVER_ERROR.getMsg() + "：", e);
        return Response.error(ResultCode.INTERNAL_SERVER_ERROR);
    }

}
