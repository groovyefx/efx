/*
 * Copyright 2013-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package efx.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javafx.util.Callback;

/**
 * 
 * @author Franklyn Donald <lovefree103@gmail.com>
 *
 */
public class StringUtil {

    public static String md5(String oldStr) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f'};
        byte[] oldbytes = oldStr.getBytes();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");// 获取对象
            md.update(oldbytes);// 初始化对象
            byte[] newBytes = md.digest();// 运行加密算法
            char[] newStr = new char[32];
            for (int i = 0; i < 16; i++) {
                byte temp = newBytes[i];
                newStr[2 * i] = hexDigits[temp >>> 4 & 0xf];
                newStr[2 * i + 1] = hexDigits[temp & 0xf];
            }
            return new String(newStr);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * 将一个字符串数组用指定的字符连接成新的字符串
     *
     * @param values
     * @param c 连接字符
     * @return
     */
    public static String join(Object[] values, char c) {
        if (values != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < values.length; i++) {
                if (i != 0) {
                    sb.append(c);
                }
                sb.append(values[i].toString());
            }
            return sb.toString();
        }
        return "";
    }

    public static String join(List<String> values, char c) {
        return join(values.toArray(new String[0]), c);
    }

    public static String join(Object[] values) {
        return join(values, ',');
    }

    public static <T> String join(List<T> objList, Callback<T, String> field) {
        String[] arr = new String[objList.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = field.call(objList.get(i));
        }
        return join(arr);
    }

    public static boolean isEmpty(String value) {
        return value == null || value.length() == 0;
    }

    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

    public static String getRandomUUID() {
        return UUID.randomUUID().toString();
    }
    
    /**
     * 在一字符串左边填充若干指定字符，使其长度达到指定长度
     *
     * @param srcString
     * @param c
     * @param length
     * @return
     */
    public static String leftPad(String srcString, char c, int length) {
        if (srcString == null) {
            srcString = "";
        }
        int tLen = srcString.length();
        int i, iMax;
        if (tLen >= length) {
            return srcString;
        }
        iMax = length - tLen;
        StringBuffer sb = new StringBuffer();
        for (i = 0; i < iMax; i++) {
            sb.append(c);
        }
        sb.append(srcString);
        return sb.toString();
    }

    public static String leftPad(int n, char c, int length) {
        return leftPad(String.valueOf(n), c, length);
    }

}
