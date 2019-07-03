package org.profile.service.impl;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.profile.service.CryptoService;

/**
 * Class <code>DefaultCryptoService</code> is used to encrypt and decrypt the
 * plain string and encoded string respectively.
 * 
 * @author pranav.arya
 */
@Component(configurationFactory = true)
@Service(CryptoService.class)
@Properties({ @Property(name = "cryptoService", value = "encrypt") })
public class DefaultCryptoService implements CryptoService {

    /** iv contains the encryption and decryption format */
    private String iv = "poghvd9716425078";

    /** key contains the key which is used for encoding and decoding. */
    private String key = "";

    /** cipher is object of Cipher class. */
    private Cipher cipher = null;

    /** keySpec variable contains the Encryption algorithm */
    private SecretKeySpec keySpec = null;

    /** ivSpec is an object of <code>IvParameterSpec</code> . */
    private IvParameterSpec ivSpec = null;

    /**
     * Instantiates a new default crypto service.
     * 
     * @throws Exception
     *             the exception
     */
    public DefaultCryptoService() throws Exception {
        this.key = "P2:6W:AC:3F:A5:VE";

        // Make sure the key length should be 16
        int len = this.key.length();
        if (len < 16) {
            int addSpaces = 16 - len;
            for (int i = 0; i < addSpaces; i++) {
                this.key = this.key + " ";
            }
        } else {
            this.key = this.key.substring(0, 16);
        }
        this.keySpec = new SecretKeySpec(this.key.getBytes(), "AES");
        this.ivSpec = new IvParameterSpec(iv.getBytes());
        this.cipher = Cipher.getInstance("AES/CBC/NoPadding");
    }

    /**
     * Bytes to Hexa conversion.
     * 
     * @param data
     *            Contains the data in byte
     * @return the string
     */
    public String bytesToHex(byte[] data) {
        if (data == null) {
            return null;
        } else {
            int len = data.length;
            String str = "";
            for (int i = 0; i < len; i++) {
                if ((data[i] & 0xFF) < 16)
                    str = str + "0"
                            + java.lang.Integer.toHexString(data[i] & 0xFF);
                else
                    str = str + java.lang.Integer.toHexString(data[i] & 0xFF);
            }
            return str;
        }
    }

    /* (non-Javadoc)
     * @see org.profile.service.CryptoService#encrypt(java.lang.String)
     */
    public String encrypt(String plainData) throws Exception {

        // Make sure the plainData length should be multiple with 16
        int len = plainData.length();
        int q = len / 16;
        int addSpaces = ((q + 1) * 16) - len;
        for (int i = 0; i < addSpaces; i++) {
            plainData = plainData + " ";
        }

        this.cipher.init(Cipher.ENCRYPT_MODE, this.keySpec, this.ivSpec);
        byte[] encrypted = cipher.doFinal(plainData.getBytes());

        return bytesToHex(encrypted);
    }

    /**
     * Hex to bytes.
     * 
     * @param str
     *            Contains the string
     * @return the byte[]
     */
    public byte[] hexToBytes(String str) {
        if (str == null) {
            return null;
        } else if (str.length() < 2) {
            return null;
        } else {
            int len = str.length() / 2;
            byte[] buffer = new byte[len];
            for (int i = 0; i < len; i++) {
                buffer[i] = (byte) Integer.parseInt(
                        str.substring(i * 2, i * 2 + 2), 16);
            }
            return buffer;
        }
    }


    /* (non-Javadoc)
     * @see org.profile.service.CryptoService#decrypt(java.lang.String)
     */
    public String decrypt(String encrData) throws Exception {
        this.cipher.init(Cipher.DECRYPT_MODE, this.keySpec, this.ivSpec);
        byte[] outText = this.cipher.doFinal(hexToBytes(encrData));

        String decrData = new String(outText).trim();
        return decrData;
    }

  
   /* public static void main(String[] args) throws Exception {
        DefaultCryptoService c = new DefaultCryptoService();
        // String encrStr = c.encrypt("pranav@99");
        String encrStr = c.decrypt("c5d97241c51ee58dbf94e2a000facebc");
        System.out.println("Encrypted Str :" + encrStr);
        // 6a5181817db3dcddd3df2df9890f652f
        // bc5b1eba42c8076424e851daa477646c
        // 793e83e47f4bda549ef370f4fa058f51

    }*/
}