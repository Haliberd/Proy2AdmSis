/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package empresacombustible;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Máximo Hernández, Javiera Méndez
 */
public class ConexionBD 
{
    private String url;
    private String usuario;
    private String password;
    private Connection conexion;
    
    public ConexionBD(String url, String usuario, String password, String nombre)
    {
        
        this.url = url;
        this.usuario = usuario;
        this.password = password;
        try {
            Class.forName("org.postgresql.Driver");
            conexion = DriverManager.getConnection(url, usuario, password);
            System.out.println("¡Conectado a la base de datos "+nombre+"!");
        } catch (Exception e) {
            e.printStackTrace();
        }        
    }
    
    public int consultaInsertar(String consultaSQL){
        try {
            Statement sentencia = conexion.createStatement();
            int respuesta = sentencia.executeUpdate(consultaSQL);
            //System.out.println(respuesta);
            return respuesta;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public ResultSet consultaBusqueda(String consultaSQL){
        try {
            Statement sentencia = conexion.createStatement();
            ResultSet resultado = sentencia.executeQuery(consultaSQL);
            return resultado;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public int consultaModificar(String consultaSQL){
        try {
            Statement sentencia = conexion.createStatement();
            int respuesta = sentencia.executeUpdate(consultaSQL);
            return respuesta;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    
    //Funcion que ejecuta funciones de postgresql
    /*
    * Para el caso de la funcion litros_disponibles, los resultados posibles son
    * Un valor positivo mayor a 0 si es que habia suficiente combustible.
    * Un valor negativo si es que no habia suficiente combustible.
    * 0 si es que no habia combustible para cargar.
    */
    public int consultaFuncion(String consultaSQL){
        try {
            Statement sentencia = conexion.createStatement();
            ResultSet resultado = sentencia.executeQuery(consultaSQL);
            //Se salta la primera linea, que corresponde a los titulos de las columnas (al parecer)
            resultado.next();
            int nResultado = resultado.getInt(1);
            return nResultado;
        } catch (SQLException ex) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
}
