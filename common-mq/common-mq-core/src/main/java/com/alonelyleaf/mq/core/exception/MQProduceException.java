package com.alonelyleaf.mq.core.exception;

public class MQProduceException extends MQException {

    public MQProduceException() {
        super();
    }

    public MQProduceException(String message) {
        super(message);
    }

    public MQProduceException(String message, Throwable cause) {
        super(message, cause);
    }

    public MQProduceException(Throwable cause) {
        super(cause);
    }
}
