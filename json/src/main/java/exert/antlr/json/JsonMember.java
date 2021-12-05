package exert.antlr.json;

public class JsonMember {
    private String key;
    private Object value;

    public JsonMember(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }
}
