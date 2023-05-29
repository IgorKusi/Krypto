package pl.pkr.view;

import pl.pkr.model.Util;

import java.math.BigInteger;

public class Static {
    public static byte[] des_key1;
    public static String des_s_key1;
    public static byte[] des_key2;
    public static String des_s_key2;
    public static byte[] des_key3;
    public static String des_s_key3;


    public static BigInteger[] bp_keyPub;
    public static BigInteger[] bp_keyPri;
    public static BigInteger bp_M;
    public static BigInteger bp_W_1;

    public static void loadDesKeys(String... keys) {
        des_s_key1 = keys[0];
        des_key1 = Util.hex_to_bytes(des_s_key1);
        
        des_s_key2 = keys[1];
        des_key2 = Util.hex_to_bytes(des_s_key2);
        
        des_s_key3 = keys[2];
        des_key3 = Util.hex_to_bytes(des_s_key3);
    }



}
