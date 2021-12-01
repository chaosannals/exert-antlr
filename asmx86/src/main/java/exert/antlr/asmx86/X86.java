package exert.antlr.asmx86;

import java.util.*;

public class X86 {
    private ArrayList<X86Instruction> codes;

    public X86() {
        codes = new ArrayList<>();
    }

    public X86 mov() {
        X86Instruction i = new X86Instruction(X86Opcode.MOV);
        codes.add(i);
        return this;
    }
}
