package com.sevenga.push.utils;

/**
 * Created by lizi on 15/9/7.
 */
import java.io.CharArrayWriter;
import java.io.IOException;

public class Base64 {
    static final char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();

    public Base64() {
    }

    public static char[] encode(byte[] content) {
        CharArrayWriter cw = new CharArrayWriter(4 * content.length / 3);
        int idx = 0;
        int x = 0;

        for(int i = 0; i < content.length; ++i) {
            if(idx == 0) {
                x = (content[i] & 255) << 16;
            } else if(idx == 1) {
                x |= (content[i] & 255) << 8;
            } else {
                x |= content[i] & 255;
            }

            ++idx;
            if(idx == 3) {
                cw.write(alphabet[x >> 18]);
                cw.write(alphabet[x >> 12 & 63]);
                cw.write(alphabet[x >> 6 & 63]);
                cw.write(alphabet[x & 63]);
                idx = 0;
            }
        }

        if(idx == 1) {
            cw.write(alphabet[x >> 18]);
            cw.write(alphabet[x >> 12 & 63]);
            cw.write(61);
            cw.write(61);
        }

        if(idx == 2) {
            cw.write(alphabet[x >> 18]);
            cw.write(alphabet[x >> 12 & 63]);
            cw.write(alphabet[x >> 6 & 63]);
            cw.write(61);
        }

        return cw.toCharArray();
    }

    public static byte[] decode(char[] message) throws IOException {
        byte[] buff = new byte[4];
        byte[] dest = new byte[message.length];
        int bpos = 0;
        int destpos = 0;

        for(int res = 0; res < message.length; ++res) {
            char c = message[res];
            if(c != 10 && c != 13 && c != 32 && c != 9) {
                if(c >= 65 && c <= 90) {
                    buff[bpos++] = (byte)(c - 65);
                } else if(c >= 97 && c <= 122) {
                    buff[bpos++] = (byte)(c - 97 + 26);
                } else if(c >= 48 && c <= 57) {
                    buff[bpos++] = (byte)(c - 48 + 52);
                } else if(c == 43) {
                    buff[bpos++] = 62;
                } else if(c == 47) {
                    buff[bpos++] = 63;
                } else {
                    if(c != 61) {
                        throw new IOException("Illegal char in base64 code.");
                    }

                    buff[bpos++] = 64;
                }

                if(bpos == 4) {
                    bpos = 0;
                    if(buff[0] == 64) {
                        break;
                    }

                    if(buff[1] == 64) {
                        throw new IOException("Unexpected \'=\' in base64 code.");
                    }

                    int v;
                    if(buff[2] == 64) {
                        v = (buff[0] & 63) << 6 | buff[1] & 63;
                        dest[destpos++] = (byte)(v >> 4);
                        break;
                    }

                    if(buff[3] == 64) {
                        v = (buff[0] & 63) << 12 | (buff[1] & 63) << 6 | buff[2] & 63;
                        dest[destpos++] = (byte)(v >> 10);
                        dest[destpos++] = (byte)(v >> 2);
                        break;
                    }

                    v = (buff[0] & 63) << 18 | (buff[1] & 63) << 12 | (buff[2] & 63) << 6 | buff[3] & 63;
                    dest[destpos++] = (byte)(v >> 16);
                    dest[destpos++] = (byte)(v >> 8);
                    dest[destpos++] = (byte)v;
                }
            }
        }

        byte[] var8 = new byte[destpos];
        System.arraycopy(dest, 0, var8, 0, destpos);
        return var8;
    }
}
