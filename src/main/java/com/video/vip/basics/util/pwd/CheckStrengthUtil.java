package com.video.vip.basics.util.pwd;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 检测密码强度
 *
 * @author venshine
 */
public class CheckStrengthUtil {

    public enum LEVEL {
        EASY, MIDIUM, STRONG, VERY_STRONG, EXTREMELY_STRONG
    }

    private static final int NUM = 1;
    private static final int SMALL_LETTER = 2;
    private static final int CAPITAL_LETTER = 3;
    private static final int OTHER_CHAR = 4;

    /**
     * Simple password dictionary
     */
//    private final static String[] DICTIONARY = {"password", "abc123", "iloveyou", "adobe123", "123123", "sunshine",
//            "1314520", "a1b2c3", "123qwe", "aaa111", "qweasd", "admin", "passwd"};
    private final static String[] DICTIONARY = null;

    /**
     * 检查字符的类型，包括数字、大写字母、小写字母和其他字符。
     *
     * @param c
     * @return
     */
    private static int checkCharacterType(char c) {
        if (c >= 48 && c <= 57) {
            return NUM;
        }
        if (c >= 65 && c <= 90) {
            return CAPITAL_LETTER;
        }
        if (c >= 97 && c <= 122) {
            return SMALL_LETTER;
        }
        return OTHER_CHAR;
    }

    /**
     * 按不同类型计算密码
     *
     * @param passwd
     * @param type
     * @return
     */
    private static int countLetter(String passwd, int type) {
        int count = 0;
        if (null != passwd && passwd.length() > 0) {
            for (char c : passwd.toCharArray()) {
                if (checkCharacterType(c) == type) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 检查密码的强度
     *
     * @param passwd
     * @return strength level
     */
    public static int checkPasswordStrength(String passwd) {
        if (CheckStringUtil.equalsNull(passwd)) {
            throw new IllegalArgumentException("password is empty");
        }
        int len = passwd.length();
        int level = 0;

        // increase points
        if (countLetter(passwd, NUM) > 0) {
            level++;
        }
        if (countLetter(passwd, SMALL_LETTER) > 0) {
            level++;
        }
        if (len > 4 && countLetter(passwd, CAPITAL_LETTER) > 0) {
            level++;
        }
        if (len > 6 && countLetter(passwd, OTHER_CHAR) > 0) {
            level++;
        }

        if (len > 4 && countLetter(passwd, NUM) > 0 && countLetter(passwd, SMALL_LETTER) > 0
                || countLetter(passwd, NUM) > 0 && countLetter(passwd, CAPITAL_LETTER) > 0
                || countLetter(passwd, NUM) > 0 && countLetter(passwd, OTHER_CHAR) > 0
                || countLetter(passwd, SMALL_LETTER) > 0 && countLetter(passwd, CAPITAL_LETTER) > 0
                || countLetter(passwd, SMALL_LETTER) > 0 && countLetter(passwd, OTHER_CHAR) > 0
                || countLetter(passwd, CAPITAL_LETTER) > 0 && countLetter(passwd, OTHER_CHAR) > 0) {
            level++;
        }

        if (len > 6 && countLetter(passwd, NUM) > 0 && countLetter(passwd, SMALL_LETTER) > 0
                && countLetter(passwd, CAPITAL_LETTER) > 0 || countLetter(passwd, NUM) > 0
                && countLetter(passwd, SMALL_LETTER) > 0 && countLetter(passwd, OTHER_CHAR) > 0
                || countLetter(passwd, NUM) > 0 && countLetter(passwd, CAPITAL_LETTER) > 0
                && countLetter(passwd, OTHER_CHAR) > 0 || countLetter(passwd, SMALL_LETTER) > 0
                && countLetter(passwd, CAPITAL_LETTER) > 0 && countLetter(passwd, OTHER_CHAR) > 0) {
            level++;
        }

        if (len > 8 && countLetter(passwd, NUM) > 0 && countLetter(passwd, SMALL_LETTER) > 0
                && countLetter(passwd, CAPITAL_LETTER) > 0 && countLetter(passwd, OTHER_CHAR) > 0) {
            level++;
        }

        if (len > 6 && countLetter(passwd, NUM) >= 3 && countLetter(passwd, SMALL_LETTER) >= 3
                || countLetter(passwd, NUM) >= 3 && countLetter(passwd, CAPITAL_LETTER) >= 3
                || countLetter(passwd, NUM) >= 3 && countLetter(passwd, OTHER_CHAR) >= 2
                || countLetter(passwd, SMALL_LETTER) >= 3 && countLetter(passwd, CAPITAL_LETTER) >= 3
                || countLetter(passwd, SMALL_LETTER) >= 3 && countLetter(passwd, OTHER_CHAR) >= 2
                || countLetter(passwd, CAPITAL_LETTER) >= 3 && countLetter(passwd, OTHER_CHAR) >= 2) {
            level++;
        }

        if (len > 8 && countLetter(passwd, NUM) >= 2 && countLetter(passwd, SMALL_LETTER) >= 2
                && countLetter(passwd, CAPITAL_LETTER) >= 2 || countLetter(passwd, NUM) >= 2
                && countLetter(passwd, SMALL_LETTER) >= 2 && countLetter(passwd, OTHER_CHAR) >= 2
                || countLetter(passwd, NUM) >= 2 && countLetter(passwd, CAPITAL_LETTER) >= 2
                && countLetter(passwd, OTHER_CHAR) >= 2 || countLetter(passwd, SMALL_LETTER) >= 2
                && countLetter(passwd, CAPITAL_LETTER) >= 2 && countLetter(passwd, OTHER_CHAR) >= 2) {
            level++;
        }

        if (len > 10 && countLetter(passwd, NUM) >= 2 && countLetter(passwd, SMALL_LETTER) >= 2
                && countLetter(passwd, CAPITAL_LETTER) >= 2 && countLetter(passwd, OTHER_CHAR) >= 2) {
            level++;
        }

        if (countLetter(passwd, OTHER_CHAR) >= 3) {
            level++;
        }
        if (countLetter(passwd, OTHER_CHAR) >= 6) {
            level++;
        }

        if (len > 12) {
            level++;
            if (len >= 16) {
                level++;
            }
        }

        // decrease points
        if ("abcdefghijklmnopqrstuvwxyz".indexOf(passwd) > 0 || "ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(passwd) > 0) {
            level--;
        }
        if ("qwertyuiop".indexOf(passwd) > 0 || "asdfghjkl".indexOf(passwd) > 0 || "zxcvbnm".indexOf(passwd) > 0) {
            level--;
        }
        if (CheckStringUtil.isNumeric(passwd) && ("01234567890".indexOf(passwd) > 0 || "09876543210".indexOf(passwd) > 0)) {
            level--;
        }

        if (countLetter(passwd, NUM) == len || countLetter(passwd, SMALL_LETTER) == len
                || countLetter(passwd, CAPITAL_LETTER) == len) {
            level--;
        }

        if (len % 2 == 0) { // aaabbb
            String part1 = passwd.substring(0, len / 2);
            String part2 = passwd.substring(len / 2);
            if (part1.equals(part2)) {
                level--;
            }
            if (CheckStringUtil.isCharEqual(part1) && CheckStringUtil.isCharEqual(part2)) {
                level--;
            }
        }
        if (len % 3 == 0) { // ababab
            String part1 = passwd.substring(0, len / 3);
            String part2 = passwd.substring(len / 3, len / 3 * 2);
            String part3 = passwd.substring(len / 3 * 2);
            if (part1.equals(part2) && part2.equals(part3)) {
                level--;
            }
        }

        if (null != DICTIONARY && DICTIONARY.length > 0) {// dictionary
            for (int i = 0; i < DICTIONARY.length; i++) {
                if (passwd.equals(DICTIONARY[i]) || DICTIONARY[i].indexOf(passwd) >= 0) {
                    level--;
                    break;
                }
            }
        }

        if (len <= 6) {
            level--;
            if (len <= 4) {
                level--;
                if (len <= 3) {
                    level = 0;
                }
            }
        }

        if (CheckStringUtil.isCharEqual(passwd)) {
            level = 0;
        }

        if (level < 0) {
            level = 0;
        }

        return level;
    }

    /**
     * 得到密码的强度水平，包括易，中，强，非常强，非常强
     *
     * @param passwd
     * @return
     */
    public static LEVEL getPasswordLevel(String passwd) {
        int level = checkPasswordStrength(passwd);
        switch (level) {
            case 0:
            case 1:
            case 2:
            case 3:
                return LEVEL.EASY;
            case 4:
            case 5:
            case 6:
                return LEVEL.MIDIUM;
            case 7:
            case 8:
            case 9:
                return LEVEL.STRONG;
            case 10:
            case 11:
            case 12:
                return LEVEL.VERY_STRONG;
            default:
                return LEVEL.EXTREMELY_STRONG;
        }
    }

    /**
     * 生成指定位数的随机字符
     *
     * @param length 位数
     * @param num    生成个数
     * @return
     */
    public static List<String> genCodes(int length, long num) {
        List<String> results = new ArrayList<String>();
        for (int j = 0; j < num; j++) {
            String val = "";
            Random random = new Random();
            for (int i = 0; i < length; i++) {
                String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num"; // 输出字母还是数字
                if ("char".equalsIgnoreCase(charOrNum)) // 字符串
                {
                    int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; //取得大写字母还是小写字母
                    val += (char) (choice + random.nextInt(26));
                } else if ("num".equalsIgnoreCase(charOrNum)) // 数字
                {
                    val += String.valueOf(random.nextInt(10));
                }
            }
            val = val.toLowerCase();
            if (results.contains(val)) {
                continue;
            } else {
                results.add(val);
            }
        }
        return results;
    }


    public static void main(String[] args) {
        System.out.println(CheckStrengthUtil.checkPasswordStrength("11111111111111111"));
        System.out.println(CheckStrengthUtil.checkPasswordStrength("q12345678"));
        System.out.println(CheckStrengthUtil.checkPasswordStrength("12345612Q31231"));
        System.out.println(CheckStrengthUtil.checkPasswordStrength("Qww111111"));
        System.out.println(CheckStrengthUtil.checkPasswordStrength("Qwww111111"));
        System.out.println(CheckStrengthUtil.checkPasswordStrength("Q1234561231231"));
        System.out.println(CheckStrengthUtil.checkPasswordStrength("qw123456123123123123"));
        System.out.println(CheckStrengthUtil.checkPasswordStrength("qwasdfasdfsdffds123456123123123123"));
        System.out.println(CheckStrengthUtil.checkPasswordStrength("Qasdfasdfsdffds123456123123123123"));
        System.out.println(CheckStrengthUtil.checkPasswordStrength("12345QWER"));
        System.out.println(CheckStrengthUtil.checkPasswordStrength("12345qweW"));
        System.out.println(CheckStrengthUtil.checkPasswordStrength("12345qW"));
//        String passwd = "Fsdjke3234_";
//        for (int i = 0; i < 100; i++) {
//            System.out.println(CheckStrengthUtil.genCodes(6, 1));
////            System.out.println(CheckStrengthUtil.checkPasswordStrength(passwd));
//        }
    }
}
