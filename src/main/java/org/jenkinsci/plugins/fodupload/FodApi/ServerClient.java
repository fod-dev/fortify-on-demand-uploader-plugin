package org.jenkinsci.plugins.fodupload.FodApi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

class ServerClient implements IHttpClient {
    private OkHttpClient _client;

    public ServerClient(OkHttpClient client) {
        _client = client;
    }

    public Response execute(Request request) throws IOException {
        return _client.newCall(request).execute();
    }
}
