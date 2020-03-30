/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package empresacombustible;
import InterfazGrafica.VistaPrincipal;
import java.io.BufferedReader;
import java.io.BufferedWriter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
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
    private static CifradoDescifrado CifDes;
    private static GenerarCargarLlaves generadorLlaves;
    
    /**
    Básicamente un menú que permite al usuario interactuar con la aplicación de 
    la empresa, ya sea para solicitar información a las distribuidoras o modificar
    los precios de alguna de ellas.
    */
    public static void main(String[] args)
    {
        generadorLlaves = new GenerarCargarLlaves(); 
        /*GenerarLlaveSecreta() solo se ejecuta para generar una llave, no es
        necesario cambiar la llave a cada rato.*/
        //generadorLlaves.generarLlaveSecreta(); 
        
        /*vistaPrincipal = new VistaPrincipal();
        vistaPrincipal.setLocationRelativeTo(null);
        vistaPrincipal.setVisible(true);*/
        CifDes = new CifradoDescifrado();
               
        try {
            int opcion = -1;
            while(opcion != 0)
            {
                Scanner s = new Scanner(System.in);
                System.out.println("-MENÚ-\n" + 
                        "1) Estación de Servicio Santiago\n" +
                        "2) Estación de Servicio Curicó\n" +
                        "3) Estación de Servicio Talca\n" +
                        "0) Salir\n" +
                        "Ingrese su opción: ");

                try {
                    opcion = s.nextInt(); 
                } catch (InputMismatchException e) {
                    System.out.println("Opción ingresada no válida.");
                }

                switch (opcion)
                {
                    case 1:
                    {
                        int opcionStgo = -1;
                        while(opcionStgo != 0)
                        {
                            menuEstacion("Santiago");

                            try {
                                opcionStgo = s.nextInt();
                            } catch (InputMismatchException e) {
                                System.out.println("Opción ingresada no válida.");
                                s = new Scanner(System.in);
                            }

                            switch(opcionStgo)
                            {
                                case 1:
                                {
                                    int opcionUnoStgo = -1;
                                    while(opcionUnoStgo != 0)
                                    {
                                        menuCombustibles("Santiago");

                                        int nuevoPrecio = 0;
                                        try {
                                            opcionUnoStgo = s.nextInt();

                                            if(opcionUnoStgo != 0)
                                            {
                                                System.out.println("Ingrese el nuevo precio: ");
                                                nuevoPrecio = s.nextInt();
                                            }
                                        } catch (InputMismatchException e) {
                                            System.out.println("Opción ingresada no válida.");
                                            s = new Scanner(System.in);
                                        }
                                        
                                        switchCaseCombustible(nuevoPrecio,opcionUnoStgo, 55500, "Santiago");
                                    } break;
                                }
                                case 2:
                                {
                                    int opcionDosStgo = -1;
                                    while(opcionDosStgo != 0)
                                    {
                                        menuInformacion("Santiago");

                                        try {
                                            opcionDosStgo = s.nextInt();
                                        } catch (InputMismatchException e) {
                                            System.out.println("Opción ingresada no válida.");
                                            s = new Scanner(System.in);
                                        }

                                        switchCaseInformacion(opcionDosStgo, 55500, "Santiago");
                                    } break;
                                }
                            }
                        } break;
                    }        
                    case 2:
                    {
                        int opcionCco = -1;
                        while(opcionCco != 0)
                        {
                            menuEstacion("Curicó");

                            try {
                                opcionCco = s.nextInt();
                            } catch (InputMismatchException e) {
                                System.out.println("Opción ingresada no válida.");
                                s = new Scanner(System.in);
                            }
                           
                            switch(opcionCco){
                                case 1:
                                {
                                    int opcionUnoCco = -1;
                                    while(opcionUnoCco != 0)
                                    {
                                        menuCombustibles("Curicó");

                                        int nuevoPrecio = 0;
                                        try {
                                            opcionUnoCco = s.nextInt();
                                            if(opcionUnoCco != 0)
                                            {
                                                System.out.println("Ingrese el nuevo precio: ");
                                                nuevoPrecio = s.nextInt();
                                            }
                                        } catch (InputMismatchException e) {
                                            System.out.println("Opción ingresada no válida.");
                                            s = new Scanner(System.in);
                                        }
                                        
                                        switchCaseCombustible(nuevoPrecio,opcionUnoCco, 45500, "Curicó");
                                    } break;
                                }
                                case 2:
                                {
                                    int opcionDosCco = -1;
                                    while(opcionDosCco != 0)
                                    {
                                        menuInformacion("Curicó");

                                        try {
                                            opcionDosCco = s.nextInt();
                                        } catch (InputMismatchException e) {
                                            System.out.println("Opción ingresada no válida.");
                                            s = new Scanner(System.in);
                                        }

                                        switchCaseInformacion(opcionDosCco, 45500, "Curicó");
                                    } break;
                                }
                            }
                        } break;
                    }   
                    case 3:
                    {
                        int opcionTalca = -1;
                        while(opcionTalca != 0)
                        {
                            menuEstacion("Talca");
                            
                            try {
                                opcionTalca = s.nextInt();
                            } catch (InputMismatchException e) {
                                System.out.println("Opción ingresada no válida.");
                                s = new Scanner(System.in);
                            }

                            switch(opcionTalca){
                                case 1:
                                {
                                    int opcionUnoTalca = -1;
                                    while(opcionUnoTalca != 0)
                                    {
                                        menuCombustibles("Talca");

                                        int nuevoPrecio = 0;
                                        try {
                                            opcionUnoTalca = s.nextInt();
                                            if(opcionUnoTalca != 0)
                                            {
                                                System.out.println("Ingrese el nuevo precio: ");
                                                nuevoPrecio = s.nextInt();
                                            }
                                        } catch (InputMismatchException e) {
                                            System.out.println("Opción ingresada no válida.");
                                            s = new Scanner(System.in);
                                        }

                                        switchCaseCombustible(nuevoPrecio,opcionUnoTalca, 35500, "Talca");
                                    } break;
                                }
                                case 2:
                                {
                                    int opcionDosTalca= -1;
                                    while(opcionDosTalca != 0)
                                    {
                                        menuInformacion("Talca");
 
                                        try {
                                            opcionDosTalca = s.nextInt();
                                        } catch (InputMismatchException e) {
                                            System.out.println("Opción ingresada no válida.");
                                            s = new Scanner(System.in);
                                        }
                                        
                                        switchCaseInformacion(opcionDosTalca, 35500, "Talca");
                                    } break;
                                }
                            }
                        } break;
                    }
                    default:
                        break;
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Valor ingresado no es válido.");
        }
    } 
    
    /**
    * Permite efectuar las consultas desde la empresa hacia una distribuidora en
    particular, mediante el uso de sockets TCP.
    * @param solicitud consulta que se enviará hacia la distribuidora
    * @param puerto al cual se conecta con la distribuidora
    * @param estacionServicio nombre de la estación de servicio a la cual se 
       conectará.
    */
    public static void consultasDistribuidora(String solicitud, int puerto, String estacionServicio)
    {
        final String host = "localhost";
        final int puertoF = puerto;
        DataInputStream input;
        DataOutputStream output;
        boolean verificoConexion = false;
        try {
            Socket socket = new Socket(host, puertoF);
            output = new DataOutputStream(socket.getOutputStream());
            
            /* Cifra la consulta que va a enviar hacia una distribuidora*/
            String solicitudCifrada = CifDes.cifrarInformacion(solicitud);
            System.out.println("Solicitud cifrada: "+solicitudCifrada);
            
            output.writeUTF(solicitudCifrada);
            
            String[] msjeSplit = solicitud.split("-");
            
            if(verificoConexion == false)
            {
                input = new DataInputStream(socket.getInputStream());
                String msje = input.readUTF();
                System.out.println("Msje Cifrado: "+msje);
                msje = CifDes.descifrarInformacion(msje);
                System.out.println("Cifrando el msje...");
                output.writeUTF(msje);
                System.out.println(input.readUTF());
                String fin = input.readUTF();
   
                if(fin.equals("true"))
                {
                    verificoConexion = true;
                }
            }
            if(verificoConexion == true)
            {
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
                        nombreArchivo = "Ventas "+estacionServicio+" "+fechaActual;
                    }
                    else if(msjeSplit[1].equals("Surtidores"))
                    {
                        nombreArchivo = "Surtidores "+estacionServicio+" "+fechaActual;
                    }
                    FileOutputStream file = new FileOutputStream(nombreArchivo+".txt");
                    inputS.read(b, 0, b.length);
                    file.write(b, 0, b.length);
                    file.close();
                    System.out.println("Información guardada en "+nombreArchivo);

                    LeeEscribeArchivo(nombreArchivo);
                }
                else if(msjeSplit[0].equals("Cambio precio"))
                {
                    input = new DataInputStream(socket.getInputStream());
                    String msje = input.readUTF();

                    //System.out.println("msjeEnc: "+msje);
                    msje = CifDes.descifrarInformacion(msje);
                    System.out.println("msjeDes: "+msje);

                    String mensaje = "";
                    while(!mensaje.equals("Fin"))
                    {
                        mensaje = input.readUTF();
                        //System.out.println("msjEnc: "+mensaje);
                        mensaje = CifDes.descifrarInformacion(mensaje);
                        System.out.println("msjeDes: "+mensaje);
                    }
                }
            }
            socket.close();
        } catch (IOException ex) {
            System.out.println(ex);
        } catch (InvalidAlgorithmParameterException ex) {
            System.out.println(ex);
        }   
    }
    
    /* Todas las funciones que siguen a continuación se ubicaron dentro de una 
    función para no repetir constantemente la misma porción de código en la 
    sección del menú.
    */
    
    /**
    * Muestra las posibles acciones que se pueden ejecutar dentro del menú de una 
    distribuidora.
    * @param nombreEstacion nombre de la estación de servicio
    */
    public static void menuEstacion(String nombreEstacion)
    {
        System.out.println("-MENÚ Estación de Servicio "+nombreEstacion+"-\n" +
                            "1) Cambiar precio del combustible\n" +
                            "2) Solicitar información\n" +
                            "0) Volver al Menú Principal\n"+
                            "Ingrese su opción: ");
    }
    
    /**
    * Muestra los distintos tipos de combustibles a los cuales se les puede modificar
    su precio.
    * @param nombreEstacion nombre de la estación de servicio
    */
    public static void menuCombustibles(String nombreEstacion)
    {
        System.out.println("- MENÚ Estación de Servicio "+nombreEstacion+" - CAMBIO DE PRECIOS\n" +
                            "1) Cambiar precio del combustible 93\n" +
                            "2) Cambiar precio del combustible 95\n" +
                            "3) Cambiar precio del combustible 97\n" +
                            "4) Cambiar precio del combustible Diesel\n" +
                            "5) Cambiar precio del combustible Kerosene\n" +
                            "0) Volver al Menú Estación de Servicio "+nombreEstacion+"\n"+
                            "Ingrese su opción: ");
    }

    /**
    * Switch-Case según el combustible que haya escogido el usuario.
    * @param nuevoPrecio nuevo precio del combustible seleccionado en el menuCombustibles
    * @param opcion combustible escogido disponible en el menuCombustibles
    * @param puerto puerto de la distribuidora a la cual se va a conectar
    * @param estacionServicio nombre de la distribuidora a la cual se va a conectar
    */
    public static void switchCaseCombustible(int nuevoPrecio, int opcion, int puerto, String estacionServicio){
        String solicitud;
        switch (opcion) {
            case 1:
            {
                solicitud = "Cambio precio-93-"+nuevoPrecio;
                consultasDistribuidora(solicitud, puerto, estacionServicio);
                break;
            }
            case 2:
            {
                solicitud = "Cambio precio-95-"+nuevoPrecio;
                consultasDistribuidora(solicitud, puerto, estacionServicio);
                break;
            }
            case 3:
            {
                solicitud = "Cambio precio-97-"+nuevoPrecio;
                consultasDistribuidora(solicitud, puerto, estacionServicio);
                break;
            }
            case 4:
            {
                solicitud = "Cambio precio-Diesel-"+nuevoPrecio;
                consultasDistribuidora(solicitud, puerto, estacionServicio);
                break;
            }
            case 5:
            {
                solicitud = "Cambio precio-Kerosene-"+nuevoPrecio;
                consultasDistribuidora(solicitud, puerto, estacionServicio);
                break;
            }
            default:
            break;
        }
    }
    
    /**
    * Muestra las opciones correspondientes a la información que puede solicitar
      la empresa a una distribuidora.
    *@param estacionServicio nombre de la estación de servicio/distribuidora
    */
    public static void menuInformacion(String estacionServicio)
    {
        System.out.println("- MENÚ Estación de Servicio "+estacionServicio+" - INFORMACIÓN\n" +
                            "1) Solicitar información de Ventas\n" +
                            "2) Solicitar información de Surtidores\n" +
                            "0) Volver al Menú Estación de Servicio "+estacionServicio+"\n"+
                            "Ingrese su opción: ");
    }
    
    /**
    * Switch-Case según el tipo de información solicitada a una distribuidora.
    * @param opcion opcion que escogió el usuario del menuInformacion
    * @param puerto puerto específico de la distribuidora a la cual se va a conectar
    * @param estacionServicio nombre de la distribuidora a la cual se va a conectar
    */
    public static void switchCaseInformacion(int opcion, int puerto, String estacionServicio){
        String solicitud;
        switch(opcion)
        {
            case 1:
            {
                System.out.println("INFO VENTAS");
                solicitud = "Informacion-Ventas";
                consultasDistribuidora(solicitud, puerto, estacionServicio);
                break;
            }
            case 2:
            {
                solicitud = "Informacion-Surtidores";
                consultasDistribuidora(solicitud, puerto, estacionServicio);
                System.out.println("INFO SURTIDORES");
                break;
            }
        }
    }
    
    /**
    * Se utiliza para poder almacenar el archivo con la información recibida desde
    la distribuidora con una fecha.
    * @return fechaActual.
    */
    public static String fechaActual()
    {
        Date date = new Date();  
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");  
        String fechaActual = formatter.format(date);  
        //System.out.println("Date Format with MM-dd-yyyy : "+fechaActual);  
        return fechaActual;
    }
    
    /**
     * Recibe un archivo con su contenido encriptado, lo lee, desencripta y guarda 
     * en un archivo nuevo.
     * @param nombreArchivo Nombre del archivo que va a desencriptar.
     */
    public static void LeeEscribeArchivo(String nombreArchivo) throws IOException, InvalidAlgorithmParameterException
    {
        BufferedReader inputStream = new BufferedReader(new FileReader(
                nombreArchivo+".txt"));
        File UIFile = new File(nombreArchivo+" Desencriptado"+".txt");
        if (!UIFile.exists()) {
            UIFile.createNewFile();
        }
        FileWriter filewriter = new FileWriter(UIFile.getAbsoluteFile());
        BufferedWriter outputStream= new BufferedWriter(filewriter);
        String count;
        while ((count = inputStream.readLine()) != null) {
            String linea = CifDes.descifrarInformacion(count);
            outputStream.write(linea+"\n");
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }
}
