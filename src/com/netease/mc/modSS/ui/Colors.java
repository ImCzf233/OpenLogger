package com.netease.mc.modSS.ui;

import java.util.regex.*;
import java.awt.*;
import java.text.*;

public class Colors
{
    private static final Pattern COLOR_PATTERN;
    
    public static int getColor(final Color color) {
        return getColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }
    
    public static int getColor(final int brightness) {
        return getColor(brightness, brightness, brightness, 255);
    }
    
    public static int getColor(final int brightness, final int alpha) {
        return getColor(brightness, brightness, brightness, alpha);
    }
    
    public static int getColor(final int red, final int green, final int blue) {
        return getColor(red, green, blue, 255);
    }
    
    public static int getColor(final int red, final int green, final int blue, final int alpha) {
        int color = 0;
        color |= alpha << 24;
        color |= red << 16;
        color |= green << 8;
        color |= blue;
        return color;
    }
    
    public static Color rainbow() {
        final long offset = 999999999999L;
        final float fade = 1.0f;
        final float hue = (System.nanoTime() + offset) / 1.0E10f % 1.0f;
        final long color = Long.parseLong(Integer.toHexString(Color.HSBtoRGB(hue, 1.0f, 1.0f)), 16);
        final Color c = new Color((int)color);
        return new Color(c.getRed() / 255.0f * fade, c.getGreen() / 255.0f * fade, c.getBlue() / 255.0f * fade, c.getAlpha() / 255.0f);
    }
    
    public static Color getRainbowColor(final float seconds, final float saturation, final float brightness, final long index) {
        final float hue = (System.currentTimeMillis() + index) % (int)(seconds * 1000.0f) / (seconds * 1000.0f);
        final Color color = Color.getHSBColor(hue, saturation, brightness);
        return color;
    }
    
    public static int reAlpha(final int color, final float alpha) {
        final Color c = new Color(color);
        final float r = 0.003921569f * c.getRed();
        final float g = 0.003921569f * c.getGreen();
        final float b = 0.003921569f * c.getBlue();
        return new Color(r, g, b, alpha).getRGB();
    }
    
    public static int getRainbowInt(final float seconds, final float saturation, final float brightness, final long index) {
        final float hue = (System.currentTimeMillis() + index) % (int)(seconds * 1000.0f) / (seconds * 1000.0f);
        final int color = Color.HSBtoRGB(hue, saturation, brightness);
        return color;
    }
    
    public static Color blendColors(final float[] fractions, final Color[] colors, final float progress) {
        if (fractions == null) {
            throw new IllegalArgumentException("Fractions can't be null");
        }
        if (colors == null) {
            throw new IllegalArgumentException("Colours can't be null");
        }
        if (fractions.length == colors.length) {
            final int[] getFractionBlack = getFraction(fractions, progress);
            final float[] range = { fractions[getFractionBlack[0]], fractions[getFractionBlack[1]] };
            final Color[] colorRange = { colors[getFractionBlack[0]], colors[getFractionBlack[1]] };
            final float max = range[1] - range[0];
            final float value = progress - range[0];
            final float weight = value / max;
            return blend(colorRange[0], colorRange[1], 1.0f - weight);
        }
        throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
    }
    
    public static int[] getFraction(final float[] fractions, final float progress) {
        final int[] range = new int[2];
        int startPoint;
        for (startPoint = 0; startPoint < fractions.length && fractions[startPoint] <= progress; ++startPoint) {}
        if (startPoint >= fractions.length) {
            startPoint = fractions.length - 1;
        }
        range[0] = startPoint - 1;
        range[1] = startPoint;
        return range;
    }
    
    public static Color blend(final Color color1, final Color color2, final double ratio) {
        final float r = (float)ratio;
        final float ir = 1.0f - r;
        final float[] rgb1 = new float[3];
        final float[] rgb2 = new float[3];
        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);
        float red = rgb1[0] * r + rgb2[0] * ir;
        float green = rgb1[1] * r + rgb2[1] * ir;
        float blue = rgb1[2] * r + rgb2[2] * ir;
        if (red < 0.0f) {
            red = 0.0f;
        }
        else if (red > 255.0f) {
            red = 255.0f;
        }
        if (green < 0.0f) {
            green = 0.0f;
        }
        else if (green > 255.0f) {
            green = 255.0f;
        }
        if (blue < 0.0f) {
            blue = 0.0f;
        }
        else if (blue > 255.0f) {
            blue = 255.0f;
        }
        Color color3 = null;
        try {
            color3 = new Color(red, green, blue);
        }
        catch (IllegalArgumentException exp) {
            final NumberFormat nf = NumberFormat.getNumberInstance();
            exp.printStackTrace();
        }
        return color3;
    }
    
    public static String getColorUnicode(final int n) {
        if (n != 1) {
            if (n == 2) {
                return "¡ìa";
            }
            if (n == 3) {
                return "¡ì3";
            }
            if (n == 4) {
                return "¡ì4";
            }
            if (n >= 5) {
                return "¡ìe";
            }
        }
        return "¡ìf";
    }
    
    public static int getColor(final float hueoffset, final float saturation, final float brightness) {
        final float speed = 4500.0f;
        final float hue = System.currentTimeMillis() % 4500L / 4500.0f;
        return Color.HSBtoRGB(hue - hueoffset / 54.0f, saturation, brightness);
    }
    
    public static String stripColor(final String text) {
        return Colors.COLOR_PATTERN.matcher(text).replaceAll("");
    }
    
    public static int color(final float r, final float g, final float b, final float a) {
        return new Color(r, g, b, a).getRGB();
    }
    
    public static int rainbow(final int delay) {
        double rState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
        rState %= 360.0;
        return Color.getHSBColor((float)(rState / 360.0), 0.6f, 1.0f).getRGB();
    }
    
    static {
        COLOR_PATTERN = Pattern.compile("(?i)¡ì[0-9A-FK-OR]");
    }
}
