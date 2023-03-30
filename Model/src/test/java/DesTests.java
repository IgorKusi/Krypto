import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.pkr.model.DES;
import pl.pkr.model.Util;

import java.io.PrintStream;
import java.util.Random;

public class DesTests {

    @Test
    public void permutation_test() {
        byte b = 0b00001111;
        byte[] arr = new byte[4];
        arr[1] = b;

        byte[] ret = DES.apply_permutation(arr, Util.P);

        Assertions.assertEquals(-128, ret[0]);
        Assertions.assertEquals(64, ret[1]);
        Assertions.assertEquals(16, ret[2]);
        Assertions.assertEquals(64, ret[3]);

    }


    @Test
    public void subkey_test() {
        byte[] stripped_key_56 = new byte[7];
        stripped_key_56[0] = 0b00001000;
        stripped_key_56[6] = 0b00010000;


        byte[] left = new byte[4],
                right = new byte[4];
        System.arraycopy(stripped_key_56, 0, left, 0, 4);
        System.arraycopy(stripped_key_56, 3, right, 0, 4);

        Assertions.assertEquals(0b00001000, left[0]);
        Assertions.assertEquals(0b00010000, right[3]);

        left[0] = (byte) 0b10010000;
        right[0] = (byte) 0b00001001;

        System.out.println(Util.byte_to_numeric(left[0]));
        System.out.println(Util.byte_to_numeric(left[3]));
        System.out.println(Util.byte_to_numeric(right[0]));
        System.out.println(Util.byte_to_numeric(right[3]));
        System.out.println();

        DES.rotate_left(left, DES.Half.LEFT);
        DES.rotate_left(left, DES.Half.LEFT);
        DES.rotate_left(right, DES.Half.RIGHT);

        System.out.println(Util.byte_to_numeric(left[0]));
        System.out.println(Util.byte_to_numeric(left[3]));
        System.out.println(Util.byte_to_numeric(right[0]));
        System.out.println(Util.byte_to_numeric(right[3]));

        Assertions.assertEquals(0b01000000, left[0]);
        Assertions.assertEquals(0b00100000, left[3]);
        Assertions.assertEquals(0b00000010, right[0]);
        Assertions.assertEquals(0b00100001, right[3]);

    }

    @Test
    public void bytes_to_hex_test() {
        byte[] bytes = new byte[]{
                5, 10, 120, 100, 34
        };

        System.out.println(Util.byte_to_hex(bytes[0]));
        System.out.println(Util.byte_to_hex(bytes[1]));
        System.out.println(Util.byte_to_hex(bytes[2]));
        System.out.println(Util.byte_to_hex(bytes[3]));
        System.out.println(Util.byte_to_hex(bytes[4]));

        System.out.println(Util.bytes_to_hex(bytes));


    }



}
