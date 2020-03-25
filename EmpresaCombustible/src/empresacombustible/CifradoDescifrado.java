/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package empresacombustible;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author Javiera Méndez
 */
public class CifradoDescifrado 
{
    GenerarCargarLlaves GCLL ;
    PublicKey llavePublica;
    PrivateKey llavePrivada;
    Cipher cipher;
    String nombre; //Sirve para diferenciar si se quieren las llaves de la empresa o de la distribuidora
    
    public CifradoDescifrado(String emisor, String receptor)
    {
        this.nombre = nombre;
        try {
            GCLL = new GenerarCargarLlaves();
            llavePublica = GCLL.cargaLlavePublica(receptor);
            llavePrivada = GCLL.cargaLlavePrivada(emisor);
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException | NoSuchPaddingException ex) {
            Logger.getLogger(CifradoDescifrado.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    //LO MEJOR ES QUE AMBOS RECIBAN STRING O ARRAY DE BTYES
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
    }
}
