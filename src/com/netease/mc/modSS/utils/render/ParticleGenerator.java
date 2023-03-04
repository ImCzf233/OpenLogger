package com.netease.mc.modSS.utils.render;

import java.util.*;
import net.minecraft.util.*;
import com.netease.mc.modSS.utils.*;
import org.lwjgl.opengl.*;

public class ParticleGenerator
{
    private int count;
    private int width;
    private int height;
    private ArrayList<Particle> particles;
    private Random random;
    int state;
    int a;
    int r;
    int g;
    int b;
    
    public ParticleGenerator(final int count, final int width, final int height) {
        this.particles = new ArrayList<Particle>();
        this.random = new Random();
        this.state = 0;
        this.a = 255;
        this.r = 255;
        this.g = 0;
        this.b = 0;
        this.count = count;
        this.width = width;
        this.height = height;
        for (int i = 0; i < count; ++i) {
            this.particles.add(new Particle(this.random.nextInt(width), this.random.nextInt(height)));
        }
    }
    
    public void drawParticles(final int mouseX, final int mouseY) {
        for (final Particle p : this.particles) {
            if (p.reset) {
                p.resetPosSize();
                p.reset = false;
            }
            p.draw(mouseX, mouseY);
        }
    }
    
    public class Particle
    {
        private int x;
        private int y;
        private int k;
        private float size;
        private boolean reset;
        private Random random;
        
        public Particle(final int x, final int y) {
            this.random = new Random();
            this.x = x;
            this.y = y;
            this.size = this.genRandom(1.0f, 3.0f);
        }
        
        public void draw(final int mouseX, final int mouseY) {
            if (this.size <= 0.0f) {
                this.reset = true;
            }
            this.size -= 0.05f;
            ++this.k;
            final int xx = (int)(MathHelper.cos(0.1f * (this.x + this.k)) * 10.0f);
            final int yy = (int)(MathHelper.cos(0.1f * (this.y + this.k)) * 10.0f);
            RenderUtils.drawBorderedCircle(this.x + xx, this.y + yy, this.size, 0, 553648127);
            final float distance = (float)Utils.distance(this.x + xx, this.y + yy, mouseX, mouseY);
            if (distance < 50.0f) {
                final float alpha1 = Math.min(1.0f, Math.min(1.0f, 1.0f - distance / 50.0f));
                GL11.glEnable(2848);
                GL11.glDisable(2929);
                GL11.glColor4f(255.0f, 255.0f, 255.0f, 255.0f);
                GL11.glDisable(3553);
                GL11.glDepthMask(false);
                GL11.glBlendFunc(770, 771);
                GL11.glEnable(3042);
                GL11.glLineWidth(0.1f);
                GL11.glBegin(1);
                GL11.glVertex2f((float)(this.x + xx), (float)(this.y + yy));
                GL11.glVertex2f((float)mouseX, (float)mouseY);
                GL11.glEnd();
            }
        }
        
        public void resetPosSize() {
            this.x = this.random.nextInt(ParticleGenerator.this.width);
            this.y = this.random.nextInt(ParticleGenerator.this.height);
            this.size = this.genRandom(1.0f, 3.0f);
        }
        
        public float genRandom(final float min, final float max) {
            return (float)(min + Math.random() * (max - min + 1.0f));
        }
    }
}
