package com.sopt.dowadog.model.common;

import com.sopt.dowadog.util.ResponseMessage;
import com.sopt.dowadog.util.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DefaultRes<T> {

    private int status;
    private String message;
    private T data;

    public DefaultRes(final int status, final String message) {
        this.status = status;
        this.message = message;
        this.data = null;
    }

    public static<T> DefaultRes<T> res(final int status, final String message) {
        return res(status, message, null);
    }

    public static<T> DefaultRes<T> res(final int status, final String message, final T t) {
        return DefaultRes.<T>builder()
                .data(t)
                .status(status)
                .message(message)
                .build();
    }

    public static final DefaultRes UNAUTHORIZATION = new DefaultRes(StatusCode.UNAUTHORIZED, ResponseMessage.AUTH_FAIL);

    public static final DefaultRes NOT_FOUND = new DefaultRes(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND);

    public static final DefaultRes FAIL_DEFAULT_RES = new DefaultRes(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR);
}