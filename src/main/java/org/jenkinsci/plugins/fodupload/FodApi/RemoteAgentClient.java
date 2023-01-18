package org.jenkinsci.plugins.fodupload.FodApi;

import hudson.Launcher;
import hudson.ProxyConfiguration;
import hudson.model.TaskListener;
import hudson.remoting.VirtualChannel;
import jenkins.security.MasterToSlaveCallable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

class RemoteAgentClient implements IHttpClient {
    private int _connectionTimeout;
    private int _writeTimeout;
    private int _readTimeout;
    private ProxyConfiguration _proxy;
    private VirtualChannel _channel;

    public RemoteAgentClient(int connectionTimeout, int writeTimeout, int readTimeout, ProxyConfiguration proxy, Launcher launcher) {
        this(connectionTimeout, writeTimeout, readTimeout, proxy);

        _channel = launcher.getChannel();
        if (_channel == null) {
            throw new IllegalStateException("Launcher doesn't support remoting but it is required");
        }
    }

    public RemoteAgentClient(int connectionTimeout, int writeTimeout, int readTimeout, ProxyConfiguration proxy, VirtualChannel channel) {
        this(connectionTimeout, writeTimeout, readTimeout, proxy);

        _channel = channel;
        if (channel == null) {
            throw new IllegalStateException("Launcher doesn't support remoting but it is required");
        }
    }

    private RemoteAgentClient(int connectionTimeout, int writeTimeout, int readTimeout, ProxyConfiguration proxy) {
        _connectionTimeout = connectionTimeout;
        _writeTimeout = writeTimeout;

        _readTimeout = readTimeout;
        _proxy = proxy;
    }

    public Response execute(Request request) throws IOException {
        RemoteProxyCallable callable = new RemoteProxyCallable(request, _connectionTimeout, _writeTimeout, _readTimeout, _proxy);

        try {
            return _channel.call(callable);
        } catch (InterruptedException e) {
            throw new IOException("Remote agent http call failed", e);
        }
    }


}

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



