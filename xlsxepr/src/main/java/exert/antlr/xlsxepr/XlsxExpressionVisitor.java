package exert.antlr.xlsxepr;

import java.util.*;
import exert.antlr.xlsxepr.grammar.XlsxExpressionBaseVisitor;
import exert.antlr.xlsxepr.grammar.XlsxExpressionLexer;
import exert.antlr.xlsxepr.grammar.XlsxExpressionParser;

public class XlsxExpressionVisitor extends XlsxExpressionBaseVisitor<XlsxValue> {
    private HashMap<String, XlsxValue> cells;

    public XlsxExpressionVisitor() {
        cells = new HashMap<>();
    }

    public void setCell(String name, XlsxValue v) {
        cells.put(name, v);
    }

    public List<XlsxValue> getCellsByRange(XlsxValue range) {
        // System.out.println("get cells by range: " + range.top + "," + range.left + "|" + range.bottom + "," + range.right);
        List<XlsxValue> result = new ArrayList<XlsxValue>();
        for (var c : cells.entrySet()) {
            if (XlsxValue.inRange(range, c.getKey())) {
                // System.out.println("match range: " + range.value);
                result.add(c.getValue());
            }
        }
        return result;
    }

    @Override
    public XlsxValue visitTotal(XlsxExpressionParser.TotalContext ctx) {
        // System.out.println("total");
        return visit(ctx.expression());
    }

    @Override
    public XlsxValue visitExpressionValue(XlsxExpressionParser.ExpressionValueContext ctx) {
        // System.out.println("expression value");
        return visit(ctx.value());
    }

    @Override
    public XlsxValue visitExpressionP(XlsxExpressionParser.ExpressionPContext ctx) {
        System.out.println("expression p");
        return visit(ctx.expression());
    }

    @Override
    public XlsxValue visitAddAndSub(XlsxExpressionParser.AddAndSubContext ctx) {
        // System.out.println("expression + -");
        Double a = null;
        XlsxValue av = visit(ctx.expression(0));
        if (av.isRange()) {
            a = 0.0;
            for(XlsxValue avi : getCellsByRange(av)) {
                // System.out.println("+ " + avi.value);
                a += avi.asNumber();
            }
        }
        else {
            a = av.asNumber();
        }

        Double b = null;
        XlsxValue bv = visit(ctx.expression(1));
        if (bv.isRange()) {
            b = 0.0;
            for(XlsxValue bvi: getCellsByRange(bv)) {
                b += bvi.asNumber();
            }
        }
        else {
            b = bv.asNumber();
        }

        switch (ctx.op.getType()) {
            case XlsxExpressionLexer.PLUS:
                return XlsxValue.fromNumber(a + b);
            case XlsxExpressionLexer.MINUS:
                return XlsxValue.fromNumber(a - b);
        }
        throw new RuntimeException("异常：不是 + - 运算");
    }

    @Override
    public XlsxValue visitMulAndDiv(XlsxExpressionParser.MulAndDivContext ctx) {
        // System.out.println("expression * /");
        Double a = null;
        XlsxValue av = visit(ctx.expression(0));
        if (av.isRange()) {
            a = 0.0;
            for(XlsxValue avi : getCellsByRange(av)) {
                a += avi.asNumber();
            }
        }
        else {
            a = av.asNumber();
        }
        Double b = null;
        XlsxValue bv = visit(ctx.expression(1));
        if (bv.isRange()) {
            b = 0.0;
            for(XlsxValue bvi: getCellsByRange(bv)) {
                b += bvi.asNumber();
            }
        }
        else {
            b = bv.asNumber();
        }
    
        switch (ctx.op.getType()) {
            case XlsxExpressionLexer.STAR:
                return XlsxValue.fromNumber(a * b);
            case XlsxExpressionLexer.SLASH:
                return XlsxValue.fromNumber(a / b);
        }
        throw new RuntimeException("异常：不是 * / 运算");
    }

    @Override
    public XlsxValue visitFunc(XlsxExpressionParser.FuncContext ctx) {
        // System.out.println("func");
        String funcName = ctx.FUNCNAME().getText();
        List<XlsxValue> args = visit(ctx.values()).asList();
        if (funcName.equals("SUM(")) {
            double r = 0;
            // System.out.println("SUM(");
            for (XlsxValue v: args) {
                if (v.type == XlsxType.XlsxNumber) {
                    r += v.asNumber();
                } else if (v.isRange()) {
                    for(XlsxValue v2: getCellsByRange(v)) {
                        // System.out.println("v2  " + v2.value);
                        r += v2.asNumber();
                    }
                }
            }
            return XlsxValue.fromNumber(r);
        } else if (funcName.equals("SUBSTITUTE(")) {
            
        }
        return visitChildren(ctx);
    }

    @Override
    public XlsxValue visitRangeSingle(XlsxExpressionParser.RangeSingleContext ctx) {
        // System.out.println("range single");
        return XlsxValue.fromRangeSingle(ctx.getText());
    }

    @Override
    public XlsxValue visitRangeRows(XlsxExpressionParser.RangeRowsContext ctx) {
        return XlsxValue.fromRangeRows(ctx.getText());
    }

    @Override
    public XlsxValue visitRangeColums(XlsxExpressionParser.RangeColumsContext ctx) {
        return XlsxValue.fromRangeColumns(ctx.getText());
    }

    @Override
    public XlsxValue visitRange(XlsxExpressionParser.RangeContext ctx) {
        return XlsxValue.fromRange(ctx.getText());
    }

    @Override
    public XlsxValue visitValueList(XlsxExpressionParser.ValueListContext ctx) {
        List<XlsxValue> left = visit(ctx.expression()).asList();
        List<XlsxValue> right = visit(ctx.values()).asList();
        left.addAll(right);
        return XlsxValue.fromArguments(left);
    }

    @Override
    public XlsxValue visitValueSingle(XlsxExpressionParser.ValueSingleContext ctx) {
        List<XlsxValue> r = new ArrayList<XlsxValue>();
        r.add(visit(ctx.expression()));
        return XlsxValue.fromArguments(r);
    }

    @Override
    public XlsxValue visitEmpty(XlsxExpressionParser.EmptyContext ctx) {
        return XlsxValue.fromArguments(new ArrayList<>());
    }

    @Override
    public XlsxValue visitNumberValue(XlsxExpressionParser.NumberValueContext ctx) {
        return XlsxValue.fromNumber(Double.parseDouble(ctx.getText()));
    }

    @Override public XlsxValue visitStringValue(XlsxExpressionParser.StringValueContext ctx) {
        return XlsxValue.fromString(ctx.STRING().getText());
    }
}
