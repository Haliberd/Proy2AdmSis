/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package empresacombustible;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Javiera Méndez
 */
public class GenerarCargarLlaves 
{
    KeyPair keyPair;
    
    public GenerarCargarLlaves() throws NoSuchAlgorithmException, IOException
    {
        try {
            generadorLlavePublicaPrivada("Empresa");
            generadorLlavePublicaPrivada("Distribuidora");
        } catch (InvalidKeySpecException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(GenerarCargarLlaves.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
    public void generadorLlavePublicaPrivada(String nombreLlave) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException
    {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair keyPair = kpg.generateKeyPair();
        
        Key pub = keyPair.getPublic();
        
        String outFilePub = "llavePublica"+nombreLlave;
        
        FileOutputStream outPub = new FileOutputStream(outFilePub + ".key");
        outPub.write(pub.getEncoded());
        outPub.close();

        System.err.println("Public key format: " + pub.getFormat());
        
        Key pvt = keyPair.getPrivate();
        
        String outFilePvt = "llavePrivada"+nombreLlave;
        
        FileOutputStream outPvt = new FileOutputStream(outFilePvt + ".key");
        outPvt.write(pvt.getEncoded());
        outPvt.close(); 
        
        System.err.println("Private key format: " + pvt.getFormat());
    }
    
    public PublicKey cargaLlavePublica(String nombreLlave) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException
    {
        Path path = Paths.get("llavePublica"+nombreLlave+".key");
        byte[]  bytes = Files.readAllBytes(path);

        X509EncodedKeySpec ks = new X509EncodedKeySpec(bytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey pubKey = kf.generatePublic(ks);
        return pubKey;
    }
        

    public PrivateKey cargaLlavePrivada(String nombreLlave) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException
    {
        Path path = Paths.get("llavePrivada"+nombreLlave+".key");
        byte[] bytes = Files.readAllBytes(path);

        PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(bytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey pvtKey = kf.generatePrivate(ks);
        return pvtKey;
    }
}
