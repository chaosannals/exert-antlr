package exert.antlr;

import java.lang.reflect.*;
import org.objectweb.asm.*;

public class AppMaker {
    private ClassWriter writer;
    private MethodVisitor methodVisitor;
    private String className;

    public AppMaker(String className) {
        this.className = className;
        writer = new ClassWriter(0);
        writer.visit(
            Opcodes.V16,
            Opcodes.ACC_PUBLIC,
            className.replace('.', '/'),
            null,
            "java/lang/Object",
            null
        );
        methodVisitor = writer.visitMethod(
            Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, 
            "calculator", 
            "(Ljava/lang/String;)D",
            null,
            null
        );
    }

    public byte[] make() {
        return writer.toByteArray();
    }
}
