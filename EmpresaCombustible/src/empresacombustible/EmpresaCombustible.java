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
                    "2) Estación de Servicio Curicó\n" +
                    "3) Estación de Servicio Talca\n" +
                    "0) Salir\n" +
                    "Ingrese su opción: ");
            Scanner s = new Scanner(System.in);
            opcion = s.nextInt();
            
            switch (opcion) {
                case 1:
                    int opcionStgo = 1;
                    while(opcionStgo != 0)
                    {
                        System.out.println("-MENÚ Estación de Servicio Santiago-\n" +
                                "1) Cambiar precio del combustible\n" +
                                "2) Solicitar información\n" +
                                "0) Volver al Menú Principal");
                        
                        System.out.println("Ingrese su opción: ");
                        opcionStgo = s.nextInt();

                        while(opcionStgo < 0 || opcionStgo > 2)
                        {
                            System.out.println("Opción no válida. Vuelva a ingresar su opción: ");
                            opcionStgo = s.nextInt();
                        }
                        
                        if(opcionStgo == 1)
                        {
                            int opcionUnoStgo = 1;
                            while(opcionUnoStgo != 0)
                            {
                                System.out.println("- MENÚ Estación de Servicio Santiago - CAMBIO DE PRECIOS\n" +
                                        "1) Cambiar precio del combustible 93\n" +
                                        "2) Cambiar precio del combustible 95\n" +
                                        "3) Cambiar precio del combustible 97\n" +
                                        "4) Cambiar precio del combustible Diesel\n" +
                                        "5) Cambiar precio del combustible Kerosene\n" +
                                        "0) Volver al Menú Estación de Servicio Santiago");

                                System.out.println("Ingrese su opción: ");
                                opcionUnoStgo = s.nextInt();
                                while(opcionUnoStgo < 0 || opcionUnoStgo > 5)
                                {
                                    System.out.println("Opción no válida. Vuelva a ingresar su opción: ");
                                    opcionUnoStgo = s.nextInt();
                                }

                                int nuevoPrecio = 0;
                                if(opcionUnoStgo != 0)
                                {
                                    System.out.println("Ingrese el nuevo precio: ");
                                    nuevoPrecio = s.nextInt();
                                }
                                
                                /////
                                menuCombustible(nuevoPrecio,opcionUnoStgo, 55500);
                                /////
                            } 
                        }
                        else if(opcionStgo == 2)
                        {
                            int opcionDosStgo = 1;
                            String solicitud;
                            while(opcionDosStgo != 0)
                            {
                                System.out.println("- MENÚ Estación de Servicio Santiago - INFORMACIÓN\n" +
                                        "1) Solicitar información de Ventas\n" +
                                        "2) Solicitar información de Surtidores\n" +
                                        "0) Volver al Menú Estación de Servicio Santiago");

                                System.out.println("Ingrese su opción: ");
                                opcionDosStgo = s.nextInt();
                                while(opcionDosStgo < 0 || opcionDosStgo > 2)
                                {
                                    System.out.println("Opción no válida. Vuelva a ingresar su opción: ");
                                    opcionDosStgo = s.nextInt();
                                }
                                
                                if(opcionDosStgo == 1)
                                {
                                    System.out.println("INFO VENTAS");
                                    solicitud = "Informacion-Ventas";
                                    consultasDistribuidora(solicitud, 55500);
                                }
                                else if(opcionDosStgo == 2){
                                    solicitud = "Informacion-Surtidores";
                                    consultasDistribuidora(solicitud, 55500);
                                    System.out.println("INFO SURTIDORES");
                                }
                            }
                        }
                    } break;
                case 2:
                    int opcionCco = 1;
                    while(opcionCco != 0)
                    {
                        System.out.println("-MENÚ Estación de Servicio Curicó-\n" +
                                "1) Cambiar precio del combustible\n" +
                                "2) Solicitar información\n" +
                                "0) Volver al Menú Principal");
                        
                        System.out.println("Ingrese su opción: ");
                        opcionCco = s.nextInt();

                        while(opcionCco < 0 || opcionCco > 2)
                        {
                            System.out.println("Opción no válida. Vuelva a ingresar su opción: ");
                            opcionCco = s.nextInt();
                        }
                        
                        if(opcionCco == 1)
                        {
                            int opcionUnoCco = 1;
                            while(opcionUnoCco != 0)
                            {
                                System.out.println("- MENÚ Estación de Servicio Curicó - CAMBIO DE PRECIOS\n" +
                                        "1) Cambiar precio del combustible 93\n" +
                                        "2) Cambiar precio del combustible 95\n" +
                                        "3) Cambiar precio del combustible 97\n" +
                                        "4) Cambiar precio del combustible Diesel\n" +
                                        "5) Cambiar precio del combustible Kerosene\n" +
                                        "0) Volver al Menú Estación de Servicio Curicó");

                                System.out.println("Ingrese su opción: ");
                                opcionUnoCco = s.nextInt();
                                while(opcionUnoCco < 0 || opcionUnoCco > 5)
                                {
                                    System.out.println("Opción no válida. Vuelva a ingresar su opción: ");
                                    opcionUnoCco = s.nextInt();
                                }

                                int nuevoPrecio = 0;
                                if(opcionUnoCco != 0)
                                {
                                    System.out.println("Ingrese el nuevo precio: ");
                                    nuevoPrecio = s.nextInt();
                                }
                                
                                /////
                                menuCombustible(nuevoPrecio,opcionUnoCco, 45500);
                                /////
                            } break;
                        }
                        else if(opcionCco == 2)
                        {
                            int opcionDosCco = 1;
                            String solicitud;
                            while(opcionDosCco != 0)
                            {
                                System.out.println("- MENÚ Estación de Servicio Curicó - INFORMACIÓN\n" +
                                        "1) Solicitar información de Ventas\n" +
                                        "2) Solicitar información de Surtidores\n" +
                                        "0) Volver al Menú Estación de Servicio Curicó");

                                System.out.println("Ingrese su opción: ");
                                opcionDosCco = s.nextInt();
                                while(opcionDosCco < 0 || opcionDosCco > 2)
                                {
                                    System.out.println("Opción no válida. Vuelva a ingresar su opción: ");
                                    opcionDosCco = s.nextInt();
                                }
                                
                                if(opcionDosCco == 1)
                                {
                                    System.out.println("INFO VENTAS");
                                    solicitud = "Informacion-Ventas";
                                    consultasDistribuidora(solicitud, 45500);
                                }
                                else if(opcionDosCco == 2){
                                    solicitud = "Informacion-Surtidores";
                                    consultasDistribuidora(solicitud, 45500);
                                    System.out.println("INFO SURTIDORES");
                                }
                            }
                        }
                    } break;
                case 3:
                    int opcionTalca = 1;
                    while(opcionTalca != 0)
                    {
                        System.out.println("-MENÚ Estación de Servicio Talca-\n" +
                                "1) Cambiar precio del combustible\n" +
                                "2) Solicitar información\n" +
                                "0) Volver al Menú Principal");
                        
                        System.out.println("Ingrese su opción: ");
                        opcionTalca = s.nextInt();

                        while(opcionTalca < 0 || opcionTalca > 2)
                        {
                            System.out.println("Opción no válida. Vuelva a ingresar su opción: ");
                            opcionTalca = s.nextInt();
                        }
                        
                        if(opcionTalca == 1)
                        {
                            int opcionUnoTalca = 1;
                            while(opcionUnoTalca != 0)
                            {
                                System.out.println("- MENÚ Estación de Servicio Talca - CAMBIO DE PRECIOS\n" +
                                        "1) Cambiar precio del combustible 93\n" +
                                        "2) Cambiar precio del combustible 95\n" +
                                        "3) Cambiar precio del combustible 97\n" +
                                        "4) Cambiar precio del combustible Diesel\n" +
                                        "5) Cambiar precio del combustible Kerosene\n" +
                                        "0) Volver al Menú Estación de Servicio Talca");

                                System.out.println("Ingrese su opción: ");
                                opcionUnoTalca = s.nextInt();
                                while(opcionUnoTalca < 0 || opcionUnoTalca > 5)
                                {
                                    System.out.println("Opción no válida. Vuelva a ingresar su opción: ");
                                    opcionUnoTalca = s.nextInt();
                                }

                                int nuevoPrecio = 0;
                                if(opcionUnoTalca != 0)
                                {
                                    System.out.println("Ingrese el nuevo precio: ");
                                    nuevoPrecio = s.nextInt();
                                }
                                
                                /////
                                menuCombustible(nuevoPrecio,opcionUnoTalca, 35500);
                                /////
                            } break;
                        }
                        else if(opcionTalca == 2)
                        {
                            int opcionDosTalca= 1;
                            String solicitud;
                            while(opcionDosTalca != 0)
                            {
                                System.out.println("- MENÚ Estación de Servicio Talca - INFORMACIÓN\n" +
                                        "1) Solicitar información de Ventas\n" +
                                        "2) Solicitar información de Surtidores\n" +
                                        "0) Volver al Menú Estación de Servicio Talca");

                                System.out.println("Ingrese su opción: ");
                                opcionDosTalca = s.nextInt();
                                while(opcionDosTalca < 0 || opcionDosTalca > 2)
                                {
                                    System.out.println("Opción no válida. Vuelva a ingresar su opción: ");
                                    opcionDosTalca = s.nextInt();
                                }
                                
                                if(opcionDosTalca == 1)
                                {
                                    System.out.println("INFO VENTAS");
                                    solicitud = "Informacion-Ventas";
                                    consultasDistribuidora(solicitud, 35500);
                                }
                                else if(opcionDosTalca == 2){
                                    solicitud = "Informacion-Surtidores";
                                    consultasDistribuidora(solicitud, 35500);
                                    System.out.println("INFO SURTIDORES");
                                }
                            }
                        }
                    } break;
                default:
                    break;
            }
        }
    } 
    
    /*HACER UN WHILE PARA QUE SIEMPRE ESTÉ ESCUCHANDO LO QUE PASA EN EL MENÚ DE LA EMPRESA*/
    public static void consultasDistribuidora(String solicitud, int puerto)
    {
        final String host = "localhost";
        final int puertoF = puerto;
        DataInputStream input;
        DataOutputStream output;

        try {
            Socket socket = new Socket(host, puertoF);
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
    
    public static void menuCombustible(int nuevoPrecio, int opcionDos, int puerto){
        String solicitud;
        switch (opcionDos) {
            case 1:
                {
                    solicitud = "Cambio precio-93-"+nuevoPrecio;
                    consultasDistribuidora(solicitud, puerto);
                    break;
                }
            case 2:
                {
                    solicitud = "Cambio precio-95-"+nuevoPrecio;
                    consultasDistribuidora(solicitud, puerto);
                    break;
                }
            case 3:
                {
                    solicitud = "Cambio precio-97-"+nuevoPrecio;
                    consultasDistribuidora(solicitud, puerto);
                    break;
                }
            case 4:
                {
                    solicitud = "Cambio precio-Diesel-"+nuevoPrecio;
                    consultasDistribuidora(solicitud, puerto);
                    break;
                }
            case 5:
                {
                    solicitud = "Cambio precio-Kerosene-"+nuevoPrecio;
                    consultasDistribuidora(solicitud, puerto);
                    break;
                }
            default:
            break;
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
