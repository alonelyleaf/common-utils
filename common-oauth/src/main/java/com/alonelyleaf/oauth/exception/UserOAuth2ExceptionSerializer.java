package com.alonelyleaf.oauth.exception;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Map;

public class UserOAuth2ExceptionSerializer extends StdSerializer<UserOAuth2Exception> {

    protected UserOAuth2ExceptionSerializer() {
        super(UserOAuth2Exception.class);
    }

    @Override
    public void serialize(UserOAuth2Exception e, JsonGenerator generator, SerializerProvider serializerProvider) throws IOException {
        generator.writeStartObject();
        generator.writeObjectField("code", e.getHttpErrorCode());
        generator.writeStringField("message", e.getMessage());
        generator.writeStringField("data", null);
        generator.writeStringField("stackMessage", "");
        if (e.getAdditionalInformation() != null) {
            for (Map.Entry<String, String> entry : e.getAdditionalInformation().entrySet()) {
                String key = entry.getKey();
                String add = entry.getValue();
                generator.writeStringField(key, add);
            }
        }
        generator.writeEndObject();
    }
}
