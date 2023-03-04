package dev.ss.world;

import dev.ss.world.instrument.*;
import java.security.*;
import java.util.concurrent.atomic.*;
import dev.ss.world.utils.*;
import java.util.*;

public class SSNative
{
    private static final Set<ClassTransformer> transformers;
    private static ClassTransformer[] transformersArray;
    private static boolean modified;
    
    public static void addTransformer(final ClassTransformer classTransformer) {
        SSNative.transformers.add(classTransformer);
        SSNative.modified = true;
    }
    
    public static byte[] transformClass(final ClassLoader loader, final String className, final Class<?> classBeingRedefined, final ProtectionDomain protectionDomain, final byte[] classfileBuffer) {
        if (classBeingRedefined == null) {
            return classfileBuffer;
        }
        final AtomicReference<byte[]> atomicReference = new AtomicReference<byte[]>();
        final byte[] newByteArray;
        final AtomicReference<byte[]> atomicReference2;
        SSNative.transformers.forEach(classTransformer -> {
            newByteArray = classTransformer.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
            if (Objects.isNull(atomicReference2.get())) {
                atomicReference2.set(newByteArray);
                return;
            }
            else if (JavaUtil.equals(atomicReference2.get(), newByteArray)) {
                return;
            }
            else {
                atomicReference2.set(newByteArray);
                return;
            }
        });
        return atomicReference.get();
    }
    
    public static ClassTransformer[] getTransformersAsArray() {
        if (SSNative.modified) {
            SSNative.modified = false;
            return SSNative.transformersArray = SSNative.transformers.toArray(new ClassTransformer[0]);
        }
        return (SSNative.transformersArray == null) ? (SSNative.transformersArray = SSNative.transformers.toArray(new ClassTransformer[0])) : SSNative.transformersArray;
    }
    
    static {
        transformers = new HashSet<ClassTransformer>();
    }
}
