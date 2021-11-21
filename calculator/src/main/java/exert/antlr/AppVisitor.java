package exert.antlr;

import exert.antlr.grammar.*;
import exert.antlr.grammar.CalculatorParser.AsLv1Context;
import exert.antlr.grammar.CalculatorParser.AsLv2Context;
import exert.antlr.grammar.CalculatorParser.AsNumberContext;
import exert.antlr.grammar.CalculatorParser.AsPriorContext;

public class AppVisitor extends CalculatorBaseVisitor<Double> {
    @Override
    public Double visitAsNumber(AsNumberContext ctx) {
        String n = ctx.getText();
        return Double.parseDouble(n);
    }

    @Override
    public Double visitAsPrior(AsPriorContext ctx) {
        return visit(ctx.opc());
    }

    @Override
    public Double visitAsLv1(AsLv1Context ctx) {
        Double left = visit(ctx.opc(0));
        Double right = visit(ctx.opc(1));
        switch (ctx.op.getType()) {
        case CalculatorLexer.PLUS:
            return left + right;
        case CalculatorLexer.MINUS:
            return left - right;
        }
        throw new RuntimeException();
    }

    @Override
    public Double visitAsLv2(AsLv2Context ctx) {
        Double left = visit(ctx.opc(0));
        Double right = visit(ctx.opc(1));
        switch (ctx.op.getType()) {
        case CalculatorLexer.STAR:
            return left * right;
        case CalculatorLexer.SLASH:
            return left / right;
        }
        throw new RuntimeException();
    }
}
