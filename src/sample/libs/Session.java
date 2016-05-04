package sample.libs;

import java.lang.ref.SoftReference;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class Session {



        protected static HashMap<String, String> session = new HashMap<String, String>();
        public static void setKeyValue(String key, String value)
        {
            session.put(key, value);
        }
        public static String getKeyValue(String key)
        {
            return session.get(key);
        }
        public static void removeKey(String key)
        {
            session.remove(key);
        }
        public static void removeKeyValue(String key, String value)
        {
            session.remove(key, value);
        }
        public static void clear()
        {
            session.clear();
        }

}
