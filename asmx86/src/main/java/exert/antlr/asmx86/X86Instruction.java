package exert.antlr.asmx86;

public class X86Instruction {
    public final static byte OP_RZP = 0x66; // 16位实模式 使用 32位寄存器，指令前缀
    public final static byte OP_AZP = 0x67; // 32位保护模式 使用 16位寄存器，指令前缀
    public X86Opcode opcode;

    public X86Instruction(X86Opcode opc) {
        opcode = opc;
    }
}
