package conectores;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelos.Credenciales;

public class Credenciales_Controller extends Base_Datos
{
    public Credenciales_Controller()
    {
        super();
    }

    /*
        --executeUpdate (para las modificaciones de una tabla) [Insert, Update, Delete]
        --executeQuery (para las consultas de una tabla) [Select]
        --execute (para las que modifican la estructura de la base de datos)
    */

    // Método para agregar un usuario
    public void add(Credenciales credenciales)
    {
        Connection conn = super.getREF();
        
        // Verificar si la conexión es nula
        if (conn == null) 
        {
            System.err.println("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
            return;
        }

        // Preparar la consulta SQL para insertar un nuevo usuario
        String sql = "Insert into Credenciales(Email, HashContrasenas, Salt, UltimoCambioContrasena, RequiereCambioContrasena, IntentosFallidos, " +
                     "CuentaBloqueada, FechaBloqueo values(?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setString(1, credenciales.getEmail());
            ps.setString(2, credenciales.getHashContrasena());
            ps.setString(3, credenciales.getSalt());
            ps.setObject(4, credenciales.getUltimoCambioContrasena());
            ps.setBoolean(5, credenciales.isRequiereCambioContrasena());
            ps.setInt(6, credenciales.getIntentosFallidos());
            ps.setBoolean(7, credenciales.isCuentaBloqueada());
            ps.setObject(8, credenciales.getFechaBloqueo());
            ps.executeUpdate();
            
            System.out.println("Credenciales agregadas correctamente");
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(Usuario_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}