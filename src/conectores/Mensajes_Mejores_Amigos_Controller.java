package conectores;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelos.Mensajes_Mejores_Amigos;

public class Mensajes_Mejores_Amigos_Controller extends Base_Datos {
    
    public Mensajes_Mejores_Amigos_Controller() 
    {
        super();
    }
    
    // Método para enviar un mensaje a un mejor amigo
    public void add(Mensajes_Mejores_Amigos mensaje) 
    {
        try 
        {
            Connection conn = super.getREF();
            if (conn == null) 
            {
                System.err.println("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
                return;
            }
            
            String sql = "INSERT INTO MensajesMejoresAmigos(MejorAmigoID, RemitenteID, Contenido, FechaEnvio) VALUES (?, ?, ?, GETDATE())";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, mensaje.getMejorAmigoID());
            ps.setInt(2, mensaje.getRemitenteID());
            ps.setString(3, mensaje.getContenido());
            ps.executeUpdate();
            
            System.out.println("Mensaje a mejor amigo enviado correctamente");
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Mensajes_Mejores_Amigos_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Método para obtener los mensajes de un mejor amigo específico
    public List<Mensajes_Mejores_Amigos> getMensajesByMejorAmigo(int mejorAmigoID) 
    {
        List<Mensajes_Mejores_Amigos> mensajes = new ArrayList<>();
        try 
        {
            Connection conn = super.getREF();
            if (conn == null) 
            {
                System.err.println("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
                return mensajes;
            }
            
            String sql = "SELECT * FROM MensajesMejoresAmigos WHERE MejorAmigoID = ? ORDER BY FechaEnvio ASC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, mejorAmigoID);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) 
            {
                Mensajes_Mejores_Amigos mensaje = new Mensajes_Mejores_Amigos();
                mensaje.setMensajeID(rs.getInt("MensajeID"));
                mensaje.setMejorAmigoID(rs.getInt("MejorAmigoID"));
                mensaje.setRemitenteID(rs.getInt("RemitenteID"));
                mensaje.setContenido(rs.getString("Contenido"));
                // Convertir timestamp SQL a LocalDateTime
                mensaje.setFechaEnvio(rs.getTimestamp("FechaEnvio").toLocalDateTime());
                mensajes.add(mensaje);
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Mensajes_Mejores_Amigos_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mensajes;
    }
    
    // Método para eliminar un mensaje
    public boolean deleteMessage(int mensajeID) 
    {
        try 
        {
            Connection conn = super.getREF();
            if (conn == null) 
            {
                System.err.println("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
                return false;
            }
            
            String sql = "DELETE FROM MensajesMejoresAmigos WHERE MensajeID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, mensajeID);
            int rowsAffected = ps.executeUpdate();
            
            return rowsAffected > 0;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Mensajes_Mejores_Amigos_Controller.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
