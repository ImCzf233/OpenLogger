package com.netease.mc.modSS.utils.render;

import net.minecraftforge.fml.relauncher.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.client.renderer.*;
import net.minecraft.util.*;
import com.google.common.primitives.*;
import java.util.*;
import java.nio.*;
import org.apache.logging.log4j.*;

@SideOnly(Side.CLIENT)
public class BufferBuilder
{
    private static final Logger LOGGER;
    private ByteBuffer byteBuffer;
    private IntBuffer rawIntBuffer;
    private ShortBuffer rawShortBuffer;
    private FloatBuffer rawFloatBuffer;
    private int vertexCount;
    private VertexFormatElement vertexFormatElement;
    private int vertexFormatIndex;
    private boolean noColor;
    private int drawMode;
    private double xOffset;
    private double yOffset;
    private double zOffset;
    private VertexFormat vertexFormat;
    private boolean isDrawing;
    
    public BufferBuilder(final int bufferSizeIn) {
        this.byteBuffer = GLAllocation.createDirectByteBuffer(bufferSizeIn * 4);
        this.rawIntBuffer = this.byteBuffer.asIntBuffer();
        this.rawShortBuffer = this.byteBuffer.asShortBuffer();
        this.rawFloatBuffer = this.byteBuffer.asFloatBuffer();
    }
    
    private void growBuffer(final int p_181670_1_) {
        if (MathHelper.roundUp(p_181670_1_, 4) / 4 > this.rawIntBuffer.remaining() || this.vertexCount * this.vertexFormat.getNextOffset() + p_181670_1_ > this.byteBuffer.capacity()) {
            final int i = this.byteBuffer.capacity();
            final int j = i + MathHelper.roundUp(p_181670_1_, 2097152);
            BufferBuilder.LOGGER.debug("Needed to grow BufferBuilder buffer: Old size {} bytes, new size {} bytes.", new Object[] { i, j });
            final int k = this.rawIntBuffer.position();
            final ByteBuffer bytebuffer = GLAllocation.createDirectByteBuffer(j);
            this.byteBuffer.position(0);
            bytebuffer.put(this.byteBuffer);
            bytebuffer.rewind();
            this.byteBuffer = bytebuffer;
            this.rawFloatBuffer = this.byteBuffer.asFloatBuffer().asReadOnlyBuffer();
            (this.rawIntBuffer = this.byteBuffer.asIntBuffer()).position(k);
            (this.rawShortBuffer = this.byteBuffer.asShortBuffer()).position(k << 1);
        }
    }
    
    public void sortVertexData(final float p_181674_1_, final float p_181674_2_, final float p_181674_3_) {
        final int i = this.vertexCount / 4;
        final float[] afloat = new float[i];
        for (int j = 0; j < i; ++j) {
            afloat[j] = getDistanceSq(this.rawFloatBuffer, (float)(p_181674_1_ + this.xOffset), (float)(p_181674_2_ + this.yOffset), (float)(p_181674_3_ + this.zOffset), this.vertexFormat.getIntegerSize(), j * this.vertexFormat.getNextOffset());
        }
        final Integer[] ainteger = new Integer[i];
        for (int k = 0; k < ainteger.length; ++k) {
            ainteger[k] = k;
        }
        Arrays.sort(ainteger, new Comparator<Integer>() {
            @Override
            public int compare(final Integer p_compare_1_, final Integer p_compare_2_) {
                return Floats.compare(afloat[p_compare_2_], afloat[p_compare_1_]);
            }
        });
        final BitSet bitset = new BitSet();
        final int l = this.vertexFormat.getNextOffset();
        final int[] aint = new int[l];
        for (int i2 = bitset.nextClearBit(0); i2 < ainteger.length; i2 = bitset.nextClearBit(i2 + 1)) {
            final int j2 = ainteger[i2];
            if (j2 != i2) {
                this.rawIntBuffer.limit(j2 * l + l);
                this.rawIntBuffer.position(j2 * l);
                this.rawIntBuffer.get(aint);
                for (int k2 = j2, l2 = ainteger[j2]; k2 != i2; k2 = l2, l2 = ainteger[l2]) {
                    this.rawIntBuffer.limit(l2 * l + l);
                    this.rawIntBuffer.position(l2 * l);
                    final IntBuffer intbuffer = this.rawIntBuffer.slice();
                    this.rawIntBuffer.limit(k2 * l + l);
                    this.rawIntBuffer.position(k2 * l);
                    this.rawIntBuffer.put(intbuffer);
                    bitset.set(k2);
                }
                this.rawIntBuffer.limit(i2 * l + l);
                this.rawIntBuffer.position(i2 * l);
                this.rawIntBuffer.put(aint);
            }
            bitset.set(i2);
        }
        this.rawIntBuffer.limit(this.rawIntBuffer.capacity());
        this.rawIntBuffer.position(this.getBufferSize());
    }
    
    public State getVertexState() {
        this.rawIntBuffer.rewind();
        final int i = this.getBufferSize();
        this.rawIntBuffer.limit(i);
        final int[] aint = new int[i];
        this.rawIntBuffer.get(aint);
        this.rawIntBuffer.limit(this.rawIntBuffer.capacity());
        this.rawIntBuffer.position(i);
        return new State(aint, new VertexFormat(this.vertexFormat));
    }
    
    private int getBufferSize() {
        return this.vertexCount * this.vertexFormat.getIntegerSize();
    }
    
    private static float getDistanceSq(final FloatBuffer p_181665_0_, final float p_181665_1_, final float p_181665_2_, final float p_181665_3_, final int p_181665_4_, final int p_181665_5_) {
        final float f = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 0 + 0);
        final float f2 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 0 + 1);
        final float f3 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 0 + 2);
        final float f4 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 1 + 0);
        final float f5 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 1 + 1);
        final float f6 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 1 + 2);
        final float f7 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 2 + 0);
        final float f8 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 2 + 1);
        final float f9 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 2 + 2);
        final float f10 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 3 + 0);
        final float f11 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 3 + 1);
        final float f12 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 3 + 2);
        final float f13 = (f + f4 + f7 + f10) * 0.25f - p_181665_1_;
        final float f14 = (f2 + f5 + f8 + f11) * 0.25f - p_181665_2_;
        final float f15 = (f3 + f6 + f9 + f12) * 0.25f - p_181665_3_;
        return f13 * f13 + f14 * f14 + f15 * f15;
    }
    
    public void setVertexState(final State state) {
        this.rawIntBuffer.clear();
        this.growBuffer(state.getRawBuffer().length * 4);
        this.rawIntBuffer.put(state.getRawBuffer());
        this.vertexCount = state.getVertexCount();
        this.vertexFormat = new VertexFormat(state.getVertexFormat());
    }
    
    public void reset() {
        this.vertexCount = 0;
        this.vertexFormatElement = null;
        this.vertexFormatIndex = 0;
    }
    
    public void begin(final int glMode, final VertexFormat format) {
        if (this.isDrawing) {
            throw new IllegalStateException("Already building!");
        }
        this.isDrawing = true;
        this.reset();
        this.drawMode = glMode;
        this.vertexFormat = format;
        this.vertexFormatElement = format.getElement(this.vertexFormatIndex);
        this.noColor = false;
        this.byteBuffer.limit(this.byteBuffer.capacity());
    }
    
    public BufferBuilder tex(final double u, final double v) {
        final int i = this.vertexCount * this.vertexFormat.getNextOffset() + this.vertexFormat.getOffset(this.vertexFormatIndex);
        switch (this.vertexFormatElement.getType()) {
            case FLOAT: {
                this.byteBuffer.putFloat(i, (float)u);
                this.byteBuffer.putFloat(i + 4, (float)v);
                break;
            }
            case UINT:
            case INT: {
                this.byteBuffer.putInt(i, (int)u);
                this.byteBuffer.putInt(i + 4, (int)v);
                break;
            }
            case USHORT:
            case SHORT: {
                this.byteBuffer.putShort(i, (short)v);
                this.byteBuffer.putShort(i + 2, (short)u);
                break;
            }
            case UBYTE:
            case BYTE: {
                this.byteBuffer.put(i, (byte)v);
                this.byteBuffer.put(i + 1, (byte)u);
                break;
            }
        }
        this.nextVertexFormatIndex();
        return this;
    }
    
    public BufferBuilder lightmap(final int p_187314_1_, final int p_187314_2_) {
        final int i = this.vertexCount * this.vertexFormat.getNextOffset() + this.vertexFormat.getOffset(this.vertexFormatIndex);
        switch (this.vertexFormatElement.getType()) {
            case FLOAT: {
                this.byteBuffer.putFloat(i, p_187314_1_);
                this.byteBuffer.putFloat(i + 4, p_187314_2_);
                break;
            }
            case UINT:
            case INT: {
                this.byteBuffer.putInt(i, p_187314_1_);
                this.byteBuffer.putInt(i + 4, p_187314_2_);
                break;
            }
            case USHORT:
            case SHORT: {
                this.byteBuffer.putShort(i, (short)p_187314_2_);
                this.byteBuffer.putShort(i + 2, (short)p_187314_1_);
                break;
            }
            case UBYTE:
            case BYTE: {
                this.byteBuffer.put(i, (byte)p_187314_2_);
                this.byteBuffer.put(i + 1, (byte)p_187314_1_);
                break;
            }
        }
        this.nextVertexFormatIndex();
        return this;
    }
    
    public void putBrightness4(final int p_178962_1_, final int p_178962_2_, final int p_178962_3_, final int p_178962_4_) {
        final int i = (this.vertexCount - 4) * this.vertexFormat.getIntegerSize() + this.vertexFormat.getUvOffsetById(1) / 4;
        final int j = this.vertexFormat.getNextOffset() >> 2;
        this.rawIntBuffer.put(i, p_178962_1_);
        this.rawIntBuffer.put(i + j, p_178962_2_);
        this.rawIntBuffer.put(i + j * 2, p_178962_3_);
        this.rawIntBuffer.put(i + j * 3, p_178962_4_);
    }
    
    public void putPosition(final double x, final double y, final double z) {
        final int i = this.vertexFormat.getIntegerSize();
        final int j = (this.vertexCount - 4) * i;
        for (int k = 0; k < 4; ++k) {
            final int l = j + k * i;
            final int i2 = l + 1;
            final int j2 = i2 + 1;
            this.rawIntBuffer.put(l, Float.floatToRawIntBits((float)(x + this.xOffset) + Float.intBitsToFloat(this.rawIntBuffer.get(l))));
            this.rawIntBuffer.put(i2, Float.floatToRawIntBits((float)(y + this.yOffset) + Float.intBitsToFloat(this.rawIntBuffer.get(i2))));
            this.rawIntBuffer.put(j2, Float.floatToRawIntBits((float)(z + this.zOffset) + Float.intBitsToFloat(this.rawIntBuffer.get(j2))));
        }
    }
    
    public int getColorIndex(final int vertexIndex) {
        return ((this.vertexCount - vertexIndex) * this.vertexFormat.getNextOffset() + this.vertexFormat.getColorOffset()) / 4;
    }
    
    public void putColorMultiplier(final float red, final float green, final float blue, final int vertexIndex) {
        final int i = this.getColorIndex(vertexIndex);
        int j = -1;
        if (!this.noColor) {
            j = this.rawIntBuffer.get(i);
            if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
                final int k = (int)((j & 0xFF) * red);
                final int l = (int)((j >> 8 & 0xFF) * green);
                final int i2 = (int)((j >> 16 & 0xFF) * blue);
                j &= 0xFF000000;
                j = (j | i2 << 16 | l << 8 | k);
            }
            else {
                final int j2 = (int)((j >> 24 & 0xFF) * red);
                final int k2 = (int)((j >> 16 & 0xFF) * green);
                final int l2 = (int)((j >> 8 & 0xFF) * blue);
                j &= 0xFF;
                j = (j | j2 << 24 | k2 << 16 | l2 << 8);
            }
        }
        this.rawIntBuffer.put(i, j);
    }
    
    private void putColor(final int argb, final int vertexIndex) {
        final int i = this.getColorIndex(vertexIndex);
        final int j = argb >> 16 & 0xFF;
        final int k = argb >> 8 & 0xFF;
        final int l = argb & 0xFF;
        this.putColorRGBA(i, j, k, l);
    }
    
    public void putColorRGB_F(final float red, final float green, final float blue, final int vertexIndex) {
        final int i = this.getColorIndex(vertexIndex);
        final int j = MathHelper.clamp_int((int)(red * 255.0f), 0, 255);
        final int k = MathHelper.clamp_int((int)(green * 255.0f), 0, 255);
        final int l = MathHelper.clamp_int((int)(blue * 255.0f), 0, 255);
        this.putColorRGBA(i, j, k, l);
    }
    
    public void putColorRGBA(final int index, final int red, final int green, final int blue) {
        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            this.rawIntBuffer.put(index, 0xFF000000 | blue << 16 | green << 8 | red);
        }
        else {
            this.rawIntBuffer.put(index, red << 24 | green << 16 | blue << 8 | 0xFF);
        }
    }
    
    public void noColor() {
        this.noColor = true;
    }
    
    public BufferBuilder color(final float red, final float green, final float blue, final float alpha) {
        return this.color((int)(red * 255.0f), (int)(green * 255.0f), (int)(blue * 255.0f), (int)(alpha * 255.0f));
    }
    
    public BufferBuilder color(final int red, final int green, final int blue, final int alpha) {
        if (this.noColor) {
            return this;
        }
        final int i = this.vertexCount * this.vertexFormat.getNextOffset() + this.vertexFormat.getOffset(this.vertexFormatIndex);
        switch (this.vertexFormatElement.getType()) {
            case FLOAT: {
                this.byteBuffer.putFloat(i, red / 255.0f);
                this.byteBuffer.putFloat(i + 4, green / 255.0f);
                this.byteBuffer.putFloat(i + 8, blue / 255.0f);
                this.byteBuffer.putFloat(i + 12, alpha / 255.0f);
                break;
            }
            case UINT:
            case INT: {
                this.byteBuffer.putFloat(i, red);
                this.byteBuffer.putFloat(i + 4, green);
                this.byteBuffer.putFloat(i + 8, blue);
                this.byteBuffer.putFloat(i + 12, alpha);
                break;
            }
            case USHORT:
            case SHORT: {
                this.byteBuffer.putShort(i, (short)red);
                this.byteBuffer.putShort(i + 2, (short)green);
                this.byteBuffer.putShort(i + 4, (short)blue);
                this.byteBuffer.putShort(i + 6, (short)alpha);
                break;
            }
            case UBYTE:
            case BYTE: {
                if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
                    this.byteBuffer.put(i, (byte)red);
                    this.byteBuffer.put(i + 1, (byte)green);
                    this.byteBuffer.put(i + 2, (byte)blue);
                    this.byteBuffer.put(i + 3, (byte)alpha);
                    break;
                }
                this.byteBuffer.put(i, (byte)alpha);
                this.byteBuffer.put(i + 1, (byte)blue);
                this.byteBuffer.put(i + 2, (byte)green);
                this.byteBuffer.put(i + 3, (byte)red);
                break;
            }
        }
        this.nextVertexFormatIndex();
        return this;
    }
    
    public void addVertexData(final int[] vertexData) {
        this.growBuffer(vertexData.length * 4 + this.vertexFormat.getNextOffset());
        this.rawIntBuffer.position(this.getBufferSize());
        this.rawIntBuffer.put(vertexData);
        this.vertexCount += vertexData.length / this.vertexFormat.getIntegerSize();
    }
    
    public void endVertex() {
        ++this.vertexCount;
        this.growBuffer(this.vertexFormat.getNextOffset());
    }
    
    public BufferBuilder pos(final double x, final double y, final double z) {
        final int i = this.vertexCount * this.vertexFormat.getNextOffset() + this.vertexFormat.getOffset(this.vertexFormatIndex);
        switch (this.vertexFormatElement.getType()) {
            case FLOAT: {
                this.byteBuffer.putFloat(i, (float)(x + this.xOffset));
                this.byteBuffer.putFloat(i + 4, (float)(y + this.yOffset));
                this.byteBuffer.putFloat(i + 8, (float)(z + this.zOffset));
                break;
            }
            case UINT:
            case INT: {
                this.byteBuffer.putInt(i, Float.floatToRawIntBits((float)(x + this.xOffset)));
                this.byteBuffer.putInt(i + 4, Float.floatToRawIntBits((float)(y + this.yOffset)));
                this.byteBuffer.putInt(i + 8, Float.floatToRawIntBits((float)(z + this.zOffset)));
                break;
            }
            case USHORT:
            case SHORT: {
                this.byteBuffer.putShort(i, (short)(x + this.xOffset));
                this.byteBuffer.putShort(i + 2, (short)(y + this.yOffset));
                this.byteBuffer.putShort(i + 4, (short)(z + this.zOffset));
                break;
            }
            case UBYTE:
            case BYTE: {
                this.byteBuffer.put(i, (byte)(x + this.xOffset));
                this.byteBuffer.put(i + 1, (byte)(y + this.yOffset));
                this.byteBuffer.put(i + 2, (byte)(z + this.zOffset));
                break;
            }
        }
        this.nextVertexFormatIndex();
        return this;
    }
    
    public void putNormal(final float x, final float y, final float z) {
        final int i = (byte)(x * 127.0f) & 0xFF;
        final int j = (byte)(y * 127.0f) & 0xFF;
        final int k = (byte)(z * 127.0f) & 0xFF;
        final int l = i | j << 8 | k << 16;
        final int i2 = this.vertexFormat.getNextOffset() >> 2;
        final int j2 = (this.vertexCount - 4) * i2 + this.vertexFormat.getNormalOffset() / 4;
        this.rawIntBuffer.put(j2, l);
        this.rawIntBuffer.put(j2 + i2, l);
        this.rawIntBuffer.put(j2 + i2 * 2, l);
        this.rawIntBuffer.put(j2 + i2 * 3, l);
    }
    
    private void nextVertexFormatIndex() {
        ++this.vertexFormatIndex;
        this.vertexFormatIndex %= this.vertexFormat.getElementCount();
        this.vertexFormatElement = this.vertexFormat.getElement(this.vertexFormatIndex);
        if (this.vertexFormatElement.getUsage() == VertexFormatElement.EnumUsage.PADDING) {
            this.nextVertexFormatIndex();
        }
    }
    
    public BufferBuilder normal(final float x, final float y, final float z) {
        final int i = this.vertexCount * this.vertexFormat.getNextOffset() + this.vertexFormat.getOffset(this.vertexFormatIndex);
        switch (this.vertexFormatElement.getType()) {
            case FLOAT: {
                this.byteBuffer.putFloat(i, x);
                this.byteBuffer.putFloat(i + 4, y);
                this.byteBuffer.putFloat(i + 8, z);
                break;
            }
            case UINT:
            case INT: {
                this.byteBuffer.putInt(i, (int)x);
                this.byteBuffer.putInt(i + 4, (int)y);
                this.byteBuffer.putInt(i + 8, (int)z);
                break;
            }
            case USHORT:
            case SHORT: {
                this.byteBuffer.putShort(i, (short)((int)(x * 32767.0f) & 0xFFFF));
                this.byteBuffer.putShort(i + 2, (short)((int)(y * 32767.0f) & 0xFFFF));
                this.byteBuffer.putShort(i + 4, (short)((int)(z * 32767.0f) & 0xFFFF));
                break;
            }
            case UBYTE:
            case BYTE: {
                this.byteBuffer.put(i, (byte)((int)(x * 127.0f) & 0xFF));
                this.byteBuffer.put(i + 1, (byte)((int)(y * 127.0f) & 0xFF));
                this.byteBuffer.put(i + 2, (byte)((int)(z * 127.0f) & 0xFF));
                break;
            }
        }
        this.nextVertexFormatIndex();
        return this;
    }
    
    public void setTranslation(final double x, final double y, final double z) {
        this.xOffset = x;
        this.yOffset = y;
        this.zOffset = z;
    }
    
    public void finishDrawing() {
        if (!this.isDrawing) {
            throw new IllegalStateException("Not building!");
        }
        this.isDrawing = false;
        this.byteBuffer.position(0);
        this.byteBuffer.limit(this.getBufferSize() * 4);
    }
    
    public ByteBuffer getByteBuffer() {
        return this.byteBuffer;
    }
    
    public VertexFormat getVertexFormat() {
        return this.vertexFormat;
    }
    
    public int getVertexCount() {
        return this.vertexCount;
    }
    
    public int getDrawMode() {
        return this.drawMode;
    }
    
    public void putColor4(final int argb) {
        for (int i = 0; i < 4; ++i) {
            this.putColor(argb, i + 1);
        }
    }
    
    public void putColorRGB_F4(final float red, final float green, final float blue) {
        for (int i = 0; i < 4; ++i) {
            this.putColorRGB_F(red, green, blue, i + 1);
        }
    }
    
    public void putColorRGBA(final int index, final int red, final int green, final int blue, final int alpha) {
        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            this.rawIntBuffer.put(index, alpha << 24 | blue << 16 | green << 8 | red);
        }
        else {
            this.rawIntBuffer.put(index, red << 24 | green << 16 | blue << 8 | alpha);
        }
    }
    
    public boolean isColorDisabled() {
        return this.noColor;
    }
    
    public void putBulkData(final ByteBuffer buffer) {
        this.growBuffer(buffer.limit() + this.vertexFormat.getNextOffset());
        this.byteBuffer.position(this.vertexCount * this.vertexFormat.getNextOffset());
        this.byteBuffer.put(buffer);
        this.vertexCount += buffer.limit() / this.vertexFormat.getNextOffset();
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    @SideOnly(Side.CLIENT)
    public class State
    {
        private final int[] stateRawBuffer;
        private final VertexFormat stateVertexFormat;
        
        public State(final int[] buffer, final VertexFormat format) {
            this.stateRawBuffer = buffer;
            this.stateVertexFormat = format;
        }
        
        public int[] getRawBuffer() {
            return this.stateRawBuffer;
        }
        
        public int getVertexCount() {
            return this.stateRawBuffer.length / this.stateVertexFormat.getIntegerSize();
        }
        
        public VertexFormat getVertexFormat() {
            return this.stateVertexFormat;
        }
    }
}
