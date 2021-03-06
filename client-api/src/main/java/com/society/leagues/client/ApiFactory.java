package com.society.leagues.client;

import com.society.leagues.client.filter.ExceptionFilter;
import com.society.leagues.client.filter.TokenFilter;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.restz.client.RestProxyFactory;
import org.restz.client.filter.DeprecatedResponseHandler;
import org.restz.client.filter.MethodCallFilter;
import org.restz.client.filter.VersionFilter;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class ApiFactory {

    public static <T> T createApi(Class<T> api, String url) {
        return createApi(api,null,url,false);
    }

    public static <T> T createApi(Class<T> api, String token, String url) {
        return createApi(api,token,url,false);
    }

    public static <T> T createApi(Class<T> api, String token, String url, boolean debug) {

        ClientConfig config = new ClientConfig().
                register(DeprecatedResponseHandler.class).
                register(MethodCallFilter.class).
                register(new VersionFilter(api)).
                register(new TokenFilter(token)).
                register(ExceptionFilter.class);

        if (debug) {
            java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(LoggingFilter.class.getName());
            LoggingFilter loggingFilter  = new LoggingFilter(LOGGER,true);
            config = config.register(loggingFilter);
        }

        Client client = ClientBuilder.newClient(config);
        return RestProxyFactory.getRestClientApi(api,url,client);
    }
}
