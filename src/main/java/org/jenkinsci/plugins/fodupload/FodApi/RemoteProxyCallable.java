package org.jenkinsci.plugins.fodupload.FodApi;

import hudson.ProxyConfiguration;
import jenkins.security.MasterToSlaveCallable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

class RemoteProxyCallable extends MasterToSlaveCallable<Response, IOException> {
    private int _connectionTimeout;
    private int _writeTimeout;
    private int _readTimeout;
    private ProxyConfiguration _proxy;

    private Request _request;

    RemoteProxyCallable(Request request, int connectionTimeout, int writeTimeout, int readTimeout, ProxyConfiguration proxy) {
        _connectionTimeout = connectionTimeout;
        _writeTimeout = writeTimeout;
        _readTimeout = readTimeout;
        _proxy = proxy;
        _request = request;
    }

    @Override
    public Response call() throws IOException {
        OkHttpClient client = Utils.CreateOkHttpClient(_connectionTimeout, _writeTimeout, _readTimeout, _proxy);

        return client.newCall(_request).execute();
    }
}
