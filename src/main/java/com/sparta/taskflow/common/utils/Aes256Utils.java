package com.sparta.taskflow.common.utils;

import com.sparta.taskflow.common.exception.server.DecryptException;
import com.sparta.taskflow.common.exception.server.EncryptException;
import com.sparta.taskflow.common.exception.server.ServerErrorCode;
import com.sparta.taskflow.common.properties.JwtSecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@Slf4j
@Component
public class Aes256Utils {

    private final SecretKeySpec secretKey;

    public Aes256Utils(JwtSecurityProperties jwtSecurityProperties) {
        String aesSecretKey = jwtSecurityProperties.getSecret().getAesKey();
        byte[] keyBytes = Base64.getDecoder().decode(aesSecretKey);
        this.secretKey = new SecretKeySpec(keyBytes, "AES");
    }

    /**
     * aes 암호화를 진행하는 메서드
     * CBC 를 사용하며 랜덤 IV 는 동적으로 생성
     *
     * @param plainText 암호화 대상 jwt payload
     */
    public String encrypt(String plainText) {
        try {
            byte[] ivBytes = new byte[16];
            new SecureRandom().nextBytes(ivBytes);  //랜덤 IV 생성
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

            Cipher cipher = initCipher(Cipher.ENCRYPT_MODE, ivSpec); //
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());

            //IV 와 암호화된 데이터 합치기
            byte[] combined = new byte[ivBytes.length + encryptedBytes.length];
            System.arraycopy(ivBytes, 0, combined, 0, ivBytes.length);
            System.arraycopy(encryptedBytes, 0, combined, ivBytes.length, encryptedBytes.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            log.error("{} : ", e.getMessage());
            throw new EncryptException(ServerErrorCode.TOKEN_ENCRYPTION_FAILED);
        }
    }

    //복호화 메서드
    public String decrypt(String encryptedText) {
        try {
            byte[] combined = Base64.getDecoder().decode(encryptedText);

            //IV 추출
            byte[] ivBytes = new byte[16];
            System.arraycopy(combined, 0, ivBytes, 0, 16);
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

            //암호화된 데이터 추출
            byte[] encryptedBytes = new byte[combined.length - 16];
            System.arraycopy(combined, 16, encryptedBytes, 0, encryptedBytes.length);

            Cipher cipher = initCipher(Cipher.DECRYPT_MODE, ivSpec);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new DecryptException(ServerErrorCode.TOKEN_DECRYPTION_FAILED);
        }
    }

    //Cipher 초기화
    private Cipher initCipher(int mode, IvParameterSpec ivSpec) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(mode, secretKey, ivSpec);
        return cipher;
    }
}
