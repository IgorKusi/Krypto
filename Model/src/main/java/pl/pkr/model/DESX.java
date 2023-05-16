package pl.pkr.model;

public class DESX extends DES {
    private byte[] key1;
    private byte[] key2;
    private byte[] key3;

    public DESX(byte[]... keys) {
        super(keys[1]);
        key1 = keys[0];
        key2 = keys[1];
        key3 = keys[2];
    }

    @Override
    public byte[] encrypt_block(byte[] block_8, byte[][] subkeys_6) {
        block_8 = xor(block_8, key1);
        block_8 = super.encrypt_block(block_8, subkeys_6);
        block_8 = xor(block_8, key3);
        return block_8;
    }

    @Override
    public byte[] decrypt_block(byte[] block_8, byte[][] subkeys_6) {
        block_8 = xor(block_8, key3);
        block_8 = super.decrypt_block(block_8, subkeys_6);
        block_8 = xor(block_8, key1);
        return block_8;
    }
}
