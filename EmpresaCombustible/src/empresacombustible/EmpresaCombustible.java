/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package empresacombustible;
import InterfazGrafica.VistaPrincipal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Máximo Hernández, Javiera Méndez
 */
public class EmpresaCombustible {

    private static VistaPrincipal vistaPrincipal;
    private static Thread serverEmpresa;
    /**
     * @param args the command line arguments
     */
    
    public EmpresaCombustible(){}
    
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
            
            switch (opcion) {
                case 1:
                    int opcionUno = 1;
                    while(opcionUno != 0)
                    {
                        System.out.println("-MENÚ Estación de Servicio Santiago-\n" +
                                "1) Cambiar precio del combustible\n" +
                                "2) Solicitar información\n" +
                                "0) Volver al Menú Principal");
                        
                        System.out.println("Ingrese su opción: ");
                        opcionUno = s.nextInt();

                        while(opcionUno < 0 || opcionUno > 2)
                        {
                            System.out.println("Opción no válida. Vuelva a ingresar su opción: ");
                            opcionUno = s.nextInt();
                        }
                        
                        if(opcionUno == 1)
                        {
                            int opcionDos = 1;
                            while(opcionDos != 0)
                            {
                                System.out.println("- MENÚ Estación de Servicio Santiago - CAMBIO DE PRECIOS\n" +
                                        "1) Cambiar precio del combustible 93\n" +
                                        "2) Cambiar precio del combustible 95\n" +
                                        "3) Cambiar precio del combustible 97\n" +
                                        "4) Cambiar precio del combustible Diesel\n" +
                                        "5) Cambiar precio del combustible Kerosene\n" +
                                        "0) Volver al Menú Estación de Servicio Santiago");

                                System.out.println("Ingrese su opción: ");
                                opcionDos = s.nextInt();
                                while(opcionDos < 0 || opcionDos > 5)
                                {
                                    System.out.println("Opción no válida. Vuelva a ingresar su opción: ");
                                    opcionDos = s.nextInt();
                                }

                                int nuevoPrecio = 0;
                                if(opcionDos != 0)
                                {
                                    System.out.println("Ingrese el nuevo precio: ");
                                    nuevoPrecio = s.nextInt();
                                }
                                
                                String solicitud;
                                switch (opcionDos) {
                                    case 1:
                                        {
                                            solicitud = "Cambio precio-93-"+nuevoPrecio;
                                            consultasDistribuidora(solicitud);
                                            break;
                                        }
                                    case 2:
                                        {
                                            solicitud = "Cambio precio-95-"+nuevoPrecio;
                                            consultasDistribuidora(solicitud);
                                            break;
                                        }
                                    case 3:
                                        {
                                            solicitud = "Cambio precio-97-"+nuevoPrecio;
                                            consultasDistribuidora(solicitud);
                                            break;
                                        }
                                    case 4:
                                        {
                                            solicitud = "Cambio precio-Diesel-"+nuevoPrecio;
                                            consultasDistribuidora(solicitud);
                                            break;
                                        }
                                    case 5:
                                        {
                                            solicitud = "Cambio precio-Kerosene-"+nuevoPrecio;
                                            consultasDistribuidora(solicitud);
                                            break;
                                        }
                                    default:
                                    break;
                                }
                            } break;
                        }
                        else if(opcionUno == 2)
                        {
                            int opcionTres = 1;
                            String solicitud;
                            while(opcionTres != 0)
                            {
                                System.out.println("- MENÚ Estación de Servicio Santiago - INFORMACIÓN\n" +
                                        "1) Solicitar información de Ventas\n" +
                                        "2) Solicitar información de Surtidores\n" +
                                        "0) Volver al Menú Estación de Servicio Santiago");

                                System.out.println("Ingrese su opción: ");
                                opcionTres = s.nextInt();
                                while(opcionTres < 0 || opcionTres > 2)
                                {
                                    System.out.println("Opción no válida. Vuelva a ingresar su opción: ");
                                    opcionTres = s.nextInt();
                                }
                                
                                if(opcionTres == 1){
                                    System.out.println("INFO VENTAS");
                                    solicitud = "Informacion-Ventas";
                                    consultasDistribuidora(solicitud);
                                }
                                else if(opcionTres == 2){
                                    solicitud = "Informacion-Surtidores";
                                    consultasDistribuidora(solicitud);
                                    System.out.println("INFO SURTIDORES");
                                }
                            }
                        }
                    }
                case 2:
                    System.out.println("La estación de servicio Curicó se encuentra en construcción.");
                    break;
                case 3:
                    System.out.println("La estación de servicio Talca se encuentra en construcción.");
                    break;
                default:
                    break;
            }
        }
    } 
    
    /*HACER UN WHILE PARA QUE SIEMPRE ESTÉ ESCUCHANDO LO QUE PASA EN EL MENÚ DE LA EMPRESA*/
    public static void consultasDistribuidora(String solicitud)
    {
        final String host = "localhost";
        final int puerto = 55500;
        DataInputStream input;
        DataOutputStream output;

        try {
            Socket socket = new Socket(host, puerto);
            output = new DataOutputStream(socket.getOutputStream());
            
            output.writeUTF(solicitud);
            
            String[] msjeSplit = solicitud.split("-");
            
            if(msjeSplit[0].equals("Informacion"))
            {
                input = new DataInputStream(socket.getInputStream());
                String mensaje = input.readUTF();
                int tamanoArchivo = Integer.parseInt(mensaje);
                InputStream inputS = socket.getInputStream();
                String fechaActual = fechaActual();
                byte[] b = new byte[tamanoArchivo];
                
                String nombreArchivo = "";
                if(msjeSplit[1].equals("Ventas"))
                {
                    nombreArchivo = "Ventas "+fechaActual+".txt";
                }
                else if(msjeSplit[1].equals("Surtidores"))
                {
                    nombreArchivo = "Surtidores "+fechaActual+".txt";
                }
                FileOutputStream file = new FileOutputStream(nombreArchivo);
                inputS.read(b, 0, b.length);
                file.write(b, 0, b.length);
                file.close();
                System.out.println("Información guardada en "+nombreArchivo);
            }
            else if(msjeSplit[0].equals("Cambio precio"))
            {
                input = new DataInputStream(socket.getInputStream());
                String mensaje = "";
                while(!mensaje.equals("Fin"))
                {
                    mensaje = input.readUTF();
                    System.out.println(mensaje);
                }
            }
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(EstacionServicio.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    
    public static String fechaActual()
    {
        Date date = new Date();  
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");  
        String fechaActual = formatter.format(date);  
        //System.out.println("Date Format with MM-dd-yyyy : "+fechaActual);  
        return fechaActual;
    }
    
    public static int verificarInputInt(Scanner s){
        int opcion = -1;
        int flag = -1;
        while(flag == -1)
        {
            try{
                System.out.println("Ingrese su opción: ");
                opcion = s.nextInt();
                System.out.println("x");
            }catch(InputMismatchException e){
                System.out.println("Debes ingresar un valor numérico...");
            }
        }
        
        /*
        finally{
            System.out.println("Finally!!! ;) ");
        }*/
        return opcion;
    }
}
