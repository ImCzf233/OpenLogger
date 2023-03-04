package com.netease.mc.modSS.ui.external;

import java.io.*;
import java.net.*;

public class UIStart extends Thread
{
    public static ServerSocket serverSocket;
    public static int port;
    public static Thread external;
    
    @Override
    public void run() {
        try {
            UIStart.serverSocket = new ServerSocket(UIStart.port);
            while (true) {
                final Socket socket = UIStart.serverSocket.accept();
                (UIStart.external = new Thread(new ExternalTheard(socket))).start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            super.run();
        }
    }
    
    static {
        UIStart.serverSocket = null;
        UIStart.port = 6666;
        UIStart.external = null;
    }
}
