/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package empresacombustible;

//import java.sql.*;
import java.sql.ResultSet;

/**
 *
 * @author Máximo Hernández, Javiera Méndez
 */
public class EmpresaCombustible {

    /**
     * @param args the command line arguments
     */
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
        }*/
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
        }
        
    }
    
}
