package conectores;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelos.Tokens_Recuperacion;

public class Tokens_Recuperacion_Controller extends Base_Datos
{
    public Tokens_Recuperacion_Controller()
    {
        super();
    }

    /*
        --executeUpdate (para las modificaciones de una tabla) [Insert, Update, Delete]
        --executeQuery (para las consultas de una tabla) [Select]
        --execute (para las que modifican la estructura de la base de datos)
    */

    // Método para agregar un usuario
    public void add(Tokens_Recuperacion TokensRec)
    {
        Connection conn = super.getREF();
        
        // Verificar si la conexión es nula
        if (conn == null) 
        {
            System.err.println("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
            return;
        }

        // Preparar la consulta SQL para insertar un nuevo usuario
        String sql = "Insert into TokensRec(Token, FechaCreacion, FechaExpiracion, Usado, IPsolicitud " +
                     " values(?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setString(1, TokensRec.getToken());
            ps.setObject(2, TokensRec.getFechaCreacion());
            ps.setObject(3, TokensRec.getFechaExpiracion());
            ps.setBoolean(4, TokensRec.isUsado());
            ps.setString(5, TokensRec.getIPsolicitud());
            ps.executeUpdate();
            
            System.out.println("Usuario agregado correctamente");
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(Usuario_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}