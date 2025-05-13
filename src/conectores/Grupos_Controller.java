package conectores;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelos.Grupos;
import modelos.Usuarios;

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
            
            // Consulta modificada para usar los nombres correctos de columnas
            String sql = "INSERT INTO Grupos(Nombre, Descripcion, CreadorID, FechaCreacion, FotoGrupo) VALUES (?, ?, ?, GETDATE(), ?)";
            PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, grupo.getNombre());
            ps.setString(2, grupo.getDescripcion());
            ps.setInt(3, grupo.getCreadorID());
            ps.setString(4, grupo.getFotoGrupo());
            ps.executeUpdate();
            
            // Obtener el ID generado
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                grupo.setGrupoID(generatedKeys.getInt(1));
            }
            
            System.out.println("Grupo agregado correctamente con ID: " + grupo.getGrupoID());
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
        
        // Consulta para obtener grupos a los que pertenece el usuario
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
    
    /**
     * Obtiene el ID del último grupo creado por un usuario específico
     * @param usuarioId ID del usuario creador
     * @return ID del grupo o -1 si ocurrió un error
     */
    public int getUltimoGrupoCreado(int usuarioId) {
        Connection conn = super.getREF();
        
        if (conn == null) {
            LOGGER.severe("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
            return -1;
        }
        
        String sql = "SELECT TOP 1 GrupoID FROM Grupos WHERE CreadorID = ? ORDER BY FechaCreacion DESC";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("GrupoID");
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener último grupo creado: {0}", ex.getMessage());
        }
        
        return -1;
    }
    
    /**
     * Agrega un miembro a un grupo
     * @param grupoId ID del grupo
     * @param usuarioId ID del usuario
     * @param esAdministrador Indica si el usuario será administrador
     * @return true si se agregó correctamente, false en caso contrario
     */
    public boolean agregarMiembroGrupo(int grupoId, int usuarioId, boolean esAdministrador) {
        Connection conn = super.getREF();
        
        if (conn == null) {
            LOGGER.severe("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
            return false;
        }
        
        // Verificar si la tabla MiembrosGrupo existe, si no, la creamos
        try {
            // Intentar crear la tabla si no existe
            String createTableSQL = "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'MiembrosGrupo') " +
                                 "CREATE TABLE MiembrosGrupo (" +
                                 "MiembroID INT IDENTITY(1,1) PRIMARY KEY, " +
                                 "GrupoID INT NOT NULL, " +
                                 "UsuarioID INT NOT NULL, " +
                                 "FechaUnion DATETIME NOT NULL DEFAULT GETDATE(), " +
                                 "EsAdministrador BIT NOT NULL DEFAULT 0)";
            
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createTableSQL);
            }
            
            // Ahora insertamos el miembro
            String sql = "INSERT INTO MiembrosGrupo (GrupoID, UsuarioID, FechaUnion, EsAdministrador) " +
                         "VALUES (?, ?, GETDATE(), ?)";
            
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, grupoId);
                ps.setInt(2, usuarioId);
                ps.setBoolean(3, esAdministrador);
                
                int filasAfectadas = ps.executeUpdate();
                return filasAfectadas > 0;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al agregar miembro al grupo: {0}", ex.getMessage());
            return false;
        }
    }

    /**
     * Elimina un grupo y sus miembros
     * @param grupoID ID del grupo
     */
    public void delete(int grupoID) {
        try {
            Connection conn = super.getREF();
            if (conn == null) {
                System.err.println("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
                return;
            }
            
            // Primero eliminar miembros del grupo
            String sqlDeleteMiembros = "DELETE FROM MiembrosGrupo WHERE GrupoID = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlDeleteMiembros)) {
                ps.setInt(1, grupoID);
                ps.executeUpdate();
            }
            
            // Luego eliminar el grupo
            String sqlDeleteGrupo = "DELETE FROM Grupos WHERE GrupoID = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlDeleteGrupo)) {
                ps.setInt(1, grupoID);
                int affectedRows = ps.executeUpdate();
                
                if (affectedRows > 0) {
                    System.out.println("Grupo eliminado correctamente");
                } else {
                    System.out.println("No se encontró el grupo con ID: " + grupoID);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Grupos_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Elimina grupos con menos de 3 miembros
     */
    public void eliminarGruposPocoPoblados() {
        Miembros_Grupo_Controller miembrosController = new Miembros_Grupo_Controller();
        
        try {
            Connection conn = super.getREF();
            if (conn == null) {
                System.err.println("Error: Conexión no disponible");
                return;
            }
            
            // Obtener todos los grupos
            String sql = "SELECT GrupoID FROM Grupos";
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                
                while (rs.next()) {
                    int grupoID = rs.getInt("GrupoID");
                    int numMiembros = miembrosController.contarMiembrosPorGrupo(grupoID);
                    
                    if (numMiembros < 3) {
                        System.out.println("Eliminando grupo " + grupoID + " por tener solo " + numMiembros + " miembros");
                        this.delete(grupoID);
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Grupos_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Obtiene los IDs de todos los miembros de un grupo
     * 
     * @param grupoId ID del grupo
     * @return Lista de IDs de usuarios miembros del grupo
     */
    public List<Integer> getMiembrosIdsByGrupo(int grupoId) {
        Connection conn = super.getREF();
        List<Integer> miembrosIds = new ArrayList<>();
        
        if (conn == null) {
            LOGGER.severe("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
            return miembrosIds;
        }
        
        String sql = "SELECT UsuarioID FROM MiembrosGrupo WHERE GrupoID = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, grupoId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    miembrosIds.add(rs.getInt("UsuarioID"));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener IDs de miembros", ex);
        }
        
        return miembrosIds;
    }

    /**
     * Obtiene los miembros de un grupo con sus detalles y rol
     * 
     * @param grupoId ID del grupo
     * @return Lista de pares [Usuario, esAdmin]
     */
    public List<Object[]> getMiembrosConDetalles(int grupoId) {
        Connection conn = super.getREF();
        List<Object[]> resultado = new ArrayList<>();
        
        if (conn == null) {
            LOGGER.severe("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
            return resultado;
        }
        
        String sql = "SELECT u.*, mg.EsAdministrador FROM MiembrosGrupo mg " +
                    "INNER JOIN Usuarios u ON mg.UsuarioID = u.UsuarioID " +
                    "WHERE mg.GrupoID = ? " +
                    "ORDER BY mg.EsAdministrador DESC, u.Nombre ASC";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, grupoId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Crear objeto Usuario
                    Usuarios usuario = new Usuarios();
                    usuario.setUsuarioID(rs.getInt("UsuarioID"));
                    usuario.setNombre(rs.getString("Nombre"));
                    usuario.setApellido(rs.getString("Apellido"));
                    usuario.setEmail(rs.getString("Email"));
                    
                    // Obtener si es administrador
                    boolean esAdmin = rs.getBoolean("EsAdministrador");
                    
                    // Agregar a la lista
                    resultado.add(new Object[]{usuario, esAdmin});
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener miembros con detalles", ex);
        }
        
        return resultado;
    }
}
