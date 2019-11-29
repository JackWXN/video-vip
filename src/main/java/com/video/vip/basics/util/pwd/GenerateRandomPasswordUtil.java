package com.video.vip.basics.util.pwd;

import java.util.Random;

/**
 * 生成密码
 * @author wxn
 */
public class GenerateRandomPasswordUtil {
    private static final byte INDEX_NUMBER = 0;
    private static final byte INDEX_LETTER = 1;
    private static final byte INDEX_SPECIAL_CHAR = 2;

    /** 特殊符号 */
    private static final char[] SPECIAL_CHARS = { '`', '~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '_', '=', '+', '[', ']', '{', '}',  ';', ':',',', '<', '.', '>', '/', '?' };

    /**
     * 按一定的概率生成一个随机的N位(N>=3)密码，必须由字母数字特殊符号组成，三者缺一不可
     *
     * @param len
     *            密码长度,必须大于等于3
     * @param paramGenChances
     *            分别是生成数字、字母、特殊符号的概率
     * @return 生成的随机密码
     */
    public static char[] generateRandomPassword(final int len, final byte[] paramGenChances) {
        if (len < 3) {
            throw new IllegalArgumentException("len must not smaller than 3, but now is " + len);
        }
        final char[] password = new char[len];
        // 之所以该复制一份是为了使函数不对外产生影响
        final byte[] genChances = paramGenChances.clone();
        final byte[] genNums = new byte[genChances.length];
        for (byte i = 0; i < genChances.length; i++) {
            genNums[i] = 0;
        }
        final Random random = new Random();
        int r;
        for (int i = 0; i < len; i++) {
            adjustGenChance(len, i, genChances, genNums);
            byte index = getPasswordCharType(random, genChances);
            genNums[index]++;
            switch (index) {
                case INDEX_NUMBER:
                    password[i] = (char) ('0' + random.nextInt(10));
                    break;
                case INDEX_LETTER:
                    r = random.nextInt(52);
                    if (r < 26) {
                        password[i] = (char) ('A' + r);
                    } else {
                        password[i] = (char) ('a' + r - 26);
                    }
                    break;
                case INDEX_SPECIAL_CHAR:
                    r = random.nextInt(SPECIAL_CHARS.length);
                    password[i] = SPECIAL_CHARS[r];
                    break;
                default:
                    password[i] = ' ';
                    break;
            }
        }
        logChances(genNums);
        return password;
    }

    /**
     * 根据当前需要生成密码字符的位置,动态调整生成概率
     *
     * @param len
     *            待生成的总长度
     * @param index
     *            当前位置
     * @param genChances
     *            分别是生成数字、字母、特殊符号的概率
     * @param genNums
     *            这些类型已经生成过的次数
     */
    public static void adjustGenChance(final int len, final int index, final byte[] genChances, final byte[] genNums) {
        final int leftCount = len - index;
        byte notGenCount = 0;
        for (byte i = 0; i < genChances.length; i++) {
            if (genNums[i] == 0) {
                notGenCount++;
            }
        }
        if (notGenCount > 0 && leftCount < genChances.length && leftCount == notGenCount) {
            for (byte i = 0; i < genChances.length; i++) {
                if (genNums[i] > 0) {
                    genChances[i] = 0;
                }
            }
        }
    }

    /**
     * 获取该密码字符的类型
     *
     * @param random
     *            随机数生成器
     * @param genChances
     *            分别是生成数字、字母、特殊符号的概率
     * @return 密码字符的类型
     */
    public static byte getPasswordCharType(final Random random, final byte[] genChances) {
        int total = 0;
        byte i = 0;
        for (; i < genChances.length; i++) {
            total += genChances[i];
        }
        int r = random.nextInt(total);
        for (i = 0; i < genChances.length; r -= genChances[i], i++) {
            if (r < genChances[i]) {
                break;
            }
        }
        return i;
    }


    /**
     * 打印生成密码中各类字符的个数
     */
    public static void logChances(byte[] genNums) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (byte i = 0; i < genNums.length; i++) {
            sb.append(genNums[i]);
            if (i != genNums.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("}");
//        System.out.println(sb.toString());
    }

    private static void test1() {
        byte[] genChances = { 1, 1, 1 };
        for (int i = 3; i < 200; i += 30) {
            try {
                char[] password = generateRandomPassword(i, genChances);
                System.out.println(password);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        System.out.println();
    }

    private static void test2() {
        byte[] genChances = { 2, 5, 3 };
        for (int i = 3; i < 200; i += 30) {
            try {
                char[] password = generateRandomPassword(i, genChances);
                System.out.println(password);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        System.out.println();
    }

    public static void main(String[] args) {
//        test1();
//        test2();

        byte[] genChances = { 2, 2, 1 };
        char[] password = generateRandomPassword(8, genChances);
        String a = new String(password);
        System.out.println(a);

    }
}
