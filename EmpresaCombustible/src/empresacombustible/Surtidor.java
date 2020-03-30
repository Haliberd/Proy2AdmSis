/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package empresacombustible;

import static empresacombustible.EmpresaCombustible.switchCaseCombustible;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author Maximo Hernandez
 */

public class Surtidor {

    private static CifradoDescifrado CifDes;
    /**
     * Metodo main encargado de inicializar el Surtidor, y mantener corriendo este mismo.
     * @param args. No son necesarios.
     * @throws IOException
     */
    public static void main(String[] args) throws IOException{
        CifDes = new CifradoDescifrado();
        try {
            int puertoSocket = Integer.parseInt(conectarAEmpresa());
            String tipo = inicio();
            Socket socket = new Socket("localhost", puertoSocket);
            DataInputStream datainput = new DataInputStream(socket.getInputStream());
            DataOutputStream dataoutput = new DataOutputStream(socket.getOutputStream());
            String bandera = "9";
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
                        }
                        else if (valorResultado > 0){
                            System.out.println("Carga exitosa. Se cargaron " + (valorResultado) + " litros.");
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
            dataoutput.writeUTF("0");
        }catch(Exception e){
                
        }
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
