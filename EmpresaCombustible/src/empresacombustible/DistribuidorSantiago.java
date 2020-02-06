/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package empresacombustible;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
public class DistribuidorSantiago 
{
    private static String nombre = "Santiago";
    //private Runnable TCliente;
    private Runnable TServidor;
    private Runnable TLocal;
    private Runnable TRecibidor;
    private static int precio93, precio95, precio97, precioDiesel, precioKerosene;
    private static double factorUtilidad;
    private static ConexionBD conexion;
  
    public DistribuidorSantiago()
    {
        String url = "jdbc:postgresql://localhost:5432/BDSantiago";
        String usuario = "postgres";
        String password = "1234";
        conexion = new ConexionBD(url, usuario, password);
        
        
        this.precio93 = 900;
        this.precio95 = 901;
        this.precio97 = 902;
        this.precioDiesel = 600;
        this.precioKerosene = 300;
        this.factorUtilidad = 0.01;
        //TCliente = new ThreadCliente();
        TServidor = new ThreadServidor();
        TLocal = new ThreadLocal();
        TRecibidor = new ThreadRecibidor();
        
        Thread l = new Thread(TLocal);
        //Thread c = new Thread(TCliente);
        Thread s = new Thread(TServidor);
        Thread t = new Thread(TRecibidor);
        t.start();
        l.start();
        //c.start();
        s.start();
    }
    
    public static void main(String[] args)
    {
        //ConexionBD conexion = new ConexionBD();
        
        /*
        String consultaSQL = "INSERT INTO public.servidor (código, nombre_s, habdescarga, velocidad) VALUES (122, 'Prueba', false, 0)";
        int respuesta = conexion.consultaInsertar(consultaSQL);
        if(respuesta > 0){
            System.out.println("Inserción realizada con éxito!");
        }
        else{
            System.out.println("Ha fracasado la inserción!");
        }*/
        
        DistribuidorSantiago DSantiago = new DistribuidorSantiago();
        
        /*
        String consultaSQL = "SELECT * FROM surtidor";
        ResultSet resultado = conexion.consultaBusqueda(consultaSQL);
        
        try {
            while(resultado.next()){
                String nombre = resultado.getString("nombre");
                String tipo = resultado.getString("tipo");
                int precio = resultado.getInt("precio");
                float litrosConsumidos = resultado.getFloat("litrosconsumidos");
                float litrosDisponibles = resultado.getFloat("litrosdisponibles");
                int cargas = resultado.getInt("cargasrealizadas");
                String refEstacion = resultado.getString("refestacion");
                System.out.println(nombre+" "+tipo+" "+precio+" "+litrosConsumidos+" "+litrosDisponibles+" "+cargas+" "+refEstacion);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        
        String consultaSQL = "SELECT * FROM EstacionDeServicio WHERE nombre = 'Santiago'";
        ResultSet resultado = conexion.consultaBusqueda(consultaSQL);
        
        try {
            while(resultado.next()){
                nombre = resultado.getString("nombre");
                factorUtilidad = resultado.getDouble("factorutilidad");
                precio93 = resultado.getInt("precio93");
                precio95 = resultado.getInt("precio95");
                precio97 = resultado.getInt("precio97");
                precioDiesel = resultado.getInt("preciodiesel");
                precioKerosene = resultado.getInt("preciokerosene");
                System.out.println(nombre+" "+factorUtilidad+" "+precio93+" "+precio95+" "+precio97+" "+precioDiesel+" "+precioKerosene);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }
    
    public static void modificarFactorUtilidad(double factor)
    {
        factorUtilidad = factor;
        String consultaSQL = "UPDATE EstacionDeServicio SET factorUtilidad = "+factorUtilidad
                +" WHERE nombre = 'Santiago'";
        System.out.println(consultaSQL);
        int respuesta = conexion.consultaModificar(consultaSQL);
        if(respuesta > 0){
            System.out.println("Cambio de factor de utilidad realizado con éxito!");
            aplicarFactorUtilidad();
        }
        else{
            System.out.println("Ha fracasado la modificación!");
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
                    modificarFactorUtilidad(factor);
                }
            }
        }
        
    }
    
    class ThreadCliente implements Runnable
    {    
        @Override
        public void run() 
        {
            /*
            //Dirección del localhost
            final String host = "127.0.0.1";
            final int puerto = 5500;
            DataInputStream input;
            DataOutputStream output;
                       
            try {
                Socket socket = new Socket(host, puerto);
                input = new DataInputStream(socket.getInputStream());
                output = new DataOutputStream(socket.getOutputStream());
                
                output.writeUTF("Mensaje hacia el servidor del distribuidor Santiago");
                
                String mensaje = input.readUTF();
                
                System.out.println(mensaje);
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(DistribuidorSantiago.class.getName()).log(Level.SEVERE, null, ex);
            }
            */
        }
    }
    
    class ThreadRecibidor implements Runnable{

        @Override
        public void run() {
            
            try (ServerSocket listener = new ServerSocket(59898)) {
            System.out.println("El servidor de servicio esta corriendo...");
            ExecutorService pool = Executors.newFixedThreadPool(6);
            while (true) {
                pool.execute(new ListenerSurtidor(listener.accept()));
                }
            } catch (IOException ex) {
                Logger.getLogger(DistribuidorSantiago.class.getName()).log(Level.SEVERE, null, ex);
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
                        consultaCargaCombustible(argumentos[0], argumentos[1]);
                    }
                    //Para continuar la conexion
                    out.writeUTF("hi");
                }
            } catch (Exception e) {
                System.out.println("Error:" + socket);
                System.out.println(e.getMessage());
            } finally {
                try { socket.close();
            } catch (IOException e) {}
                System.out.println("Closed: " + socket);
            }
        }
        
    }
    
    class ThreadServidor implements Runnable
    {
        @Override
        public void run() 
        {
            ServerSocket servidor = null;
            Socket socket = null;
            DataInputStream input;
            DataOutputStream output;
            
            final int puerto = 5500;
            
            try { 
                servidor = new ServerSocket(puerto);
                System.out.println("Servidor Estacion de Servicio Santiago INICIADO");
                while(true)
                {
                    socket = servidor.accept();
                    System.out.println("Se ha conectado la empresa!");
                    input = new DataInputStream(socket.getInputStream());
                    output = new DataOutputStream(socket.getOutputStream());
                    
                    String mensaje = input.readUTF();
                    //System.out.println(mensaje);
                    
                    // HACER UN FOR QUE VERIFIQUE SI UNA SENTENCIA X ESTABA DENTRO DEL STRING, PARA GENERAR LAS CONSULTAS DESDE LOS SURTIDORES
                    String[] msjeSplit = mensaje.split("-");
                    
                    if(msjeSplit[0].equals("Cambio precio"))
                    {
                        int precio = Integer.parseInt(msjeSplit[2]);
                        if(msjeSplit[1].equals("93"))
                        {
                            consultaCambioPrecio("precio93", precio);
                            output.writeUTF("El precio actual es $"+precio93);
                            precio93 = precio;
                            output.writeUTF("El nuevo precio es $"+precio93);
                        }
                        else if(msjeSplit[1].equals("95"))
                        {
                            consultaCambioPrecio("precio95", precio);
                            output.writeUTF("El precio actual es $"+precio95);
                            precio95 = precio;
                            output.writeUTF("El nuevo precio es $"+precio95);
                        }
                        else if(msjeSplit[1].equals("97"))
                        {
                            consultaCambioPrecio("precio97", precio);
                            output.writeUTF("El precio actual es $"+precio97);
                            precio97 = precio;
                            output.writeUTF("El nuevo precio es $"+precio97);
                        }
                        else if(msjeSplit[1].equals("Diesel"))
                        {
                            consultaCambioPrecio("precioDiesel", precio);
                            output.writeUTF("El precio actual es $"+precioDiesel);
                            precioDiesel = precio;
                            output.writeUTF("El nuevo precio es $"+precioDiesel);
                        }
                        else if(msjeSplit[1].equals("Kerosene"))
                        {
                            consultaCambioPrecio("precioKerosene", precio);
                            output.writeUTF("El precio actual es $"+precioKerosene);
                            precioKerosene = precio;
                            output.writeUTF("El nuevo precio es $"+precioKerosene);
                        }
                    }
                    output.writeUTF("Fin");
                    socket.close();
                    System.out.println("Se ha desconectado a la empresa");
                    
                }
            } catch (IOException ex) {
                Logger.getLogger(DistribuidorSantiago.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void consultaCargaCombustible(String tipo, String cantidad){
        String consultaSQL = "SELECT litros_disponibles( " + tipo + "::varchar(45), " + Integer.parseInt(cantidad) + ");";
        conexion.consultaFuncion(consultaSQL);
    }
    
    public void consultaCambioPrecio(String combustible, int precio)
    {  
        String consultaSQL = "UPDATE EstacionDeServicio SET "+combustible+"="+precio
                +" WHERE nombre = 'Santiago'";
        //System.out.println(consultaSQL);
        int respuesta = conexion.consultaModificar(consultaSQL);
        if(respuesta > 0){
            System.out.println("Cambio de "+combustible+" realizado con éxito!");
        }
        else{
            System.out.println("Ha fracasado la modificación!");
        }
    }
    
    /* El factor de utilidad se aplica siempre que se inicia la aplicación, por ende, no es necesario hacer el cambio en la BD
    , pues al volver a cargar la BD volvería a aplicar el factor de utilidad a los precios*/
    public static void aplicarFactorUtilidad()
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
                " WHERE nombre = 'Santiago'";
        //System.out.println(consultaSQL);
        int respuesta = conexion.consultaModificar(consultaSQL);
        if(respuesta > 0){
            System.out.println("Factor de utilidad aplicado a los precios Estacion de Servicio SANTIAGO");
        }
        else{
            System.out.println("No se ha podido aplicar el factor de utilidad a los precios!");
        }
    }
}
