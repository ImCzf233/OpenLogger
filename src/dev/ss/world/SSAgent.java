package dev.ss.world;

import dev.ss.world.instrument.*;
import dev.ss.world.utils.*;
import java.util.*;

public class SSAgent
{
    public void retransformclass(final Instrumentation instrumentation, final ClassTransformer classTransformer, final String string) {
        Reflections.load();
        instrumentation.addTransformer(classTransformer);
        instrumentation.retransformClasses(Arrays.stream(instrumentation.getAllLoadedClasses()).filter(arg_0 -> retransformclass(string, arg_0)).toArray(SSAgent::retransformclass));
    }
    
    private static Class[] retransformclass(final int n) {
        return new Class[n];
    }
    
    private static boolean retransformclass(final String string, final Class clazz) {
        return clazz.getName().startsWith(string);
    }
}
