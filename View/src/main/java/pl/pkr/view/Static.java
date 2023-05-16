package pl.pkr.view;

import pl.pkr.model.Util;

public class Static {
    public static byte[] key1;
    public static String s_key1;
    public static byte[] key2;
    public static String s_key2;
    public static byte[] key3;
    public static String s_key3;
    
    public static void loadKeys(String... keys) {
        s_key1 = keys[0];
        key1 = Util.hex_to_bytes(s_key1);
        
        s_key2 = keys[1];
        key2 = Util.hex_to_bytes(s_key2);
        
        s_key3 = keys[2];
        key3 = Util.hex_to_bytes(s_key3);
    }

}
