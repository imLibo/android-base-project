package com.cnksi.android.utils;

import android.text.TextUtils;

import java.util.Random;

/**
 * 随机数工具类
 *
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/7/30
 * @since 1.0
 */
public class RandomUtil {

    public static final String NUMBERS_AND_LETTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String NUMBERS = "0123456789";
    public static final String LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String CAPITAL_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String LOWER_CASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";


    private RandomUtil() {
        throw new Error("Do not need instantiate!");
    }

    /**
     * 获取一个定长的由数字和字母组成的随机字符串
     *
     * @param length 长度
     * @return 随机字符串
     * @see RandomUtil#getRandom(String source, int length)
     */
    public static String getRandomNumbersAndLetters(int length) {
        return getRandom(NUMBERS_AND_LETTERS, length);
    }

    /**
     * 获取一个定长的数字随机字符串
     *
     * @param length 长度
     * @return 随机数字符串
     * @see RandomUtil#getRandom(String source, int length)
     */
    public static String getRandomNumbers(int length) {
        return getRandom(NUMBERS, length);
    }

    /**
     * 获取一个定长的字母随机字符串
     *
     * @param length 长度
     * @return 随机字母字符串
     * @see RandomUtil#getRandom(String source, int length)
     */
    public static String getRandomLetters(int length) {
        return getRandom(LETTERS, length);
    }

    /**
     * 获取一个定长由大写字母组成的随机字符串
     *
     * @param length 长度
     * @return 随机字符串 包括大写字母
     * @see RandomUtil#getRandom(String source, int length)
     */
    public static String getRandomCapitalLetters(int length) {
        return getRandom(CAPITAL_LETTERS, length);
    }

    /**
     * 获取一个定长由小写字母组成的随机字符串
     *
     * @param length 长度
     * @return 随机字符串 包括小写字母
     * @see RandomUtil#getRandom(String source, int length)
     */
    public static String getRandomLowerCaseLetters(int length) {
        return getRandom(LOWER_CASE_LETTERS, length);
    }

    /**
     * 获取一个定长的自定义数据源的随机字符串
     *
     * @param source 源字符串
     * @param length 长度
     * @return <ul>
     * <li>if source is null or empty, return null</li>
     * <li>else see {@link RandomUtil#getRandom(char[] sourceChar, int length)}</li>
     * </ul>
     */
    public static String getRandom(String source, int length) {
        return TextUtils.isEmpty(source) ? null : getRandom(source.toCharArray(), length);
    }

    /**
     * 获取一个定长的自定义数据源的随机字符串
     *
     * @param sourceChar 源
     * @param length     长度
     * @return <ul>
     * <li>if sourceChar is null or empty, return null</li>
     * <li>if length less than 0, return null</li>
     * </ul>
     */
    public static String getRandom(char[] sourceChar, int length) {
        if (sourceChar == null || sourceChar.length == 0 || length < 0) {
            return null;
        }
        StringBuilder str = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            str.append(sourceChar[random.nextInt(sourceChar.length)]);
        }
        return str.toString();
    }

    /**
     * 获取一个 0 到 max 之间的随机数
     *
     * @param max 最大随机数
     * @return <ul>
     * <li>if max <= 0, return 0</li>
     * <li>else return random int between 0 and max</li>
     * </ul>
     */
    public static int getRandom(int max) {
        return getRandom(0, max);
    }

    /**
     * 获取一个 min 到 max 之间的随机数
     *
     * @param min 最小随机数
     * @param max 最大随机数
     * @return <ul>
     * <li>if min > max, return 0</li>
     * <li>if min == max, return min</li>
     * <li>else return random int between min and max</li>
     * </ul>
     */
    public static int getRandom(int min, int max) {
        if (min > max) {
            return 0;
        }
        if (min == max) {
            return min;
        }
        return min + new Random().nextInt(max - min);
    }

    /**
     * 改组算法，使用默认的随机源随机排列指定的数组
     * Shuffling algorithm, Randomly permutes the specified array using a default source of randomness
     *
     * @param objArray
     * @return
     */
    public static boolean shuffle(Object[] objArray) {
        if (objArray == null) {
            return false;
        }
        return shuffle(objArray, getRandom(objArray.length));
    }

    /**
     * 改组算法，随机置换指定数组
     * Shuffling algorithm, Randomly permutes the specified array
     *
     * @param objArray
     * @param shuffleCount
     * @return
     */
    public static boolean shuffle(Object[] objArray, int shuffleCount) {
        int length;
        if (objArray == null || shuffleCount < 0 || (length = objArray.length) < shuffleCount) {
            return false;
        }
        for (int i = 1; i <= shuffleCount; i++) {
            int random = getRandom(length - i);
            Object temp = objArray[length - i];
            objArray[length - i] = objArray[random];
            objArray[random] = temp;
        }
        return true;
    }

    /**
     * 改组算法，使用默认的随机源随机置换指定的int数组
     * Shuffling algorithm, Randomly permutes the specified int array using a default source of randomness
     *
     * @param intArray
     * @return
     */
    public static int[] shuffle(int[] intArray) {
        if (intArray == null) {
            return null;
        }
        return shuffle(intArray, getRandom(intArray.length));
    }

    /**
     * 混洗算法，随机置换指定的int数组
     * Shuffling algorithm, Randomly permutes the specified int array
     *
     * @param intArray
     * @param shuffleCount
     * @return
     */
    public static int[] shuffle(int[] intArray, int shuffleCount) {
        int length;
        if (intArray == null || shuffleCount < 0 || (length = intArray.length) < shuffleCount) {
            return null;
        }

        int[] out = new int[shuffleCount];
        for (int i = 1; i <= shuffleCount; i++) {
            int random = getRandom(length - i);
            out[i - 1] = intArray[random];
            int temp = intArray[length - i];
            intArray[length - i] = intArray[random];
            intArray[random] = temp;
        }
        return out;
    }
}
