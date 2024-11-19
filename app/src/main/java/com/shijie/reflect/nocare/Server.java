package com.shijie.reflect.nocare;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        int port = 12345;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("服务监听启动在端口" + port);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("建立新的连接");
                InputStream inputStream = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                String message = reader.readLine();
                System.out.println("收到消息：\n" + message);
                socket.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
