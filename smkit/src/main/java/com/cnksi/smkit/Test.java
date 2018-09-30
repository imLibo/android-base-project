package com.cnksi.smkit;

import java.io.IOException;
import java.util.Map;

public class Test {

    public static void main(String[] args) throws Exception {
        //验证公钥和私钥
//        testKeyPair();
        //验证SM2加解密
        testSM2();
//        generate();
    }

    private static void generate() throws IOException {
        String pubk = "C4F7D581BEFEF25C8BBB6DAD52A6AB8234FA7DB7A988592BC592DAF2BE630647E3746788CBDC59042D85260DD48B6A7347D82C5314E8AC261588A33151DFCA17";
        String prik = "E7CB09606A53320B347F61F3F142DCB118F723A9BC27879F2805BE778F24AEE5";
//
        String plainText = "恭喜你 解密成功";
//
        System.out.println("加密: ");
        String cipherText = SM2Kit.encrypt(SMKit.hexToByte(pubk), plainText.getBytes());
        System.out.println(cipherText);

        System.out.println("解密: ");
        plainText = new String(SM2Kit.decrypt(SMKit.hexToByte(prik), SMKit.hexToByte
                ("049E2A4A1AA4CF772622ABBBF1C6D661EE58FF01FF9843782E5A63185ABF6C2EFA9B2D59B2B1E0D0A795BFEF53FABB24C03A02265751B820591200F0D31C551ED67DFDFC65CC9DF7D6287D5BF3358BED992881B69FBA13C8AF76EFC157455DB81ECFACC7B443EA1DB0")), "UTF-8");
        System.out.println(plainText);

        System.out.println(SMKit.hexStringToString("06C37E7DD716AA6B988466E67EBB1E774D5F223AA58C3424EDC16AA0A9209C93E4427901D9359DE87136AA40D712958D02623C358674057050BBF7E7A9952ED1",2));

    }

    /**
     * 验证用自己生成的公钥和私钥进行加解密
     */
    private static void testSM2() throws IOException {
        Map.Entry<String, String> keyPair = SM2Kit.generateKeyPair();
        String pubk = keyPair.getKey();
        String prik = keyPair.getValue();
        System.out.println("公钥：" + pubk);
        System.out.println("私钥：" + prik);

        String plainText = "验证SM2算法通过";
        System.out.println("待加密：" + plainText);

        System.out.println("加密: ");
        String cipherText = SM2Kit.encrypt(SMKit.hexToByte(pubk), plainText.getBytes());
        System.out.println(cipherText);

        System.out.println("解密: ");
        plainText = new String(SM2Kit.decrypt(SMKit.hexToByte(prik), SMKit.hexToByte(cipherText)), "UTF-8");
        System.out.println(plainText);
    }

    /**
     * 验证公钥和私钥
     *
     * @throws IOException
     */
    private static void testKeyPair() throws IOException {
        String plainText = "hello world";
        System.out.println("待加密：" + plainText);
        //你给的公钥
        String pubk = "C4F7D581BEFEF25C8BBB6DAD52A6AB8234FA7DB7A988592BC592DAF2BE630647E3746788CBDC59042D85260DD48B6A7347D82C5314E8AC261588A33151DFCA17";
        //你给的私钥
        String prik = "E7CB09606A53320B347F61F3F142DCB118F723A9BC27879F2805BE778F24AEE5";

        System.out.println("加密: ");
        String cipherText = SM2Kit.encrypt(SMKit.hexToByte(pubk), plainText.getBytes());
        System.out.println(cipherText);

        System.out.println("解密: ");
        plainText = new String(SM2Kit.decrypt(SMKit.hexToByte(prik), SMKit.hexToByte(cipherText)), "UTF-8");
        System.out.println(plainText);
    }


}
