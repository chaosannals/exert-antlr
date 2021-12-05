package exert.antlr.json;

import java.util.*;
import exert.antlr.json.grammar.*;
import exert.antlr.json.grammar.JsonDemoParser.*;

public class JsonVisitor extends JsonDemoBaseVisitor<Object> {
    @Override
    public Object visitNullValue(NullValueContext ctx) {
        return null;
    }

    @Override
    public Object visitBoolValue(BoolValueContext ctx) {
        return Boolean.parseBoolean(ctx.getText());
    }

    @Override
    public Object visitNumberValue(NumberValueContext ctx) {
        return Double.parseDouble(ctx.getText());
    }

    @Override
    public Object visitStringValue(StringValueContext ctx) {
        String text = ctx.getText();
        return text.substring(1, text.length() - 1);
    }

    @Override
    public Object visitVoidArray(VoidArrayContext ctx) {
        return new ArrayList<Object>();
    }

    @Override
    public Object visitNormalArray(NormalArrayContext ctx) {
        return visit(ctx.values());
    }

    @Override
    public Object visitUniqueValue(UniqueValueContext ctx) {
        ArrayList<Object> result = new ArrayList<>();
        result.add(visit(ctx.value()));
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object visitMultipleValue(MultipleValueContext ctx) {
        ArrayList<Object> result = new ArrayList<>();
        Object head = visit(ctx.value());
        result.add(head);
        Object tail = visit(ctx.values());
        result.addAll((ArrayList<Object>) tail);
        return result;
    }

    @Override
    public Object visitVoidObject(VoidObjectContext ctx) {
        return new HashMap<String, Object>();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object visitNormalObject(NormalObjectContext ctx) {
        HashMap<String, Object> result = new HashMap<>();
        List<JsonMember> ms = (List<JsonMember>)visit(ctx.members());
        for(JsonMember e: ms) {
            result.put(e.getKey(), e.getValue());
        }
        return result;
    }

    @Override
    public Object visitUniqueMember(UniqueMemberContext ctx) {
        ArrayList<Object> result = new ArrayList<>();
        result.add(visit(ctx.member()));
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object visitMultipleMember(MultipleMemberContext ctx) {
        ArrayList<JsonMember> result = new ArrayList<>();
        Object head = visit(ctx.member());
        result.add((JsonMember) head);
        Object tail = visit(ctx.members());
        result.addAll((List<JsonMember>) tail);
        return result;
    }

    @Override
    public Object visitMember(MemberContext ctx) {
        String ks = ctx.STRING().getText();
        Object key = ks.substring(1, ks.length() - 1);
        Object value = visit(ctx.value());
        return new JsonMember((String) key, value);
    }
}
