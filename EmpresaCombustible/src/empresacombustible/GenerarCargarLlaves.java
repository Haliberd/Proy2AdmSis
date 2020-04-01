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
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Javiera Méndez
 */
public class GenerarCargarLlaves 
{
    KeyPair keyPair;

    /*Genera una nueva llave secreta*/
    public SecretKey generarLlaveSecreta()
    {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecretKey skey = kgen.generateKey();
            
            String outFilePub = "llaveSecreta";
        
            FileOutputStream outPub = new FileOutputStream(outFilePub + ".key");
            outPub.write(skey.getEncoded());
            outPub.close();
            return skey;
        } catch (NoSuchAlgorithmException | IOException ex) {
            Logger.getLogger(GenerarCargarLlaves.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /*Lee el archivo de la clave secreta ubicado en la ruta del proyecto*/
    public SecretKey cargaLlaveSecreta() throws IOException, NoSuchAlgorithmException
    {
        Path path = Paths.get("llaveSecreta.key");
        byte[] keyb = Files.readAllBytes(path);

        SecretKeySpec skey = new SecretKeySpec(keyb, "AES");
        return skey;
    }
    
    /* Métodos para generar y cargar llaves para cifrar y descifrar con RSA.
    
    public GenerarCargarLlaves() throws NoSuchAlgorithmException, IOException
    {
        generadorLlavePublicaPrivada("Empresa");
        generadorLlavePublicaPrivada("Distribuidora");
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
    }*/
}
