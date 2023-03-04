package dev.ss.world.instrument;

public interface Instrumentation
{
    Class<?>[] getAllLoadedClasses();
    
    ClassTransformer[] getTransformers();
    
    void retransformClasses(final Class<?>[] p0);
    
    Class<?>[] getLoadedClasses(final ClassLoader p0);
    
    void addTransformer(final ClassTransformer p0);
}
