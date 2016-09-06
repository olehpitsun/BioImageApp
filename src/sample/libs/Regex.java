package sample.libs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
    public static boolean checkWithRegex (String userNameString, String regex){
        Pattern pat = Pattern.compile(regex);
        Matcher mat = pat.matcher(userNameString);
        return mat.matches();
    }
}
