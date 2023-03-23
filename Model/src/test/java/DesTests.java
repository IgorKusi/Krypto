import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.pkr.model.DES;
import pl.pkr.model.Util;

import java.io.PrintStream;
import java.util.Random;

public class DesTests {
    static class DesTestFixture {
        public static boolean[] arr_32 = new boolean[]{
                true, true, true, true, false, false, false, false,
                true, false, true, false, false, true, false, true,
                false, false, false, false, true, true, true, true,
                false, true, false, true
        };

        public static boolean[] arr_32_rotated = new boolean[]{
                true, true, true, false, false, false, false, true,
                false, true, false, false, true, false, true, false,
                false, false, false, true, true, true, true, false,
                true, false, true, true
        };

        public static boolean[] arr_56 = new boolean[]{
                true, true, true, true, false, false, false, false,
                true, false, true, false, false, true, false, true,
                false, false, false, false, true, true, true, true,
                false, true, false, true,
                true, true, true, false, false, false, false, true,
                false, true, false, false, true, false, true, false,
                false, false, false, true, true, true, true, false,
                true, false, true, true
        };

        public static boolean[] rand_bools_128 = {
                false, true, false, true, true, false, true, true,
                false, false, false, true, true, false, true, false,
                true, true, true, false, false, true, false, true,
                true, false, true, false, false, true, false, true,
                true, false, true, false, false, true, true, false,
                true, true, false, false, true, false, true, true,
                true, false, true, false, false, true, true, false,
                false, true, false, true, true, false, true, false,
                false, true, false, true, true, false, true, false,
                false, true, true, false, true, true, false, false,
                true, false, true, true, false, true, false, false,
                true, false, true, true, false, true, false, false,
                true, false, true, true, false, true, true, true,
                false, false, true, true, false, true, false, false,
                true, false, true, true, false, true, false, false,
                true, false, true, true, false, true, true, false,
                true, true, false, false, true, false, true, true
        };

        public static boolean[] get_bits(int bit_num) {
            return get_bits(bit_num, 0);
        }

        public static boolean[] get_bits(int bit_num, int start_index) {
            boolean[] ret = new boolean[bit_num];

            while (start_index > 128) start_index %= 128;
            for (int i = 0; i < bit_num; ++i) {
                ret[i] = rand_bools_128[(i + start_index) % 128];
            }

            return ret;
        }

        public static boolean[] get_rand_bits(int bit_num) {
            boolean[] ret = new boolean[bit_num];

            Random r = new Random();
            for (int i = 0; i < bit_num; i++) {
                ret[i] = r.nextBoolean();
            }

            return ret;
        }

        public static String byte_to_numeric(byte b) {
            return String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
        }
    }

    @Test
    public void PC1_test(){
        boolean[] key_64 = DesTestFixture.get_bits(64);
        Util.Pair<boolean[]> ret = DES.KeyManipulation.apply_PC1(key_64);
        Assertions.assertEquals(28, ret.left().length);
        Assertions.assertEquals(28, ret.right().length);

        System.out.println( Util.bits_to_numeric(key_64));
        System.out.println(Util.bits_to_numeric(ret.left()));
        System.out.print(Util.bits_to_numeric(ret.right()));


    }



}



/*
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.pkr.model.DES;
import pl.pkr.model.Util;

import java.util.Arrays;
import java.util.Random;

public class DesTests {

    static class DesTestFixture {
        public static boolean[] arr_32 = new boolean[] {
                true, true, true, true, false, false, false, false,
                true, false, true, false, false, true, false, true,
                false, false, false, false, true, true, true, true,
                false, true, false, true
        };

        public static boolean[] arr_32_rotated = new boolean[] {
                true, true, true, false, false, false, false, true,
                false, true, false, false, true, false, true, false,
                false, false, false, true, true, true, true, false,
                true, false, true, true
        };

        public static boolean[] arr_56 = new boolean[] {
                true, true, true, true, false, false, false, false,
                true, false, true, false, false, true, false, true,
                false, false, false, false, true, true, true, true,
                false, true, false, true,
                true, true, true, false, false, false, false, true,
                false, true, false, false, true, false, true, false,
                false, false, false, true, true, true, true, false,
                true, false, true, true
        };

        public static boolean[] rand_bools_128 = {
                false, true, false, true, true, false, true, true,
                false, false, false, true, true, false, true, false,
                true, true, true, false, false, true, false, true,
                true, false, true, false, false, true, false, true,
                true, false, true, false, false, true, true, false,
                true, true, false, false, true, false, true, true,
                true, false, true, false, false, true, true, false,
                false, true, false, true, true, false, true, false,
                false, true, false, true, true, false, true, false,
                false, true, true, false, true, true, false, false,
                true, false, true, true, false, true, false, false,
                true, false, true, true, false, true, false, false,
                true, false, true, true, false, true, true, true,
                false, false, true, true, false, true, false, false,
                true, false, true, true, false, true, false, false,
                true, false, true, true, false, true, true, false,
                true, true, false, false, true, false, true, true
        };

        public static boolean[] get_bits(int bit_num) {
            return get_bits(bit_num, 0);
        }

        public static boolean[] get_bits(int bit_num, int start_index) {
            boolean[] ret = new boolean[bit_num];

            while (start_index > 128) start_index %= 128;
            for (int i = 0; i < bit_num; ++i) {
                ret[i] = rand_bools_128[(i + start_index) % 128];
            }

            return ret;
        }

        public static boolean[] get_rand_bits(int bit_num) {
            boolean[] ret = new boolean[bit_num];

            Random r = new Random();
            for (int i = 0; i < bit_num; i++) {
                ret[i] = r.nextBoolean();
            }

            return ret;
        }

        public static String byte_to_numeric(byte b) {
            return String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
        }






    @Test
    public void rotate_left_test() {
        boolean[] testArr = new boolean[] {
                true, true, true, true, false, false, false, false,
                true, false, true, false, false, true, false, true,
                false, false, false, false, true, true, true, true,
                false, true, false, true
        };

        boolean[] testArr_rotated = new boolean[] {
                true, true, true, false, false, false, false, true,
                false, true, false, false, true, false, true, false,
                false, false, false, true, true, true, true, false,
                true, false, true, true
        };

        boolean[] ret = DES.KeyManipulation.rotate_left(testArr);

        for (int i = 0; i < 28; i++) {
            Assertions.assertEquals(testArr_rotated[i], ret[i]);
        }

    }

    @Test
    public void connect_halves_to_56_test() {
        boolean[] testArr = new boolean[] {
                true, true, true, true, false, false, false, false,
                true, false, true, false, false, true, false, true,
                false, false, false, false, true, true, true, true,
                false, true, false, true,
                true, true, true, false, false, false, false, true,
                false, true, false, false, true, false, true, false,
                false, false, false, true, true, true, true, false,
                true, false, true, true
        };

        boolean[] left = new boolean[] {
                true, true, true, true, false, false, false, false,
                true, false, true, false, false, true, false, true,
                false, false, false, false, true, true, true, true,
                false, true, false, true,
        };

        boolean[] right = new boolean[] {
                true, true, true, false, false, false, false, true,
                false, true, false, false, true, false, true, false,
                false, false, false, true, true, true, true, false,
                true, false, true, true
        };

        boolean[] ret = DES.KeyManipulation.connect_halves_to_56(left, right);

        for (int i = 0; i < 56; i++) {
            Assertions.assertEquals(testArr[i], ret[i]);
        }

    }

    @Test
    public void testFeistel()
    {
         boolean[] testBlock32 = new boolean[] {
                 true, true, false, false, true, true, false, false,
                 true, true, false, false, true, true, false, false,
                 true, true, false, false, true, true, false, false,
                 true, true, false, false, true, true, false, false,
         };

         boolean[] testBlock48 = new boolean[]{
                false, true, true, false, false, true, true, false,
                false, true, true, false, false, true, true, false,
                false, true, true, false, false, true, true, false,
                false, true, true, false, false, true, true, false,
                false, true, true, false, false, true, true, false,
         };

         //boolean[][] testBoxes = DES.TextManipulation.Feistel.split_48_to_8x6(testBlock48);
        // boolean[][] test8x4Boxes = new boolean[8][4];



    }


    @Test
    public void testSplit48to8x6(){
        boolean[] testBlock48 = new boolean[]{
                false, true, true, false, false, true
                , true, false, false, true, true, false,
                false, true, true, false, false, true,
                true, false, false, true, true, false,
                false, true, true, false, false, true,
                true, false, false, true, true, false,
                false, true, true, false, true, false,
                true, false, false, true, true, false

         };

        Assertions.assertFalse(DES.TextManipulation.Feistel.split_48_to_8x6(testBlock48)[0][0]);
        Assertions.assertTrue(DES.TextManipulation.Feistel.split_48_to_8x6(testBlock48)[1][0]);
        Assertions.assertTrue(DES.TextManipulation.Feistel.split_48_to_8x6(testBlock48)[2][5]);
        Assertions.assertFalse(DES.TextManipulation.Feistel.split_48_to_8x6(testBlock48)[7][5]);
    }

    @Test
    public void connect8x4to32_test() {
        boolean[][] test8x4boxxxx = new boolean[][] {
                {true, false, false, true},
                {true, true, true, true},
                {false, false, false, false},
                {true, false, false, true},
                {true, false, false, true},
                {true, false, false, true},
                {true, true, false, false},
                {true, false, false, true}
        };

        Assertions.assertTrue(DES.TextManipulation.Feistel.connect_8x4_to_32(test8x4boxxxx)[0]);
        Assertions.assertTrue(DES.TextManipulation.Feistel.connect_8x4_to_32(test8x4boxxxx)[4]);
        Assertions.assertTrue(DES.TextManipulation.Feistel.connect_8x4_to_32(test8x4boxxxx)[5]);
        Assertions.assertTrue(DES.TextManipulation.Feistel.connect_8x4_to_32(test8x4boxxxx)[6]);
        Assertions.assertTrue(DES.TextManipulation.Feistel.connect_8x4_to_32(test8x4boxxxx)[7]);
        Assertions.assertFalse(DES.TextManipulation.Feistel.connect_8x4_to_32(test8x4boxxxx)[8]);
        Assertions.assertFalse(DES.TextManipulation.Feistel.connect_8x4_to_32(test8x4boxxxx)[9]);
        Assertions.assertFalse(DES.TextManipulation.Feistel.connect_8x4_to_32(test8x4boxxxx)[10]);
        Assertions.assertFalse(DES.TextManipulation.Feistel.connect_8x4_to_32(test8x4boxxxx)[11]);
        Assertions.assertTrue(DES.TextManipulation.Feistel.connect_8x4_to_32(test8x4boxxxx)[31]);
        Assertions.assertFalse(DES.TextManipulation.Feistel.connect_8x4_to_32(test8x4boxxxx)[30]);
        Assertions.assertEquals(32, DES.TextManipulation.Feistel.connect_8x4_to_32(test8x4boxxxx).length);
    }

    @Test
    public void pickouter2andinner4test(){
        boolean[] testblock6 = new boolean[] {true, true, false, false, true, false};

        Assertions.assertTrue(DES.TextManipulation.Feistel.pick_outer_2_and_inner_4(testblock6).left()[0]);
        Assertions.assertFalse(DES.TextManipulation.Feistel.pick_outer_2_and_inner_4(testblock6).left()[1]);
        Assertions.assertTrue(DES.TextManipulation.Feistel.pick_outer_2_and_inner_4(testblock6).left().length == 2 );

        Assertions.assertTrue(DES.TextManipulation.Feistel.pick_outer_2_and_inner_4(testblock6).right()[0]);
        Assertions.assertFalse(DES.TextManipulation.Feistel.pick_outer_2_and_inner_4(testblock6).right()[2]);
        Assertions.assertTrue(DES.TextManipulation.Feistel.pick_outer_2_and_inner_4(testblock6).right().length == 4 );

    }


    @Test
    public void bits_to_int_test() {
        boolean[] block_4 = DesTestFixture.get_bits(4),
                  block_8 = DesTestFixture.get_bits(8),
                  block_6 = DesTestFixture.get_bits(6),
                  block_2 = new boolean[] { true, false };

        int[] rets = {
                5,
                91,
                22,
                2
        };

        Assertions.assertEquals(rets[0], DES.TextManipulation.Feistel.bits_to_int(block_4));
        Assertions.assertEquals(rets[1], DES.TextManipulation.Feistel.bits_to_int(block_8));
        Assertions.assertEquals(rets[2], DES.TextManipulation.Feistel.bits_to_int(block_6));
        Assertions.assertEquals(rets[3], DES.TextManipulation.Feistel.bits_to_int(block_2));

    }

    @Test
    public void int_to_bit4_test() {
        boolean[][] blocks_of_4 = new boolean[][] {
                {false, false, false, false},
                {false, false, false, true},
                {false, false, true, false},
                {false, false, true, true},
                {false, true, false, false},
                {false, true, false, true},
                {false, true, true, false},
                {false, true, true, true},
                {true, false, false, false},
                {true, false, false, true},
                {true, false, true, false},
                {true, false, true, true},
                {true, true, false, false},
                {true, true, false, true},
                {true, true, true, false},
                {true, true, true, true}
        };

        for (int i = 0; i < 16; i++) {
            boolean[] ret = DES.TextManipulation.Feistel.int_to_bit_4(i);
            System.out.println(Util.bits_to_numeric(blocks_of_4[i]));
            System.out.println(Util.bits_to_numeric(ret));
            System.out.println();

            for (int j = 0; j < 4; j++) {
                Assertions.assertEquals(blocks_of_4[i][j], ret[j]);
            }
        }

    }

    @Test
    public void connect_halves_to_64_test() {
        boolean[] block_64 = DesTestFixture.get_bits(64);
        boolean[] left_32 = DesTestFixture.get_bits(32);
        boolean[] right_32 = DesTestFixture.get_bits(32, 32);

        boolean[] ret_64 = DES.TextManipulation.connect_halves_to_64(left_32, right_32);

//        System.out.println(Util.bits_to_numeric(block_64));
//        System.out.println(Util.bits_to_numeric(left_32));
//        System.out.println(Util.bits_to_numeric(right_32));
//        System.out.println(Util.bits_to_numeric(ret_64));

        for (int i = 0; i < 64; i++) {
            Assertions.assertEquals(block_64[i], ret_64[i]);
        }

    }

    @Test
    public void byte_arr_to_bits_64_test() {
        byte[] bytes = new byte[] {
                1, 2, 7, 15, 13, 12, 0, 9
        };

        StringBuilder s_bytes = new StringBuilder();
        for (byte b : bytes) {
            s_bytes.append(DesTestFixture.byte_to_numeric(b)).append(" ");
        }
        s_bytes = new StringBuilder(s_bytes.toString().trim());

        boolean[] ret = DES.TextManipulation.byte_arr_to_bits_64(bytes);
        String s_ret = Util.bits_to_numeric(ret);

        Assertions.assertEquals(s_bytes.toString().trim(), s_ret.trim());

    }



    @Test
    public void bits64tobytearrtestxdlol(){
        boolean[] test_bits = DesTestFixture.get_bits(64);

        byte[] byte_arr = DES.TextManipulation.bits_64_to_byte_arr(test_bits);
        Assertions.assertEquals(8, byte_arr.length);

        System.out.println(Util.bits_to_numeric(test_bits));
        for (int i = 0; i < 8; i++) {
            System.out.print(DesTestFixture.byte_to_numeric(byte_arr[i]));
            System.out.print(" ");
        }
        System.out.println();

    }

    @Test
    public void getSubkeys_test() {
        boolean[] left = new boolean[32];
        boolean[] right = new boolean[32];
        left[4] = true;

        Util.Pair<boolean[]> halves = new Util.Pair<>(
                DES.KeyManipulation.rotate_left(left),
                right
        );

        boolean[] subkey = DES.KeyManipulation.apply_PC2(halves);

        //15

        for (int i = 0; i < 48; i++) {
            if (i == 15) {
                Assertions.assertTrue(subkey[i]);
                continue;
            }
            Assertions.assertFalse(subkey[i]);
        }

    }

    @Test
    public void applyPC1(){
        boolean[] testKey_64 = DesTestFixture.get_bits(64);


    }



    @Test
    public void reverseSubkeys_test(){
        boolean[][] testsubkeys = new boolean[16][48];

        for (int i = 0; i < 16; i++) {
            testsubkeys[i] = DesTestFixture.get_bits(48, 48 * i);
        }
        boolean[][] revsubkeys = DES.KeyManipulation.reverseSubkeys(testsubkeys);

        for (int i = 0; i < 16; i++) {
            Assertions.assertEquals(testsubkeys[i], revsubkeys[16 - i - 1]);
        }
    }

    @Test
    public void writeIntToBitsTest() {
        boolean[] five = new boolean[] {
                false, false, false, false, false, true, false, true
        };

        boolean[] bits = Util.writeIntToBits(5);

        for (int i = 0; i < 8; i++) {
            Assertions.assertEquals(five[i], bits[i]);
        }
    }




    @Test
    public void readIntFromBitsTest(){
        boolean[] bits = new boolean[] {false, false, false, false, false, false, false, true};
        int value = Util.readIntFromBits(bits);
        Assertions.assertEquals(1, value);
        bits = new boolean[] {false, false, false, true, false, true, true, false};
        value = Util.readIntFromBits(bits);
        Assertions.assertEquals(22, value);
    }
    //@Test
   // public void applyPC2_test(){
       // boolean[] right = new boolean[] {true, false};
       // boolean[] left = new boolean[] {true, false};

       // Pair<boolean[]> testKeyHalves = new Pair<>();
    }



*/
