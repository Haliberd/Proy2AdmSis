/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package empresacombustible;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

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
    
    public ConexionBD()
    {
        url = "jdbc:postgresql://localhost:5432/Prueba";
        usuario = "postgres";
        password = "1234";

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
            conexion.close();
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
            conexion.close();
            return resultado;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
