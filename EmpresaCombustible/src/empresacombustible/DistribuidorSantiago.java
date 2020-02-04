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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Javiera Méndez
 */
public class DistribuidorSantiago 
{
    private Runnable TCliente;
    private Runnable TServidor;
    private int precio93, precio95, precio97, precioDiesel, precioKerosene;
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
        TCliente = new ThreadCliente();
        TServidor = new ThreadServidor();
        
        Thread c = new Thread(TCliente);
        Thread s = new Thread(TServidor);
        c.start();
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
                    System.out.println("Se ha desconectado al cliente");
                    
                }
            } catch (IOException ex) {
                Logger.getLogger(DistribuidorSantiago.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void consultaCambioPrecio(String combustible, int precio)
    {  
        String consultaSQL = "UPDATE EstacionDeServicio SET "+combustible+"="+precio
                +" WHERE nombre = 'Santiago'";
        //System.out.println(consultaSQL);
        int respuesta = conexion.consultaCambioPrecio(consultaSQL);
        if(respuesta > 0){
            System.out.println("Cambio de "+combustible+" realizado con éxito!");
        }
        else{
            System.out.println("Ha fracasado la inserción!");
        }
    }
}
