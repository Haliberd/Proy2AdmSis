/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package empresacombustible;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 *
 * @author Javiera Méndez
 */
public class CifradoDescifrado 
{
    GenerarCargarLlaves GCLL ;
    PublicKey llavePublica;
    PrivateKey llavePrivada;
    SecretKey llaveSecreta;
    Cipher cipher;
    String nombre; //Sirve para diferenciar si se quieren las llaves de la empresa o de la distribuidora
    
    public CifradoDescifrado()
    {
        try 
        {
            GCLL = new GenerarCargarLlaves();
            llaveSecreta = GCLL.cargaLlaveSecreta();
            cipher = Cipher.getInstance("AES");
            //llaveSecreta = GCLL.generarLlaveSecreta();
        } catch (NoSuchAlgorithmException | IOException ex) {
            Logger.getLogger(CifradoDescifrado.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
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
    
    public String cifrarInformacion(String texto)
    {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, llaveSecreta);
            
            
            byte[] cipherText = cipher.doFinal(texto.getBytes("UTF-8"));
            /*String textoCifrado = new String(cipherText, "UTF8");
            System.out.println(textoCifrado);    
            return textoCifrado;*/
            return Base64.getEncoder().encodeToString(cipherText);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException ex) {
            Logger.getLogger(CifradoDescifrado.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    
    public String descifrarInformacion(String texto) throws InvalidAlgorithmParameterException
    {
        try {
            cipher.init(Cipher.DECRYPT_MODE, llaveSecreta);
            byte[] cipherText = cipher.doFinal(Base64.getDecoder().decode(texto));
            return new String(cipherText);
            /*
            byte[] encryptedBytes = Base64.decodeBase64(texto);
            byte[] input = texto.getBytes();	  
            cipher.update(encryptedBytes);
            
            byte[] cipherText = cipher.doFinal();
            String textoCifrado = new String(cipherText, "UTF8");
            System.out.println(textoCifrado);    
            return textoCifrado; */
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(CifradoDescifrado.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    
    /*
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
