package conectores;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelos.Mensajes_Individuales;

public class Mensajes_Individuales_Controller extends Base_Datos
{
    public Mensajes_Individuales_Controller()
    {
        super();
    }

    /*
        --executeUpdate (para las modificaciones de una tabla) [Insert, Update, Delete]
        --executeQuery (para las consultas de una tabla) [Select]
        --execute (para las que modifican la estructura de la base de datos)
    */

    // Método para agregar un usuario
    public void add(Mensajes_Individuales MensIndv)
    {
        Connection conn = super.getREF();
        
        // Verificar si la conexión es nula
        if (conn == null) 
        {
            System.err.println("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
            return;
        }

        // Preparar la consulta SQL para insertar un nuevo usuario
        String sql = "Insert into HistorialContrasenas(Contenido, FechaEnvio, FechaLeido, EsArchivado" +
                     "values(?, ?, ?, ?)";
        
        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setString(1, MensIndv.getContenido());
            ps.setObject(2, MensIndv.getFechaEnvio());
            ps.setObject(3, MensIndv.getFechaLeido());
            ps.setBoolean(4, MensIndv.isEsArchivado());
            ps.executeUpdate();
            
            System.out.println("Mensaje agregado correctamente");
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(Usuario_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}