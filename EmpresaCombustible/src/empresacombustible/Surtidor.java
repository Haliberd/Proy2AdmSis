/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package empresacombustible;

import static empresacombustible.EmpresaCombustible.switchCaseCombustible;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.util.InputMismatchException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Maximo Hernandez
 */

public class Surtidor {

    private static CifradoDescifrado CifDes;
    private static int cantidadDeCombustibleConocida = 0;
    /**
     * Metodo main encargado de inicializar el Surtidor, y mantener corriendo este mismo.
     * @param args. No son necesarios.
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, FileNotFoundException, InvalidAlgorithmParameterException{
        CifDes = new CifradoDescifrado();
        String tipo = inicio();
        String empresa = conectarAEmpresa();
        try {
            int puertoSocket = Integer.parseInt(empresa);
            Socket socket = new Socket("localhost", puertoSocket);
            DataInputStream datainput = new DataInputStream(socket.getInputStream());
            DataOutputStream dataoutput = new DataOutputStream(socket.getOutputStream());
            
            programaConectado(datainput, dataoutput, tipo, nombreEmpresa(empresa));
        }catch(Exception e){
            System.out.println("El surtidor esta funcionando en modo offline.");    
            programaDesconectado(tipo, nombreEmpresa(empresa));
        }
    }
    
    //Metodo para obtener el nombre de la empresa a partir del puerto a utilizar.
    private static String nombreEmpresa(String empresa){
        switch(empresa){
            case "59898":
                return "Santiago";
            case "49898":
                return "Curico";
            case "39898":
                return "Talca";
        }
        return "";
    }
    
    private static void programaDesconectado(String tipo, String nombreEmpresa) throws FileNotFoundException, IOException, InvalidAlgorithmParameterException{
        ArrayList<String> comandosIngresados = new ArrayList<>();
        if(cantidadDeCombustibleConocida==0)
            cantidadDeCombustibleConocida = ultimaCantidadDeCombustibleRecibida(tipo, nombreEmpresa);
        String bandera = "9";
        
        while (bandera.compareTo("0") != 0){
            bandera = menu();
            if(bandera.compareTo("1") == 0){
                Scanner scanner = new Scanner(System.in);
                System.out.println("Cantidad: ");
                //Metodo para validar si es que la cantidad ingresada es mayor a 0. Para evitar realizar cargas negativas.
                String cantidad = cantidadDeCombustibleACargar(scanner);
                /*
                *Metodo para determinar si es que existe combustible disponible.
                *Solo se almacenan las consultas que son posibles de realizar, es decir, las consultas realizadas con
                *cantidadDeCombustibleConocida>=0.
                */
                if(cantidadDeCombustibleConocida > 0){
                    if((cantidadDeCombustibleConocida - Integer.parseInt(cantidad)) < 0){
                        System.out.println("No habia suficiente combustible para cargar.\n"
                                + "Solo se cargaron " + (cantidadDeCombustibleConocida) + " litros.");
                        comandosIngresados.add(tipo + "-" + cantidadDeCombustibleConocida);
                        cantidadDeCombustibleConocida = 0;
                    }
                    else{
                        System.out.println("Carga exitosa. Se cargaron " + cantidad + " litros.");
                        cantidadDeCombustibleConocida = (cantidadDeCombustibleConocida - Integer.parseInt(cantidad));
                        comandosIngresados.add(tipo + "-" + cantidad);
                    }
                    
                }
                else{
                    System.out.println("No habia combustible para cargar.");
                }
                
            }
        }
        //Ingresa los cambios realizados de forma offline.
        imprimirComandosIngresados(comandosIngresados, tipo, nombreEmpresa);
        ultimaCantidadDeCombustibleConocida(CifDes.cifrarInformacion(Integer.toString(cantidadDeCombustibleConocida)), tipo, nombreEmpresa);
    }
    
    //Metodo para validar de forma offline las cantidad de combustible a cargar ingresadas.
    private static String cantidadDeCombustibleACargar(Scanner scanner){
        String cantidad = "0";
        while(Integer.parseInt(cantidad) <= 0){
            try{
                cantidad = scanner.nextLine();
                if(Integer.parseInt(cantidad) <= 0)
                    System.out.println("Por favor, ingrese una cantidad de combustible mayor a 0.\nCantidad: ");
            }
            catch(Exception e){
                System.out.println("Por favor, ingrese un caracter valido.\nCantidad: ");
                cantidad = "0";
            }
        }
        return cantidad;
    }
    
    //Metodo que escribe dentro de un archivo las peticiones realizadas de forma correcta dentro del surtidor en modo offline
    private static void imprimirComandosIngresados(ArrayList<String> comandosIngresados, String tipo, String nombreEmpresa) throws IOException{
        File UIFile = new File(tipo + "-" + nombreEmpresa +".txt");
        if (!UIFile.exists()) {
            UIFile.createNewFile();
        }
        FileWriter filewriter = new FileWriter(UIFile.getAbsoluteFile(), true);
        BufferedWriter oS = new BufferedWriter(filewriter);
        PrintWriter outputStream = new PrintWriter(oS);
        for(String cadena : comandosIngresados){
            outputStream.print(CifDes.cifrarInformacion(cadena) + "\n");
        }
        outputStream.flush();
        outputStream.close();
    }
    
    private static void leerComandosAlmacenados(String tipo, String nombreEmpresa, DataOutputStream dataoutput) throws FileNotFoundException, IOException{
        dataoutput.writeUTF(CifDes.cifrarInformacion("1"));
        File UIFile = new File(tipo + "-" + nombreEmpresa +".txt");
        if (UIFile.exists()) {
            BufferedReader inputStream = new BufferedReader(new FileReader(UIFile.getAbsoluteFile()));
            String count;
            while ((count = inputStream.readLine()) != null) {
                dataoutput.writeUTF(count);
            }
            dataoutput.writeUTF(CifDes.cifrarInformacion("0"));
            inputStream.close();
            UIFile.getAbsoluteFile().delete();
        }
        else{
            dataoutput.writeUTF(CifDes.cifrarInformacion("0"));
        }
    }      
    
    private static void programaConectado(DataInputStream datainput, DataOutputStream dataoutput, String tipo, String nombreEmpresa) throws IOException, InvalidAlgorithmParameterException{
        

        String bandera = "9";
        leerComandosAlmacenados(tipo, nombreEmpresa, dataoutput);
        
        //Envia el tipo de combustible a cargar.
        dataoutput.writeUTF(CifDes.cifrarInformacion(tipo));
        cantidadDeCombustibleConocida = Integer.parseInt(CifDes.descifrarInformacion(datainput.readUTF()));
        System.out.println("IniCombustible conocido: " + cantidadDeCombustibleConocida);
        
        while (bandera.compareTo("0") != 0){
            bandera = menu();
            if(bandera.compareTo("1") == 0){
                Scanner scanner = new Scanner(System.in);
                int cantidad = 0;                    
                try {
                    System.out.println("Cantidad: ");
                    cantidad = scanner.nextInt();

                    //System.out.println("1 "+tipo + "-" + cantidad);
                    String tipCant = CifDes.cifrarInformacion(tipo + "-" + cantidad);
                    System.out.println("TipCant: "+tipCant);
                    dataoutput.writeUTF(tipCant);
                    String resultado = datainput.readUTF();
                    System.out.println("r Cif: "+resultado);
                    resultado = CifDes.descifrarInformacion(resultado);
                    System.out.println("r Des: "+resultado);

                    int valorResultado = Integer.valueOf(resultado);
                    if(valorResultado < 0){
                        System.out.println("No habia suficiente combustible para cargar.\n"
                                + "Solo se cargaron " + (valorResultado*-1) + " litros.");
                        cantidadDeCombustibleConocida += valorResultado;
                    }
                    else if (valorResultado > 0){
                        System.out.println("Carga exitosa. Se cargaron " + (valorResultado) + " litros.");
                        cantidadDeCombustibleConocida -= valorResultado;
                    }
                    else{
                        System.out.println("No habia combustible para cargar.");
                    }

                } catch (InputMismatchException e) {
                    System.out.println("Cantidad ingresada no válida.");
                    scanner = new Scanner(System.in);
                }

                /*
                System.out.println("1 "+tipo + "-" + cantidad);
                String tipCant = CifDes.cifrarInformacion(tipo + "-" + cantidad);
                System.out.println("TipCant: "+tipCant);
                dataoutput.writeUTF(tipCant);
                String resultado = datainput.readUTF();
                System.out.println("r Cif: "+resultado);
                resultado = CifDes.descifrarInformacion(resultado);
                System.out.println("r Des: "+resultado);*/

                /*
                try{
                    int valorResultado = Integer.valueOf(resultado);
                    if(valorResultado < 0){
                        System.out.println("No habia suficiente combustible para cargar.\n"
                                + "Solo se cargaron " + (valorResultado*-1) + " litros.");
                    }
                    else if (valorResultado > 0){
                        System.out.println("Carga exitosa. Se cargaron " + (valorResultado) + " litros.");
                    }
                    else{
                        System.out.println("No habia combustible para cargar.");
                    }
                }catch(Exception e){

                }*/
            }
        }            
            
        dataoutput.writeUTF(CifDes.cifrarInformacion("0"));
        ultimaCantidadDeCombustibleConocida(datainput.readUTF(), tipo, nombreEmpresa);
    }
    
    private static int ultimaCantidadDeCombustibleRecibida(String tipo, String nombreEmpresa) throws IOException, InvalidAlgorithmParameterException{
        File UIFile = new File(tipo + "-" + nombreEmpresa + "-cantidadCombustible.txt");
        if (UIFile.exists()) {
            BufferedReader inputStream = new BufferedReader(new FileReader(UIFile.getAbsoluteFile()));
            String count;
            while ((count = inputStream.readLine()) != null) {
                inputStream.close();
                UIFile.getAbsoluteFile().delete();
                return Integer.parseInt(CifDes.descifrarInformacion(count));
            }
            inputStream.close();
            UIFile.getAbsoluteFile().delete();
        }
        else{
            
        }
        return 0;
    }
    
    private static void ultimaCantidadDeCombustibleConocida(String cantidad, String tipo, String nombreEmpresa) throws IOException{
        File UIFile = new File(tipo + "-" + nombreEmpresa + "-cantidadCombustible.txt");
        if (!UIFile.exists()) {
            UIFile.createNewFile();
        }
        FileWriter filewriter = new FileWriter(UIFile.getAbsoluteFile());
        BufferedWriter oS = new BufferedWriter(filewriter);
        PrintWriter outputStream = new PrintWriter(oS);
        outputStream.print(cantidad + "\n");
        outputStream.flush();
        outputStream.close();
    }
    

    /**
     * Sirve para ingresar el tipo de combustible a utilizar.
     * @return la opcion ingresada, dentro de la variable bandera.
     */
    public static String inicio(){
        Scanner scanner = new Scanner(System.in);
        String bandera = "-1";
        while (bandera.compareTo("93") != 0 && bandera.compareTo("95") != 0 && bandera.compareTo("97") != 0 
                && bandera.compareTo("Diesel") != 0 && bandera.compareTo("Kerosene") != 0 && bandera.compareTo("0") != 1){
            System.out.println("\nBienvenido al distribuidor de combustible\n"
                    + "Ingrese una opcion correspondiente al tipo de combustible a distribuir:\n"
                    + "1) 93\n"
                    + "2) 95\n"
                    + "3) 97\n"
                    + "4) Diesel\n"
                    + "5) Kerosene\n"
                    + "0) Salir\n"
                    + "Ingrese su opcion: ");
            bandera = scanner.nextLine();
            switch(bandera){
                case "1":
                    bandera = "93";
                    break;
                case "2":
                    bandera = "95";
                    break;
                case "3":
                    bandera = "97";
                    break;
                case "4":
                    bandera = "Diesel";
                    break;
                case "5":
                    bandera = "Kerosene";
                    break;
                case "0":
                    break;
                default:
                    bandera = "-1";
                    break;
            }
        }
        return bandera;
    }
    

    /**
     * Sirve para conectar a la estacion de servicio deseada
     * @return El numero de puerto segun la opcion determinada.
     */
    public static String conectarAEmpresa(){
        Scanner scanner = new Scanner(System.in);
        String bandera = "-1";
        while (bandera.compareTo("59898") != 0 && bandera.compareTo("49898") != 0 && bandera.compareTo("39898") != 0){
            System.out.println("\nBienvenido al distribuidor de combustible\n"
                    + "Ingrese una opcion correspondiente a la empresa a conectar:\n"
                    + "1) Santiago\n"
                    + "2) Curico\n"
                    + "3) Talca\n"
                    + "Ingrese su opcion: ");
            bandera = scanner.nextLine();
            switch(bandera){
                case "1":
                    bandera = "59898";
                    break;
                case "2":
                    bandera = "49898";
                    break;
                case "3":
                    bandera = "39898";
                    break;
                default:
                    bandera = "-1";
                    break;
            }
        }
        return bandera;
    }

    /**
     * Menu de opciones
     * @return la opcion deseada segun el menu.
     */
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
