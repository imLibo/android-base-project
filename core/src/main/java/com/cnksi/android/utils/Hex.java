//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.cnksi.android.utils;


import com.cnksi.android.exception.DecoderException;
import com.cnksi.android.exception.EncoderException;

public class Hex {
    private static final char[] DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public Hex() {
    }

    public static byte[] decodeHex(char[] data) throws DecoderException {
        int len = data.length;
        if ((len & 1) != 0) {
            throw new DecoderException("Odd number of characters.");
        } else {
            byte[] out = new byte[len >> 1];
            int i = 0;

            for(int j = 0; j < len; ++i) {
                int f = toDigit(data[j], j) << 4;
                ++j;
                f |= toDigit(data[j], j);
                ++j;
                out[i] = (byte)(f & 255);
            }

            return out;
        }
    }

    protected static int toDigit(char ch, int index) throws DecoderException {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new DecoderException("Illegal hexadecimal charcter " + ch + " at index " + index);
        } else {
            return digit;
        }
    }

    public static char[] encodeHex(byte[] data) {
        int l = data.length;
        char[] out = new char[l << 1];
        int i = 0;

        for(int var4 = 0; i < l; ++i) {
            out[var4++] = DIGITS[(240 & data[i]) >>> 4];
            out[var4++] = DIGITS[15 & data[i]];
        }

        return out;
    }

    public byte[] decode(byte[] array) throws DecoderException {
        return decodeHex((new String(array)).toCharArray());
    }

    public Object decode(Object object) throws DecoderException {
        try {
            char[] charArray = object instanceof String ? ((String)object).toCharArray() : (char[])object;
            return decodeHex(charArray);
        } catch (ClassCastException var3) {
            throw new DecoderException(var3.getMessage());
        }
    }

    public byte[] encode(byte[] array) {
        return (new String(encodeHex(array))).getBytes();
    }

    public Object encode(Object object) throws EncoderException {
        try {
            byte[] byteArray = object instanceof String ? ((String)object).getBytes() : (byte[])object;
            return encodeHex(byteArray);
        } catch (ClassCastException var3) {
            throw new EncoderException(var3.getMessage());
        }
    }
}
