package com.netease.mc.modSS.protecter.nativetest;

public class Native
{
    public native void ShowMSG();
    
    public static void runNative() {
        System.load("C:\\Users\\1234\\source\\repos\\TestJNI\\x64\\Release\\TestJNI.dll");
        final Native na = new Native();
        na.ShowMSG();
        System.out.println("Native");
    }
}
