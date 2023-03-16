package pl.pkr.model;

import pl.pkr.model.Util.Pair;

import java.nio.charset.StandardCharsets;
import java.util.BitSet;

import static pl.pkr.model.DES.KeyManipulation.*;
import static pl.pkr.model.DES.TextManipulation.*;
import static pl.pkr.model.DES.TextManipulation.Feistel.*;


public class DES {
    boolean[] key;
    Pair<boolean[]> keyHalves;
    boolean[][] subkeys;

    public DES(boolean[] key) {
        this.key = key;
        this.keyHalves = applyPC1(this.key);
        this.subkeys = getSubkeys(this.keyHalves);
    }

    public String encryptString(String string) {
        byte[] allBytes = string.getBytes(StandardCharsets.UTF_8);
        byte[] buffer = new byte[8];
        int lastByte = 0;
        StringBuilder ret = new StringBuilder();

        for (int i = 0; i < allBytes.length; i++) {
            buffer[i % 8] = allBytes[i];
            lastByte = i % 8;

            if ((i + 1) % 8 == 0) {
                boolean[] encrypted = encrypt(byte_arr_to_bits_64(buffer));
                ret.append(new String(bits_64_to_byte_arr(encrypted)));
            }
        }

        if (lastByte != 7) {
            boolean[] encrypted = encrypt(byte_arr_to_bits_64(buffer));
            ret.append(new String(bits_64_to_byte_arr(encrypted)));
        }

        return ret.toString();
    }

    public String decryptString(String string) {
        this.subkeys = reverseSubkeys(this.subkeys);
        String ret = encryptString(string);
        this.subkeys = reverseSubkeys(this.subkeys);
        return ret;
    }

    public boolean[] encrypt(boolean[] block_64) {
        block_64 = applyIP(block_64);
        Pair<boolean[]> halves = split_64_in_halves(block_64);

        for (int i = 0; i < 16; ++i) {
            halves = round(halves, i);
        }

        block_64 = connect_halves_to_64(halves.left(), halves.right());
        block_64 = applyFP(block_64);

        return block_64;
    }

    public boolean[] decrypt(boolean[] block_64) {
        this.subkeys = reverseSubkeys(subkeys);
        boolean[] ret = encrypt(block_64);
        this.subkeys = reverseSubkeys(subkeys);
        return ret;
    }


    Pair<boolean[]> round(Pair<boolean[]> block_halves_32, int round_index) {
        boolean[] arr = feistel(block_halves_32.right(), this.subkeys[round_index]);
        arr = xor(block_halves_32.left(), arr);

        return new Pair<>(block_halves_32.right(), arr);
    }



    public static class TextManipulation {
        public static boolean[] byte_arr_to_bits_64(byte[] bytes) {
            boolean[] ret = new boolean[64];
            BitSet bits = BitSet.valueOf(bytes);

            for (int i = 0; i < 64; ++i) {
                ret[i] = bits.get(i);
            }

            return ret;
        }

        public static byte[] bits_64_to_byte_arr(boolean[] bits) {
            byte[] ret = new byte[8];

            byte b = 0b00000000;
            for (int i = 0; i < 64; ++i) {
                b |= bits[i] ? 0b00000001 : 0;
                b <<= 1;

                if ((i + 1) % 8 == 0) {
                    ret[i / 8] = b;
                    b = 0b00000000;
                }
            }

            return ret;
        }
        public static boolean[] applyIP(boolean[] block_64) {
            boolean[] ret = new boolean[64];
            for (int i = 0; i < 64; ++i)
                ret[i] = block_64[ Util.IP[i] ];

            return ret;
        }

        public static Pair<boolean[]> split_64_in_halves(boolean[] block_64) {
            boolean[] left = new boolean[32],
                      right = new boolean[32];

            for (int i = 0; i < 32; ++i) {
                left[i] = block_64[i];
                right[i] = block_64[32 + i];
            }

            return new Pair<>(left, right);
        }

        public static boolean[] connect_halves_to_64(boolean[] left_32, boolean[] right_32) {
            boolean[] ret = new boolean[64];

            for (int i = 0; i < 32; ++i) {
                ret[i] = left_32[i];
                ret[32 + i] = right_32[i];
            }

            return ret;
        }

        public static boolean[] applyFP(boolean[] block_64) {
            boolean[] ret = new boolean[64];

            for (int i = 0; i < 64; ++i)
                ret[i] = block_64[ Util.FP[i] ];

            return ret;
        }


        public static class Feistel {

            public static boolean[] applyE(boolean[] half_32) {
                boolean[] ret = new boolean[48];

                for (int i = 0; i < 48; ++i)
                    ret[i] = half_32[ Util.E[i] ];

                return ret;
            }

            public static boolean[] xor(boolean[] block_48, boolean[] subkey_48) {
                boolean[] ret = new boolean[48];

                for (int i = 0; i < 48; ++i)
                    ret[i] = block_48[i] ^ subkey_48[i];

                return ret;
            }

            public static boolean[][] split_48_to_8x6(boolean[] block_48) {
                boolean[][] ret = new boolean[8][6];

                for (int box = 0; box < 8; ++box) {
                    for (int bit = 0; bit < 6; bit++) {
                        ret[box][bit] = block_48[8 * box + bit];
                    }
                }

                return ret;
            }

            public static Pair<boolean[]> pick_outer_2_and_inner_4(boolean[] block_6) {
                boolean[] outer2 = new boolean[2];
                boolean[] inner4 = new boolean[4];

                outer2 = new boolean[]{ block_6[0], block_6[5] };
                for (int i = 1; i < 5; ++i) {
                    inner4[i - 1] = block_6[i];
                }

                return new Pair<>(outer2, inner4);
            }

            public static int bits_to_int(boolean[] arr) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < arr.length; i++) {
                    sb.append(arr[i] ? "1" : "0");
                }

                return Integer.parseInt(sb.toString(), 2);
            }

            public static boolean[] int_to_bit_4(int input) {
                boolean[] ret = new boolean[4];
                int i = 0;

                while (input >= 1) {
                    ret[i] = input % 2 == 1;
                    input /= 2;

                    ++i;
                }
                return ret;
            }

            public static boolean[] connect_4_to_32(boolean[][] boxes_8x4) {
                boolean[] ret = new boolean[32];

                for (int i = 0; i < 32; ++i) {
                    ret[i] = boxes_8x4[i / 4][i % 4];
                }

                return ret;
            }

            public static boolean[] applyP(boolean[] block_32) {
                boolean[] ret = new boolean[32];

                for (int i = 0; i < 32; ++i)
                    ret[i] = block_32[ Util.P[i] ];

                return ret;
            }


            public static boolean[] feistel(boolean[] block_32, boolean[] subkey_48) {
                boolean[] block_48 = applyE(block_32);
                block_48 = xor(block_48, subkey_48);
                boolean[][] boxes = split_48_to_8x6(block_48);

                boolean[][] s_out_boxes = new boolean[8][4];
                for (int i = 0; i < 8; i++) {
                    Pair<boolean[]> s_input_bit = pick_outer_2_and_inner_4(boxes[i]);
                    Pair<Integer> s_input = new Pair<>(
                            bits_to_int(s_input_bit.outer()),
                            bits_to_int(s_input_bit.inner())
                    );
                    int s_output = Util.S_Box[i][s_input.outer()][s_input.inner()];
                    s_out_boxes[i] = int_to_bit_4(s_output);
                }

                boolean[] linked_s_out = connect_4_to_32(s_out_boxes);
                return applyP(linked_s_out);
            }


        }
    }


    public static class KeyManipulation {
        public static Pair<boolean[]> applyPC1(boolean[] key_64) {
            boolean[] left = new boolean[28],
                      right = new boolean[28];

            int[] pc1_l = Util.PC1.left();
            int[] pc1_r = Util.PC1.right();

            for (int i = 0; i < 28; ++i) {
                left[i] = key_64[ pc1_l[i] ];
                right[i] = key_64[ pc1_r[i] ];
            }

            return new Pair<>(left, right);
        }

        public static boolean[] rotate_left(boolean[] half_28) {
            boolean[] ret = new boolean[28];
            for (int i = 0; i < 27; ++i) {
                ret[i] = half_28[i + 1];
            }
            ret[27] = half_28[0];

            return ret;
        }

        public static boolean[] connect_halves_to_56(boolean[] left_28, boolean[] right_28) {
            boolean[] ret = new boolean[56];
            for (int i = 0; i < 28; ++i) {
                ret[i] = left_28[i];
                ret[28 + i] = right_28[i];
            }

            return ret;
        }

        public static boolean[] apply_PC2(Pair<boolean[]> keyHalves) {
            boolean[] k_56 = connect_halves_to_56(keyHalves.left(), keyHalves.right());
            boolean[] ret = new boolean[48];

            for (int i = 0; i < 48; ++i) {
                ret[i] = k_56[ Util.PC2[i] ];
            }

            return ret;
        }

        public static boolean[][] getSubkeys(Pair<boolean[]> keyHalves) {
            boolean[][] ret = new boolean[16][48];

            for (int key_index = 0; key_index < 16; ++key_index) {

                //rotate-left both halves 1 or 2 times as decided by bit_rot
                for (int rot_num = 0; rot_num < Util.bit_rot[key_index]; ++rot_num)
                    keyHalves = new Pair<>(
                        rotate_left(keyHalves.left()),
                        rotate_left(keyHalves.right())
                    );

                ret[key_index] = apply_PC2(keyHalves);
            }

            return ret;
        }

        public static boolean[][] reverseSubkeys(boolean[][] subkeys) {
            boolean[][] ret = new boolean[16][48];
            for (int i = 0; i < 16; ++i)
                ret[i] = subkeys[16 - i - 1];

            return ret;
        }
    }


}
