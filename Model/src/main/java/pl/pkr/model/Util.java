package pl.pkr.model;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Util {
    public static BigInteger generateKey() {
        return new BigInteger(64, new Random());
    }

    public record Pair<T>(T left, T right) {
        public static <T> Pair<T> fromSPair(SPair<T> sPair) {
            return new Pair<>(
                    sPair.outer,
                    sPair.inner
            );
        }
    }

    public record SPair<T>(T outer, T inner) {}

    public static final int[] IP =
                    {58, 50, 42, 34, 26, 18, 10, 2,
                    60, 52, 44, 36, 28, 20, 12, 4,
                    62, 54, 46, 38, 30, 22, 14, 6,
                    64, 56, 48, 40, 32, 24, 16, 8,
                    57, 49, 41, 33, 25, 17,  9, 1,
                    59, 51, 43, 35, 27, 19, 11, 3,
                    61, 53, 45, 37, 29, 21, 13, 5,
                    63, 55, 47, 39, 31, 23, 15, 7};

    public static final int[] FP =
            {40, 8, 48, 16, 56, 24, 64, 32,
                    39, 7, 47, 15, 55, 23, 63, 31,
                    38, 6, 46, 14, 54, 22, 62, 30,
                    37, 5, 45, 13, 53, 21, 61, 29,
                    36, 4, 44, 12, 52, 20, 60, 28,
                    35, 3, 43, 11, 51, 19, 59, 27,
                    34, 2, 42, 10, 50, 18, 58, 26,
                    33, 1, 41,  9, 49, 17, 57, 25};

    public static final int[] E =
                    {32, 1, 2, 3, 4, 5,
                    4, 5, 6, 7, 8, 9,
                    8, 9, 10, 11, 12, 13,
                    12, 13, 14, 15, 16, 17,
                    16, 17, 18, 19, 20, 21,
                    20, 21, 22, 23, 24, 25,
                    24, 25, 26, 27, 28, 29,
                    28, 29, 30, 31, 32, 1};

    public static final int[] P =
                   {16, 7, 20, 21, 29, 12, 28, 17,
                    1, 15, 23, 26, 5, 18, 31, 10,
                    2, 8, 24, 14, 32, 27, 3, 9,
                    19, 13, 30, 6, 22, 11, 4, 25};

    public static final Pair<int[]> PC1 = new Pair<>(
            new int[]
                    {57, 49, 41, 33, 25, 17, 9,
                    1, 58, 50, 42, 34, 26, 18,
                    10, 2, 59, 51, 43, 35, 27,
                    19, 11, 3, 60, 52, 44, 36
            },

            new int[]
                    {63, 55, 47, 39, 31, 23, 15,
                     7, 62, 54, 46, 38, 30, 22,
                     14, 6, 61, 53, 45, 37, 29,
                     21, 13, 5, 28, 20, 12, 4}

    );

    public static final int[] PC2 =
              {14, 17, 11, 24, 1, 5,
               3, 28, 15, 6, 21, 10,
               23, 19, 12, 4, 26, 8,
               16, 7, 27, 20, 13, 2,
               41, 52, 31, 37, 47, 55,
               30, 40, 51, 45, 33, 48,
               44, 49, 39, 56, 34, 53,
               46, 42, 50, 36, 29, 32};


    public static final int[] bit_rot = new int[] {
            1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2 ,2, 2, 2, 1
    };




    public static final int[][][] S_Box = new int[][][] {
            {
                { 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7 },
                { 0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8 },
                { 4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0 },
                { 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13 }
            },{
                { 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10 },
                { 3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5 },
                { 0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15 },
                { 13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9 }
            },{
                { 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8 },
                { 13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1 },
                { 13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7 },
                { 1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12 }
            },{
                { 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15 },
                { 13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9 },
                { 10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 2, 14, 5, 2, 8, 4 },
                { 3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14 }
            },{
                { 2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9 },
                { 14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6 },
                { 4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14 },
                { 11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3 }
            },{
                { 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11 },
                { 10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8 },
                { 9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6 },
                { 4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13 }

            },{
                { 4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1 },
                { 13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6 },
                { 1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2 },
                { 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12 }
            },{
                { 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7 },
                { 1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2 },
                { 7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8 },
                { 2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11 }
            }
    };

    public static boolean[] connect_halves(Pair<boolean[]> halves, int half_size) {
        boolean[] ret = new boolean[half_size * 2];

        for (int i = 0; i < half_size; i++) {
            ret[i] = halves.left()[i];
            ret[half_size + i] = halves.right()[i];
        }

        return ret;
    }

    public static Pair<boolean[]> split_in_half(boolean[] bit_block, int block_size) {
        boolean[] left = new boolean[block_size / 2];
        boolean[] right = new boolean[block_size / 2];

        for (int i = 0; i < block_size / 2; i++) {
            left[i] = bit_block[i];
            right[i] = bit_block[(block_size / 2) + i];
        }

        return new Pair<>(left, right);
    }

    public static String bits_to_numeric(boolean[] bits) {
        StringBuilder sb = new StringBuilder(bits.length);

        for (int i = 0; i < bits.length; i++) {
            sb.append(bits[i] ? "1" : "0");
            if ((i + 1) % 8 == 0) sb.append(" ");
        }

        return sb.toString();
    }

    public static boolean[] xor(boolean[] block_left, boolean[] block_right, int block_size) {
        boolean[] ret = new boolean[block_size];

        for (int i = 0; i < block_size; i++) {
            ret[i] = block_left[i] ^ block_right[i];
        }

        return ret;
    }

    public static int bits_to_int(boolean[] bit_block, int block_size) {
        int ret = 0;

        for (int i = 0; i < block_size; i++) {
            ret = ret | (bit_block[i] ? 1 : 0);
            if (i != block_size - 1) ret = ret << 1;
        }

        return ret;
    }

    public static byte bits_8_to_byte(boolean[] bits) {
        byte ret = 0;

        for (int i = 0; i < 8; i++) {
            ret |= (bits[i] ? 1 : 0);
            if (i != 7) ret <<= 1;
        }

        return ret;
    }

    public static boolean[] int_to_bits_4(int value) {
        boolean[] ret = new boolean[4];
        int i = 0;

        while (value > 0) {
            ret[i] = value % 2 == 1;
            value /= 2;
            ++i;
        }

        return ret;
    }

    public static boolean[] byte_to_bits(byte b) {
        boolean[] bits = new boolean[8];
        int i = 7;

        while (Math.abs(b) > 0) {
            //TODO modulo ujemne i -1/2 = -1
            bits[i] = b % 2 == 1;
            b >>= 1;
            --i;
        }

        return bits;
    }

    public static boolean[][] string_to_bits(String string) {
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);

        int nRows = bytes.length / 8 + 1;
        int emptyBytes = nRows - bytes.length;

        boolean[][] matrix = new boolean[nRows][64];

        for (int row = 0; row < nRows; row++) {

            //row -> 8x8 bits
            for (int byte_in_row = 0; byte_in_row < 8; byte_in_row++) {
                boolean[] byte_bits =
                        8 * row + byte_in_row >= bytes.length
                                ? new boolean[8]
                                : byte_to_bits(bytes[row * 8 + byte_in_row]);

                for (int bit_in_byte = 0; bit_in_byte < 8; bit_in_byte++) {
                    matrix[row][8 * byte_in_row + bit_in_byte] = byte_bits[bit_in_byte];
                }
            }
        }

        boolean[] filled_bytes = new boolean[8];
        boolean[] temp = int_to_bits_4(emptyBytes);
        for (int i = 0; i < 4; i++) {
            filled_bytes[4 + i] = temp[i];
        }

        for (int offset_bit = 0; offset_bit < 8; offset_bit++) {
            matrix[nRows - 1][7 * 8 + offset_bit] = filled_bytes[offset_bit];
        }

        return matrix;
    }

    public static boolean[][] numeric_to_bits(String string) {

        String[] bytes = string.split(" ");
        boolean[][] matrix = new boolean[bytes.length / 8][64];

        for (int row = 0; row < bytes.length / 8; row++) {
            for (int byte_in_row = 0; byte_in_row < 8; byte_in_row++) {
                for (int bit_in_byte = 0; bit_in_byte < 8; bit_in_byte++) {
                    matrix[row][8 * byte_in_row + bit_in_byte]
                            = bytes[8 * row + byte_in_row].charAt(bit_in_byte) == '1';
                }
            }
        }

        return matrix;
    }

    public static boolean[] key_string_to_bits(String key_64) {
        boolean[] bits = new boolean[64];

        for (int i = 0; i < key_64.length(); i++) {
            bits[64 - key_64.length() + i] = key_64.charAt(i) == '1';
        }

        return bits;
    }

}
