package exert.antlr.json;

import java.io.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import exert.antlr.json.grammar.*;

public class App {
    public static void main(String[] args) {
        System.out.print("antlr json.");
        InputStream is = App.class.getResourceAsStream("/object.json");
        try {
            Object r = parse(is);
            System.out.println(r);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 
     * @param program
     * @return
     * @throws IOException
     */
    public static Object parse(InputStream is) throws IOException {
        CharStream input = CharStreams.fromStream(is);
        JsonDemoLexer lexer = new JsonDemoLexer(input);
        JsonDemoParser parser = new JsonDemoParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.value();
        JsonVisitor av = new JsonVisitor();
        return av.visit(tree);
    }
}
