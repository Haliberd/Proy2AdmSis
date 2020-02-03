/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package empresacombustible;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;



/**
 *
 * @author Maximo Hernandez
 */
public class Surtidor {
    public static void main(String[] args) throws IOException{
        try {
            Socket socket = new Socket("localhost", 59898);
            DataInputStream datainput = new DataInputStream(socket.getInputStream());
            DataOutputStream dataoutput = new DataOutputStream(socket.getOutputStream());
            String bandera = "9";
            while (bandera.compareTo("0") != 0){
                bandera = menu();
                if(bandera.compareTo("1") == 0){
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("Cantidad");
                    String cantidad = scanner.nextLine();
                    dataoutput.writeUTF(cantidad);
                    System.out.println(datainput.readUTF());
                }
            }
        }catch(Exception e){
                
        }
        
    }
    
    
    //Se puede caer si se ingresa algo diferente a un numero
    public static String menu(){
        Scanner scanner = new Scanner(System.in);
        String bandera = "9";
        while (bandera.compareTo("1") != 0 && bandera.compareTo("0") != 0){
            System.out.println("\nBienvenido al distribuidor de combustible\n"
                    + "Ingrese una opcion correspondiente a lo que desee hacer:\n"
                    + "1) Cargar combustible\n"
                    + "0) Cerrar el programa\n"
                    + "Ingrese su opcion: ");
            bandera = scanner.nextLine();
        }
        return bandera;
    }
}
