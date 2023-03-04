package com.netease.mc.modSS.utils.misc;

import java.util.function.*;
import javax.annotation.*;
import java.util.stream.*;
import java.util.*;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.*;

public final class ASMUtils
{
    public static AbstractInsnNode findPattern(final AbstractInsnNode start, final int[] pattern, final char[] mask) {
        if (pattern.length != mask.length) {
            throw new IllegalArgumentException("Mask must be same length as pattern");
        }
        return findPattern(start, pattern.length, node -> true, (found, next) -> mask[found] != 'x' || next.getOpcode() == pattern[found], (first, last) -> first);
    }
    
    public static <T> T findPattern(final AbstractInsnNode start, final int patternSize, final Predicate<AbstractInsnNode> isValidNode, final BiPredicate<Integer, AbstractInsnNode> nodePredicate, final BiFunction<AbstractInsnNode, AbstractInsnNode, T> outputFunction) {
        if (start != null) {
            int found = 0;
            AbstractInsnNode next = start;
            do {
                final boolean validNode = isValidNode.test(next);
                if (!validNode || nodePredicate.test(found, next)) {
                    if (validNode) {
                        ++found;
                    }
                }
                else {
                    for (int i = 1; i <= found - 1; ++i) {
                        next = next.getPrevious();
                    }
                    found = 0;
                }
                if (found >= patternSize) {
                    final AbstractInsnNode end = next;
                    for (int j = 1; j <= found - 1; ++j) {
                        next = next.getPrevious();
                    }
                    return outputFunction.apply(next, end);
                }
                next = next.getNext();
            } while (next != null);
        }
        return null;
    }
    
    public static AbstractInsnNode findPattern(final AbstractInsnNode start, final int[] pattern, final String mask) {
        return findPattern(start, pattern, mask.toCharArray());
    }
    
    public static AbstractInsnNode findPattern(final AbstractInsnNode start, final int... opcodes) {
        final StringBuilder mask = new StringBuilder();
        for (final int op : opcodes) {
            mask.append((op == -666) ? '?' : 'x');
        }
        return findPattern(start, opcodes, mask.toString());
    }
    
    public static AbstractInsnNode findPattern(final InsnList instructions, final int... opcodes) {
        return findPattern(instructions.getFirst(), opcodes);
    }
    
    public static AbstractInsnNode findPattern(final MethodNode node, final int... opcodes) {
        return findPattern(node.instructions, opcodes);
    }
    
    @Nullable
    public static AbstractInsnNode forward(final AbstractInsnNode start, final int n) {
        AbstractInsnNode node = start;
        for (int i = 0; i < Math.abs(n) && node != null; ++i, node = ((n > 0) ? node.getNext() : node.getPrevious())) {}
        return node;
    }
    
    public static String getClassData(final ClassNode node) {
        final StringBuilder builder = new StringBuilder("METHODS:\n");
        for (final MethodNode method : node.methods) {
            builder.append("\t");
            builder.append(method.name);
            builder.append(method.desc);
            builder.append("\n");
        }
        builder.append("\nFIELDS:\n");
        for (final FieldNode field : node.fields) {
            builder.append("\t");
            builder.append(field.desc);
            builder.append(" ");
            builder.append(field.name);
            builder.append("\n");
        }
        return builder.toString();
    }
    
    public static int addNewLocalVariable(final MethodNode method, final String name, final String desc, final LabelNode start, final LabelNode end) {
        final Optional<LocalVariableNode> lastVar = (Optional<LocalVariableNode>)method.localVariables.stream().max(Comparator.comparingInt(var -> var.index));
        final int newIndex = lastVar.map(var -> var.desc.matches("[JD]") ? (var.index + 2) : (var.index + 1)).orElse(0);
        final LocalVariableNode variable = new LocalVariableNode(name, desc, (String)null, start, end, newIndex);
        method.localVariables.add(variable);
        return newIndex;
    }
    
    public static InsnList newInstance(final String name, final String[] argTypes, @Nullable final InsnList args) {
        final String desc = Stream.of(argTypes).collect(Collectors.joining("", "(", ")V"));
        return newInstance(name, desc, args);
    }
    
    public static InsnList newInstance(final String name, final String desc, @Nullable final InsnList args) {
        final InsnList list = new InsnList();
        list.add((AbstractInsnNode)new TypeInsnNode(187, name));
        list.add((AbstractInsnNode)new InsnNode(89));
        if (args != null) {
            list.add(args);
        }
        list.add((AbstractInsnNode)new MethodInsnNode(183, name, "<init>", desc, false));
        return list;
    }
    
    public static MethodNode findMethod(final ClassNode classNode, final String name, final String desc) {
        for (final MethodNode methodNode : classNode.methods) {
            if (methodNode.name.equals(name) && methodNode.desc.equals(desc)) {
                return methodNode;
            }
        }
        return null;
    }
    
    public static AbstractInsnNode findMethodInsn(final MethodNode mn, final int opcode, final String owner, final String name, final String desc) {
        for (final AbstractInsnNode insn : mn.instructions.toArray()) {
            if (insn instanceof MethodInsnNode) {
                final MethodInsnNode method = (MethodInsnNode)insn;
                if (method.getOpcode() == opcode && method.owner.equals(owner) && method.name.equals(name) && method.desc.equals(desc)) {
                    return insn;
                }
            }
        }
        return null;
    }
    
    public static AbstractInsnNode findFieldInsnNode(final MethodNode mn, final int opcode, final String owner, final String name, final String desc) {
        for (final AbstractInsnNode insn : mn.instructions.toArray()) {
            if (insn instanceof FieldInsnNode) {
                final FieldInsnNode field = (FieldInsnNode)insn;
                if (field.getOpcode() == opcode && field.owner.equals(owner) && field.name.equals(name) && field.desc.equals(desc)) {
                    return insn;
                }
            }
        }
        return null;
    }
    
    public static List<AbstractInsnNode> getFieldList(final MethodNode mn, final int opcode, final String owner, final String name, final String desc) {
        final List<AbstractInsnNode> list = new ArrayList<AbstractInsnNode>();
        for (final AbstractInsnNode insn : mn.instructions.toArray()) {
            if (insn instanceof FieldInsnNode) {
                final FieldInsnNode field = (FieldInsnNode)insn;
                if (field.getOpcode() == opcode && field.owner.equals(owner) && field.name.equals(name) && field.desc.equals(desc)) {
                    list.add((AbstractInsnNode)field);
                }
            }
        }
        return list;
    }
    
    public static AbstractInsnNode findPatternInsn(final MethodNode mn, final int[] pattern) {
        for (final AbstractInsnNode insn : mn.instructions.toArray()) {
            for (final int opcode : pattern) {
                if (opcode == insn.getOpcode()) {
                    return insn;
                }
            }
        }
        return null;
    }
    
    public static AbstractInsnNode findInsnLdc(final MethodNode mn, final String s) {
        for (final AbstractInsnNode insn : mn.instructions.toArray()) {
            if (insn instanceof LdcInsnNode) {
                final LdcInsnNode ldc = (LdcInsnNode)insn;
                if (ldc.cst instanceof String) {
                    final String var = (String)ldc.cst;
                    if (var.equals(s)) {
                        return insn;
                    }
                }
            }
        }
        return null;
    }
    
    public static AbstractInsnNode head(final MethodNode method) {
        return method.instructions.getFirst().getNext();
    }
    
    public static AbstractInsnNode bottom(final MethodNode method) {
        return method.instructions.get(method.instructions.size() - 2);
    }
    
    public static ClassNode getNode(final byte[] classBuffer) {
        final ClassNode classNode = new ClassNode();
        final ClassReader reader = new ClassReader(classBuffer);
        reader.accept((ClassVisitor)classNode, 0);
        return classNode;
    }
    
    public static byte[] toBytes(final ClassNode classNode) {
        final ClassWriter writer = new ClassWriter(3);
        classNode.accept((ClassVisitor)writer);
        return writer.toByteArray();
    }
    
    public interface MagicOpcodes
    {
        public static final int NONE = -666;
    }
}
