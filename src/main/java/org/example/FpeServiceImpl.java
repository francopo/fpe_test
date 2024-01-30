package org.example;

import com.privacylogistics.FF3Cipher;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

public class FpeServiceImpl implements FpeService {

    private static String BASE32_CHARS = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ"; // Avoid using similar characters (I1O0)
    private static char PADDING_CHAR = BASE32_CHARS.charAt(0);

    private static int MAX_SEQUENCE_NUMBER = (int) Math.pow(2, BASE32_CHARS.length() - 1) / 2;
    private static int PSEUDO_RANDOM_CODE_LENGTH = 6;
    private String key;
    private String tweak;

    public FpeServiceImpl(String key, String tweak) {
        this.key = key;
        this.tweak = tweak;
    }

    @Override
    public String sequenceNumberToPseudoRandomCode(int input) {
        if (input > MAX_SEQUENCE_NUMBER) {
            throw new IllegalArgumentException("input must be greater than " + MAX_SEQUENCE_NUMBER);
        }

        String base32String = base10ToBase32(input);
        String paddedBase32String = StringUtils.leftPad(base32String, PSEUDO_RANDOM_CODE_LENGTH, PADDING_CHAR);
        String encoded = fpsEncrypt(paddedBase32String);

        System.out.println(String.format("Encode: %d -> %s -> %s -> %s", input, base32String, paddedBase32String, encoded));

        return encoded;
    }

    @Override
    public int pseudoRandomCodeToSequenceNumber(String input) {
        if (input == null || !StringUtils.containsOnly(input, BASE32_CHARS)) {
            throw new IllegalArgumentException("input must contains " + BASE32_CHARS);
        }
        if (input.length() != PSEUDO_RANDOM_CODE_LENGTH) {
            throw new IllegalArgumentException("input length must be equal to " + PSEUDO_RANDOM_CODE_LENGTH);
        }

        String paddedBase32String = fpsDecrypt(input);
        String base32String = removePaddingCharacters(paddedBase32String, PADDING_CHAR);
        int decoded = base32ToBase10(base32String);

        System.out.println(String.format("Decode: %s -> %s -> %s -> %d", input, paddedBase32String, base32String, decoded));

        return decoded;
    }

    private String removePaddingCharacters(String input, char paddingChar) {
        StringBuilder sb = new StringBuilder(input);
        while (sb.length() > 1 && sb.charAt(0) == paddingChar) {
            sb.deleteCharAt(0);
        }
        return sb.toString();
    }

    private String fpsEncrypt(String plaintext) {
        FF3Cipher c6 = new FF3Cipher(key, tweak, BASE32_CHARS);
        try {
            String ciphertext = c6.encrypt(plaintext);
            return ciphertext;
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }

    private String fpsDecrypt(String ciphertext) {
        FF3Cipher c6 = new FF3Cipher(key, tweak, BASE32_CHARS);
        try {
            String decrypted = c6.decrypt(ciphertext);
            return decrypted;
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }

    private String base10ToBase32(int input) {
        StringBuilder base32String = new StringBuilder();
        while (input > 0) {
            int remainder = input % 32;
            base32String.insert(0, BASE32_CHARS.charAt(remainder));
            input /= 32;
        }

        return base32String.toString();
    }

    private int base32ToBase10(String input) {
        int output = 0;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            int value = BASE32_CHARS.indexOf(c);
            output = output * 32 + value;
        }

        return output;
    }
}
