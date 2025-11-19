package com.offlineexam.network;

public class NetworkConfig {
    public static final String DEFAULT_HOST = "127.0.0.1";
    public static final int DEFAULT_PORT = 5000;

    public static String getServerHost() {
        return System.getProperty("exam.server.host", DEFAULT_HOST);
    }

    public static int getServerPort() {
        return Integer.getInteger("exam.server.port", DEFAULT_PORT);
    }
}
