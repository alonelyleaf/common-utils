package com.alonelyleaf.mq.core.exception;

/**
 * 消费过程中抛出此异常，消息会重新投递
 *
 */
public class RetryException extends MQException {

    public RetryException() {
        super();
    }

    public RetryException(String message) {
        super(message);
    }

    public RetryException(String message, Throwable cause) {
        super(message, cause);
    }

    public RetryException(Throwable cause) {
        super(cause);
    }
}
