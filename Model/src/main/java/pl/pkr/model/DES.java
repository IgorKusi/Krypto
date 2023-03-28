package pl.pkr.model;

import pl.pkr.model.Util.Pair;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class DES {
    private byte[] key;
    private byte[][] subkeys;

    public DES(byte[] key) {
        this.key = key;
        this.subkeys = generate_subkeys(key);
    }


    public String encrypt_string(String string) {
        byte[] string_bytes = string.getBytes();
        byte[] all_bytes = new byte[ string_bytes.length + 8 - (string_bytes.length % 8) ];
        System.arraycopy(string_bytes, 0, all_bytes, 0, string_bytes.length);
        byte[] in_buffer = new byte[8],
                out_buffer = new byte[8];
        byte[] encrypted = new byte[all_bytes.length];

        for (int i = 0; i < all_bytes.length; i+=8) {
            System.arraycopy(all_bytes, i, in_buffer, 0, 8);
            out_buffer = encrypt_block(in_buffer, this.subkeys);
            System.arraycopy(out_buffer, 0, encrypted, i, 8);
        }

        return new BigInteger(encrypted).toString(16);
    }

    public String decrypt_string(String string) {
        this.subkeys = reverse_subkeys(this.subkeys);
        BigInteger temp = new BigInteger(string, 16);
        String ret = encrypt_string(new String(temp.toByteArray(), StandardCharsets.UTF_8));
        this.subkeys = reverse_subkeys(this.subkeys);
        return new String(new BigInteger(ret).toByteArray(), StandardCharsets.UTF_8);
    }

    public static byte[] encrypt_block(byte[] block_8, byte[][] subkeys_6) {
        byte[] ret = apply_permutation(block_8, Util.IP);
        byte[] left = new byte[4],
                right = new byte[4];

        System.arraycopy(ret, 0, left, 0, 4);
        System.arraycopy(ret, 4, right, 0, 4);

        Pair<byte[]> halves = new Pair<>(left, right);
        for (int i = 0; i < 16; i++) {
            halves = round(halves, subkeys_6[i]);
        }

        System.arraycopy(halves.left(), 0, ret, 0, 4);
        System.arraycopy(halves.right(), 0, ret, 4, 4);

        ret = apply_permutation(ret, Util.FP);
        return ret;
    }

    public static Pair<byte[]> round(Pair<byte[]> halves_4, byte[] subkey_6) {
        byte[] temp_right = new byte[4],
                temp_left = halves_4.right();

        temp_right = feistel(halves_4.right(), subkey_6);
        temp_right = xor(halves_4.left(), temp_right);
        return new Pair<>(temp_left, temp_right);
    }

    public static byte[] feistel(byte[] block_4, byte[] subkey_6) {
        byte[] temp_block = apply_permutation(block_4, Util.E);
        temp_block = xor(temp_block, subkey_6);
        temp_block = apply_S_boxes(temp_block);
        temp_block = apply_permutation(temp_block, Util.P);

        return temp_block;
    }

    public static byte[] apply_S_boxes(byte[] block_6) {
        byte[] indices = new byte[8];
        byte[] S_values = new byte[8];
        byte[] out = new byte[4];

        indices[0] = block_6[0];
        indices[1] = (byte) (block_6[0] << 6 | block_6[1] >> 2);
        indices[2] = (byte) (block_6[1] << 4 | block_6[2] >> 4);
        indices[3] = (byte) (block_6[2] << 2);
        indices[4] = block_6[3];
        indices[5] = (byte) (block_6[3] << 6 | block_6[4] >> 2);
        indices[6] = (byte) (block_6[4] << 4 | block_6[5] >> 4);
        indices[7] = (byte) (block_6[5] << 2);

        for (int i = 0; i < 8; i++) {
            indices[i] >>= 2;
            int inner_4 = (indices[i] & 0b00011110) >> 1;
            int outer_2 = ( (indices[i] & 0b00100000) >> 4 | (indices[i] & 0b00000001) );
            S_values[i] = (byte) Util.S_Box[i][outer_2][inner_4];
        }

        for (int i = 0; i < 4; i++) {
            out[i] = (byte) ( (S_values[i * 2] << 4) | S_values[(i * 2) + 1] );
        }

        return out;
    }

    public static byte[] xor(byte[] block_1, byte[] block_2) {
        byte[] ret = new byte[block_1.length];
        for (int i = 0; i < block_1.length; i++) {
            ret[i] = (byte) ( block_1[i] ^ block_2[i] );
        }
        return ret;
    }

    public static byte[][] generate_subkeys(byte[] key_8) {
        byte[] stripped_key_56 = apply_permutation(key_8, Util.PC1);
        byte[][] subkeys = new byte[16][6];

        for (int round = 0; round < 16; round++) {
            byte[] left = new byte[4],
                    right = new byte[4];
            System.arraycopy(stripped_key_56, 0, left, 0, 4);
            System.arraycopy(stripped_key_56, 3, right, 0, 4);

            for (int j = 0; j < Util.bit_rot[round]; ++j) {
                left = rotate_left(left, Half.LEFT);
                right = rotate_left(right, Half.RIGHT);
            }

            byte[] connected_56 = new byte[7];
            for (int j = 0; j < 4; j++) {
                connected_56[j] |= left[j];
                connected_56[3 + j] |= right[j];
            }

            subkeys[round] = apply_permutation(connected_56, Util.PC2);
        }

        return subkeys;
    }

    public static byte[] apply_permutation(byte[] og_block, int[] pattern) {
        byte[] ret = new byte[ pattern.length / 8 ];

        for (int i = 0; i < pattern.length; i++) {
            //calculate byte via integer division
            int byte_index = (pattern[i] - 1) / 8;
            //the remainder will be the offset from the rightmost bit within that byte
            int bit_index = 7 - (pattern[i] - 1) % 8;
            //extract the bit by shifting it to the leftmost position and nullifying all other bits
            byte selected = (byte) ( (og_block[byte_index] >> bit_index) & 0b01 );
            //position of the bit in output is decided by the index of iteration through the pattern, also a right-offset position
            ret[i / 8] |= selected << ( 7 - (i % 8) );
        }

        return ret;
    }

    public enum Half {
        LEFT, RIGHT
    }
    public static byte[] rotate_left(byte[] half_4, Half part) {
        byte[] ret = half_4;
        
        if (part == Half.LEFT) {
            //select first bit (MSB), that gets moved to last position
            byte msb = (byte) (ret[0] & 0b10000000);

            for (int i = 0; i < 4; i++) {
                ret[i] <<= 1;
            }
            //clear second half of last byte for connection, also clear last position in the significant half to carry over MSB
            ret[3] &= 0b11100000;
            //shift msb to its target position
            msb >>= 3;
            //I hate java neither >> nor >>> fill with zeroes, so we have to clear other bits
            msb &= 0b00010000;
            //copy msb to the rotated half
            ret[3] |= msb;

        } else {
            //in right half, first half of the first byte is overlapped - MSB is on the 5th position
            byte msb = (byte) ( ret[0] & 0b00001000 );

            for (int i = 0; i < 4; i++) {
                ret[i] <<= 1;
            }
            //make sure the last bit is ready to take the copied over MSB
            ret[3] &= 0b11111110;
            //shift msb to its target position
            msb >>= 3;
            //copy msb to the rotated half
            ret[3] |= msb;
            //make sure the first byte is ready for connection
            ret[0] &= 0b00001111;
        }

        return ret;
    }


    
    public static byte[][] reverse_subkeys(byte[][] subkeys_6){
        byte[][] ret = new byte[16][6];
        for (int i = 0; i < 16; i++) {
            ret[i] = subkeys_6[16 - i - 1];
        }
        return ret;
    }
    
    
    
}