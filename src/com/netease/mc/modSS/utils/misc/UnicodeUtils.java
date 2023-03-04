package com.netease.mc.modSS.utils.misc;

public class UnicodeUtils
{
    public static String cnToUnicode(final String cn) {
        final char[] chars = cn.toCharArray();
        String returnStr = "";
        for (int i = 0; i < chars.length; ++i) {
            returnStr = returnStr + "\\u" + Integer.toString(chars[i], 16);
        }
        return returnStr;
    }
    
    public static String unicodeToCn(final String unicode) {
        final String[] strs = unicode.split("\\\\u");
        String returnStr = "";
        for (int i = 1; i < strs.length; ++i) {
            returnStr += (char)(int)Integer.valueOf(strs[i], 16);
        }
        return returnStr;
    }
}
