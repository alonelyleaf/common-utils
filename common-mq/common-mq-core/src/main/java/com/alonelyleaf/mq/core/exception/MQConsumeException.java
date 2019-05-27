package com.alonelyleaf.mq.core.exception;

public class MQConsumeException extends MQException {

    public MQConsumeException() {
        super();
    }

    public MQConsumeException(String message) {
        super(message);
    }

    public MQConsumeException(String message, Throwable cause) {
        super(message, cause);
    }

    public MQConsumeException(Throwable cause) {
        super(cause);
    }
}
