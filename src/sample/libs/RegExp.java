package sample.libs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExp {
    public static boolean checkWithRegExp(String userNameString){
        Pattern p = Pattern.compile("^[a-z0-9_-]{6,32}$");
        Matcher m = p.matcher(userNameString);
        return m.matches();
    }
}
