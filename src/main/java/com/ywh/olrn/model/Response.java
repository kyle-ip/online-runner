package com.ywh.olrn.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ywh.olrn.exception.ResultCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author ywh
 * @since 11/07/2020
 */
@Builder
@ApiModel
@AllArgsConstructor
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> implements Serializable {

    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "响应码", example = "0")
    private int code;

    @ApiModelProperty(value = "提示信息")
    private String msg;

    @ApiModelProperty(value = "响应数据")
    private T data;

    @ApiModelProperty(value = "时间戳", example = "1567489129")
    private long timestamp;

    public static Response success() {
        return Response
            .builder()
            .code(ResultCode.SUCCESS.getCode())
            .msg(ResultCode.SUCCESS.getMsg())
            .timestamp(currentTimestamp())
            .build();
    }

    public static Response success(String msg) {
        return Response
            .builder()
            .code(ResultCode.SUCCESS.getCode())
            .msg(msg)
            .timestamp(currentTimestamp())
            .build();
    }

    public static Response success(Object data) {
        return Response
            .builder()
            .code(ResultCode.SUCCESS.getCode())
            .msg(ResultCode.SUCCESS.getMsg())
            .data(data)
            .timestamp(currentTimestamp())
            .build();
    }

    public static Response error() {
        return Response
            .builder()
            .code(ResultCode.INTERNAL_SERVER_ERROR.getCode())
            .msg(ResultCode.INTERNAL_SERVER_ERROR.getMsg())
            .timestamp(currentTimestamp())
            .build();
    }

    public static Response error(String msg) {
        return Response
            .builder()
            .code(ResultCode.INTERNAL_SERVER_ERROR.getCode())
            .msg(msg)
            .timestamp(currentTimestamp())
            .build();
    }

    public static Response error(ResultCode resultCode) {
        return Response
            .builder()
            .code(resultCode.getCode())
            .msg(resultCode.getMsg())
            .timestamp(currentTimestamp())
            .build();
    }

    public static long currentTimestamp() {
        return System.currentTimeMillis() / TimeUnit.SECONDS.toMillis(1);
    }
}
