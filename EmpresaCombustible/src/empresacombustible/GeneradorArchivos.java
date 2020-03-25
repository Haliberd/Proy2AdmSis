/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package empresacombustible;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Javiera Méndez
 */
public class GeneradorArchivos 
{
    /**
     * Permite crear y escribir dentro de una archivo
     * En este caso contiene la información de las ventas generadas en cada 
     * surtidor de una distribuidora.
     * @param informacion contiene la información retornada desde la BD.
     * @return retorna el tamaño en bytes del archivo creado
     */
    public int generarInfoVentas(ResultSet informacion) throws Exception
    {
        int tamanoArchivo = 0;
        try {
            File archivo = new File("informacion_Ventas_Encriptado.txt");
            if(!archivo.exists())
            {
                archivo.createNewFile();
            } 
            PrintWriter pw = new PrintWriter(archivo);
            
            String linea = ("|Surtidor    |"+"Valor Total"+" |Litros Vendidos");
            pw.println(linea);
            try {
                while(informacion.next()){
                    String surtidor = informacion.getString("surtidor");
                    int valorTotal  = informacion.getInt("valortotal");
                    double litrosVendidos = informacion.getDouble("litrosvendidos");
                    linea = "|"+surtidor+"  |"+valorTotal+"        |"+litrosVendidos+" ";
                    pw.println(linea);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            pw.close();
            //System.out.println("Archivo creado con éxito. El tamaño del archivo es: "+archivo.length());
            tamanoArchivo = Math.toIntExact(archivo.length());
        } catch (IOException ex) {
            Logger.getLogger(EstacionServicio.class.getName()).log(Level.SEVERE, null, ex);
        }     
        return tamanoArchivo;
    }
    
    /**
     * Permite crear y escribir dentro de una archivo
     * En este caso contiene la información de los surtidores pertenecientes
     * a una distribuidora en específico.
     * @param informacion contiene la información retornada desde la BD.
     * @return retorna el tamaño en bytes del archivo creado
     */
    public int generarInfoSurtidores(ResultSet informacion)
    {
        int tamanoArchivo = 0;
        try {
            File archivo = new File("informacion_Surtidores_Encriptado.txt");
            if(!archivo.exists())
            {
                archivo.createNewFile();
            } 
            PrintWriter pw = new PrintWriter(archivo);
            
            String linea = ("|EstaciónNº  |"+"Precio "+" |Litros Consumidos "+" |Litros Disponibles "+"|NºCargas"+"  |Tipo");
            //System.out.println(linea);
            pw.println(linea);
            try {
                while(informacion.next()){
                    String nombre = informacion.getString("nombre");
                    String tipo = informacion.getString("tipo");
                    int precio  = informacion.getInt("precio");
                    double litrosConsumidos = informacion.getDouble("litrosconsumidos");
                    double litrosDisponibles = informacion.getDouble("litrosdisponibles");
                    int cargas = informacion.getInt("cargasrealizadas");
                    linea = "|"+nombre+"  |"+precio+"     |"+litrosConsumidos+
                            "                |"+litrosDisponibles+
                            "             |"+cargas+"         |"+tipo;
                    pw.println(linea);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            pw.close();
            //System.out.println("Archivo creado con éxito. El tamaño del archivo es: "+archivo.length());
            tamanoArchivo = Math.toIntExact(archivo.length());
        } catch (IOException ex) {
            Logger.getLogger(EstacionServicio.class.getName()).log(Level.SEVERE, null, ex);
        }     
        return tamanoArchivo;
    }
}
