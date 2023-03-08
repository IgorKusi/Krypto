package pl.pkr.model;

import java.math.BigInteger;
import java.util.Random;

public class Util {

    public static BigInteger generateKey() {
        return new BigInteger(64, new Random());
    }


}
