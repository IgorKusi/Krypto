package pl.pkr.model;
import pl.pkr.model.Util.*;

import java.nio.charset.StandardCharsets;

//1 - strip key from 64b to 56b
//2 - push 64b to IP -> split to 2 halves
//3 - both halves - 16rounds
//4 - FP

public class DES {

    boolean[] key_64;
    boolean[][] subkeys_48;

    public DES(boolean[] key_64) {
        this.key_64 = key_64;
        this.subkeys_48 = KeyManipulation.get_subkeys(this.key_64);
    }


    public String encrypt_string(String plaintext) {
        boolean[][] bit_matrix = Util.string_to_bits(plaintext);

        StringBuilder ret = new StringBuilder();

        for (boolean[] byyte : bit_matrix) {
            ret.append(Util.bits_to_numeric(
                    TextManipulation.encrypt_block(byyte, this.subkeys_48))
            );
        }

        return ret.toString().trim();
    }

    public String decrypt_string(String ciphertext) {
        boolean[][] bit_matrix = Util.numeric_to_bits(ciphertext);
        boolean[][] decrypted_matrix = new boolean[bit_matrix.length][64];

        boolean[][] subkeys = KeyManipulation.reverse_subkeys(this.subkeys_48);

        for (int i = 0; i < bit_matrix.length; i++) {
            decrypted_matrix[i] = TextManipulation.encrypt_block(bit_matrix[i], subkeys);
        }

        byte[] bytes = new byte[decrypted_matrix.length * 8];
        int bytes_i = 0;

        for (int row = 0; row < decrypted_matrix.length; row++) {
            for (int byte_in_row = 0; byte_in_row < 8; byte_in_row++) {
                boolean[] bit_buffer = new boolean[8];

                for (int bit_in_byte = 0; bit_in_byte < 8; bit_in_byte++) {
                    bit_buffer[bit_in_byte] = decrypted_matrix[row][8 * byte_in_row + bit_in_byte];
                }

                bytes[bytes_i] = Util.bits_8_to_byte(bit_buffer);
            }
        }

        return new String(bytes, StandardCharsets.UTF_8);
    }



    public static class KeyManipulation {
        public static Pair<boolean[]> apply_PC1(boolean[] key_64){
            boolean[] left = new boolean[28];
            boolean[] right = new boolean[28];

            for(int i=0; i<28; i++){
                left[i] = key_64[ Util.PC1.left()[i] - 1 ];
                right[i] = key_64[ Util.PC1.right()[i] - 1];
            }

            return new Pair<>(left, right);
        }

        public static boolean[] rotate_left(boolean[] block_28) {
            boolean[] rotated = new boolean[28];

            for (int i = 0; i < 27; i++) {
                rotated[i] = block_28[i + 1];
            }
            rotated[27] =  block_28[0];

            return rotated;
        }

        public static boolean[] apply_PC2(Pair<boolean[]> halves_28) {
            boolean[] block_56 = Util.connect_halves(halves_28, 28);
            boolean[] block_48 = new boolean[48];

            for (int i = 0; i < 48; i++) {
                block_48[i] = block_56[ Util.PC2[i] - 1 ];
            }

            return block_48;
        }

        public static boolean[][] get_subkeys(boolean[] key_64) {
            boolean[][] subkeys_48 = new boolean[16][48];

            Pair<boolean[]> key_halves_28 = apply_PC1(key_64);

            for (int i = 0; i < 16; ++i) {
                //rotation loop
                for (int j = 0; j < Util.bit_rot[i]; ++j) {
                    Pair<boolean[]> temp = new Pair<>(
                            key_halves_28.left(),
                            key_halves_28.right()
                    );

                    key_halves_28 = new Pair<>(
                            rotate_left(temp.left()),
                            rotate_left(temp.right())
                    );
                }

                subkeys_48[i] = apply_PC2(key_halves_28);
            }

            return subkeys_48;
        }

        public static boolean[][] reverse_subkeys(boolean[][] subkeys){
            boolean[][] ret = new boolean[16][48];
            for (int i = 0; i < 16; i++) {
                ret[i] = subkeys[16 - i - 1];
            }
            return ret;
        }

    }

    public static class TextManipulation {

        public static boolean[] apply_IP(boolean[] block_64) {
            boolean[] ret = new boolean[64];

            for (int i = 0; i < 64; i++) {
                ret[i] = block_64[ Util.IP[i] - 1 ];
            }

            return ret;
        }

        public static boolean[] apply_FP(boolean[] block_64){
            boolean[] ret = new boolean[64];

            for(int i = 0; i<64; i++){
                ret[i] = block_64[ Util.FP[i] -1 ];
            }
            return ret;
        }


        public static class Feistel {

            public static boolean[] apply_E(boolean[] block_32){
                boolean[] block_48 = new boolean[48];
                for(int i=0; i<48; i++){
                    block_48[i] = block_32[ Util.E[i] - 1 ];
                }
                
                return block_48;

            }

            public static boolean[][] split_48_into_8x6(boolean[] block_48) {
                boolean[][] boxes = new boolean[8][6];

                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 6; j++) {
                        boxes[i][j] = block_48[6 * i + j];
                    }
                }

                return boxes;
            }

            public static SPair<boolean[]> pick_outer_2_and_inner_4(boolean[] block_6) {
                boolean[] outer = new boolean[2];
                outer[0] = block_6[0];
                outer[1] = block_6[5];

                boolean[] inner = new boolean[4];
                inner[0] = block_6[1];
                inner[1] = block_6[2];
                inner[2] = block_6[3];
                inner[3] = block_6[4];

                return new SPair<>(outer, inner);
            }

            public static int apply_SBox(SPair<boolean[]> s_input, int s_index) {
                int row = Util.bits_to_int(s_input.outer(), 2);
                int col = Util.bits_to_int(s_input.inner(), 4);

                return Util.S_Box[s_index][row][col];
            }

            public static boolean[] connect_8x4_to_32(boolean[][] boxes) {
                boolean[] ret = new boolean[32];

                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 4; j++) {
                        ret[4 * i + j] = boxes[i][j];
                    }
                }

                return ret;
            }

            public static boolean[] apply_P(boolean[] block_32) {
                boolean[] ret = new boolean[32];

                for (int i = 0; i < 32; i++) {
                    ret[i] = block_32[ Util.P[i] - 1 ];
                }

                return ret;
            }


            public static boolean[] feistel(boolean[] block_32, boolean[] subkey_48) {
                boolean[] expanded_48 = apply_E(block_32);
                boolean[] xor_48 = Util.xor(expanded_48, subkey_48, 48);
                boolean[][] boxes_8x6 = split_48_into_8x6(xor_48);

                boolean[][] boxes_8x4 = new boolean[8][4];
                for (int i = 0; i < 8; i++) {
                    SPair<boolean[]> s_input = pick_outer_2_and_inner_4(boxes_8x6[i]);
                    int s_out = apply_SBox(s_input, i);
                    boxes_8x4[i] = Util.int_to_bits_4(s_out);
                }

                boolean[] connected_32 = connect_8x4_to_32(boxes_8x4);
                connected_32 = apply_P(connected_32);

                return connected_32;
            }
        }

        public static Pair<boolean[]> round(Pair<boolean[]> halves_32, boolean[] subkey_48) {
            boolean[] temp_32;

            temp_32 = Feistel.feistel(halves_32.right(), subkey_48);
            temp_32 = Util.xor(halves_32.left(), temp_32, 32);

            return new Pair<>(
                    halves_32.right(),
                    temp_32
            );
        }

        public static boolean[] encrypt_block(boolean[] block_64, boolean[][] subkeys_48) {
            block_64 = apply_IP(block_64);
            Pair<boolean[]> block_halves_32 = Util.split_in_half(block_64, 64);

            for (int i = 0; i < 16; i++) {
                block_halves_32 = round(block_halves_32, subkeys_48[i]);
            }

            block_64 = Util.connect_halves(block_halves_32, 32);
            block_64 = apply_FP(block_64);
            return block_64;
        }

    }

}


























