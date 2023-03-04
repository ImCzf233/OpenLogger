package com.netease.mc.modSS.protecter;

import java.util.*;
import java.lang.reflect.*;
import java.security.*;
import java.io.*;
import java.math.*;

public class NativeLoader extends Thread
{
    private byte[][] classes;
    private String user;
    private String password;
    
    public NativeLoader(final byte[][] classes, final String user, final String password) {
        this.classes = null;
        this.user = null;
        this.password = null;
        this.classes = classes;
        this.user = user;
        this.password = password;
    }
    
    @Override
    public void run() {
        try {
            final String className = "LoadClient";
            System.out.println("Start Loading");
            ClassLoader contextClassLoader = null;
            for (final Thread thread : Thread.getAllStackTraces().keySet()) {
                if (thread.getName().toLowerCase().equals("client thread")) {
                    contextClassLoader = thread.getContextClassLoader();
                }
            }
            if (contextClassLoader == null) {
                return;
            }
            this.setContextClassLoader(contextClassLoader);
            final Method declaredMethod = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE, ProtectionDomain.class);
            declaredMethod.setAccessible(true);
            Class clazz = null;
            for (final byte[] array : this.classes) {
                final Class clazz2 = (Class)declaredMethod.invoke(contextClassLoader, null, array, 0, array.length, contextClassLoader.getClass().getProtectionDomain());
                if (clazz2 != null && clazz2.getName().contains(className)) {
                    clazz = clazz2;
                }
            }
            if (clazz != null) {
                clazz.getDeclaredMethod("RLoad", String.class, String.class).invoke(null, "FUCKU", "FUCKU");
            }
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        catch (InvocationTargetException e2) {
            e2.printStackTrace();
        }
        catch (IllegalAccessException e3) {
            e3.printStackTrace();
        }
    }
    
    public static int a(final byte[][] array, final String s, final String s2) {
        try {
            new NativeLoader(array, s, s2).run();
        }
        catch (Exception ex) {}
        return 0;
    }
    
    public static byte[][] a(final int n) {
        return new byte[n][];
    }
    
    public static String x(final String text, final String left, final String right) {
        String result = "";
        int zLen;
        if (left == null || left.isEmpty()) {
            zLen = 0;
        }
        else {
            zLen = text.indexOf(left);
            if (zLen > -1) {
                zLen += left.length();
            }
            else {
                zLen = 0;
            }
        }
        int yLen = text.indexOf(right, zLen);
        if (yLen < 0 || right == null || right.isEmpty()) {
            yLen = text.length();
        }
        result = text.substring(zLen, yLen);
        return result;
    }
    
    private static byte[] b(final String str) {
        if (str == null || str.trim().equals("")) {
            return new byte[0];
        }
        final byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; ++i) {
            final String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte)Integer.parseInt(subStr, 16);
        }
        return bytes;
    }
    
    private static String m(final String str) {
        byte[] digest = null;
        try {
            final MessageDigest md5 = MessageDigest.getInstance("md5");
            digest = md5.digest(str.getBytes("utf-8"));
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        }
        final String md5Str = new BigInteger(1, digest).toString(16);
        return md5Str;
    }
    
    private static String c(final byte[] bytes) {
        final char[] HEX_CHAR = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        final char[] buf = new char[bytes.length * 2];
        int a = 0;
        int index = 0;
        for (final byte b : bytes) {
            if (b < 0) {
                a = 256 + b;
            }
            else {
                a = b;
            }
            buf[index++] = HEX_CHAR[a / 16];
            buf[index++] = HEX_CHAR[a % 16];
        }
        return new String(buf);
    }
}
