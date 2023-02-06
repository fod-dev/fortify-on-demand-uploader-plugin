package org.jenkinsci.plugins.fodupload.FodApi;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

public class ResponseContent implements Serializable { //, AutoCloseable {
    private final transient InputStream _body;
    private final boolean _isSuccessful;
    private final int _code;
    private final String _message;

    private boolean _bodyParsed = false;
    private String _bodyContent = null;

    public ResponseContent(InputStream body, boolean isSuccessful, int code, String message) {
        _body = body;
        _isSuccessful = isSuccessful;
        _code = code;
        _message = message;
    }

    private String getRawBody(InputStream stream) throws IOException {
        if (stream == null) return null;

        String content = null;

        content = IOUtils.toString(stream, "utf-8");

        return content;
    }

    public void parseBody() throws IOException {
        if (!_bodyParsed) {
            _bodyParsed = true;
            if (_body == null) return;

            _bodyContent = IOUtils.toString(_body, "utf-8");
            _body.close();
        }
    }

    public String bodyContent() {
        return _bodyContent;
    }

    public boolean isSuccessful() {
        return _isSuccessful;
    }

    public int code() {
        return _code;
    }

    public String message() {
        return _message;
    }
}
