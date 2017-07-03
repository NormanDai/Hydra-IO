package com.hydra.core.common.utils;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

public class StringUtil {

    private StringUtil() {
        throw new IllegalAccessError("StringUtil class");
    }

    /**
     * 1、生成签约申请编码
     */
    public static String getTimeUUid() {
        Date nowTime = new Date(System.currentTimeMillis());
        SimpleDateFormat sdFormatter = new SimpleDateFormat("YYYYMMDDHHmmssSSS");
        String uuId = UUID.randomUUID().randomUUID().toString().replace("-", "");
        String str = "PD" + sdFormatter.format(nowTime) + uuId.substring(0, 6).toUpperCase();
        return str;
    }

    /**
     * 2、产生随机的六位数
     *
     * @return
     */
    public static String getSixNbr() {
        Random rad = new Random();
        String result = String.valueOf(rad.nextInt(1000000));
        if (result.length() != 6) {
            return getSixNbr();
        }
        return result;
    }

    /**
     * 3、生成随机数
     *
     * @param length
     * @return
     */
    public static String getRandomNbr(int length) {
        Random random = new Random();
        /** 设置备选验证码:数字"0-9" **/
        String base = "0123456789";
        int size = base.length();
        StringBuilder randomCode = new StringBuilder();
        /** 随机产生legnth位数字的验证码。**/
        for (int i = 0; i < length; i++) {
            /** 得到随机产生的验证码数字。**/
            int start = random.nextInt(size);
            String strRand = base.substring(start, start + 1);
            randomCode.append(strRand);
        }
        return randomCode.toString();
    }

    /**
     * 4、格式化字符串
     *
     * @param msgTemplate
     * @param positionValues
     * @return
     */
    public static String formatMsg(String msgTemplate, Object... positionValues) {
        try {
            return MessageFormat.format(msgTemplate, positionValues);
        } catch (Exception var5) {
            StringBuilder buf = new StringBuilder("资源信息占位符替换异常，占位符参数信息：");
            for (int i = 0; i < positionValues.length; ++i) {
                buf.append(" arg[" + i + "]=" + positionValues[i]);
            }
            return msgTemplate;
        }
    }

    /**
     * 5、将字符串orignalString左补fillchar，直到使其长度等于lenth
     *
     * @param orignalString
     * @param fillchar
     * @param lenth
     * @return
     */
    public static String fillLeft(String orignalString, char fillchar, int lenth) {
        return Strings.padStart(orignalString, lenth, fillchar);
    }

    /**
     * 6、判断字符串str非空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return !Strings.isNullOrEmpty(str);
    }

    public static boolean isEmpty(String str) {
        return Strings.isNullOrEmpty(str);
    }
    /**
     * 7、生成由随机数字组成的字符串
     *
     * @param i
     * @return
     */
    public static String generateRandomString(int i) {
        char ac[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
        };
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (int j = 0; j < i; j++)
            builder.append(ac[random.nextInt(ac.length)]);

        return builder.toString();
    }

    /**
     * 9、获取16进制中每个字符对应的byte数值
     *
     * @param c
     * @return
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 10、将byte数组转化成16进制的字符串
     *
     * @param abyte0
     * @return
     */
    public static String bytesToHexString(byte abyte0[]) {
        StringBuilder stringbuilder = new StringBuilder("");
        if (abyte0 == null || abyte0.length <= 0)
            return null;
        for (int i = 0; i < abyte0.length; i++) {
            int j = abyte0[i] & 0xff;
            String s = Integer.toHexString(j);
            if (s.length() < 2)
                stringbuilder.append(0);
            stringbuilder.append(s);
        }

        return stringbuilder.toString();
    }

    /**
     * 11、将oldString中的#依次替换为arg
     *
     * @param oldString
     * @param arg
     * @return
     */
    public static String replaceString(String oldString, String... arg) {
        for (String replaceStr : arg) {
            oldString = oldString.replaceFirst("#", replaceStr);

        }
        return oldString;

    }

    /**
     * 12、将数值转为格式化字符串
     *
     * @param paramDouble
     * @param paramInt
     * @return
     */
    public static String formatNumber(double paramDouble, int paramInt) {
        return NumberFormat.getNumberInstance().format(round(paramDouble, paramInt));
    }

    public static double round(double paramDouble, int paramInt) {
        if (paramInt < 0) {
            throw new RuntimeException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal localBigDecimal1 = new BigDecimal(Double.toString(paramDouble));
        BigDecimal localBigDecimal2 = new BigDecimal("1");
        return localBigDecimal1.divide(localBigDecimal2, paramInt, 4).doubleValue();
    }

    /**
     * 13、屏蔽字符串中间字符
     *
     * @param str         原字符串
     * @param leftCount   保留左边位数
     * @param lightCount  保留右边位数
     * @param replaceChar 中间替换的字符
     * @return
     */
    public static String encryptString(String str, int leftCount, int lightCount, String replaceChar) {

        if (str != null) {
            StringBuilder cardNoEn = new StringBuilder();
            int strLength = str.length();
            int sumCount = leftCount + lightCount;
            String cardNoLeft = "";
            String cardNoLight = "";
            String strEn = "";

            if (strLength > sumCount) {
                /** 取前leftCount位 **/
                cardNoLeft = str.substring(0, leftCount);
                /** 取后lightCount位 **/
                cardNoLight = str.substring(strLength - lightCount, strLength);
                /** 需要补充加密字符 **/
                String formatStr = "%" + (strLength - sumCount) + "s";
                strEn = String.format(formatStr, "").replaceAll("\\s", replaceChar);

                str = cardNoEn.append(cardNoLeft).append(strEn).append(cardNoLight).toString();
            }
        }
        return str;
    }

    /**
     * 14、判断是否为全数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 15、判断是否为全数字
     *
     * @param s
     * @return
     */
    public static boolean isNumber(String s) {
        if (null == s || "".equals(s))
            return false;
        String s1 = "0123456789";
        for (int i = 0; i < s.length(); i++)
            if (s1.indexOf(s.charAt(i)) < 0)
                return false;

        return true;
    }
}
