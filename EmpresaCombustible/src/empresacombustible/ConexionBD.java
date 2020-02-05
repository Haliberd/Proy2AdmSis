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
    
    public ConexionBD(String url, String usuario, String password)
    {
        
        this.url = url;
        this.usuario = usuario;
        this.password = password;
        try {
            Class.forName("org.postgresql.Driver");
            conexion = DriverManager.getConnection(url, usuario, password);
            System.out.println("Conexión exitosa!");
        } catch (Exception e) {
            e.printStackTrace();
        }        
    }
    
    public int consultaInsertar(String consultaSQL){
        try {
            Statement sentencia = conexion.createStatement();
            int respuesta = sentencia.executeUpdate(consultaSQL);
            System.out.println(respuesta);
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
    
    public void consultaFuncion(String consultaSQL){
        try {
            Statement sentencia = conexion.createStatement();
            sentencia.execute(consultaSQL);
        } catch (SQLException ex) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
