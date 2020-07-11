package com.ywh.olrn.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.servlet.http.HttpServletResponse;

/**
 * @author ywh
 * @since 11/07/2020
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS(0, "操作成功"),

    FAILURE(HttpServletResponse.SC_BAD_REQUEST, "业务异常"),

    TICKET_INVALID(HttpServletResponse.SC_UNAUTHORIZED, "Service Ticket 无效"),

    UNAUTHORIZED(HttpServletResponse.SC_UNAUTHORIZED, "访问需授权"),

    LOGOUT_REQUIRED(HttpServletResponse.SC_UNAUTHORIZED, "请先登出"),

    NOT_FOUND(HttpServletResponse.SC_NOT_FOUND, "资源不存在"),

    MSG_NOT_READABLE(HttpServletResponse.SC_BAD_REQUEST, "数据解析错误"),

    METHOD_NOT_SUPPORTED(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "错误的请求方法"),

    MEDIA_TYPE_NOT_SUPPORTED(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "不支持的 Media Type"),

    REQ_REJECT(HttpServletResponse.SC_FORBIDDEN, "禁止访问"),

    INTERNAL_SERVER_ERROR(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "服务器错误"),

    PARAM_MISS(HttpServletResponse.SC_BAD_REQUEST, "缺少必要参数"),

    PARAM_TYPE_ERROR(HttpServletResponse.SC_BAD_REQUEST, "参数类型错误"),

    PARAM_BIND_ERROR(HttpServletResponse.SC_BAD_REQUEST, "参数绑定错误"),

    PARAM_VALID_ERROR(HttpServletResponse.SC_BAD_REQUEST, "非法的参数值"),

    PARAM_CONVERT_ERROR(HttpServletResponse.SC_BAD_REQUEST, "参数类型转换错误");

    private final int code;

    private final String msg;
}
