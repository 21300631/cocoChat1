package conectores;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelos.Grupos;

public class Grupos_Controller extends Base_Datos {
    
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
}
