package org.jenkinsci.plugins.fodupload.FodApi;

import hudson.ProxyConfiguration;
import javafx.util.Pair;
import jenkins.security.MasterToSlaveCallable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;

class RemoteProxyCallable extends MasterToSlaveCallable<ResponseContent, IOException> {
    private int _connectionTimeout;
    private int _writeTimeout;
    private int _readTimeout;
    private ProxyConfiguration _proxy;

    private HttpRequest _request;

    RemoteProxyCallable(HttpRequest request, int connectionTimeout, int writeTimeout, int readTimeout, ProxyConfiguration proxy) {
        _connectionTimeout = connectionTimeout;
        _writeTimeout = writeTimeout;
        _readTimeout = readTimeout;
        _proxy = proxy;
        _request = request;
    }

    @Override
    public ResponseContent call() throws IOException {
        OkHttpClient client = Utils.CreateOkHttpClient(_connectionTimeout, _writeTimeout, _readTimeout, _proxy);

        return Utils.ResponseContentFromOkHttp3(client.newCall(Utils.HttpRequestToOkHttpRequest(_request)).execute());
    }


}
