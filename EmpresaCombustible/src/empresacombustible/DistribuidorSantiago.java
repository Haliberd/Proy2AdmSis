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
  
    public DistribuidorSantiago()
    {
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
        ConexionBD conexion = new ConexionBD();
        /*
        String consultaSQL = "INSERT INTO public.servidor (código, nombre_s, habdescarga, velocidad) VALUES (122, 'Prueba', false, 0)";
        int respuesta = conexion.consultaInsertar(consultaSQL);
        if(respuesta > 0){
            System.out.println("Inserción realizada con éxito!");
        }
        else{
            System.out.println("Ha fracasado la inserción!");
        }
        
        String consultaSQL = "SELECT * FROM SERVIDOR";
        ResultSet resultado = conexion.consultaBusqueda(consultaSQL);
        
        try {
            while(resultado.next()){
                int codigo = resultado.getInt("código");
                String nombre = resultado.getString("nombre_s");
                boolean habdescarga = resultado.getBoolean("habdescarga");
                int velocidad = resultado.getInt("velocidad");
                System.out.println(codigo+" "+nombre+" "+habdescarga+" "+velocidad);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        
        DistribuidorSantiago DSantiago = new DistribuidorSantiago();
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
                    System.out.println(mensaje);
                    
                    // HACER UN FOR QUE VERIFIQUE SI UNA SENTENCIA X ESTABA DENTRO DEL STRING, PARA GENERAR LAS CONSULTAS DESDE LOS SURTIDORES
                    String[] msjeSplit = mensaje.split("-");
                    
                    if(msjeSplit[0].equals("Cambio precio"))
                    {
                        int precio = Integer.parseInt(msjeSplit[2]);
                        if(msjeSplit[1].equals("93"))
                        {
                            output.writeUTF("Cambiando el precio del combustible 93...");
                            output.writeUTF("El precio actual es $"+precio93);
                            precio93 = precio;
                            output.writeUTF("El nuevo precio es $"+precio93);
                        }
                        else if(msjeSplit[1].equals("95"))
                        {
                            output.writeUTF("Cambiando el precio del combustible 95...");
                            output.writeUTF("El precio actual es $"+precio95);
                            precio95 = precio;
                            output.writeUTF("El nuevo precio es $"+precio95);
                        }
                        else if(msjeSplit[1].equals("97"))
                        {
                            output.writeUTF("Cambiando el precio del combustible 97...");
                            output.writeUTF("El precio actual es $"+precio97);
                            precio97 = precio;
                            output.writeUTF("El nuevo precio es $"+precio97);
                        }
                        else if(msjeSplit[1].equals("Diesel"))
                        {
                            output.writeUTF("Cambiando el precio del combustible Diesel...");
                            output.writeUTF("El precio actual es $"+precioDiesel);
                            precioDiesel = precio;
                            output.writeUTF("El nuevo precio es $"+precioDiesel);
                        }
                        else if(msjeSplit[1].equals("Kerosene"))
                        {
                            output.writeUTF("Cambiando el precio del combustible Kerosene...");
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
}
