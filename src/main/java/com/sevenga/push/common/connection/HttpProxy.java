package com.sevenga.push.common.connection;

/**
 * Created by lizi on 15/9/7.
 */
import com.sevenga.push.common.ServiceHelper;
import com.sevenga.push.utils.Preconditions;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpProxy {
    private static final Logger LOG = LoggerFactory.getLogger(NativeHttpClient.class);
    private String host;
    private int port;
    private String username;
    private String password;
    private boolean authenticationNeeded;

    public HttpProxy(String host, int port) {
        this.authenticationNeeded = false;
        this.host = host;
        this.port = port;
    }

    public HttpProxy(String host, int port, String username, String password) {
        this(host, port);
        Preconditions.checkArgument(null != username, "username should not be null");
        Preconditions.checkArgument(null != password, "password should not be null");
        this.username = username;
        this.password = password;
        this.authenticationNeeded = true;
        LOG.info("Http Proxy - host:" + host + ", port:" + port + ", username:" + username + ", password:" + password);
    }

    public Proxy getNetProxy() {
        return new Proxy(Type.HTTP, new InetSocketAddress(this.host, this.port));
    }

    public boolean isAuthenticationNeeded() {
        return this.authenticationNeeded;
    }

    public String getProxyAuthorization() {
        return ServiceHelper.getBasicAuthorization(this.username, this.password);
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }
}

