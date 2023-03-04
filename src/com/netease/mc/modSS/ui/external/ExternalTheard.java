package com.netease.mc.modSS.ui.external;

import java.net.*;
import java.io.*;
import com.netease.mc.modSS.*;

public class ExternalTheard implements Runnable
{
    private Socket socket;
    
    public ExternalTheard(final Socket socket) {
        this.socket = null;
        this.socket = socket;
    }
    
    public PrintWriter getWriter(final Socket socket) throws IOException {
        final OutputStream socketOut = socket.getOutputStream();
        return new PrintWriter(socketOut, true);
    }
    
    public BufferedReader getReader(final Socket socket) throws IOException {
        final InputStreamReader socketIn = new InputStreamReader(socket.getInputStream());
        return new BufferedReader(socketIn);
    }
    
    public String Echo(final String msg) {
        return msg;
    }
    
    @Override
    public void run() {
        try {
            System.out.println("New Connection " + this.socket.getInetAddress() + ":" + this.socket.getPort());
            final BufferedReader br = this.getReader(this.socket);
            final PrintWriter pw = this.getWriter(this.socket);
            String msg = null;
            while ((msg = br.readLine()) != null) {
                System.out.println(msg);
                pw.println(msg);
                System.out.println(msg.split("\\[ZWX]")[0]);
                if (msg.split("\\[ZWX]")[0].equals("Module")) {
                    final String name = msg.split("\\[ZWX]")[1];
                    final boolean state = Boolean.parseBoolean(msg.split("\\[ZWX]")[2]);
                    if (ShellSock.getClient().modManager.getModulebyName(name) != null) {
                        System.out.println(12345);
                        if (state != ShellSock.getClient().modManager.getModulebyName(name).isEnabled()) {
                            ShellSock.getClient().modManager.getModulebyName(name).toggle();
                        }
                    }
                }
                if (msg.equals("exit")) {
                    break;
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            try {
                if (this.socket != null) {
                    this.socket.close();
                }
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        finally {
            try {
                if (this.socket != null) {
                    this.socket.close();
                }
            }
            catch (Exception e3) {
                e3.printStackTrace();
            }
        }
    }
}
