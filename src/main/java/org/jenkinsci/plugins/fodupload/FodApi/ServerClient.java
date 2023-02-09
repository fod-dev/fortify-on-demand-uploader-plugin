package org.jenkinsci.plugins.fodupload.FodApi;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;

class ServerClient implements IHttpClient {
    private OkHttpClient _client;

    public ServerClient(OkHttpClient client) {
        _client = client;
    }

    public ResponseContent execute(HttpRequest request) throws IOException {
        return Utils.ResponseContentFromOkHttp3(_client.newCall(Utils.HttpRequestToOkHttpRequest(request)).execute());
    }

    public ResponseContent execute(Request request) throws IOException {
        return Utils.ResponseContentFromOkHttp3(_client.newCall(request).execute());
    }

    OkHttpClient client(){
        return _client;
    }
}
