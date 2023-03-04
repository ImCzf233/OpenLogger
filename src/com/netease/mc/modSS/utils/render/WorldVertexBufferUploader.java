package com.netease.mc.modSS.utils.render;

import net.minecraftforge.fml.relauncher.*;
import net.minecraft.client.renderer.vertex.*;
import java.nio.*;
import java.util.*;
import org.lwjgl.opengl.*;

@SideOnly(Side.CLIENT)
public class WorldVertexBufferUploader
{
    public void draw(final BufferBuilder bufferBuilderIn) {
        if (bufferBuilderIn.getVertexCount() > 0) {
            final VertexFormat vertexformat = bufferBuilderIn.getVertexFormat();
            final int i = vertexformat.getNextOffset();
            final ByteBuffer bytebuffer = bufferBuilderIn.getByteBuffer();
            final List<VertexFormatElement> list = (List<VertexFormatElement>)vertexformat.getElements();
            for (int j = 0; j < list.size(); ++j) {
                final VertexFormatElement vertexformatelement = list.get(j);
                final VertexFormatElement.EnumUsage vertexformatelement$enumusage = vertexformatelement.getUsage();
                final int k = vertexformatelement.getType().getGlConstant();
                final int l = vertexformatelement.getIndex();
                bytebuffer.position(vertexformat.getOffset(j));
                vertexformatelement.getUsage().preDraw(vertexformat, j, i, bytebuffer);
            }
            glDrawArrays(bufferBuilderIn.getDrawMode(), 0, bufferBuilderIn.getVertexCount());
            for (int i2 = 0, j2 = list.size(); i2 < j2; ++i2) {
                final VertexFormatElement vertexformatelement2 = list.get(i2);
                final VertexFormatElement.EnumUsage vertexformatelement$enumusage2 = vertexformatelement2.getUsage();
                final int k2 = vertexformatelement2.getIndex();
                vertexformatelement2.getUsage().postDraw(vertexformat, i2, i, bytebuffer);
            }
        }
        bufferBuilderIn.reset();
    }
    
    public static void glDrawArrays(final int mode, final int first, final int count) {
        GL11.glDrawArrays(mode, first, count);
    }
}
