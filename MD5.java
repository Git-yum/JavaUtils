package com.company;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MD5 {
    private int[] init = {0x67452301,0xEFCDAB89,0x98BADCFE,0x10325476};
    private final char[] hex = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
    private final int[][] move = {{7, 12, 17, 22 },{5, 9, 14, 20},{4, 11, 16, 23},{6, 10, 15, 21}};
    private final int[][] ac = {
            {-680876936,-389564586,606105819,-1044525330,-176418897,1200080426,-1473231341,-45705983,
                    1770035416,-1958414417,-42063,-1990404162,1804603682,-40341101,-1502002290,1236535329},
            {-165796510,-1069501632,643717713,-373897302,-701558691,38016083,-660478335,-405537848,
                    568446438,-1019803690,-187363961,1163531501,-1444681467,-51403784,1735328473,-1926607734},
            {-378558,-2022574463,1839030562,-35309556,-1530992060,1272893353,-155497632,-1094730640,
                    681279174,-358537222,-722521979,76029189,-640364487,-421815835,530742520,-995338651},
            {-198630844,1126891415,-1416354905,-57434055,1700485571,-1894986606,-1051523,-2054922799,
                    1873313359,-30611744,-1560198380,1309151649,-145523070,-1120210379,718787259,-343485551}
    };

    private int FF(int a, int b, int c, int d, int x, int s, int ac) {
        a += (b & c | ~b & d) + x + ac;
        return (a << s | a >>> 32 - s) + b;
    }

    private int GG(int a, int b, int c, int d, int x, int s, int ac) {
        a += (b & d | c & ~d) + x + ac;
        return (a << s | a >>> 32 - s) + b;
    }

    private int HH(int a, int b, int c, int d, int x, int s, int ac) {
        a += (b ^ c ^ d) + x + ac;
        return (a << s | a >>> 32 - s) + b;
    }

    private int II(int a, int b, int c, int d, int x, int s, int ac) {
        a += (c ^ (b | ~d)) + x + ac;
        return (a << s | a >>> 32 - s) + b;
    }

    private void calculate(int[] groups){
        int[] loop = {init[0],init[1],init[2],init[3]};
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 16; j++) {
                if (i == 0){
                    loop[(16-j)%4] = FF(loop[(16-j)%4],loop[(17-j)%4],loop[(18-j)%4],loop[(19-j)%4],groups[j],move[i][j%4],ac[i][j]);
                }else if (i == 1){
                    loop[(16-j)%4] = GG(loop[(16-j)%4],loop[(17-j)%4],loop[(18-j)%4],loop[(19-j)%4],groups[(5*j+1)%16],move[i][j%4],ac[i][j]);
                }else if(i == 2){
                    loop[(16-j)%4] = HH(loop[(16-j)%4],loop[(17-j)%4],loop[(18-j)%4],loop[(19-j)%4],groups[(3*j+5)%16],move[i][j%4],ac[i][j]);
                }else {
                    loop[(16-j)%4] = II(loop[(16-j)%4],loop[(17-j)%4],loop[(18-j)%4],loop[(19-j)%4],groups[(7*j)%16],move[i][j%4],ac[i][j]);
                }
            }
        }
        for (int i = 0; i < init.length; i++) {
            init[i] += loop[i];
        }
    }

    private int[] byteToInt_Array(byte[] bytes){
        int[] groups = new int[16];
        for (int i = 0; i < groups.length; i++) {
            groups[i] = (bytes[4 * i] & 0xFF) | (bytes[4 * i + 1] & 0xFF) << 8 |
                    (bytes[4 * i + 2] & 0xFF) << 16 | (bytes[4 * i + 3] & 0xFF) << 24;
        }

        return groups;
    }

    public String FileMD5(String filePath){
        return FileMD5(new File(filePath));
    }

    public String FileMD5(File file) {
        long lowLen = 0;
        int count = 0;
        byte[] bytes = new byte[64];
        try(FileInputStream fis = new FileInputStream(file)) {
            while ((count = fis.read(bytes))!= -1){
                lowLen += count;
                if (count == 64){
                    calculate(byteToInt_Array(bytes));
                }else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //文件内无字节或恰好读完64倍数的字节
        if (count == -1){
            count = 0;
        }
        //补位
        bytes[count++] = (byte) 0x80;
        while (count % 64 != 56){
            if (count == 64){
                calculate(byteToInt_Array(bytes));
                count = 0;
            }
            bytes[count++] = 0;
        }
        //低64bit长度:byte转bit
        lowLen *= 8;
        for (int i = count; i < bytes.length; i++) {
            //小端赋值：地位赋值低内存
            bytes[i] = (byte) (lowLen & 0xFF);
            lowLen >>= 8;
        }
        calculate(byteToInt_Array(bytes));
        return initArrayToMD5();
    }

    private String initArrayToMD5(){
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < init.length; i++) {
            //4个字节
            for (int j = 0; j < 4; j++) {
                sb.append(hex[init[i] >> 4 * (2*j+1) & 0x0F]);
                sb.append(hex[init[i] >> 4 * 2*j & 0x0F]);
            }
        }
        return sb.toString();
    }
}
