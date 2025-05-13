package conectores;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelos.Grupos;

public class Grupos_Controller extends Base_Datos {
    
    private static final Logger LOGGER = Logger.getLogger(Grupos_Controller.class.getName());
    
    public Grupos_Controller() {
        super();
    }
    
    public void add(Grupos grupo) {
        try {
            Connection conn = super.getREF();
            if (conn == null) {
                System.err.println("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
                return;
            }
            
            String sql = "INSERT INTO Grupos(Nombre, Descripcion, CreadorID, FechaCreacion, FotoGrupo) VALUES (?, ?, ?, GETDATE(), ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, grupo.getNombre());
            ps.setString(2, grupo.getDescripcion());
            ps.setInt(3, grupo.getCreadorID());
            ps.setString(4, grupo.getFotoGrupo());
            ps.executeUpdate();
            
            System.out.println("Grupo agregado correctamente");
        } catch (SQLException ex) {
            Logger.getLogger(Grupos_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<Grupos> getGruposByUsuario(int usuarioId) {
        Connection conn = super.getREF();
        List<Grupos> grupos = new ArrayList<>();
        
        if (conn == null) {
            System.err.println("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
            return grupos;
        }
        
        String sql = "SELECT g.* FROM Grupos g " +
                     "INNER JOIN MiembrosGrupo mg ON g.GrupoID = mg.GrupoID " +
                     "WHERE mg.UsuarioID = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Grupos grupo = new Grupos();
                    grupo.setGrupoID(rs.getInt("GrupoID"));
                    grupo.setNombre(rs.getString("Nombre"));
                    grupo.setDescripcion(rs.getString("Descripcion"));
                    
                    if (rs.getObject("FechaCreacion") != null) {
                        java.sql.Timestamp timestamp = rs.getTimestamp("FechaCreacion");
                        if (timestamp != null) {
                            grupo.setFechaCreacion(new java.util.Date(timestamp.getTime()));
                        }
                    }
                    
                    grupo.setFotoGrupo(rs.getString("FotoGrupo"));
                    grupos.add(grupo);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener grupos del usuario: {0}", ex.getMessage());
        }
        
        return grupos;
    }
}
