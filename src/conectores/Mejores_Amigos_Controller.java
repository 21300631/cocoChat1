package conectores;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelos.Mejores_Amigos;

public class Mejores_Amigos_Controller extends Base_Datos {
    
    public Mejores_Amigos_Controller() 
    {
        super();
    }
    
    // Método para añadir un mejor amigo
    public void add(Mejores_Amigos mejorAmigo) 
    {
        try 
        {
            Connection conn = super.getREF();
            if (conn == null) 
            {
                System.err.println("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
                return;
            }
            
            String sql = "INSERT INTO MejoresAmigos(ContactoID, FechaSeleccion, Prioridad) VALUES (?, GETDATE(), ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, mejorAmigo.getContactoID());
            ps.setInt(2, mejorAmigo.getPrioridad());
            ps.executeUpdate();
            
            System.out.println("Mejor amigo añadido correctamente");
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Mejores_Amigos_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Método para obtener la lista de mejores amigos de un usuario
    public List<Mejores_Amigos> getMejores_AmigosByUsuario(int usuarioID) 
    {
        List<Mejores_Amigos> mejores_Amigos = new ArrayList<>();
        try {
            Connection conn = super.getREF();
            if (conn == null) {
                System.err.println("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
                return mejores_Amigos;
            }
            
            String sql = "SELECT ma.* FROM MejoresAmigos ma " +
                         "JOIN Contactos c ON ma.ContactoID = c.ContactoID " +
                         "WHERE c.UsuarioID = ? ORDER BY ma.Prioridad ASC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, usuarioID);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Mejores_Amigos amigo = new Mejores_Amigos();
                amigo.setMejorAmigoID(rs.getInt("MejorAmigoID"));
                amigo.setContactoID(rs.getInt("ContactoID"));
                amigo.setFechaSeleccion(rs.getDate("FechaSeleccion").toLocalDate());
                amigo.setPrioridad(rs.getInt("Prioridad"));
                mejores_Amigos.add(amigo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Mejores_Amigos_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mejores_Amigos;
    }

    // Método para eliminar un mejor amigo
    public void delete(int mejorAmigoID) 
    {
        try {
            Connection conn = super.getREF();
            if (conn == null) {
                System.err.println("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
                return;
            }
            
            String sql = "DELETE FROM MejoresAmigos WHERE MejorAmigoID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, mejorAmigoID);
            ps.executeUpdate();
            
            System.out.println("Mejor amigo eliminado correctamente");
        } catch (SQLException ex) {
            Logger.getLogger(Mejores_Amigos_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}