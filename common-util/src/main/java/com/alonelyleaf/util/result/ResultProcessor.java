package com.alonelyleaf.util.result;


import com.alonelyleaf.util.CommonUtil;
import com.alonelyleaf.util.exception.business.*;

import java.lang.reflect.Array;
import java.util.Collection;


public class ResultProcessor extends CommonUtil {

    /**
     * 判断调用接口是否错误，错误返回true
     *
     * @param result
     * @return
     */
    public static boolean isError(Result result) {
        return result.getRet() < 0;
    }

    /**
     * 判断调用接口是否成功，成功返回true
     *
     * @param result
     * @return
     */
    public static boolean isSuccess(Result result) {
        return result.getRet() >= 0;
    }

    /**
     * 失败
     *
     * @return
     */
    public Result fail() {

        return new Result().setRet(-1);
    }

    /**
     * 失败
     *
     * @param data
     * @return
     */
    public Result fail(Object data) {

        return fail().setData(data);
    }

    /**
     * 成功
     *
     * @return
     */
    public Result success() {
        return new Result().setRet(0);
    }

    /**
     * 成功
     *
     * @param data
     * @return
     */
    public Result success(Object data) {

        if (data == null) {
            return success();
        }

        if (data instanceof Collection) {
            Collection collection = (Collection) data;
            return new Result().setRet(collection.size()).setData(data);
        }

        if (data.getClass().isArray()) {
            return new Result().setRet(Array.getLength(data)).setData(data);
        }

        return new Result().setRet(1).setData(data);
    }

    /**
     * 参数错误
     *
     * @param field
     * @param message
     */
    public void throwBadRequestException(String field, String message) {

        throw new BadRequestException(field, message);
    }

    public void throwBadRequestException(String field, String message, Object data) {

        throw new BadRequestException(field, message, data);
    }


    /**
     * 参数错误
     *
     * @param code
     * @param field
     * @param message
     */
    public void throwBadRequestException(Integer code, String field, String message) {

        throw new BadRequestException(code, field, message);
    }

    public void throwBadRequestException(Integer code, String field, String message, Object data) {

        throw new BadRequestException(code, field, message).setData(data);
    }

    /**
     * 接口参数错误
     *
     * @param field
     * @param message
     */
    public void throwProtocolNotMatchException(String field, String message) {

        throw new ProtocolNotMatchException(field, message);
    }

    public void throwProtocolNotMatchException(String field, String message, Object data) {

        throw new ProtocolNotMatchException(field, message, data);
    }

    /**
     * 前置条件不满足
     *
     * @param message
     */
    public void throwPreconditionFailedException(String message) {

        throw new PreconditionFailedException(message);
    }

    public void throwPreconditionFailedException(String message, Object data) {

        throw new PreconditionFailedException(message, data);
    }

    /**
     * 用户尚未登录
     *
     * @param message
     */
    public void throwUnauthorizedException(String message) {

        throw new UnauthorizedException(message);
    }

    public void throwUnauthorizedException(String message, Object data) {

        throw new UnauthorizedException(message, data);
    }

    /**
     * 服务器内部异常
     *
     * @param message
     */
    public void throwInternalServerException(String message) {

        throw new InternalServerException(message);
    }

    public void throwInternalServerException(String message, Object data) {

        throw new InternalServerException(message, data);
    }

    /**
     * 远程服务不可达
     *
     * @param message
     */
    public void throwRemoteServiceUnavailableException(String message) {

        throw new RemoteServiceUnavailableException(message);
    }

    public void throwRemoteServiceUnavailableException(String message, Object data) {

        throw new RemoteServiceUnavailableException(message, data);
    }

    /**
     * 资源冲突
     *
     * @param message
     */
    public void throwResourceConflictException(String message) {

        throw new ResourceConflictException(message);
    }

    public void throwResourceConflictException(String message, Object data) {

        throw new ResourceConflictException(message, data);
    }

    /**
     * 资源未找到
     *
     * @param message
     */
    public void throwResourceNotFoundException(String message) {

        throw new ResourceNotFoundException(message);
    }

    public void throwResourceNotFoundException(String message, Object data) {

        throw new ResourceNotFoundException(message, data);
    }

    /**
     * 请求不被接收
     *
     * @param message
     */
    public void throwNotAcceptableException(String message) {

        throw new NotAcceptableException(message);
    }

    public void throwNotAcceptableException(String message, Object data) {

        throw new NotAcceptableException(message, data);
    }

    /**
     * 请求被拒绝
     *
     * @param message
     */
    public void throwForbiddenException(String message) {

        throw new ForbiddenException(message);
    }

    public void throwForbiddenException(String message, Object data) {

        throw new ForbiddenException(message, data);
    }


    // ---------------------------- private -------------------------------

}
