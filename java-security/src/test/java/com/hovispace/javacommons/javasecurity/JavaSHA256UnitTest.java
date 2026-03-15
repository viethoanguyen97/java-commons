package com.hovispace.javacommons.javasecurity;

import com.google.common.hash.Hashing;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * The SHA (Secure Hash Algorithm) is one of the popular cryptographic hash functions.
 * A cryptographic hash can be used to make a signature for a text or a data file.
 *
 * The SHA-256 algorithm generates an almost-unique, fixed-size 256-bit (32-byte) hash.
 * This is a one-way function, so the result cannot be decrypted back to the original value.
 */
public class JavaSHA256UnitTest {

    @Test
    public void test_sha256_hashing_by_MessageDigest() throws Exception {
        String hashString = "hello world";
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(hashString.getBytes());

        byte[] digest = md.digest();
        String hashResult = DatatypeConverter.printHexBinary(digest).toLowerCase();
        String expectedHashResult = "b94d27b9934d3e08a52e52d7da7dabfac484efe37a5380ee9088f7ace2efcde9";

        assertThat(hashResult).isEqualTo(expectedHashResult);
    }

    @Test
    public void test_sha256_hashing_by_guava() throws Exception {
        String originalString = "hello world";
        String sha256Hex = Hashing.sha256().hashString(originalString, UTF_8).toString();
        String expectedHashResult = "b94d27b9934d3e08a52e52d7da7dabfac484efe37a5380ee9088f7ace2efcde9";

        assertThat(sha256Hex).isEqualTo(expectedHashResult);
    }

    @Test
    public void test_sha256_hashing_by_apache_codecs() throws Exception {
        String originalString = "hello world";
        String sha256Hex = DigestUtils.sha256Hex(originalString);
        String expectedHashResult = "b94d27b9934d3e08a52e52d7da7dabfac484efe37a5380ee9088f7ace2efcde9";

        assertThat(sha256Hex).isEqualTo(expectedHashResult);
    }

    @Test
    public void test_sha3_256_hashing_by_MessageDigest() throws Exception {
        String hashString = "hello world";
        MessageDigest md = MessageDigest.getInstance("SHA3-256");
        md.update(hashString.getBytes());

        byte[] digest = md.digest();
        String hashResult = DatatypeConverter.printHexBinary(digest).toLowerCase();
        String expectedHashResult = "644bcc7e564373040999aac89e7622f3ca71fba1d972fd94a31c3bfbf24e3938";

        assertThat(hashResult).isEqualTo(expectedHashResult);
    }

    @Test
    public void test_sha3_256_hashing_by_apache_codecs() throws Exception {
        String originalString = "hello world";
        String sha256Hex = DigestUtils.sha3_256Hex(originalString);
        String expectedHashResult = "644bcc7e564373040999aac89e7622f3ca71fba1d972fd94a31c3bfbf24e3938";

        assertThat(sha256Hex).isEqualTo(expectedHashResult);
    }
}
