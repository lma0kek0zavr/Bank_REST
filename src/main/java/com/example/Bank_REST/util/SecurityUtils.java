package com.example.Bank_REST.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.Bank_REST.service.domain.UserDomainService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SecurityUtils {

    @Value("${security.card.secrete}")
    private String cardSecrete;

    private final String ALGORITHM = "AES";

    private SecretKey cardSecretKey;

    private final PasswordEncoder passwordEncoder;

    private final UserDomainService userDomainService;

    @PostConstruct
    public void init() {
        cardSecretKey = generateSecretKey(cardSecrete);
    }

    /**
     * Encodes a given password using the password encoder.
     *
     * @param password the password to be encoded
     * @return the encoded password
     */
    public String encodePassword(String password) { 
        return passwordEncoder.encode(password);
    }

    /**
     * Checks if a given password matches the provided encoded password.
     *
     * @param password        the password to be checked
     * @param encodedPassword the encoded password to match against
     * @return true if the password matches the encoded password, false otherwise
     */
    public boolean checkPassword(String password, String encodedPassword) { 
        return passwordEncoder.matches(password, encodedPassword);
    }

    /**
     * Masks a given card number, replacing all but the last four digits with asterisks.
     *
     * @param cardNumber the card number to be masked
     * @return          the masked card number
     */
    public String maskCard(String cardNumber) {
        String part = cardNumber.substring(cardNumber.length() - 4, cardNumber.length());
        return "**** **** **** " + part;
    }

    /**
     * Encrypts a given card number using the AES algorithm and returns the encrypted value as a Base64 encoded string.
     *
     * @param cardNumber the card number to be encrypted
     * @return the encrypted card number as a Base64 encoded string
     */
    public String encodeCardNumber(String cardNumber) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, cardSecretKey);
            byte[] encrypted = cipher.doFinal(cardNumber.getBytes());

            return Base64.getEncoder().encodeToString(encrypted);
        } catch (
            NoSuchPaddingException
            | NoSuchAlgorithmException
            | InvalidKeyException
            | IllegalBlockSizeException
            | BadPaddingException e
        ) {
            throw new RuntimeException("Error encrypting card number", e.getCause());
        }
    }

    /**
     * Decrypts a given encoded card number using the AES algorithm and returns the decrypted value as a string.
     *
     * @param encodedCardNumber the encoded card number to be decrypted
     * @return                  the decrypted card number as a string
     */
    public String decodeCardNumber(String encodedCardNumber) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, cardSecretKey);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encodedCardNumber));

            return new String(decrypted);
        } catch (
            NoSuchPaddingException
            | NoSuchAlgorithmException
            | InvalidKeyException
            | IllegalBlockSizeException
            | BadPaddingException e
        ) {
            throw new RuntimeException("Error decrypting card number", e.getCause());
        }
    }

    /**
     * Generates a secret key based on the provided secret string.
     *
     * @param secrete the secret string used to generate the secret key
     * @return the generated secret key
     */
    private SecretKey generateSecretKey(String secrete) {
        byte[] keyBytes = Arrays.copyOf(secrete.getBytes(), 32);

        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    /**
     * Retrieves the ID of the currently authenticated user.
     *
     * @return the ID of the current user
     */
    public Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String userName = auth.getName();

        return userDomainService.getByUserName(userName).getId();
    }
}
