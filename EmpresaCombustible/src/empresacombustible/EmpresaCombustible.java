/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package empresacombustible;
import InterfazGrafica.VistaPrincipal;

//import java.sql.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Máximo Hernández, Javiera Méndez
 */
public class EmpresaCombustible {

    private static VistaPrincipal vistaPrincipal;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        vistaPrincipal = new VistaPrincipal();
        vistaPrincipal.setLocationRelativeTo(null);
        vistaPrincipal.setVisible(true);
        //FALTA HACER EL WHILE PARA COMPROBAR SI LA OPCIÓN INGRESADA POR EL USUARIO ES VÁLIDA
        int opcion = 1;
        while(opcion != 0)
        {
            System.out.println("-MENÚ-\n" + 
                    "1) Estación de Servicio Santiago\n" +
                    "2) -En construcción- Estación de Servicio Curicó\n" +
                    "3) -En construcción- Estación de Servicio Talca\n" +
                    "0) Salir\n" +
                    "Ingrese su opción: ");
            Scanner s = new Scanner(System.in);
            opcion = s.nextInt();
            
            if(opcion == 1)
            {
                int opcionUno = 1;
                
                while(opcionUno != 0)
                {
                    System.out.println("-MENÚ Estación de Servicio Santiago-\n" +
                            "1) Cambiar precio del combustible 93\n" +
                            "2) Cambiar precio del combustible 95\n" +
                            "3) Cambiar precio del combustible 97\n" +
                            "4) Cambiar precio del combustible Diesel\n" +
                            "5) Cambiar precio del combustible Kerosene\n" +
                            "0) Volver al Menú Principal");
                    
                    System.out.println("Ingrese su opción: ");
                    opcionUno = s.nextInt();
                    while(opcionUno < 0 || opcionUno > 5)
                    {
                        System.out.println("Ingrese su opción: ");
                        opcionUno = s.nextInt();
                    }
                    
                    int nuevoPrecio = 0;
                    if(opcionUno != 0)
                    {
                        System.out.println("Ingrese el nuevo precio: ");
                        nuevoPrecio = s.nextInt();
                    }
                    
                    if(opcionUno == 1)
                    {
                        String combustible = "93";
                        testCambioPrecioSantiago(nuevoPrecio, combustible);
                    }
                    else if(opcionUno == 2)
                    {
                        String combustible = "95";
                        testCambioPrecioSantiago(nuevoPrecio, combustible);
                    }
                    else if(opcionUno == 3)
                    {
                        String combustible = "97";
                        testCambioPrecioSantiago(nuevoPrecio, combustible);
                    }
                    else if(opcionUno == 4)
                    {
                        String combustible = "Diesel";
                        testCambioPrecioSantiago(nuevoPrecio, combustible);
                    }
                    else if(opcionUno == 5)
                    {
                        String combustible = "Kerosene";
                        testCambioPrecioSantiago(nuevoPrecio, combustible);
                    }
                }
            }
            else if(opcion == 2)
            {
                System.out.println("La estación de servicio Curicó se encuentra en construcción.");
            }
            else if(opcion == 3)
            {
                System.out.println("La estación de servicio Talca se encuentra en construcción.");
            }
        }
    } 
    
    public static void testCambioPrecioSantiago(int nuevoPrecio, String combustible)
    {
        //Dirección del localhost
        final String host = "127.0.0.1";
        final int puerto = 5500;
        DataInputStream input;
        DataOutputStream output;

        try {
            Socket socket = new Socket(host, puerto);
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
            
            String precio = Integer.toString(nuevoPrecio);
            output.writeUTF("Cambio precio-"+combustible+"-"+precio);

            String mensaje = "";
            while(!mensaje.equals("Fin"))
            {
                mensaje = input.readUTF();
                System.out.println(mensaje);
            }
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(DistribuidorSantiago.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
}
