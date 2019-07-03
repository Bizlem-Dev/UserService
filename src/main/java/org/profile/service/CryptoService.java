package org.profile.service;

/**
 * Interface <code>CryptoService</code> is contains the encryption and
 * decryption messages which encrypt and decrypt the string into respective
 * algorithm.
 * 
 * @author pranav.arya
 */
public interface CryptoService {

    /**
     * Encrypt the respective String.
     * 
     * @param plainData
     *            Contains the plain string
     * @return the encrypted string
     * @throws Exception
     *             the exception
     */
    String encrypt(String plainData) throws Exception;

    /**
     * Decrypt the respective String.
     * 
     * @param encrData
     *            Contains the encrypted string
     * @return the decrypted string
     * @throws Exception
     *             the exception
     */
    String decrypt(String encrData) throws Exception;

}
