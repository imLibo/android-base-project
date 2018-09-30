package com.cnksi.smkit;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * 国密SM2算法工具类
 */
public class SM2Kit {

    /**
     * 生成随机秘钥对，key为公钥，value为私钥
     *
     * @return
     */
    public static Map.Entry<String, String> generateKeyPair() {
        SM2 sm2 = SM2.Instance();
        AsymmetricCipherKeyPair key = sm2.ecc_key_pair_generator.generateKeyPair();
        ECPrivateKeyParameters ecpriv = (ECPrivateKeyParameters) key.getPrivate();
        ECPublicKeyParameters ecpub = (ECPublicKeyParameters) key.getPublic();
        BigInteger privateKey = ecpriv.getD();
        ECPoint publicKey = ecpub.getQ();
        return new HashMap.SimpleEntry<>(SMKit.byteToHex(publicKey.getEncoded()),
                SMKit.byteToHex(privateKey.toByteArray()));
    }

    /**
     * 加密
     *
     * @param publicKey
     * @param data
     * @return
     */
    public static String encrypt(byte[] publicKey, byte[] data) {
        if (publicKey == null || publicKey.length == 0 || data == null || data.length == 0) {
            return null;
        }
        //追加0x04标志位
        if (publicKey.length == 64) {
            byte[] temp = new byte[65];
            temp[0] = 0x4;
            System.arraycopy(publicKey, 0, temp, 1, publicKey.length);
            publicKey = temp;
        }
        byte[] source = new byte[data.length];
        System.arraycopy(data, 0, source, 0, data.length);

        Cipher cipher = new Cipher();
        SM2 sm2 = SM2.Instance();
        ECPoint userKey = sm2.ecc_curve.decodePoint(publicKey);

        ECPoint c1 = cipher.Init_enc(sm2, userKey);
        cipher.Encrypt(source);
        byte[] c3 = new byte[32];
        cipher.Dofinal(c3);
        //C1 C2 C3拼装成加密字串
        return SMKit.byteToHex(c1.getEncoded()) + SMKit.byteToHex(source) + SMKit.byteToHex(c3);
    }

    /**
     * 解密
     *
     * @param privateKey
     * @param encryptedData
     * @return
     * @throws IOException
     */
    public static byte[] decrypt(byte[] privateKey, byte[] encryptedData) throws IOException {
        if (privateKey == null || privateKey.length == 0) {
            return null;
        }
        if (encryptedData == null || encryptedData.length == 0) {
            return null;
        }
        //加密字节数组转换为十六进制的字符串 长度变为encryptedData.length * 2
        String data = SMKit.byteToHex(encryptedData);
        /***分解加密字串
         * （C1 = C1标志位2位 + C1实体部分128位 = 130）
         * （C3 = C3实体部分64位  = 64）
         * （C2 = encryptedData.length * 2 - C1长度  - C2长度）
         */
        byte[] c1Bytes = SMKit.hexToByte(data.substring(0, 130));
        int c2Len = encryptedData.length - 97;
        byte[] c2 = SMKit.hexToByte(data.substring(130, 130 + 2 * c2Len));
        byte[] c3 = SMKit.hexToByte(data.substring(130 + 2 * c2Len, 194 + 2 * c2Len));

        SM2 sm2 = SM2.Instance();
        BigInteger userD = new BigInteger(1, privateKey);

        //通过C1实体字节来生成ECPoint
        ECPoint c1 = sm2.ecc_curve.decodePoint(c1Bytes);
        Cipher cipher = new Cipher();
        cipher.Init_dec(userD, c1);
        cipher.Decrypt(c2);
        cipher.Dofinal(c3);
        //返回解密结果
        return c2;
    }

    /**
     * 加密
     *
     * @param publicKey
     * @param data
     * @return
     * @throws IOException
     */
    public static String encrypt(String publicKey, String data) throws IOException {
        return encrypt(SMKit.hexToByte(publicKey), data.getBytes());
    }

    /**
     * 解密
     *
     * @param privateKey
     * @param encryptedText
     * @return
     * @throws IOException
     */
    public static String decrypt(String privateKey, String encryptedText) throws IOException {
        return new String(decrypt(SMKit.hexToByte(privateKey), SMKit.hexToByte(encryptedText)));
    }

    public static class SM2 {
        //正式参数
        public static String[] ecc_param = {
                "FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFF",
                "FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFC",
                "28E9FA9E9D9F5E344D5A9E4BCF6509A7F39789F515AB8F92DDBCBD414D940E93",
                "FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFF7203DF6B21C6052B53BBF40939D54123",
                "32C4AE2C1F1981195F9904466A39C9948FE30BBFF2660BE1715A4589334C74C7",
                "BC3736A2F4F6779C59BDCEE36B692153D0A9877CC62A474002DF32E52139F0A0"
        };

        public static SM2 Instance() {
            return new SM2();
        }

        public final BigInteger ecc_p;
        public final BigInteger ecc_a;
        public final BigInteger ecc_b;
        public final BigInteger ecc_n;
        public final BigInteger ecc_gx;
        public final BigInteger ecc_gy;
        public final ECCurve ecc_curve;
        public final ECPoint ecc_point_g;
        public final ECDomainParameters ecc_bc_spec;
        public final ECKeyPairGenerator ecc_key_pair_generator;
        public final ECFieldElement ecc_gx_fieldelement;
        public final ECFieldElement ecc_gy_fieldelement;

        public SM2() {
            this.ecc_p = new BigInteger(ecc_param[0], 16);
            this.ecc_a = new BigInteger(ecc_param[1], 16);
            this.ecc_b = new BigInteger(ecc_param[2], 16);
            this.ecc_n = new BigInteger(ecc_param[3], 16);
            this.ecc_gx = new BigInteger(ecc_param[4], 16);
            this.ecc_gy = new BigInteger(ecc_param[5], 16);

            this.ecc_gx_fieldelement = new ECFieldElement.Fp(this.ecc_p, this.ecc_gx);
            this.ecc_gy_fieldelement = new ECFieldElement.Fp(this.ecc_p, this.ecc_gy);

            this.ecc_curve = new ECCurve.Fp(this.ecc_p, this.ecc_a, this.ecc_b);
            this.ecc_point_g = new ECPoint.Fp(this.ecc_curve, this.ecc_gx_fieldelement, this.ecc_gy_fieldelement);

            this.ecc_bc_spec = new ECDomainParameters(this.ecc_curve, this.ecc_point_g, this.ecc_n);

            ECKeyGenerationParameters ecc_ecgenparam = new ECKeyGenerationParameters(this.ecc_bc_spec, new SecureRandom());

            this.ecc_key_pair_generator = new ECKeyPairGenerator();
            this.ecc_key_pair_generator.init(ecc_ecgenparam);
        }
    }

    public static class Cipher {
        private int ct;
        private ECPoint p2;
        private SM3Kit sm3keybase;
        private SM3Kit sm3c3;
        private byte key[];
        private byte keyOff;

        public Cipher() {
            this.ct = 1;
            this.key = new byte[32];
            this.keyOff = 0;
        }

        private void Reset() {
            this.sm3keybase = new SM3Kit();
            this.sm3c3 = new SM3Kit();

            byte p[] = SMKit.byteConvert32Bytes(p2.getX().toBigInteger());
            this.sm3keybase.update(p, 0, p.length);
            this.sm3c3.update(p, 0, p.length);

            p = SMKit.byteConvert32Bytes(p2.getY().toBigInteger());
            this.sm3keybase.update(p, 0, p.length);
            this.ct = 1;
            NextKey();
        }

        private void NextKey() {
            SM3Kit sm3keycur = new SM3Kit(this.sm3keybase);
            sm3keycur.update((byte) (ct >> 24 & 0xff));
            sm3keycur.update((byte) (ct >> 16 & 0xff));
            sm3keycur.update((byte) (ct >> 8 & 0xff));
            sm3keycur.update((byte) (ct & 0xff));
            sm3keycur.doFinal(key, 0);
            this.keyOff = 0;
            this.ct++;
        }

        public ECPoint Init_enc(SM2 sm2, ECPoint userKey) {
            AsymmetricCipherKeyPair key = sm2.ecc_key_pair_generator.generateKeyPair();
            ECPrivateKeyParameters ecpriv = (ECPrivateKeyParameters) key.getPrivate();
            ECPublicKeyParameters ecpub = (ECPublicKeyParameters) key.getPublic();
            BigInteger k = ecpriv.getD();
            ECPoint c1 = ecpub.getQ();
            this.p2 = userKey.multiply(k);
            Reset();
            return c1;
        }

        public void Encrypt(byte data[]) {
            this.sm3c3.update(data, 0, data.length);
            for (int i = 0; i < data.length; i++) {
                if (keyOff == key.length) {
                    NextKey();
                }
                data[i] ^= key[keyOff++];
            }
        }

        public void Init_dec(BigInteger userD, ECPoint c1) {
            this.p2 = c1.multiply(userD);
            Reset();
        }

        public void Decrypt(byte data[]) {
            for (int i = 0; i < data.length; i++) {
                if (keyOff == key.length) {
                    NextKey();
                }
                data[i] ^= key[keyOff++];
            }
            this.sm3c3.update(data, 0, data.length);
        }

        public void Dofinal(byte c3[]) {
            byte p[] = SMKit.byteConvert32Bytes(p2.getY().toBigInteger());
            this.sm3c3.update(p, 0, p.length);
            this.sm3c3.doFinal(c3, 0);
            Reset();
        }
    }
}
