package org.sinytra.connector.transformer.transform;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.sinytra.connector.transformer.jar.IntermediateMapping;

public class PlayerListTransformer extends ClassVisitor {
    private final IntermediateMapping mapping;
    private String className;

    public PlayerListTransformer(ClassVisitor classVisitor, IntermediateMapping mapping) {
        super(Opcodes.ASM9, classVisitor);
        this.mapping = mapping;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.className = name;
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        // 特殊处理 PlayerList 类的方法
        if (className.equals("net/minecraft/server/players/PlayerList")) {
            // 确保类加载器可以找到这个类
            try {
                Class.forName("net.minecraft.server.players.PlayerList", true, this.getClass().getClassLoader());
            } catch (ClassNotFoundException e) {
                System.err.println("Warning: PlayerList class not found during transformation: " + e.getMessage());
            }
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }
} 