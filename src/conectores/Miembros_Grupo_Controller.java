package conectores;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelos.Miembros_Grupo;

public class Miembros_Grupo_Controller extends Base_Datos
{
    public Miembros_Grupo_Controller()
    {
        super();
    }

    /*
        --executeUpdate (para las modificaciones de una tabla) [Insert, Update, Delete]
        --executeQuery (para las consultas de una tabla) [Select]
        --execute (para las que modifican la estructura de la base de datos)
    */

    // Método para agregar un usuario
    public void add(Miembros_Grupo MiembrosGrup)
    {
        Connection conn = super.getREF();
        
        // Verificar si la conexión es nula
        if (conn == null) 
        {
            System.err.println("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
            return;
        }

        // Preparar la consulta SQL para insertar un nuevo usuario
        String sql = "Insert into MiembrosGrupo(Nombre, Apellido" +
                     "values(?, ?)";
        
        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setObject(1, MiembrosGrup.getFechaUnion());
            ps.setBoolean(2, MiembrosGrup.isEsAdministrador());
            ps.executeUpdate();
            
            System.out.println("Miembro agregado correctamente");
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(Usuario_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}