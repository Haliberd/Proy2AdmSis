/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package empresacombustible;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Javiera Méndez
 */
public class EstacionServicio 
{
    private static String nombre, url, usuario, password;
    private static int puertoServidorEmpresa, puertoServidorSurtidores;
    private Runnable TServidor, TLocal, TRecibidor;
    private static int precio93, precio95, precio97, precioDiesel, precioKerosene;
    private static double factorUtilidad;
    private static ConexionBD conexion;
    private static GeneradorArchivos generador;
  
    /**
     * Permite crear la conexión hacia la BD de la distribuidora
     * Además, inicia los hilos de ejecución.
     * @param nombreEstacion nombre de la estación de servicio.
     * @param url URL de la base de datos de la distribuidora.
     * @param usuario usuario de la base de datos.
     * @param password contraseña del usuario de la BD
     * @param puertoEmpresa puerto en el cual se conecta con la empresa.
     * @param puertoSurtidores puerto en cual se conecta con sus surtidores.
     */
    public EstacionServicio(String nombreEstacion, String url, String usuario, String password, int puertoEmpresa, int puertoSurtidores)
    {
        this.nombre = nombreEstacion;
        this.url = url;
        this.usuario = usuario;
        this.password = password;
        this.puertoServidorEmpresa = puertoEmpresa;
        this.puertoServidorSurtidores = puertoSurtidores;
        
        conexion = new ConexionBD(url, usuario, password, nombreEstacion);
        generador = new GeneradorArchivos();

        TServidor = new ListenerEmpresa();
        TLocal = new ThreadLocal();
        TRecibidor = new ThreadRecibidor();
        
        Thread l = new Thread(TLocal);
        Thread s = new Thread(TServidor);
        Thread t = new Thread(TRecibidor);
        t.start();
        l.start();
        s.start();
    }
    
    /**
     * Menú de opciones que permite ejecutar la aplicación de una distribuidora.
     * @param args 
     */
    public static void main(String[] args)
    {
        try {
            int opcion = -1;
            boolean inicioEstacion = false;
            while((opcion != 0) && (inicioEstacion == false))
            {
                System.out.println("- BIENVENIDOS -\n" + 
                        "1) EJECUTAR Estación de Servicio Santiago\n" +
                        "2) EJECUTAR Estación de Servicio Curicó\n" +
                        "3) EJECUTAR Estación de Servicio Talca\n" +
                        "0) Salir\n" +
                        "Ingrese su opción: ");
                Scanner s = new Scanner(System.in);
                
                try {
                    opcion = s.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Opción ingresada no válida.");
                    s = new Scanner(System.in);
                }
                
                String nombre, url, usuario, password;
                int puertoEmpresa, puertoSurtidores;
                switch (opcion) {
                    case 1:
                    {
                        nombre = "Santiago";
                        url = "jdbc:postgresql://localhost:5432/BDSantiago";
                        usuario = "postgres";
                        password = "1234";
                        puertoEmpresa = 55500;
                        puertoSurtidores = 59898;
                        EstacionServicio Santiago = new EstacionServicio(nombre, url, usuario, password, puertoEmpresa, puertoSurtidores);
                        System.out.println("Iniciando Estacion de Servicio Santiago...");
                        consultaFactorPrecios();
                        inicioEstacion = true;
                        break;
                    }
                    case 2:
                    {
                        nombre = "Curico";
                        url = "jdbc:postgresql://localhost:5432/BDCurico";
                        usuario = "postgres";
                        password = "1234";
                        puertoEmpresa = 45500;
                        puertoSurtidores = 49898;
                        EstacionServicio Curico = new EstacionServicio(nombre, url, usuario, password, puertoEmpresa, puertoSurtidores);
                        System.out.println("Iniciando Estacion de Servicio Curicó...");
                        consultaFactorPrecios();
                        inicioEstacion = true;
                        break;
                    }
                    case 3:
                    {
                        nombre = "Talca";
                        url = "jdbc:postgresql://localhost:5432/BDTalca";
                        usuario = "postgres";
                        password = "1234";
                        puertoEmpresa = 35500;
                        puertoSurtidores = 39898;
                        EstacionServicio Talca = new EstacionServicio(nombre, url, usuario, password, puertoEmpresa, puertoSurtidores);
                        System.out.println("Iniciando Estacion de Servicio Talca...");
                        consultaFactorPrecios();
                        inicioEstacion = true;
                        break;
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
     * Es el menú de la distribuidora
     * Permite cambiar el factor de utilidad de la misma y/o cargar combustible.
     */
    class ThreadLocal implements Runnable{
        
        @Override
        public void run() {
            
            try {
                int opcion = -1;
                while(opcion != 0)
                {
                    Scanner s = new Scanner(System.in);
                    System.out.println("-MENÚ-\n" +
                            "1)Cambiar Factor de Utilidad\n" +
                            "2)Cargar Combustible\n" +
                            "0)Salir\n" +
                            "Ingrese su opción: ");

                    try {
                        opcion = s.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Opción ingresada no válida.");
                        s = new Scanner(System.in);
                    }

                    switch(opcion)
                    {
                        case 1:
                        {
                            s = new Scanner(System.in);
                            System.out.println("Ingrese el nuevo factor de utilidad (incluyendo decimales, por ejemplo: 0,01): ");

                            try {
                                double factor = s.nextDouble();
                                consultaModificarFactorUtilidad(factor);
                            } catch (InputMismatchException e) {
                                System.out.println("Valor ingresado no es válido.");
                                s = new Scanner(System.in);
                            }
                            break;
                        }
                        case 2:
                        {
                            s = new Scanner(System.in);
                            String resultado = "";
                            while(resultado.length() == 0){
                                try{
                                    String tipo = tipoCombustible();
                                    System.out.println("Ingrese la cantidad de combustible a cargar(ingrese -1 para volver al menu anterior)");
                                    resultado = s.nextLine();
                                    if(Integer.parseInt(resultado) >= 0){
                                        consultaCargarCombustible(tipo, Integer.parseInt(resultado));}
                                    else if(Integer.parseInt(resultado) == -1){}
                                    else{
                                        throw new Exception("Numero negativo inválido");
                                    }
                                }
                                catch (Exception e){
                                    System.out.println("Por favor, ingrese un número válido");
                                    resultado = "";
                                }
                            }
                            break;
                        }
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("Valor ingresado no es válido.");
            }
        }      
    }
    
    /*
    * Clase encargada de recibir las conexiones de los Surtidores y generar un Thread para estos.
    */
    class ThreadRecibidor implements Runnable{

        @Override
        public void run() 
        {
            try (ServerSocket listener = new ServerSocket(puertoServidorSurtidores))
            {
                ExecutorService pool = Executors.newFixedThreadPool(6);
                while (true) 
                {
                    pool.execute(new ListenerSurtidor(listener.accept()));
                }
            } catch (IOException ex) {
                Logger.getLogger(EstacionServicio.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /*
    * Clase encargada de almacenar los movimientos de los Surtidores en la base de datos de la estacion.
    */
    class ListenerSurtidor implements Runnable
    {
        private Socket socket;

        ListenerSurtidor(Socket socket)
        {
            this.socket = socket;
        }

        @Override
        public void run()
        {
            //System.out.println("Connected: " + socket);
            String bandera = "9";
            try {
                DataInputStream in = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                while (bandera.compareTo("0") != 0) {
                    bandera = in.readUTF();
                    String[] argumentos = bandera.split("-");
                    if(argumentos.length == 2){
                        String resultado = Integer.toString(consultaCargaCombustible(argumentos[0], argumentos[1]));
                        out.writeUTF(resultado);
                    }
                    else{//Para finalizar la conexion
                        System.out.println("Closed: " + socket);
                        socket.close();
                    }
                }
            } catch (Exception e) {
                System.out.println("Error:" + socket);
                System.out.println(e.getMessage());
            } /*finally {
                try { socket.close();
            } catch (IOException e) {}
                System.out.println("Closed: " + socket);
            }*/
        }
    }
    
    /**
     * Thread que siempre está escuchando las posibles consultas que se pueden 
     * efectuar desde la empresa
     * Además, una vez recibida la consulta, envía la información solicitada hacia
     * la empresa (Sockets TCP).
     */
    class ListenerEmpresa implements Runnable
    {
        @Override
        public void run() 
        {
            ServerSocket servidor = null;
            Socket socket = null;
            DataInputStream input;
            DataOutputStream output; 
            
            try { 
                servidor = new ServerSocket(puertoServidorEmpresa);
                //System.out.println("¡Servidor Estación de Servicio "+nombre+" INICIADO!");
                while(true)
                {
                    socket = servidor.accept();
                    input = new DataInputStream(socket.getInputStream());
                    output = new DataOutputStream(socket.getOutputStream());
                    
                    String mensaje = input.readUTF();

                    String[] msjeSplit = mensaje.split("-");
                    if(msjeSplit[0].equals("Informacion"))
                    {
                        if(msjeSplit[1].equals("Ventas"))
                        {
                            DataOutputStream msje = new DataOutputStream(socket.getOutputStream());  
                            byte[] b;
                            int tamanoArchivo = consultaInfoVentas();
                            msje.writeUTF(Integer.toString(tamanoArchivo));
                            FileInputStream archivo = new FileInputStream("informacion_Ventas.txt");
                            b = new byte[tamanoArchivo];
                            archivo.read(b, 0, b.length);
                            OutputStream outputS = socket.getOutputStream();
                            outputS.write(b, 0, b.length);
                        }
                        else if(msjeSplit[1].equals("Surtidores"))
                        {
                            DataOutputStream msje = new DataOutputStream(socket.getOutputStream());  
                            byte[] b;
                            int tamanoArchivo = consultaInfoSurtidores();
                            msje.writeUTF(Integer.toString(tamanoArchivo));
                            FileInputStream archivo = new FileInputStream("informacion_Surtidores.txt");
                            b = new byte[tamanoArchivo];
                            archivo.read(b, 0, b.length);
                            OutputStream outputS = socket.getOutputStream();
                            outputS.write(b, 0, b.length);
                        }
                    }
                    else if(msjeSplit[0].equals("Cambio precio"))
                    {
                        int precio = Integer.parseInt(msjeSplit[2]);
                        if(msjeSplit[1].equals("93"))
                        {
                            consultaCambioPrecio("precio93", precio);
                            output.writeUTF("El precio actual es $"+precio93);
                            precio93 = precio + (int) (precio* factorUtilidad);
                            output.writeUTF("El nuevo precio es $"+precio93);
                        }
                        else if(msjeSplit[1].equals("95"))
                        {
                            consultaCambioPrecio("precio95", precio);
                            output.writeUTF("El precio actual es $"+precio95);
                            precio95 = precio + (int) (precio* factorUtilidad);
                            output.writeUTF("El nuevo precio es $"+precio95);
                        }
                        else if(msjeSplit[1].equals("97"))
                        {
                            consultaCambioPrecio("precio97", precio);
                            output.writeUTF("El precio actual es $"+precio97);
                            precio97 = precio + (int) (precio* factorUtilidad);
                            output.writeUTF("El nuevo precio es $"+precio97);
                        }
                        else if(msjeSplit[1].equals("Diesel"))
                        {
                            consultaCambioPrecio("precioDiesel", precio);
                            output.writeUTF("El precio actual es $"+precioDiesel);
                            precioDiesel = precio + (int) (precio* factorUtilidad);
                            output.writeUTF("El nuevo precio es $"+precioDiesel);
                        }
                        else if(msjeSplit[1].equals("Kerosene"))
                        {
                            consultaCambioPrecio("precioKerosene", precio);
                            output.writeUTF("El precio actual es $"+precioKerosene);
                            precioKerosene = precio + (int) (precio* factorUtilidad);
                            output.writeUTF("El nuevo precio es $"+precioKerosene);
                        }
                    }
                    output.writeUTF("Fin");
                    socket.close();
                    //System.out.println("Se ha desconectado a la empresa");
                }
            } catch (IOException ex) {
                Logger.getLogger(EstacionServicio.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Funcion encargada de asignar la columna de precio combustible correspondiente segun el tipo de combustible
     * @param tipo hace referencia al tipo de combustible que se desea cargar
     * @param cantidad hace referencia a la cantidad que se desea cargar
     * @return existen 3 posibles casos, la variable cantidad si es que se pudo realizar la carga correctamente
     *         la diferencia entre la cantidad actual de combustible y la cantidad deseada (lo cual resulta en un numero negativo)
     *         si es que no hay suficiente combustible para realizar la carga.
     *         0 si es que no hay combustible.
     */
    public int consultaCargaCombustible(String tipo, String cantidad){
        String tipo_columna = "";
        switch(tipo){
            case ("93"):
                tipo_columna = "'Precio93'";
                break;
            case ("95"):
                tipo_columna = "'Precio95'";
                break;
            case ("97"):
                tipo_columna = "'Precio97'";
                break;
            case ("petroleo"):
                tipo_columna = "'PrecioDiesel'";
                break;
            case ("kerosene"):
                tipo_columna = "'PrecioKerosene'";
                break;
        }
        String consultaSQL = "SELECT litros_disponibles( " + tipo_columna + "::varchar(45), " + tipo + "::varchar(45), " + Integer.parseInt(cantidad) + ");";
        int resultado = conexion.consultaFuncion(consultaSQL);
        return resultado;
    }
    
    /**
     * Permite modificar los precios del combustible en la BD de la distribuidora.
     * @param combustible tipo de combustible al cual se le modificará el precio
     * @param precio nuevo precio que se asignará al tipo de combustible.
     */
    public void consultaCambioPrecio(String combustible, int precio)
    {  
        precio = precio + (int) (precio * factorUtilidad);
        String consultaSQL = "UPDATE EstacionDeServicio SET "+combustible+"="+precio
                +" WHERE nombre = '"+nombre+"'";
        //System.out.println(consultaSQL);
        int respuesta = conexion.consultaModificar(consultaSQL);
        if(respuesta > 0){
            System.out.println("Cambio de "+combustible+" realizado con éxito!");
        }
        else{
            System.out.println("Ha fracasado la modificación!");
        }
    }
    
    /**
     * Permite modificar el factor de utilidad en la BD de la distribuidora.
     * @param factor nuevo valor que se le asignará al factor de utilidad.
     */
    public static void consultaModificarFactorUtilidad(double factor)
    {
        factorUtilidad = factor;
        String consultaSQL = "UPDATE EstacionDeServicio SET factorUtilidad = "+factorUtilidad
                +" WHERE nombre = '"+nombre+"'";
        //System.out.println(consultaSQL);
        int respuesta = conexion.consultaModificar(consultaSQL);
        if(respuesta > 0){
            System.out.println("Cambio de factor de utilidad realizado con éxito!");
            consultaAplicarFactorUtilidad();
        }
        else{
            System.out.println("Ha fracasado la modificación!");
        }
    }
    
    /**
     * Carga combustible a la estacion deseada.
     * @param tipo hace referencia al tipo de combustible que se desea cargar
     * @param cantidad hace referencia a la cantidad de combustible a cargar
     */
    public void consultaCargarCombustible(String tipo, int cantidad){
        String consultaSQL = "UPDATE Surtidor SET LitrosDisponibles = " + cantidad +
                "WHERE tipo = '" + tipo + "'";
        int respuesta = conexion.consultaModificar(consultaSQL);
        if(respuesta > 0){
            System.out.println("Carga de combustible realizada con éxito!");
        }
        else{
            System.out.println("Ha fracasado la modificación!");
        }
    }
    
    /**
     * Menu para seleccionar el tipo de combustible
     * @return la opcion del tipo de combustible
     */
    public static String tipoCombustible(){
        Scanner scanner = new Scanner(System.in);
        String bandera = "-1";
        while (bandera.compareTo("93") != 0 && bandera.compareTo("95") != 0 && bandera.compareTo("97") != 0 
                && bandera.compareTo("Diesel") != 0 && bandera.compareTo("Kerosene") != 0 && bandera.compareTo("0") != 1){
            System.out.println("\n-Bienvenido al distribuidor de combustible-\n"
                    + "Ingrese el surtidor al cual desea cargar combustible:\n"
                    + "1) 93\n"
                    + "2) 95\n"
                    + "3) 97\n"
                    + "4) petroleo\n"
                    + "5) kerosene\n"
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
     * Permite aplicar el factor de utilidad a los distintos precios de los 
     * combustibles tanto dentro de la aplicación como en la BD de la distribuidora.
     */
    public static void consultaAplicarFactorUtilidad()
    {
        precio93 = precio93 + (int)(precio93 * factorUtilidad);
        precio95 = precio95 + (int)(precio95 * factorUtilidad);
        precio97 = precio97 + (int)(precio97 * factorUtilidad);
        precioDiesel = precioDiesel + (int)(precioDiesel * factorUtilidad);
        precioKerosene = precioKerosene + (int)(precioKerosene * factorUtilidad);
       
        String consultaSQL = "UPDATE EstacionDeServicio SET precio93 = "+precio93+
                " ,precio95 = "+precio95+
                " ,precio97 = "+precio97+
                " ,precioDiesel = "+precioDiesel+
                " ,precioKerosene = "+precioKerosene+
                " WHERE nombre = '"+nombre+"'";
        //System.out.println(consultaSQL);
        int respuesta = conexion.consultaModificar(consultaSQL);
        if(respuesta > 0){
            System.out.println("Factor de utilidad aplicado a los precios Estacion de Servicio "+nombre);
        }
        else{
            System.out.println("No se ha podido aplicar el factor de utilidad a los precios!");
        }
    }
     
    /**
    * Carga los distintos precios y el factor de utilidad almacenados en la BD
    * en la aplicación de la distribuidora (se cargan en la aplicación solo para
    * poder efectuar procesos pequeños y respuestas hacia la empresa, por ejemplo:
    * precio actual vs nuevo precio.).
    */
    public static void consultaFactorPrecios(){
        String consultaSQL = "SELECT factorutilidad, precio93, precio95, precio97, preciodiesel, preciokerosene FROM estaciondeservicio"
                + " WHERE nombre = '"+nombre+"'";
        ResultSet informacion = conexion.consultaBusqueda(consultaSQL);
        try {
            while(informacion.next()){
                factorUtilidad = informacion.getDouble("factorutilidad");
                precio93 = informacion.getInt("precio93");
                precio95 = informacion.getInt("precio95");
                precio97 = informacion.getInt("precio97");
                precioDiesel = informacion.getInt("precioDiesel");
                precioKerosene = informacion.getInt("precioKerosene");
                //System.out.println("FactorUtilidad: "+factorUtilidad);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Permite tanto generar una consulta a la BD y obtener la información 
     * Como llamar al método para generar un archivo con la información obtenida
     * (Reporte Ventas)
     * @return tamaño en bytes del archivo creado.
     */
    public static int consultaInfoVentas()
    {
        String consultaSQL = "SELECT refsurtidor as Surtidor, sum(precio) AS valorTotal, sum(litros) as litrosVendidos FROM Ventas "+
                            "GROUP BY refsurtidor";
        ResultSet informacion = conexion.consultaBusqueda(consultaSQL);
        
        int tamanoArchivo = generador.generarInfoVentas(informacion);
        return tamanoArchivo;
    }
    
    /**
     * Permite tanto generar una consulta a la BD y obtener la información 
     * Como llamar al método para generar un archivo con la información obtenida
     * (Reporte Surtidores)
     * @return tamaño en bytes del archivo creado
     */
    public static int consultaInfoSurtidores()
    {
        String consultaSQL = "SELECT nombre, tipo, precio, litrosconsumidos, litrosdisponibles, cargasrealizadas FROM Surtidor";
        ResultSet informacion = conexion.consultaBusqueda(consultaSQL);
        
        int tamanoArchivo = generador.generarInfoSurtidores(informacion);
        return tamanoArchivo;
    }
}
