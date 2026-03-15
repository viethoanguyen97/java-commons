package com.hovispace.javacommons.javasecurity;

import com.google.common.hash.HashCode;
import com.google.common.io.Files;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.security.MessageDigest;

import static com.google.common.hash.Hashing.md5;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * MD5 is a widely used cryptographic hash function, which produces a hash of 128 bit.
 */
public class JavaMD5UnitTest {

    @Test
    public void test_hash_string_by_md5_hash_function() throws Exception {
        String hashString = "hello world";

        // There is a hashing functionality in java.security.MessageDigest class.
        // The idea is to first instantiate MessageDigest with the kind of algorithm you want to use as an argument:
        // BE AWARE: MessageDigest is not thread-safe -> we should use a new instance for every thread.
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(hashString.getBytes());

        // use digest() function to generate a hash code:
        byte[] digest = md.digest();
        String hashResult = DatatypeConverter.printHexBinary(digest).toUpperCase();
        String expectedHashResult = "5EB63BBBE01EEED093CB22BB8F5ACDC3";

        assertThat(hashResult).isEqualTo(expectedHashResult);
    }

    @Test
    public void test_verify_md5_checksum() throws Exception {
        String fileName = "src/test/resources/test_md5.txt";

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(readAllBytes(get(fileName)));
        byte[] digest = md.digest();
        String checksum = DatatypeConverter.printHexBinary(digest).toUpperCase();
        String expectedChecksum = "5EB63BBBE01EEED093CB22BB8F5ACDC3";

        assertThat(checksum).isEqualTo(expectedChecksum);
    }

    @Test
    public void test_md5_hashing_by_apache_commons_codec_libraries() throws Exception {
        String hashString = "hello world";
        String hashResult = DigestUtils.md5Hex(hashString).toUpperCase();
        String expectedHashResult = "5EB63BBBE01EEED093CB22BB8F5ACDC3";

        assertThat(hashResult).isEqualTo(expectedHashResult);
    }

    @Test
    public void test_verify_md5_checksum_by_guava_libraries() throws Exception {
        String fileName = "src/test/resources/test_md5.txt";

        // Files.hash deprecated
        // Guava suggests avoiding using MD5 hash function (neither fast nor secure) and replacing MD5 by SHA-256
        HashCode hashCode = Files.asByteSource(new File(fileName)).hash(md5());
        String checksum = hashCode.toString().toUpperCase();
        String expectedChecksum = "5EB63BBBE01EEED093CB22BB8F5ACDC3";

        assertThat(checksum).isEqualTo(expectedChecksum);
    }
}
