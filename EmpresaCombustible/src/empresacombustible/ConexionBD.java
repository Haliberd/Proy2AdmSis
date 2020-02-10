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
    
    /**
     * Permite ejecutar la conexión con una BD en particular (depende del valor
     * de los parámetros del constructor).
     * @param url URL de la base de datos.
     * @param usuario Usuario para poder ingresar a la BD.
     * @param password Contraseña del usuario para poder ingresar a la BD.
     * @param nombre nombre de la distribuidora/estación de servicio a la que 
     * pertenece la BD.
     */
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
    
    /**
     * Permite efectuar una consulta de tipo INSERT a la BD.
     * @param consultaSQL consulta que se ejecutará en la BD.
     * @return sirve para saber si la consulta se generó con éxito o no.
     */
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
    
    /**
     * Permite efectuar una consulta de tipo SELECT a la BD.
     * @param consultaSQL consulta que se ejecutará en la BD
     * @return retorna una tabla con la información solicitada.
     */
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
    
    /**
     * Permite efecutar una consulta tipo UPDATE a la BD.
     * @param consultaSQL consulta que se ejecutará en la BD
     * @return sirve para saber si la consulta se generó con éxito o no.
     */
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
    
    
    /**
    * Funcion que ejecuta funciones de postgresql
    * Para el caso de la funcion litros_disponibles, los resultados posibles son:
    * Un valor positivo mayor a 0 si es que habia suficiente combustible
    * Un valor negativo si es que no habia suficiente combustible
    * 0 si es que no habia combustible para cargar.
    * @param consultaSQL consulta que se ejecutará en la BD.
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
