package exert.antlr;

/**
 * 二进制 class 文件的类加载器。
 * 
 */
public class AppLoader extends ClassLoader {
    public Class<?> load(String className, byte[] asms) {
        return super.defineClass(className, asms, 0, asms.length);
    }
}
