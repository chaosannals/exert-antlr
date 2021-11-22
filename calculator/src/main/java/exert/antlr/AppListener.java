package exert.antlr;

import org.objectweb.asm.*;
import exert.antlr.grammar.CalculatorBaseListener;
import exert.antlr.grammar.CalculatorLexer;
import exert.antlr.grammar.CalculatorParser.AsLv1Context;
import exert.antlr.grammar.CalculatorParser.AsLv2Context;
import exert.antlr.grammar.CalculatorParser.AsNumberContext;
import exert.antlr.grammar.CalculatorParser.AsPriorContext;

public class AppListener extends CalculatorBaseListener {
    private ClassWriter writer;
    private MethodVisitor methodVisitor;
    private String className;
    private String methodName;

    public AppListener() {
        className = "exert.antlr.demo.Calculator";
        methodName = "calculate";
        writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES); // 栈帧全自动计算
        writer.visit(Opcodes.V16, Opcodes.ACC_PUBLIC, className.replace('.', '/'), null, "java/lang/Object", null);
        methodVisitor = writer.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, methodName, "()D", null, null);
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    @Override
    public void enterAsNumber(AsNumberContext ctx) {
        String t = ctx.getText();
        Double v = Double.parseDouble(t);
        methodVisitor.visitLdcInsn(v);
    }

    @Override
    public void enterAsPrior(AsPriorContext ctx) {

    }

    @Override
    public void exitAsLv2(AsLv2Context ctx) {
        switch (ctx.op.getType()) {
        case CalculatorLexer.STAR:
            methodVisitor.visitInsn(Opcodes.DMUL);
            break;
        case CalculatorLexer.SLASH:
            methodVisitor.visitInsn(Opcodes.DDIV);
            break;
        }
    }

    @Override
    public void exitAsLv1(AsLv1Context ctx) {
        switch (ctx.op.getType()) {
        case CalculatorLexer.PLUS:
            methodVisitor.visitInsn(Opcodes.DADD);
            break;
        case CalculatorLexer.MINUS:
            methodVisitor.visitInsn(Opcodes.DSUB);
            break;
        }
    }

    public byte[] make() {
        methodVisitor.visitInsn(Opcodes.DRETURN);
        methodVisitor.visitMaxs(0, 0); // 类设置了自动计算，参数无效。
        methodVisitor.visitEnd();
        writer.visitEnd();
        return writer.toByteArray();
    }
}
