package com.alonelyleaf.spring.handler;

import com.alonelyleaf.spring.common.i18n.Message;
import com.alonelyleaf.util.exception.business.BadRequestException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.alonelyleaf.util.exception.business.BusinessException;
import com.alonelyleaf.util.result.error.Error;
import com.alonelyleaf.util.result.Result;
import com.alonelyleaf.util.result.StatusCode;
import com.alonelyleaf.util.result.error.FieldError;
import com.alonelyleaf.util.JSONUtil;
import com.alonelyleaf.util.ServletUtil;
import com.alonelyleaf.util.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 统一异常处理类
 *
 */
@ControllerAdvice
public class BusinessExceptionHandler {

    protected Logger logger = LoggerFactory.getLogger("BusinessHandler");

    /**
     * 处理参数错误
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(HttpServletRequest request, BadRequestException e) {

        Result result = new Result().error(new Error().setErrorCode(e.getCode()).setMsg(e.getMessage())
                .setFieldErrors(e.getFieldErrors()))
                .setData(e.getData());

        logger.info("[BUSINESS-HANDLE]request: {} return: {}",
                ServletUtil.getRequestURI(request), JSONUtil.serialize(result));

        return new ResponseEntity(result, HttpStatus.OK);
    }

    /**
     * 处理json类型错误
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(HttpServletRequest request, BusinessException e) {

        Result result = createErrorResult(e.getCode(), e).setData(e.getData());
        logger.info("[BUSINESS-HANDLE]request: {} return: {}",
                ServletUtil.getRequestURI(request), JSONUtil.serialize(result));

        return new ResponseEntity(result, HttpStatus.OK);
    }

    /**
     * 处理请求方法不合法错误
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleHttpRequestMethodNotSupportedException(HttpServletRequest request, Exception e) {

        return new ResponseEntity<Object>(createErrorResult(StatusCode.BAD_REQUEST.getCode(), e), HttpStatus.OK);
    }

    /**
     * 处理请求参数类型不正确错误
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(value = HttpMessageConversionException.class)
    public ResponseEntity<Object> handleHttpMessageConversionException(HttpServletRequest request, Exception e) {

        return new ResponseEntity<>(createProtocolNotMatchResult(e), HttpStatus.OK);
    }

    /**
     * 构造请求参数错误结果
     *
     * @param e
     * @return
     */
    private Result createProtocolNotMatchResult(Exception e) {

        Result result = new Result().error(new Error().setErrorCode(StatusCode.PROTOCOL_NOT_MATCH.getCode()));

        if (ValidateUtil.isNotEmpty(e.getCause()) && e.getCause() instanceof JsonMappingException) {

            JsonMappingException jsonMappingException = (JsonMappingException) e.getCause();

            List<FieldError> fieldErrorList = jsonMappingException.getPath().stream()
                    .map(reference -> new FieldError().setField(reference.getFieldName()).setMsg(Message.Common.PROTOCOL_NOT_MATCH))
                    .collect(Collectors.toList());

            result.getError().setFieldErrors(fieldErrorList);
        }

        return result;
    }

    /**
     * 对于未能捕获的异常返回500
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(value = Throwable.class)
    public ResponseEntity<Object> handleException(HttpServletRequest request, Exception e) {

        StringBuilder sb = new StringBuilder();
        sb.append("[EXCEPTION-HANDLE]");
        sb.append(" request: ").append(ServletUtil.getRequestURI(request));
        sb.append(" error: ").append(e.getMessage());

        logger.error(sb.toString(), e);

        return new ResponseEntity<Object>(createUnCatchErrorResult(StatusCode.INTERNAL_ERROR.getCode(), e), HttpStatus.OK);
    }

    /**
     * 未捕获异常处理
     *
     * @param code
     * @param e
     * @return
     */
    protected Result createUnCatchErrorResult(Integer code, Exception e) {
        // DEFAULT:do not throw the exception message to client
        return createErrorResult(code, Message.Common.INTERNAL_ERROR);
    }

    protected Result createErrorResult(Integer code, Exception e) {
        return createErrorResult(code, e.getMessage());
    }

    protected Result createErrorResult(Integer code, String message) {
        return new Result().error(new Error().setErrorCode(code).setMsg(message));
    }
}
