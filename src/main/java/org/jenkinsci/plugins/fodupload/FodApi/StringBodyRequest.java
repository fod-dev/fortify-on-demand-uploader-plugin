package org.jenkinsci.plugins.fodupload.FodApi;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressFBWarnings("SE_NO_SERIALVERSIONID")
public class StringBodyRequest extends HttpRequest {
    private final String _body;
    private final String _contentType;

    public StringBodyRequest(String url, Verb verb, String contentType, String body) {
        super(url, verb);
        _body = body;
        _contentType = contentType;
    }

    public String body() {
        return _body;
    }

    public String contentType() {
        return _contentType;
    }
}

