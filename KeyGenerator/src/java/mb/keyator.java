/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mb;

import utils.DesEncrypter;

/**
 *
 * @author Pedro
 */
public class keyator {

    String fullNameString = "";
    String serialNumberEncoded = "";
    
    /*serialNumberEncoded = calculateSecurityHash(fullNameString, "MD2") + calculateSecurityHash(fullNameString, "MD5") + calculateSecurityHash(fullNameString, "SHA1");


    String serial = ""
            + serialNumberEncoded.charAt(32)
            + serialNumberEncoded.charAt(32)
            + serialNumberEncoded.charAt(32)
            + serialNumberEncoded.charAt(32)
            + "-"
            + serialNumberEncoded.charAt(32)
            + serialNumberEncoded.charAt(32)
            + serialNumberEncoded.charAt(32)
            + serialNumberEncoded.charAt(32)
            + "-"
            + serialNumberEncoded.charAt(32)
            + serialNumberEncoded.charAt(32)
            + serialNumberEncoded.charAt(32)
            + serialNumberEncoded.charAt(32)
            + "-"
            + serialNumberEncoded.charAt(32)
            + serialNumberEncoded.charAt(32)
            + serialNumberEncoded.charAt(32)
            + serialNumberEncoded.charAt(32);*/

    private String calculateSecurityHash(String stringInput, String algorithmName) throws java.security.NoSuchAlgorithmException {
        String hexMessageEncode = "";
        byte[] buffer = stringInput.getBytes();
        java.security.MessageDigest messageDigest = java.security.MessageDigest.getInstance(algorithmName);
        messageDigest.update(buffer);
        byte[] messageDigestBytes = messageDigest.digest();
        for (int index = 0; index < messageDigestBytes.length; index++) {
            int countEncode = messageDigestBytes[index] & 0xff;
            if (Integer.toHexString(countEncode).length() == 1) {
                hexMessageEncode = hexMessageEncode + "0";
            }
            hexMessageEncode = hexMessageEncode + Integer.toHexString(countEncode);
        }
        return hexMessageEncode;
    }

    public String encriptonator(String chave) {
        DesEncrypter encrypter = new DesEncrypter("aabbccaa");
        return encrypter.encrypt(chave);
    }
}
