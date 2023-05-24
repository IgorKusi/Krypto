package pl.pkr.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

public class Backpack implements Serializable {
    private BigInteger M = BigInteger.ZERO,
            W = BigInteger.ZERO,
            W_1 = BigInteger.ZERO;
    private BigInteger[] privateKey = new BigInteger[ 8 ];
    private BigInteger[] publicKey = new BigInteger[ 8 ];

    public Backpack() {
        generateKeys();
    }

    public Backpack(BigInteger[] publicKey, BigInteger[] privateKey, BigInteger M, BigInteger W, BigInteger W_1) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.M = M;
        this.W = W;
        this.W_1 = W_1;
    }

    public BigInteger getM() {
        return M;
    }

    public BigInteger getW() {
        return W;
    }

    public BigInteger getW_1() {
        return W_1;
    }

    public BigInteger[] getPrivateKey() {
        return privateKey;
    }

    public BigInteger[] getPublicKey() {
        return publicKey;
    }

    @Override
    public String toString() {
        return "Backpack{" +
                "M=" + M +
                ", W=" + W +
                ", W_1=" + W_1 +
                ", privateKey=" + Arrays.toString(privateKey) +
                ", publicKey=" + Arrays.toString(publicKey) +
                '}';
    }

    public BigInteger[] encryptBytes(byte[] bytes) {
        BigInteger[] ret = new BigInteger[ bytes.length ];
        for (int i = 0; i < bytes.length; i++) {
            ret[ i ] = encryptByte(bytes[ i ]);
        }

        return ret;
    }

    public BigInteger encryptByte(byte b) {
        BigInteger ret = BigInteger.ZERO;

        for (int i = 0; i < 8; i++) {
            if ( (b & (1 << i)) != 0 ) {
                ret = ret.add(publicKey[ i ]);
            }
        }

        return ret;
    }

    private void generateKeys() {
        privateKey = generateSuperRiser();

        for (int i = 0; i < 8; i++) {
            M = M.add(privateKey[ i ]);
        }
        W = M.nextProbablePrime();
        M = W.nextProbablePrime();

        W_1 = W.modInverse(M);

        for (int i = 0; i < 8; i++) {
            publicKey[ i ] = privateKey[ i ].multiply(W).mod(M);
        }
    }

    private BigInteger[] generateSuperRiser() {
        BigInteger[] ret = new BigInteger[ 8 ];
        Random rng = new Random();

        ret[ 0 ] = new BigInteger(30, rng);
        ret[ 1 ] = ret[ 0 ].add(new BigInteger(16, rng));

        BigInteger next;
        BigInteger prevSum;

        for (int i = 2; i < 8; i++) {
            prevSum = BigInteger.ZERO;
            for (int j = 0; j < i; j++) {
                prevSum = prevSum.add(ret[ j ]);
            }
            next = ret[ i - 1 ];

            while ( next.compareTo(prevSum) < 0 ) {
                next = next.add(new BigInteger(16, rng));
            }

            ret[ i ] = next;
        }

        return ret;
    }

}
