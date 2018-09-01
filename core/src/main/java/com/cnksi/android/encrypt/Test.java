package com.cnksi.android.encrypt;

import com.cnksi.android.utils.StringUtil;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;

public class Test {

    public static void main() throws Exception {

        String plainText = "1234qwer!";

        Map<String, Object> keyPair = RSAUtil.getKeyPair();
        PublicKey publicKey = (PublicKey) keyPair.get(RSAUtil.RSA_PUBLIC_KEY);
        PrivateKey privateKey = (PrivateKey) keyPair.get(RSAUtil.RSA_PRIVATE_KEY);
        String publicKeyStr = RSAUtil.getKey(publicKey);
        String privateKeyStr = RSAUtil.getKey(privateKey);

        String encryptStr = RSAUtil.encryptString(publicKeyStr, plainText);
        System.out.println("encryptStr1->" + encryptStr);
        String decryptStr = RSAUtil.decryptString(privateKeyStr, encryptStr);
        System.out.println("decryptStr1->" + decryptStr);

        encryptStr = RSAUtil.encryptString(publicKey, plainText);
        System.out.println("encryptStr2->" + encryptStr);
        decryptStr = RSAUtil.decryptString(privateKey, encryptStr);
        System.out.println("decryptStr2->" + decryptStr);

        publicKey = RSAUtil.getRSAPublidKey
                ("00a2dfa509c41a9abf517ce56ef82013f1fce6404d38824b3643b3298c85b72e8c652128e88ede82aa59161d8dbb87be612a6611a29b0417ae6667666cb67241ea6e663b44aadc6df10306febf581b700b19876a971e3a35604937f053b6688e841566d15b9ca8fe649132f8c7994173bef05087c37eedd7e0c78c8318c88dfacb", "010001");

        encryptStr = RSAUtil.encryptString(publicKey, StringUtil.reverse(MD5Util.md5(plainText) + plainText));
        System.out.println("encryptStr3->" + encryptStr);
    }

}
