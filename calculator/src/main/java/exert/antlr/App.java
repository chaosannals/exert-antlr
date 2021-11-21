package exert.antlr;

import java.io.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import exert.antlr.grammar.*;

public class App {
    public static void main(String[] args) {
        try {
            String code = "1.2 + 2.6 * (2 - 1)";
            InputStream is = new ByteArrayInputStream(code.getBytes());
            CharStream input = CharStreams.fromStream(is);
            CalculatorLexer lexer = new CalculatorLexer(input);
            CalculatorParser parser = new CalculatorParser(new CommonTokenStream(lexer));
            ParseTree tree = parser.opc();
            System.out.println(tree.toStringTree());
            AppVisitor av = new AppVisitor();
            Double r = av.visit(tree);
            System.out.println(r);
        } catch (IOException e) {

        }
    }
}