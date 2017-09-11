package com.cheuks.bin.original.test.consul;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class server1 {

    private final static Logger LOG = LoggerFactory.getLogger(server1.class);

    public static void main(String[] args) throws InterruptedException {
        server1 sd = new server1();
        //健康检查
        sd.server_health();
        synchronized (server1.class) {
            server1.class.wait();
        }
    }

    public void server_health() {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    int port = 10010;
                    ServerSocket server = new ServerSocket(port);
                    Socket client;
                    OutputStream out;
                    while (true) {
                        client = server.accept();
                        System.err.println(String.format("有客到：%s,服务器侦听端口%d", client.getInetAddress().getHostAddress(), port));
                        out = client.getOutputStream();
                        out.write("hi".getBytes());
                        out.flush();
//                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

}
