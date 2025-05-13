package conectores;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

        // Preparar la consulta SQL corregida
        String sql = "INSERT INTO MiembrosGrupo(GrupoID, UsuarioID, FechaUnion, EsAdministrador) " +
                     "VALUES(?, ?, GETDATE(), ?)";
        
        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, MiembrosGrup.getGrupoID());
            ps.setInt(2, MiembrosGrup.getUsuarioID());
            ps.setBoolean(3, MiembrosGrup.isEsAdministrador());
            ps.executeUpdate();
            
            System.out.println("Miembro agregado correctamente");
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(Miembros_Grupo_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Método para contar miembros por grupo
    public int contarMiembrosPorGrupo(int grupoID) {
        Connection conn = super.getREF();
        if (conn == null) {
            System.err.println("Error: Conexión no disponible");
            return -1;
        }

        // Ajustado para usar el nombre correcto de columna
        String sql = "SELECT COUNT(*) AS total FROM MiembrosGrupo WHERE GrupoID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, grupoID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Miembros_Grupo_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
    
    // Método para obtener los miembros de un grupo
    public List<Miembros_Grupo> getMiembrosByGrupo(int grupoID) {
        List<Miembros_Grupo> miembros = new ArrayList<>();
        Connection conn = super.getREF();
        
        if (conn == null) {
            System.err.println("Error: Conexión no disponible");
            return miembros;
        }
        
        String sql = "SELECT * FROM MiembrosGrupo WHERE GrupoID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, grupoID);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Miembros_Grupo miembro = new Miembros_Grupo(
                    rs.getInt("MiembroGrupoID"),
                    rs.getInt("GrupoID"),
                    rs.getInt("UsuarioID"),
                    rs.getTimestamp("FechaUnion").toLocalDateTime().toLocalDate(),
                    rs.getBoolean("EsAdministrador")
                );
                
                miembros.add(miembro);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Miembros_Grupo_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return miembros;
    }
    
    // Método para verificar si un usuario es miembro de un grupo
    public boolean esUsuarioMiembro(int usuarioID, int grupoID) {
        Connection conn = super.getREF();
        
        if (conn == null) {
            System.err.println("Error: Conexión no disponible");
            return false;
        }
        
        String sql = "SELECT COUNT(*) AS total FROM MiembrosGrupo WHERE UsuarioID = ? AND GrupoID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioID);
            ps.setInt(2, grupoID);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("total") > 0;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Miembros_Grupo_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    // Método para eliminar un miembro de un grupo
    public void eliminarMiembro(int usuarioID, int grupoID) {
        Connection conn = super.getREF();
        
        if (conn == null) {
            System.err.println("Error: Conexión no disponible");
            return;
        }
        
        String sql = "DELETE FROM MiembrosGrupo WHERE UsuarioID = ? AND GrupoID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioID);
            ps.setInt(2, grupoID);
            int filasAfectadas = ps.executeUpdate();
            
            if (filasAfectadas > 0) {
                System.out.println("Usuario ID " + usuarioID + " eliminado del grupo ID " + grupoID);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Miembros_Grupo_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}