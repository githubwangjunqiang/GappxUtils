package http.utils;

import android.os.Build;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class ABDECF {

    private static final byte[] PADDING_Key = {12, -13, -58, -101, 15, -14, 32, 110, 97, -7, 25, -115, 30, 5, -22, -125, -41, -76, -20, -71, -80, 54, -1, 80, -86, -73, -8, -3, -122, 55, -81, -112};

    private static SecretKey getSecretKey() {
        byte[] key = "f4n7BeFEPaDzdxNw12v0Ne5Hrk5AqaJj".getBytes();
        for (int i = 0, j = Math.min(PADDING_Key.length, key.length); i < j; i++) {
            key[i] ^= PADDING_Key[i];
        }
        if (key.length < PADDING_Key.length) {
            byte[] nkey = new byte[PADDING_Key.length];
            System.arraycopy(key, 0, nkey, 0, key.length);
            System.arraycopy(PADDING_Key, key.length, nkey, key.length, PADDING_Key.length - key.length);
            key = nkey;
        }
        return new SecretKeySpec(key, 0, PADDING_Key.length, "AES");
    }

    protected static byte[] encrypt(String content) throws Exception {
        return encrypt(content.getBytes(Charset.forName("UTF-8")));
    }

    public static byte[] encrypt(byte[] content) throws Exception {
        int pid;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pid = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
        } else {
            pid = new Random().nextInt(Integer.MAX_VALUE);
        }
        byte[] pids = ByteBuffer.allocate(4).putInt(pid).array();
        byte[] ivs = new byte[16];
        new SecureRandom().nextBytes(ivs);
        SecretKey key = getSecretKey();
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");// 创建密码器
        cipher.init(Cipher.ENCRYPT_MODE, key, getParams(ivs, 0, ivs.length));// 初始化
        byte[] ev = cipher.doFinal(content);
        ByteBuffer buf = ByteBuffer.allocate(ev.length + ivs.length + pids.length);
        buf.put(ev);
        for (int i = 0; i < pids.length; i++) {
            buf.put(ivs, i * 4, 4);
            buf.put(pids[i]);
        }
        return buf.array();
    }

    protected static String decrypt_RS(String content) throws Exception {
        return new String(decrypt(content.getBytes(Charset.forName("UTF-8"))));
    }

    protected static String decrypt_RS(byte[] content) throws Exception {
        return new String(decrypt(content), Charset.forName("UTF-8"));
    }

    protected static byte[] decrypt(byte[] content) throws Exception {
        int evLength = content.length - 16;
        byte[] ivs = new byte[16];
        ByteBuffer.wrap(content, evLength, 16).get(ivs);
        SecretKey key = getSecretKey();
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");// 创建密码器
        cipher.init(Cipher.DECRYPT_MODE, key, getParams(ivs, 0, ivs.length));// 初始化
        return cipher.doFinal(content, 0, evLength);
    }

    private static AlgorithmParameterSpec getParams(final byte[] buf, int offset, int len) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return new IvParameterSpec(buf, offset, len);
        }
        return new GCMParameterSpec(128, buf, offset, len);
    }
}
