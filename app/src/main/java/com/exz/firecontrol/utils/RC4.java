package com.exz.firecontrol.utils;

import java.io.UnsupportedEncodingException;

/**
 * Created by hhuav007 on 2017/11/27.
 */

public class RC4
{
    public static byte[] encry(String data, String key) throws UnsupportedEncodingException
    {
        if (data == null || key == null) {
            return null;
        }
        byte b_data[] = data.getBytes("UTF-8");
        return RC4Base(b_data, key);
    }
    public static String decry(byte[] data, String key) {
        if (data == null || key == null) {
            return null;
        }
        return asString(RC4Base(data, key));
    }



    public static String encry2String(String data, String key) throws UnsupportedEncodingException {
        if (data == null || key == null) {
            return null;
        }
        return toHexString(asString(encry(data, key)));
    }
    public static String decry(String data, String key) throws UnsupportedEncodingException {
        if (data == null || key == null) {
            return null;
        }
        return new String(RC4Base(HexString2Bytes(data), key),"UTF-8");
    }



    private static String asString(byte[] buf) {
        StringBuffer strbuf = new StringBuffer(buf.length);
        for (int i = 0; i < buf.length; i++) {
            strbuf.append((char) buf[i]);
        }
        return strbuf.toString();
    }

    private static String toHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch & 0xFF);
            if (s4.length() == 1) {
                s4 = '0' + s4;
            }
            str = str + s4;
        }
        return str;// 0x表示十六进制
    }

    private static byte[] HexString2Bytes(String src) {
        int size = src.length();
        byte[] ret = new byte[size / 2];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < size / 2; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    private static byte uniteBytes(byte src0, byte src1) {
        char _b0 = (char) Byte.decode("0x" + new String(new byte[] { src0 }))
                              .byteValue();
        _b0 = (char) (_b0 << 4);
        char _b1 = (char) Byte.decode("0x" + new String(new byte[] { src1 }))
                              .byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }


    private static byte[] initKey(String aKey) {
        byte[] b_key = aKey.getBytes();
        byte state[] = new byte[256];

        for (int i = 0; i < 256; i++) {
            state[i] = (byte) i;
        }
        int index1 = 0;
        int index2 = 0;
        if (b_key == null || b_key.length == 0) {
            return null;
        }
        for (int i = 0; i < 256; i++) {
            index2 = ((b_key[index1] & 0xff) + (state[i] & 0xff) + index2) & 0xff;
            byte tmp = state[i];
            state[i] = state[index2];
            state[index2] = tmp;
            index1 = (index1 + 1) % b_key.length;
        }
        return state;
    }


    private static byte[] RC4Base(byte[] input, String mKkey) {
        int x = 0;
        int y = 0;
        byte key[] = initKey(mKkey);
        int xorIndex;
        byte[] result = new byte[input.length];

        for (int i = 0; i < input.length; i++) {
            x = (x + 1) & 0xff;
            y = ((key[x] & 0xff) + y) & 0xff;
            byte tmp = key[x];
            key[x] = key[y];
            key[y] = tmp;
            xorIndex = ((key[x] & 0xff) + (key[y] & 0xff)) & 0xff;
            result[i] = (byte) (input[i] ^ key[xorIndex]);
        }
        return result;
    }

    //rc4不包括16进制的加密解密
    public static String HloveyRC4(String aInput,String aKey)
    {
        int[] iS = new int[256];
        byte[] iK = new byte[256];

        for (int i=0;i<256;i++)
            iS[i]=i;

        int j = 1;

        for (short i= 0;i<256;i++)
        {
            iK[i]=(byte)aKey.charAt((i % aKey.length()));
        }

        j=0;

        for (int i=0;i<255;i++)
        {
            j=(j+iS[i]+iK[i]) % 256;
            int temp = iS[i];
            iS[i]=iS[j];
            iS[j]=temp;
        }


        int i=0;
        j=0;
        char[] iInputChar = aInput.toCharArray();
        char[] iOutputChar = new char[iInputChar.length];
        for(short x = 0;x<iInputChar.length;x++)
        {
            i = (i+1) % 256;
            j = (j+iS[i]) % 256;
            int temp = iS[i];
            iS[i]=iS[j];
            iS[j]=temp;
            int t = (iS[i]+(iS[j] % 256)) % 256;
            int iY = iS[t];
            char iCY = (char)iY;
            iOutputChar[x] =(char)( iInputChar[x] ^ iCY) ;
        }

        return new String(iOutputChar);

    }

    public static void main(String args[]) throws Exception {
        String inputStr = "中文3423424324324343434%$##@";
        String key = "nax7jzotxa";
        String str = encry2String(inputStr, key);
        System.out.println(str);
        // 打印加密后的字符串
        // String str = "a4e4fa145d9a93317cb9a88e4a";
        //str = "af10fa80f07e421b32cd157dc5d230c423427e4662290687c386baaddcbf0b69a68136b8e08781f2ad78d3ee2930a86fa09b63b3f72ae10d570016ccc6fce1e76b1ff893b7d959dd35ada39172be0cf913fc517a1342c81fc0869b48ee4166e59a7721815e62ca44bfeded23dccb206d6e49eeadd16bb0bd368384302eeb92ab53d28f3ad9a4062ed0ecdf7dfd5e32cadb7abacb914c663a9a82fc9e937d56adeae6a6d3ede22f55a6a9dc71b17e7dea0f";
        //System.out.println(str);
        // 打印解密后的字符串
        System.out.println(decry(str, key));

        System.out.println("***********************************************");
        String inputStr1 = "json{\"result\":-1}";
        String key1 = "cnhbydtlik";

        String str1 = HloveyRC4(inputStr1,key1);

        //打印加密后的字符串
        System.out.println(str1);

        //打印解密后的字符串
        System.out.println(HloveyRC4(str1,key1));

    }
}
