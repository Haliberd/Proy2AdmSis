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
    private static int puertoServidorEmpresa;
    private static int puertoServidorSurtidores;
    //private Runnable TCliente;
    private Runnable TServidor;
    private Runnable TLocal;
    private Runnable TRecibidor;
    private static int precio93, precio95, precio97, precioDiesel, precioKerosene;
    private static double factorUtilidad;
    private static ConexionBD conexion;
    private static GeneradorArchivos generador;
  
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
    
    public static void main(String[] args)
    {
        
        int opcion = 1;
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
            opcion = s.nextInt();
            String nombre, url, usuario, password;
            int puertoEmpresa, puertoSurtidores;
            switch (opcion) {
                case 1:
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
                case 2:
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
                case 3:
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
                default:
                    break;
            }
        }
    }

    class ThreadLocal implements Runnable{
        
        @Override
        public void run() {
            int opcion = 1;
            while(opcion != 0)
            {
                Scanner s = new Scanner(System.in);
                System.out.println("-MENÚ-\n" +
                        "1)Cambiar Factor de Utilidad\n" +
                        "0)Salir\n" +
                        "Ingrese su opción: ");

                opcion = s.nextInt();
                if(opcion == 1)
                {
                    s = new Scanner(System.in);
                    System.out.println("Ingrese el nuevo factor de utilidad (incluyendo decimales, por ejemplo: 0,01): ");
                    double factor = s.nextDouble();
                    consultaModificarFactorUtilidad(factor);
                }
            }
        }
        
    }
    
    class ThreadRecibidor implements Runnable{

        @Override
        public void run() {
            
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
    
    class ListenerSurtidor implements Runnable{

        private Socket socket;

        ListenerSurtidor(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
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
    
    /* El factor de utilidad se aplica siempre que se inicia la aplicación, por ende, no es necesario hacer el cambio en la BD
    , pues al volver a cargar la BD volvería a aplicar el factor de utilidad a los precios*/
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
    
    public static int consultaInfoVentas()
    {
        String consultaSQL = "SELECT refsurtidor as Surtidor, sum(precio) AS valorTotal, sum(litros) as litrosVendidos FROM Ventas "+
                            "GROUP BY refsurtidor";
        ResultSet informacion = conexion.consultaBusqueda(consultaSQL);
        
        int tamanoArchivo = generador.generarInfoVentas(informacion);
        return tamanoArchivo;
    }
    
    public static int consultaInfoSurtidores()
    {
        String consultaSQL = "SELECT nombre, tipo, precio, litrosconsumidos, litrosdisponibles, cargasrealizadas FROM Surtidor";
        ResultSet informacion = conexion.consultaBusqueda(consultaSQL);
        
        int tamanoArchivo = generador.generarInfoSurtidores(informacion);
        return tamanoArchivo;
    }
}
