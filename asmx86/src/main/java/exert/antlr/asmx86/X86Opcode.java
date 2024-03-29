package exert.antlr.asmx86;

public enum X86Opcode {
    MOV,
    PUSH,
    POP,
    LEA,
    MOVSX,
    MOVZX,

    ADD,
    SUB,
    IMUL,
    IDIV,
    MUL,
    DIV,
    INC,
    DEC,
    NEG,
    
    AND,
    OR,
    XOR,
    NOT,
    SAL,
    SAR,
    SHR,

    JMP,
    JZ,
    JNZ,
    JE,
    JNE,
    JA,
    JNA,
    JAE,
    JNAE,
    JB,
    JNB,
    JBE,
    JNBE,
    JG,
    JNG,
    JGE,
    JNGE,
    JL,
    JNL,
    JLE,
    JNLE,
    CMP,
    TEST,
    SETE,
    SETNE,
    SETG,
    SETGE,
    SETL,
    SETLE,
    CALL,
    RET,
}
