package org.jenkinsci.plugins.fodupload.FodApi;

import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public interface IHttpClient {
    Response execute(Request request) throws IOException;
}
