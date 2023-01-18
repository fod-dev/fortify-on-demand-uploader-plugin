package org.jenkinsci.plugins.fodupload.FodApi;

import hudson.ProxyConfiguration;
import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

class Utils {

    /**
     * Creates a okHttp client to connect with.
     * <p>
     * at_return returns a client object
     */
    static OkHttpClient CreateOkHttpClient(int connectionTimeout, int writeTimeout, int readTimeout, ProxyConfiguration proxy) {
        OkHttpClient.Builder baseClient = new OkHttpClient().newBuilder()
                .connectTimeout(connectionTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS);

        // If there's no proxy just create a normal client
        if (proxy == null)
            return baseClient.build();

        OkHttpClient.Builder proxyClient = baseClient
                .proxy(new java.net.Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(proxy.name, proxy.port)));
        // Otherwise set up proxy
        Authenticator proxyAuthenticator;
        final String credentials = Credentials.basic(proxy.getUserName(), proxy.getPassword());

        proxyAuthenticator = (route, response) -> response.request().newBuilder()
                .header("Proxy-Authorization", credentials)
                .build();

        proxyClient.proxyAuthenticator(proxyAuthenticator);
        return proxyClient.build();
    }

}
