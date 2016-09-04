package sample.tools;

/**
 * Created by oleh on 11.08.2016.
 */
public class DivideString {
    private String fullPath;
    private char pathSeparator,
            extensionSeparator;

    public DivideString(String str, char sep, char ext) {
        fullPath = str;
        pathSeparator = sep;
        extensionSeparator = ext;
    }

    public String extension() {
        int dot = fullPath.lastIndexOf(extensionSeparator);
        return fullPath.substring(dot + 1);
    }

    // получение имени файла без расширения
    public String filename() {
        int dot = fullPath.lastIndexOf(extensionSeparator);
        int sep = fullPath.lastIndexOf(pathSeparator);
        return fullPath.substring(sep + 1, dot);
    }

    public String path() {
        int sep1 = fullPath.lastIndexOf(pathSeparator);
        return fullPath.substring(0, sep1);
    }
}
