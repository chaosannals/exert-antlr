package exert.antlr.xlsxepr;

import java.io.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import exert.antlr.xlsxepr.grammar.*;

public class App {
    /**
     * 真正术语： excel formula（Excel 公式）
     * 误用： excel expression （表达式）命名了。。。
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("antlr xlsx express.");

        // for(int i = 0; i < 1000; ++i) {
        //     String a = XlsxValue.toXlsxColumn(i);
        //     System.out.println(i + "  " + a + "  " + XlsxValue.fromXlsxColumn(a));
        // }

        InputStream i = App.class.getResourceAsStream("/one.txt");
        InputStreamReader isr = new InputStreamReader(i);
        BufferedReader br = new BufferedReader(isr);
        XlsxExpressionVisitor xlsxVistor = new XlsxExpressionVisitor();
        xlsxVistor.setCell("B1", XlsxValue.fromNumber(1234.0));
        xlsxVistor.setCell("A1", XlsxValue.fromNumber(34.0));
        xlsxVistor.setCell("E2", XlsxValue.fromNumber(34.0));
        xlsxVistor.setCell("E3", XlsxValue.fromNumber(12.0));
        try {
            while(true) {
                String line = br.readLine();
                if (line == null) break;
                System.out.println(line);
                XlsxValue r = xlsxVistor.visit(parse(line));
                if (r.type == XlsxType.XlsxNumber) {
                    System.out.println("r: " + r.asNumber());
                }
            }
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
    public static ParseTree parse(String code) throws IOException {
        CharStream input = CharStreams.fromString(code);
        XlsxExpressionLexer lexer = new XlsxExpressionLexer(input);
        XlsxExpressionParser parser = new XlsxExpressionParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.total();
        // System.out.println(tree);
        return tree;
    }
}
