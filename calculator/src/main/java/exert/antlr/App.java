package exert.antlr;

import java.io.*;
import java.lang.reflect.*;
import java.nio.file.*;

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
            AppListener al = new AppListener();
            ParseTreeWalker worker = new ParseTreeWalker();
            worker.walk(al, tree);
            byte[] c = al.make();
            // Files.write(Path.of("Calculator.class"), c);
            AppLoader loader = new AppLoader();
            Class<?> ac = loader.load(al.getClassName(), c);
            Method m = ac.getMethod(al.getMethodName());
            Object r = m.invoke(null);
            System.out.println(r);
        } catch (IOException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用 visitor 解释计算。
     * 
     * @param program
     * @return
     * @throws IOException
     */
    public Double interpert(String program) throws IOException {
        InputStream is = new ByteArrayInputStream(program.getBytes());
        CharStream input = CharStreams.fromStream(is);
        CalculatorLexer lexer = new CalculatorLexer(input);
        CalculatorParser parser = new CalculatorParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.opc();
        AppVisitor av = new AppVisitor();
        return av.visit(tree);
    }
}
