package pl.pkr.model;

import pl.pkr.model.Util.Pair;

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

        byte len_diff = (byte) (all_bytes.length - string_bytes.length);
        all_bytes[ all_bytes.length - 1 ] = len_diff;

        System.arraycopy(string_bytes, 0, all_bytes, 0, string_bytes.length);
        byte[] in_buffer = new byte[ 8 ],
                out_buffer = new byte[ 8 ];
        byte[] encrypted = new byte[ all_bytes.length ];

        for (int i = 0; i < all_bytes.length; i += 8) {
            System.arraycopy(all_bytes, i, in_buffer, 0, 8);
            out_buffer = encrypt_block(in_buffer, this.subkeys);
            System.arraycopy(out_buffer, 0, encrypted, i, 8);
        }

        StringBuilder ret = new StringBuilder();
        for (byte b : encrypted) {
            ret.append(String.format("%02x", b));
        }

        return ret.toString().toUpperCase();
    }

    public String encrypt_bytes(byte[] bytes) {
        //pads with n % 8 bytes
        byte[][] byte_matrix = new byte[ bytes.length / 8 + 1 ][ 8 ];
        for (int row = 0; row < bytes.length / 8; row++) {
            for (int byte_in_row = 0; byte_in_row < 8; byte_in_row++) {
                byte_matrix[ row ][ byte_in_row ] = bytes[ 8 * row + byte_in_row ];
            }
        }

        byte_matrix[ byte_matrix.length - 1 ][ 7 ] = (byte) (8 * byte_matrix.length - bytes.length);

        byte[][] encrypted_matrix = new byte[ bytes.length / 8 + 1 ][ 8 ];

        for (int i = 0; i < byte_matrix.length; i++) {
            encrypted_matrix[ i ] = encrypt_block(byte_matrix[ i ], this.subkeys);
        }

        StringBuilder ret = new StringBuilder(encrypted_matrix.length * 8);

        for (int row = 0; row < encrypted_matrix.length; row++) {
            for (int byte_in_row = 0; byte_in_row < 8; byte_in_row++) {
                ret.append(String.format("%02x", encrypted_matrix[ row ][ byte_in_row ]));
            }
        }

        return ret.toString().toUpperCase();
    }

    public byte[] decrypt_to_bytes(String string) {
        byte[] in_bytes = new byte[ string.length() / 2 ];

        for (int i = 0; i < string.length(); i += 2) {
            byte b = 0;
            b |= Util.HEX_STRING.indexOf(string.charAt(i));
            b <<= 4;
            b |= Util.HEX_STRING.indexOf(string.charAt(i + 1));

            in_bytes[ i / 2 ] = b;
        }

        byte[][] byte_matrix = new byte[ in_bytes.length / 8 ][ 8 ];
        for (int row = 0; row < in_bytes.length / 8; row++) {
            for (int byte_in_row = 0; byte_in_row < 8; byte_in_row++) {
                byte_matrix[ row ][ byte_in_row ] = in_bytes[ 8 * row + byte_in_row ];
            }
        }

        byte[][] decrypted_matrix = new byte[ in_bytes.length / 8 ][ 8 ];


        for (int i = 0; i < byte_matrix.length; i++) {
            decrypted_matrix[ i ] = decrypt_block(byte_matrix[ i ], this.subkeys);
        }

        int len_diff = decrypted_matrix[ decrypted_matrix.length - 1 ][ 7 ];

        byte[] ret = new byte[ 8 * decrypted_matrix.length - len_diff ];
        for (int y = 0; y < decrypted_matrix.length - 1; y++) {
            for (int x = 0; x < 8; x++) {
                ret[ y * 8 + x ] = decrypted_matrix[ y ][ x ];
            }
        }

        for (int i = 0; i < 8 - len_diff; i++) {
            ret[ (decrypted_matrix.length - 1) * 8 + i ] = decrypted_matrix[ decrypted_matrix.length - 1 ][ i ];
        }

        return ret;
    }

    public String decrypt_string(String string) {

        byte[] in_bytes = new byte[ string.length() / 2 ];

        for (int i = 0; i < string.length(); i += 2) {
            byte b = 0;
            b |= Util.HEX_STRING.indexOf(string.charAt(i));
            b <<= 4;
            b |= Util.HEX_STRING.indexOf(string.charAt(i + 1));

            in_bytes[ i / 2 ] = b;
        }

        byte[][] byte_matrix = new byte[ in_bytes.length / 8 ][ 8 ];
        for (int row = 0; row < in_bytes.length / 8; row++) {
            for (int byte_in_row = 0; byte_in_row < 8; byte_in_row++) {
                byte_matrix[ row ][ byte_in_row ] = in_bytes[ 8 * row + byte_in_row ];
            }
        }

        byte[][] decrypted_matrix = new byte[ in_bytes.length / 8 ][ 8 ];


        for (int i = 0; i < byte_matrix.length; i++) {
            decrypted_matrix[ i ] = decrypt_block(byte_matrix[ i ], this.subkeys);
        }

        int len_diff = decrypted_matrix[ decrypted_matrix.length - 1 ][ 7 ];

        StringBuilder ret = new StringBuilder(decrypted_matrix.length * 8 - len_diff);

        for (int row = 0; row < decrypted_matrix.length - 1; row++) {
            for (int byte_in_row = 0; byte_in_row < 8; byte_in_row++) {
                ret.append((char) decrypted_matrix[ row ][ byte_in_row ]);
            }
        }
        for (int i = 0; i < 8 - len_diff; i++) {
            ret.append((char) decrypted_matrix[ decrypted_matrix.length - 1 ][ i ]);
        }

        return ret.toString();
    }

    public byte[] encrypt_block(byte[] block_8, byte[][] subkeys_6) {
        byte[] ret = apply_permutation(block_8, Util.IP);
        byte[] left = new byte[ 4 ],
                right = new byte[ 4 ];

        System.arraycopy(ret, 0, left, 0, 4);
        System.arraycopy(ret, 4, right, 0, 4);

        Pair<byte[]> halves = new Pair<>(left, right);
        for (int i = 0; i < 16; i++) {
            halves = round(halves, subkeys_6[ i ]);
        }

        System.arraycopy(halves.left(), 0, ret, 4, 4);
        System.arraycopy(halves.right(), 0, ret, 0, 4);

        ret = apply_permutation(ret, Util.FP);
        return ret;
    }

    /**
     * Automatically reverses the subkeys
     * @param block_8
     * @param subkeys_6
     * @return
     */
    public byte[] decrypt_block(byte[] block_8, byte[][] subkeys_6) {
        byte[][] rev_subkeys = reverse_subkeys(subkeys_6);

        byte[] ret = apply_permutation(block_8, Util.IP);
        byte[] left = new byte[ 4 ],
                right = new byte[ 4 ];

        System.arraycopy(ret, 0, left, 0, 4);
        System.arraycopy(ret, 4, right, 0, 4);

        Pair<byte[]> halves = new Pair<>(left, right);
        for (int i = 0; i < 16; i++) {
            halves = round(halves, rev_subkeys[ i ]);
        }

        System.arraycopy(halves.left(), 0, ret, 4, 4);
        System.arraycopy(halves.right(), 0, ret, 0, 4);

        ret = apply_permutation(ret, Util.FP);
        return ret;
    }

    public static Pair<byte[]> round(Pair<byte[]> halves_4, byte[] subkey_6) {
        byte[] temp_right = new byte[ 4 ],
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
        byte[] indices = new byte[ 8 ];
        byte[] S_values = new byte[ 8 ];
        byte[] out = new byte[ 4 ];

        indices[ 0 ] = (byte) (block_6[ 0 ] & 0b11111100);
        indices[ 1 ] = (byte) ((((block_6[ 0 ] << 6) & 0b11000000) | ((block_6[ 1 ] >> 2) & 0b00111111)) & 0b11111100);
        indices[ 2 ] = (byte) ((((block_6[ 1 ] << 4) & 0xF0) | ((block_6[ 2 ] >> 4) & 0x0F)) & 0b11111100);
        indices[ 3 ] = (byte) ((block_6[ 2 ] << 2) & 0b11111100);
        indices[ 4 ] = (byte) (block_6[ 3 ] & 0b11111100);
        indices[ 5 ] = (byte) ((((block_6[ 3 ] << 6) & 0b11000000) | ((block_6[ 4 ] >> 2) & 0b00111111)) & 0b11111100);
        indices[ 6 ] = (byte) (((block_6[ 4 ] << 4) & 0xF0 | ((block_6[ 5 ] >> 4) & 0x0F)) & 0b11111100);
        indices[ 7 ] = (byte) ((block_6[ 5 ] << 2) & 0b11111100);

        for (int i = 0; i < 8; i++) {
            indices[ i ] >>= 2;
            int inner_4 = (indices[ i ] & 0b00011110) >> 1;
            int outer_2 = ((indices[ i ] & 0b00100000) >> 4 | (indices[ i ] & 0b00000001));
            S_values[ i ] = (byte) Util.S_Box[ i ][ outer_2 ][ inner_4 ];
        }

        for (int i = 0; i < 4; i++) {
            out[ i ] = (byte) ((S_values[ i * 2 ] << 4) | S_values[ (i * 2) + 1 ]);
        }

        return out;
    }

    public static byte[] xor(byte[] block_1, byte[] block_2) {
        byte[] ret = new byte[ block_1.length ];
        for (int i = 0; i < block_1.length; i++) {
            ret[ i ] = (byte) (block_1[ i ] ^ block_2[ i ]);
        }
        return ret;
    }

    public static byte[][] generate_subkeys(byte[] key_8) {
        byte[] stripped_key_56 = apply_permutation(key_8, Util.PC1);
        byte[][] subkeys = new byte[ 16 ][ 6 ];
        byte[] left = new byte[4],
                right = new byte[4];
        System.arraycopy(stripped_key_56, 0, left, 0, 4);
        System.arraycopy(stripped_key_56, 3, right, 0, 4);

        for (int round = 0; round < 16; round++) {

            for (int j = 0; j < Util.bit_rot[round]; ++j) {
                left = rotate_left(left, Half.LEFT);
                right = rotate_left(right, Half.RIGHT);
            }

            byte[] connected_56 = new byte[ 8 ];
            for (int j = 0; j < 4; j++) {
                connected_56[ j ] = left[ j ];
                connected_56[ 4 + j ] = right[ j ];
            }

            subkeys[ round ] = apply_permutation(connected_56, Util.PC2);
        }

        return subkeys;
    }

    public static byte[] apply_permutation(byte[] og_block, int[] pattern) {
        byte[] ret = new byte[ pattern.length / 8 ];

        for (int i = 0; i < pattern.length; i++) {
            //calculate byte via integer division
            int byte_index = (pattern[ i ] - 1) / 8;
            //the remainder will be the offset from the rightmost bit within that byte
            int bit_index = 7 - (pattern[ i ] - 1) % 8;
            //extract the bit by shifting it to the leftmost position and nullifying all other bits
            byte selected = (byte) ((og_block[ byte_index ] >> bit_index) & 0b01);
            //position of the bit in output is decided by the index of iteration through the pattern, also a right-offset position
            ret[ i / 8 ] |= selected << (7 - (i % 8));
        }

        return ret;
    }

    public enum Half {
        LEFT, RIGHT
    }

    public static byte[] rotate_left(byte[] half_4, Half part) {
        byte[] ret = half_4;

        if ( part == Half.LEFT ) {
            //select first bit (MSB), that gets moved to last position
            byte msb = (byte) (ret[ 0 ] & 0b10000000);
            byte temp_msb = 0;

            //clear second half of last byte for connection, also clear last position in the significant half to carry over MSB
            ret[ 3 ] &= 0b11100000;

            for (int i = 0; i < 4; i++) {
                if ( i < 3 ) temp_msb = (byte) (((ret[ i + 1 ] & 0b10000000) >> 7) & 1);
                ret[ i ] <<= 1;
                if ( i < 3 ) ret[ i ] |= temp_msb;
            }

            //shift msb to its target position
            msb >>= 3;
            //I hate java neither >> nor >>> fill with zeroes, so we have to clear other bits
            msb &= 0b00010000;
            //copy msb to the rotated half
            ret[ 3 ] |= msb;

        } else {
            //in right half, first half of the first byte is overlapped - MSB is on the 5th position
            byte msb = (byte) (ret[ 0 ] & 0b00001000);
            byte temp_msb = 0;

            for (int i = 0; i < 4; i++) {
                if ( i < 3 ) temp_msb = (byte) (((ret[ i + 1 ] & 0b10000000) >> 7) & 1);
                ret[ i ] <<= 1;
                if ( i < 3 ) ret[ i ] |= temp_msb;
            }
            //make sure the last bit is ready to take the copied over MSB
            ret[ 3 ] &= 0b11111110;
            //shift msb to its target position
            msb >>= 3;
            //copy msb to the rotated half
            ret[ 3 ] |= msb;
            //make sure the first byte is ready for connection
            ret[ 0 ] &= 0b00001111;
        }

        return ret;
    }


    public static byte[][] reverse_subkeys(byte[][] subkeys_6) {
        byte[][] ret = new byte[ 16 ][ 6 ];
        for (int i = 0; i < 16; i++) {
            ret[ i ] = subkeys_6[ 16 - i - 1 ];
        }
        return ret;
    }

}