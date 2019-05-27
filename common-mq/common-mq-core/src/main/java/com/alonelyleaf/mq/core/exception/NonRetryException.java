package com.alonelyleaf.mq.core.exception;

/**
 * 消费过程中抛出此异常，消息将按照正常消费处理，不可再重复消费
 *
 */
public class NonRetryException extends MQException {

    public NonRetryException() {
        super();
    }

    public NonRetryException(String message) {
        super(message);
    }

    public NonRetryException(String message, Throwable cause) {
        super(message, cause);
    }

    public NonRetryException(Throwable cause) {
        super(cause);
    }
}
