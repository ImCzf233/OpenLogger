package com.netease.mc.modSS.utils;

import java.awt.image.*;
import java.awt.*;
import java.io.*;

public class ResourcePackImageScaler
{
    public static final int SIZE = 64;
    
    public static BufferedImage scalePackImage(final BufferedImage image) throws IOException {
        if (image == null) {
            return null;
        }
        final BufferedImage smallImage = new BufferedImage(64, 64, 2);
        final Graphics graphics = smallImage.getGraphics();
        graphics.drawImage(image, 0, 0, 64, 64, null);
        graphics.dispose();
        return smallImage;
    }
}
