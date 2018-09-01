package com.cnksi.android.encrypt;

import android.os.Build;
import android.util.Base64;

import com.cnksi.android.exception.DecoderException;
import com.cnksi.android.log.KLog;
import com.cnksi.android.utils.Hex;
import com.cnksi.android.utils.StringUtil;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

/**
 * RSA 工具类
 *
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/7/30
 * @since 1.0
 */
public class RSAUtil {

    public static final String RSA_PUBLIC_KEY = "RSAPublicKey";
    public static final String RSA_PRIVATE_KEY = "RSAPrivateKey";

    /**
     * 算法名称
     */
    private static final String ALGORITHOM = "RSA";

    /**
     * 随机获取密钥(公钥和私钥), 客户端公钥加密，服务器私钥解密
     *
     * @return 结果密钥对
     * @throws Exception 异常
     */
    public static Map<String, Object> getKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = getKeyPairGenerator();
        keyPairGen.initialize(1024);

        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        Map<String, Object> keyMap = new HashMap<>(2);
        keyMap.put(RSA_PUBLIC_KEY, publicKey);
        keyMap.put(RSA_PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /**
     * 获取公钥/私钥
     *
     * @param keyMap      密钥对
     * @param isPublicKey true：获取公钥，false：获取私钥
     * @return 获取密钥字符串
     */
    public static String getKey(Map<String, Object> keyMap, boolean isPublicKey) {
        Key key = (Key) keyMap.get(isPublicKey ? RSA_PUBLIC_KEY : RSA_PRIVATE_KEY);
        return new String(Base64.encode(key.getEncoded(), Base64.DEFAULT));
    }

    /**
     * 获取公钥/私钥
     *
     * @param key
     * @return 获取密钥字符串
     */
    public static String getKey(Key key) {
        return new String(Base64.encode(key.getEncoded(), Base64.DEFAULT));
    }

    /**
     * 根据给定的系数和专用指数构造一个RSA专用的公钥对象。
     *
     * @param modulus        系数。
     * @param publicExponent 专用指数。
     * @return RSA专用公钥对象。
     */
    public static RSAPublicKey generateRSAPublicKey(byte[] modulus, byte[] publicExponent) {
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(new BigInteger(modulus), new BigInteger(publicExponent));
        try {
            return (RSAPublicKey) getKeyFactory().generatePublic(publicKeySpec);
        } catch (InvalidKeySpecException ex) {
            KLog.e("RSA", "RSAPublicKeySpec is unavailable.", ex);
        } catch (NullPointerException ex) {
            KLog.e("RSA", "RSAUtils#KEY_FACTORY is null, can not generate KeyFactory instance.", ex);
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            KLog.e("RSA", e.getMessage());
        }
        return null;
    }

    /**
     * 根据给定的系数和专用指数构造一个RSA专用的私钥对象。
     *
     * @param modulus         系数。
     * @param privateExponent 专用指数。
     * @return RSA专用私钥对象。
     */
    public static RSAPrivateKey generateRSAPrivateKey(byte[] modulus, byte[] privateExponent) {
        RSAPrivateKeySpec privateKeySpec = new RSAPrivateKeySpec(new BigInteger(modulus), new BigInteger(privateExponent));
        try {
            return (RSAPrivateKey) getKeyFactory().generatePrivate(privateKeySpec);
        } catch (InvalidKeySpecException ex) {
            KLog.e("RSA", "RSAPrivateKeySpec is unavailable.", ex);
        } catch (NullPointerException ex) {
            KLog.e("RSA", "RSAUtils#KEY_FACTORY is null, can not generate KeyFactory instance.", ex);
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            KLog.e("RSA", e.getMessage());
        }
        return null;
    }

    /**
     * 根据给定的16进制系数和专用指数字符串构造一个RSA专用的私钥对象。
     *
     * @param hexModulus         系数。
     * @param hexPrivateExponent 专用指数。
     * @return RSA专用私钥对象。
     */
    public static RSAPrivateKey getRSAPrivateKey(String hexModulus, String hexPrivateExponent) {
        if (StringUtil.isBlank(hexModulus) || StringUtil.isBlank(hexPrivateExponent)) {
            return null;
        }
        byte[] modulus = null;
        byte[] privateExponent = null;
        try {
            modulus = Hex.decodeHex(hexModulus.toCharArray());
            privateExponent = Hex.decodeHex(hexPrivateExponent.toCharArray());
        } catch (DecoderException ex) {
            ex.printStackTrace();
        }
        if (modulus != null && privateExponent != null) {
            return generateRSAPrivateKey(modulus, privateExponent);
        }
        return null;
    }

    /**
     * 根据给定的16进制系数和专用指数字符串构造一个RSA专用的公钥对象。
     *
     * @param hexModulus        系数。
     * @param hexPublicExponent 专用指数。
     * @return RSA专用公钥对象。
     */
    public static RSAPublicKey getRSAPublidKey(String hexModulus, String hexPublicExponent) {
        if (StringUtil.isBlank(hexModulus) || StringUtil.isBlank(hexPublicExponent)) {
            return null;
        }
        byte[] modulus = null;
        byte[] publicExponent = null;
        try {
            modulus = Hex.decodeHex(hexModulus.toCharArray());
            publicExponent = Hex.decodeHex(hexPublicExponent.toCharArray());
        } catch (DecoderException ex) {
            ex.printStackTrace();
        }
        if (modulus != null && publicExponent != null) {
            return generateRSAPublicKey(modulus, publicExponent);
        }
        return null;
    }


    /**
     * 获取数字签名
     *
     * @param data       二进制位
     * @param privateKey 私钥(BASE64编码)
     * @return 数字签名结果字符串
     * @throws Exception 异常
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64.decode(privateKey.getBytes(), Base64.DEFAULT);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = getKeyFactory();
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initSign(privateK);
        signature.update(data);
        return new String(Base64.encode(signature.sign(), Base64.DEFAULT));
    }

    /**
     * 数字签名校验
     *
     * @param data      二进位组
     * @param publicKey 公钥(BASE64编码)
     * @param sign      数字签名字符串
     * @return true：校验成功，false：校验失败
     * @throws Exception 异常
     */
    public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
        byte[] keyBytes = Base64.decode(publicKey.getBytes(), Base64.DEFAULT);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = getKeyFactory();
        PublicKey publicK = keyFactory.generatePublic(keySpec);

        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(Base64.decode(sign.getBytes(), Base64.DEFAULT));
    }

    /**
     * 获取 KeyFactory
     *
     * @throws NoSuchAlgorithmException 异常
     */
    private static KeyFactory getKeyFactory() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyFactory keyFactory;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            keyFactory = KeyFactory.getInstance(ALGORITHOM, "BC");
        } else {
            keyFactory = KeyFactory.getInstance(ALGORITHOM);
        }
        return keyFactory;
    }

    /**
     * 获取 KeyFactory
     *
     * @throws NoSuchAlgorithmException 异常
     */
    private static KeyPairGenerator getKeyPairGenerator() throws NoSuchProviderException, NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            keyPairGen = KeyPairGenerator.getInstance(ALGORITHOM, "BC");
        } else {
            keyPairGen = KeyPairGenerator.getInstance("RSA");
        }
        return keyPairGen;
    }

    /**
     * 使用指定的公钥加密数据。
     *
     * @param publicKey 给定的公钥。
     * @param data      要加密的数据。
     * @return 加密后的数据。
     */
    public static byte[] encrypt(PublicKey publicKey, byte[] data) throws Exception {
        Cipher ci = Cipher.getInstance(ALGORITHOM);
        ci.init(Cipher.ENCRYPT_MODE, publicKey);
        return ci.doFinal(data);
    }

    /**
     * 使用给定的公钥加密给定的字符串。
     * 若 {@code publicKey} 为 {@code null}，或者 {@code plainText} 为 {@code null}
     * 则返回 {@code null}。
     *
     * @param publicKey 给定的公钥。
     * @param plainText 字符串。
     * @return 给定字符串的密文。
     */
    public static String encryptString(PublicKey publicKey, String plainText) {
        if (publicKey == null || plainText == null) {
            return null;
        }
        byte[] data = plainText.getBytes();
        try {
            byte[] enData = encrypt(publicKey, data);
            return new String(Hex.encodeHex(enData));
        } catch (Exception ex) {
            KLog.e("RSA", ex.getMessage());
        }
        return null;
    }

    /**
     * 使用指定的公钥加密数据。
     *
     * @param publicKeyStr 给定的公钥。
     * @param data         要加密的数据。
     * @return 加密后的数据。
     */
    public static byte[] encrypt(String publicKeyStr, byte[] data) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decode(publicKeyStr, Base64.DEFAULT));
        PublicKey publicKey = getKeyFactory().generatePublic(keySpec);
        return encrypt(publicKey, data);
    }

    /**
     * 使用给定的公钥加密给定的字符串。
     *
     * @param publicKeyStr 给定的公钥。
     * @param plainText    字符串。
     * @return 给定字符串的密文。
     */
    public static String encryptString(String publicKeyStr, String plainText) {
        if (publicKeyStr == null || plainText == null) {
            return null;
        }
        byte[] data = plainText.getBytes();
        try {
            byte[] enData = encrypt(publicKeyStr, data);
            return new String(Hex.encodeHex(enData));
        } catch (Exception ex) {
            KLog.e("RSA", ex.getMessage());
        }
        return null;
    }


    /**
     * 使用指定的私钥解密数据。
     *
     * @param privateKeyStr 给定的私钥。
     * @param data          要解密的数据。
     * @return 原数据。
     */
    public static byte[] decrypt(String privateKeyStr, byte[] data) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decode(privateKeyStr, Base64.DEFAULT));
        PrivateKey privateKey = getKeyFactory().generatePrivate(keySpec);
        return decrypt(privateKey, data);
    }


    /**
     * 使用给定的私钥解密给定的字符串。
     * <p/>
     * 若私钥为 {@code null}，或者 {@code encrypttext} 为 {@code null}或空字符串则返回
     * {@code null}。 私钥不匹配时，返回 {@code null}。
     *
     * @param privateKeyStr 给定的私钥。
     * @param encrypt       密文。
     * @return 原文字符串。
     */
    public static String decryptString(String privateKeyStr, String encrypt) {
        if (privateKeyStr == null || StringUtil.isBlank(encrypt)) {
            return null;
        }
        try {
            byte[] enData = Hex.decodeHex(encrypt.toCharArray());
            byte[] data = decrypt(privateKeyStr, enData);
            return new String(data);
        } catch (Exception ex) {
            KLog.e("RSA", ex.getMessage());
        }
        return null;
    }

    /**
     * 使用指定的私钥解密数据。
     *
     * @param privateKey 给定的私钥。
     * @param data       要解密的数据。
     * @return 原数据。
     */
    public static byte[] decrypt(PrivateKey privateKey, byte[] data) throws Exception {
        Cipher ci = Cipher.getInstance(ALGORITHOM);
        ci.init(Cipher.DECRYPT_MODE, privateKey);
        return ci.doFinal(data);
    }

    /**
     * 使用给定的私钥解密给定的字符串。
     * <p/>
     * 若私钥为 {@code null}，或者 {@code encrypttext} 为 {@code null}或空字符串则返回
     * {@code null}。 私钥不匹配时，返回 {@code null}。
     *
     * @param privateKey 给定的私钥。
     * @param encrypt    密文。
     * @return 原文字符串。
     */
    public static String decryptString(PrivateKey privateKey, String encrypt) {
        if (privateKey == null || StringUtil.isBlank(encrypt)) {
            return null;
        }
        try {
            byte[] enData = Hex.decodeHex(encrypt.toCharArray());
            byte[] data = decrypt(privateKey, enData);
            return new String(data);
        } catch (Exception ex) {
            KLog.e("RSA", ex.getMessage());
        }
        return null;
    }

}
