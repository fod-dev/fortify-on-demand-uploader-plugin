package org.jenkinsci.plugins.fodupload.FodApi;

import java.io.Serializable;
import java.util.*;

public abstract class HttpRequest implements Serializable {
    private final String _url;
    private final List<Map.Entry<String, String>> _headers;
    private final Verb _verb;

    public static HttpRequest Json(String url, Verb verb, String body) {
        return new StringBodyRequest(url, verb, "application/json", body);
    }

    public static HttpRequest Get(String url) {
        return new StringBodyRequest(url, Verb.Get, null, null);
    }

    HttpRequest(String url, Verb verb) {
        _url = url;
        _headers = new ArrayList<>();
        _verb = verb;
    }

    public String url() {
        return _url;
    }

    public Collection<Map.Entry<String, String>> headers() {
        return _headers;
    }

    public Verb verb() {
        return _verb;
    }

    public HttpRequest addHeader(String name, String value) {
        _headers.add(new AbstractMap.SimpleEntry<>(name, value));
        return this;
    }

    public HttpRequest setHeader(String name, String value) {
        for (Map.Entry<String, String> h : _headers) {
            if (h.getKey().equalsIgnoreCase(name)) _headers.remove(h);
        }
        _headers.add(new AbstractMap.SimpleEntry<>(name, value));
        return this;
    }

    public enum Verb {
        Get("GET"), Post("POST"), Put("PUT"), Patch("PATCH"), Delete("DELETE");

        private final String _val;

        Verb(String val) {
            this._val = val;
        }


    }
}

