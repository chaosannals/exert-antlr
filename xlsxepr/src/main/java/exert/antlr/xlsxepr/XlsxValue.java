package exert.antlr.xlsxepr;

import java.util.*;
import java.util.regex.*;

public class XlsxValue {
    public XlsxType type;
    public Object value;
    public Integer top;
    public Integer left;
    public Integer right;
    public Integer bottom;

    public static XlsxValue fromRange(String v) {
        Pattern p = Pattern.compile("([A-Z]+)([0-9]+):([A-Z]+)([0-9]+)");
        Matcher m = p.matcher(v);
        if (m.find()) {
            XlsxValue r = new XlsxValue();
            r.type = XlsxType.XlsxRange;
            r.value = v;
            r.top = Integer.parseInt(m.group(2));
            r.left = fromXlsxColumn(m.group(1));
            r.right = fromXlsxColumn(m.group(3));
            r.bottom = Integer.parseInt(m.group(4));
            return r;
        }
        throw new RuntimeException("不是有效的范围 Range");
    }

    public static XlsxValue fromRangeSingle(String v) {
        Pattern p = Pattern.compile("([A-Z]+)([0-9]+)");
        Matcher m = p.matcher(v);
        if (m.find()) {
            XlsxValue r = new XlsxValue();
            r.type = XlsxType.XlsxRangeSingle;
            r.value = v;
            r.top = Integer.parseInt(m.group(2));
            r.left = fromXlsxColumn(m.group(1));
            r.right = null;
            r.bottom = null;
            return r;
        }
        throw new RuntimeException("不是有效的范围 RangeSignle");
    }

    public static XlsxValue fromRangeRows(String v) {
        Pattern p = Pattern.compile("([0-9]+):([0-9]+)");
        Matcher m = p.matcher(v);
        if (m.find()) {
            XlsxValue r = new XlsxValue();
            r.type = XlsxType.XlsxRangeRows;
            r.value = v;
            r.top = Integer.parseInt(m.group(1));
            r.left = null;
            r.right = null;
            r.bottom = Integer.parseInt(m.group(2));
            return r;
        }
        throw new RuntimeException("不是有效的范围 RangeRows");
    }

    public static XlsxValue fromRangeColumns(String v) {
        Pattern p = Pattern.compile("([A-Z]+):([A-Z]+)");
        Matcher m = p.matcher(v);
        if (m.find()) {
            XlsxValue r = new XlsxValue();
            r.type = XlsxType.XlsxRangeColumns;
            r.value = v;
            r.top = null;
            r.left = fromXlsxColumn(m.group(1));
            r.right = fromXlsxColumn(m.group(2));
            r.bottom = null;
            return r;
        }
        throw new RuntimeException("不是有效的范围 RangeColumns");
    }

    public static XlsxValue fromNumber(Double v) {
        XlsxValue r = new XlsxValue();
        r.type = XlsxType.XlsxNumber;
        r.value = v;
        return r;
    }

    public static XlsxValue fromString(String v) {
        XlsxValue r = new XlsxValue();
        r.type = XlsxType.XlsxString;
        r.value = v;
        return r;
    }

    public static XlsxValue fromArguments(List<XlsxValue> v) {
        XlsxValue r = new XlsxValue();
        r.type = XlsxType.XlsxArguments;
        r.value = v;
        return r;
    }

    public Double asNumber() {
        if (type == XlsxType.XlsxNumber) {
            return (Double) value;
        }
        throw new RuntimeException("不是数字");
    }

    public List<XlsxValue> asList() {
        if (type == XlsxType.XlsxArguments) {
            return (List<XlsxValue>) value;
        }
        throw new RuntimeException("不是参数");
    }

    public boolean isRange() {
        return type == XlsxType.XlsxRange ||
                type == XlsxType.XlsxRangeSingle ||
                type == XlsxType.XlsxRangeColumns ||
                type == XlsxType.XlsxRangeRows;
    }

    public static boolean inRange(XlsxValue range, String cl) {
        Pattern p = Pattern.compile("([A-Z]+)([0-9]+)");
        Matcher m = p.matcher(cl);
        if (!m.find()) {
            throw new RuntimeException("不是有效的 确定位置");
        }
        int c = XlsxValue.fromXlsxColumn(m.group(1));
        int r = Integer.parseInt(m.group(2));
        if (range.type == XlsxType.XlsxRange) {
            // System.out.println("in range: " + r + "," + c);
            return r >= range.top && r <= range.bottom && c >= range.left && c <= range.right;
        }
        if (range.type == XlsxType.XlsxRangeSingle) {
            return r == range.top && c == range.left;
        }
        if (range.type == XlsxType.XlsxRangeRows) {
            return r >= range.top && r <= range.bottom;
        }
        if (range.type == XlsxType.XlsxRangeColumns) {
            return c >= range.left && c <= range.right;
        }
        throw new RuntimeException("不是有效的范围类型");
    }

    public static int fromXlsxColumn(String v) {
        String t = new StringBuffer(v).reverse().toString();
        int r = 0;
        for (int i = 0; i < t.length(); ++i) {
            char c = t.charAt(i);
            r += ((int) c - (int) 'A' + 1) * Math.pow(26, i);
        }
        return r;
    }

    public static String toXlsxColumn(int i) {
        int c = (i - 1) % 26;
        i = Math.floorDiv(i - 1, 26);
        StringBuffer sb = new StringBuffer();
        sb.append((char) (c + (int) 'A'));
        while (i > 0) {
            c = (i - 1) % 26;
            i = Math.floorDiv(i - 1, 26);
            sb.append((char) (c + (int) 'A'));
        }
        return sb.reverse().toString();
    }
}
