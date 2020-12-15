# Java Security

## Cryptographic hash functions
### MD5
- [MD5](https://en.wikipedia.org/wiki/MD5) produces a hash of 128 bit.
- Note: [Guava's official documentation](https://guava.dev/releases/23.0/api/docs/com/google/common/hash/Hashing.html#md5--) indicates, the reason is rather to advise not to use MD5 in general for security concerns. 
  This means we can still use this method if we, for example, need to integrate with the legacy system that requires MD5. Otherwise, we're better off considering safer options, like SHA-256.  
### SHA-256
- [SHA-256](https://en.wikipedia.org/wiki/SHA-2) algorithm generates an almost-unique, fixed-size 256-bit (32-byte) hash.
- This is a one-way function, so the result cannot be decrypted back to the original value.
## References: 
- [MD5 Hashing in Java](https://www.baeldung.com/java-md5)
- [SHA-256 and SHA3-256 Hashing in Java](https://www.baeldung.com/sha-256-hashing-java)

