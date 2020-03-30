/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package empresacombustible;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author Javiera Méndez
 */
public class CifradoDescifrado 
{
    GenerarCargarLlaves GCLL ;
    //PublicKey llavePublica;
    //PrivateKey llavePrivada;
    SecretKey llaveSecreta;
    Cipher cipher;
    String nombre;
    
    public CifradoDescifrado()
    {
        try 
        {
            GCLL = new GenerarCargarLlaves();
            llaveSecreta = GCLL.cargaLlaveSecreta();
            cipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException | IOException | NoSuchPaddingException ex) {
            Logger.getLogger(CifradoDescifrado.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*
        try {
        GCLL = new GenerarCargarLlaves();
        //llavePublica = GCLL.cargaLlavePublica(receptor);
        //llavePrivada = GCLL.cargaLlavePrivada(emisor);
        //cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException | NoSuchPaddingException ex) {
        Logger.getLogger(CifradoDescifrado.class.getName()).log(Level.SEVERE, null, ex);
        }*/ 

        /*
        try {
        GCLL = new GenerarCargarLlaves();
        //llavePublica = GCLL.cargaLlavePublica(receptor);
        //llavePrivada = GCLL.cargaLlavePrivada(emisor);
        //cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException | NoSuchPaddingException ex) {
        Logger.getLogger(CifradoDescifrado.class.getName()).log(Level.SEVERE, null, ex);
        }*/     
    }

    public SecretKey getLlaveSecreta() {
        return llaveSecreta;
    }
    
    public String cifrarInformacion(String texto)
    {
        
        /*
        try {
            cipher.init(Cipher.ENCRYPT_MODE, llaveSecreta);
            byte[] cipherText = cipher.doFinal(texto.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(cipherText);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException ex) {
            Logger.getLogger(CifradoDescifrado.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";*/
        String encryptedString;
        byte[] encryptText = texto.getBytes();
        try {
            cipher.init(Cipher.ENCRYPT_MODE, llaveSecreta);
            encryptedString = Base64.encodeBase64String(cipher.doFinal(encryptText));
            return encryptedString;
        } 
        catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            //return "Error";
            
        }
        return "";
    }
    
    public String descifrarInformacion(String texto) throws InvalidAlgorithmParameterException
    {
        /*
        try {
            cipher.init(Cipher.DECRYPT_MODE, llaveSecreta);
            byte[] cipherText = cipher.doFinal(Base64.getDecoder().decode(texto));
            return new String(cipherText);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(CifradoDescifrado.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";*/
        String encryptedString;
        byte[] encryptText = null;
        try {
            encryptText = Base64.decodeBase64(texto);
            cipher.init(Cipher.DECRYPT_MODE, llaveSecreta);
            encryptedString = new String(cipher.doFinal(encryptText));
            return encryptedString;
        } catch (Exception e) {
            e.printStackTrace();
            //return "Error";
        }
        return "";
    }
    
    /* Métodos que sirven para cifrar y descifrar con RSA
    public String cifrarInformacion(String texto)
    {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, llavePublica);
            byte[] input = texto.getBytes();	  
            cipher.update(input);
            
            byte[] cipherText = cipher.doFinal();
            String textoCifrado = new String(cipherText, "UTF8");
            System.out.println(textoCifrado);    
            return textoCifrado;      
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException ex) {
            Logger.getLogger(CifradoDescifrado.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return "";
    }
    
    public String descifrarInformacion(byte[] textoCifrado)
    {      
        try {
            cipher.init(Cipher.DECRYPT_MODE, llavePrivada);
            byte[] decipheredText = cipher.doFinal(textoCifrado);
            String textoDescifrado = new String(decipheredText);
            System.out.println(textoDescifrado);
            return textoDescifrado;
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(CifradoDescifrado.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }*/
}
