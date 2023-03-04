package com.netease.mc.modSS.utils.render;

import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class MyTessellator
{
    private final BufferBuilder buffer;
    private final WorldVertexBufferUploader vboUploader;
    private static final MyTessellator INSTANCE;
    
    public static MyTessellator getInstance() {
        return MyTessellator.INSTANCE;
    }
    
    public MyTessellator(final int bufferSize) {
        this.vboUploader = new WorldVertexBufferUploader();
        this.buffer = new BufferBuilder(bufferSize);
    }
    
    public void draw() {
        this.buffer.finishDrawing();
        this.vboUploader.draw(this.buffer);
    }
    
    public BufferBuilder getBuffer() {
        return this.buffer;
    }
    
    static {
        INSTANCE = new MyTessellator(2097152);
    }
}
