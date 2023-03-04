package com.netease.mc.modSS.protecter.debugger;

import com.netease.mc.modSS.*;
import java.lang.reflect.*;
import sun.misc.*;
import java.util.*;
import java.security.*;
import java.io.*;
import java.math.*;

public class ShellNative
{
    public static byte[] getClassByteCode(final String className) {
        final String jarname = "/" + className.replace('.', '/') + ".class";
        final InputStream is = ShellSock.class.getResourceAsStream(jarname);
        final ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        byte[] imgdata = null;
        try {
            int ch;
            while ((ch = is.read()) != -1) {
                bytestream.write(ch);
            }
            imgdata = bytestream.toByteArray();
        }
        catch (IOException e) {
            e.printStackTrace();
            try {
                bytestream.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        finally {
            try {
                bytestream.close();
            }
            catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        return imgdata;
    }
    
    public static void writeFileByBytes(final String fileName, final byte[] bytes, final boolean append) throws IOException {
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(fileName, append));
            out.write(bytes);
        }
        finally {
            if (out != null) {
                out.close();
            }
        }
    }
    
    public static void SelfAttach() {
        try {
            final Class cls = Class.forName("sun.tools.attach.HotSpotVirtualMachine");
            final Field field = cls.getDeclaredField("ALLOW_ATTACH_SELF");
            field.setAccessible(true);
            final Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setInt(field, field.getModifiers() & 0xFFFFFFEF);
            field.setBoolean(null, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static long getAgent(final long jvmtiAddress) {
        final Unsafe unsafe = getUnsafe();
        final long agentAddr = unsafe.allocateMemory(512L);
        final long jvmtiStackAddr = unsafe.allocateMemory(512L);
        unsafe.putLong(jvmtiStackAddr, jvmtiAddress);
        unsafe.putLong(jvmtiStackAddr + 8L, 3459047088308908526L);
        unsafe.putLong(jvmtiStackAddr + 360L, -8029759187451903488L);
        System.out.println("long:" + Long.toHexString(jvmtiStackAddr + 360L));
        unsafe.putLong(agentAddr, jvmtiAddress - 144624L);
        unsafe.putLong(agentAddr + 8L, jvmtiStackAddr);
        unsafe.putLong(agentAddr + 16L, agentAddr);
        unsafe.putLong(agentAddr + 24L, 32370056120500224L);
        unsafe.putLong(agentAddr + 32L, jvmtiStackAddr);
        unsafe.putLong(agentAddr + 40L, agentAddr);
        unsafe.putLong(agentAddr + 48L, 15762796267503617L);
        unsafe.putLong(agentAddr + 56L, 0L);
        unsafe.putLong(agentAddr + 64L, 0L);
        unsafe.putLong(agentAddr + 72L, 0L);
        unsafe.putLong(agentAddr + 80L, 0L);
        unsafe.putLong(agentAddr + 88L, 32088645561286657L);
        unsafe.putLong(agentAddr + 96L, agentAddr + 104L);
        unsafe.putLong(agentAddr + 104L, 18367622009667905L);
        return agentAddr;
    }
    
    private static Unsafe getUnsafe() {
        Unsafe unsafe = null;
        try {
            final Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe)field.get(null);
        }
        catch (Exception e) {
            throw new AssertionError((Object)e);
        }
        return unsafe;
    }
    
    public static String encodeHexString(final byte[] data) {
        return new String(encodeHex(data));
    }
    
    public static char[] encodeHex(final byte[] data) {
        return encodeHex(data, true);
    }
    
    public static char[] encodeHex(final byte[] data, final boolean toLowerCase) {
        final char[] DIGITS_LOWER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        final char[] DIGITS_UPPER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }
    
    protected static char[] encodeHex(final byte[] data, final char[] toDigits) {
        final int l = data.length;
        final char[] out = new char[l << 1];
        int i = 0;
        int j = 0;
        while (i < l) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0xF & data[i]];
            ++i;
        }
        return out;
    }
    
    public static String readFile(final String fileName) {
        try {
            final File file = new File(fileName);
            final FileReader fr = new FileReader(file);
            final BufferedReader br = new BufferedReader(fr);
            final String line;
            if ((line = br.readLine()) != null) {
                return line;
            }
            br.close();
            fr.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        return null;
    }
    
    public static String getCPUSerialNumber() {
        String serial;
        try {
            final Process process = Runtime.getRuntime().exec(new String[] { "wmic", "cpu", "get", "ProcessorId" });
            process.getOutputStream().close();
            final Scanner sc = new Scanner(process.getInputStream());
            serial = sc.next();
            serial = sc.next();
        }
        catch (IOException e) {
            throw new RuntimeException("");
        }
        return serial;
    }
    
    public static String getMD5Str(final String str) {
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
    
    public static String bytesToHex(final byte[] bytes) {
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
    
    public static String getSubString(final String text, final String left, final String right) {
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
    
    public static byte[] toBytes(final String str) {
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
}
