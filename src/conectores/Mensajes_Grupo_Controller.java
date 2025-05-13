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
import modelos.Mensajes_Grupo;
import modelos.Usuarios;

public class Mensajes_Grupo_Controller extends Base_Datos {
    
    private static final Logger LOGGER = Logger.getLogger(Mensajes_Grupo_Controller.class.getName());
    
    public Mensajes_Grupo_Controller() {
        super();
    }
    
    /**
     * Envía un mensaje a un grupo y devuelve el ID del mensaje creado
     * 
     * @param mensaje El mensaje a enviar
     * @return ID del mensaje creado, o -1 en caso de error
     */
    public int enviarMensaje(Mensajes_Grupo mensaje) {
        Connection conn = super.getREF();
        
        if (conn == null) {
            LOGGER.severe("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
            return -1;
        }
        
        // Verificar si la tabla existe, si no, crearla
        try {
            String createTableSQL = "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'MensajesGrupo') " +
                                 "CREATE TABLE MensajesGrupo (" +
                                 "MensajeID INT IDENTITY(1,1) PRIMARY KEY, " +
                                 "GrupoID INT NOT NULL, " +
                                 "RemitenteID INT NOT NULL, " +
                                 "Contenido NVARCHAR(MAX) NOT NULL, " +
                                 "FechaEnvio DATETIME NOT NULL DEFAULT GETDATE(), " +
                                 "EsAnuncio BIT NOT NULL DEFAULT 0)";
            
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createTableSQL);
            }
            
            // Insertar el mensaje
            String sql = "INSERT INTO MensajesGrupo (GrupoID, RemitenteID, Contenido, FechaEnvio, EsAnuncio) " +
                         "VALUES (?, ?, ?, GETDATE(), ?); SELECT SCOPE_IDENTITY() AS ID";
            
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, mensaje.getGrupoID());
                ps.setInt(2, mensaje.getRemitenteID());
                ps.setString(3, mensaje.getContenido());
                ps.setBoolean(4, mensaje.isEsAnuncio());
                
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("ID");
                    }
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al enviar mensaje a grupo", ex);
        }
        
        return -1;
    }
    
    // Método para obtener mensajes de un grupo
    public List<Mensajes_Grupo> getMensajesByGrupo(int grupoID, int limite) {
        List<Mensajes_Grupo> mensajes = new ArrayList<>();
        Connection conn = super.getREF();
        
        if (conn == null) {
            LOGGER.log(Level.SEVERE, "Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
            return mensajes;
        }
        
        String sql = "SELECT * FROM MensajesGrupo WHERE GrupoID = ? ORDER BY FechaEnvio DESC";
        
        // Si hay límite, añadirlo a la consulta
        if (limite > 0) {
            sql += " FETCH FIRST ? ROWS ONLY";
        }
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, grupoID);
            
            if (limite > 0) {
                pstmt.setInt(2, limite);
            }
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Mensajes_Grupo mensaje = new Mensajes_Grupo();
                    mensaje.setMensajeID(rs.getInt("MensajeID"));
                    mensaje.setGrupoID(rs.getInt("GrupoID"));
                    mensaje.setRemitenteID(rs.getInt("RemitenteID"));
                    mensaje.setContenido(rs.getString("Contenido"));
                    mensaje.setFechaEnvio(rs.getTimestamp("FechaEnvio").toLocalDateTime());
                    mensaje.setEsAnuncio(rs.getBoolean("EsAnuncio"));
                    
                    mensajes.add(mensaje);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al recuperar mensajes del grupo", ex);
        }
        
        return mensajes;
    }
    
    // Método para obtener mensajes de un grupo con datos del remitente
    public List<Object[]> getMensajesConRemitente(int grupoId, int limite) {
        Connection conn = super.getREF();
        List<Object[]> resultado = new ArrayList<>();
        
        if (conn == null) {
            LOGGER.severe("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
            return resultado;
        }
        
        // Consulta SQL corregida - usando TOP en lugar de FETCH FIRST
        String sql = "SELECT TOP " + limite + " m.*, u.* " +
                    "FROM MensajesGrupo m " +
                    "INNER JOIN Usuarios u ON m.RemitenteID = u.UsuarioID " +
                    "WHERE m.GrupoID = ? " +
                    "ORDER BY m.FechaEnvio ASC";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, grupoId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Crear objeto Mensaje_Grupo
                    Mensajes_Grupo mensaje = new Mensajes_Grupo();
                    mensaje.setMensajeID(rs.getInt("MensajeID"));
                    mensaje.setGrupoID(rs.getInt("GrupoID"));
                    mensaje.setRemitenteID(rs.getInt("RemitenteID"));
                    mensaje.setContenido(rs.getString("Contenido"));
                    
                    // Manejar fechas con posibles nulos
                    if (rs.getObject("FechaEnvio") != null) {
                        mensaje.setFechaEnvio(rs.getTimestamp("FechaEnvio").toLocalDateTime());
                    }
                    
                    mensaje.setEsAnuncio(rs.getBoolean("EsAnuncio"));
                    
                    // Crear objeto Usuario (remitente)
                    Usuarios remitente = new Usuarios();
                    remitente.setUsuarioID(rs.getInt("UsuarioID"));
                    remitente.setNombre(rs.getString("Nombre"));
                    remitente.setApellido(rs.getString("Apellido"));
                    remitente.setEmail(rs.getString("Email"));
                    
                    // Agregar par [mensaje, remitente] al resultado
                    resultado.add(new Object[]{mensaje, remitente});
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al recuperar mensajes con datos de remitente", ex);
        }
        
        return resultado;
    }
    
    // Método para eliminar un mensaje de grupo
    public boolean eliminarMensaje(int mensajeID, int usuarioID) {
        Connection conn = super.getREF();
        
        if (conn == null) {
            LOGGER.log(Level.SEVERE, "Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
            return false;
        }
        
        // Primero verificamos si el usuario es el remitente o un administrador del grupo
        String verificarSQL = "SELECT m.GrupoID, m.RemitenteID FROM MensajesGrupo m WHERE m.MensajeID = ?";
        
        try (PreparedStatement verificarStmt = conn.prepareStatement(verificarSQL)) {
            verificarStmt.setInt(1, mensajeID);
            
            try (ResultSet rs = verificarStmt.executeQuery()) {
                if (rs.next()) {
                    int remitenteID = rs.getInt("RemitenteID");
                    int grupoID = rs.getInt("GrupoID");
                    
                    // Si el usuario no es el remitente, verificar si es admin
                    if (remitenteID != usuarioID) {
                        String adminSQL = "SELECT COUNT(*) FROM MiembrosGrupo " +
                                         "WHERE GrupoID = ? AND UsuarioID = ? AND EsAdministrador = 1";
                        
                        try (PreparedStatement adminStmt = conn.prepareStatement(adminSQL)) {
                            adminStmt.setInt(1, grupoID);
                            adminStmt.setInt(2, usuarioID);
                            
                            try (ResultSet adminRs = adminStmt.executeQuery()) {
                                if (adminRs.next() && adminRs.getInt(1) == 0) {
                                    // No es remitente ni admin
                                    LOGGER.log(Level.WARNING, 
                                        "El usuario {0} no tiene permisos para eliminar el mensaje {1}", 
                                        new Object[]{usuarioID, mensajeID});
                                    return false;
                                }
                            }
                        }
                    }
                    
                    // Proceder con el borrado
                    String deleteSQL = "DELETE FROM MensajesGrupo WHERE MensajeID = ?";
                    
                    try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSQL)) {
                        deleteStmt.setInt(1, mensajeID);
                        
                        int filasAfectadas = deleteStmt.executeUpdate();
                        if (filasAfectadas > 0) {
                            LOGGER.log(Level.INFO, "Mensaje {0} eliminado por usuario {1}", 
                                    new Object[]{mensajeID, usuarioID});
                            return true;
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al eliminar mensaje de grupo", ex);
        }
        
        return false;
    }
    
    // Método para obtener anuncios de un grupo
    public List<Mensajes_Grupo> getAnunciosGrupo(int grupoID) {
        List<Mensajes_Grupo> anuncios = new ArrayList<>();
        Connection conn = super.getREF();
        
        if (conn == null) {
            LOGGER.log(Level.SEVERE, "Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
            return anuncios;
        }
        
        String sql = "SELECT * FROM MensajesGrupo WHERE GrupoID = ? AND EsAnuncio = 1 " +
                     "ORDER BY FechaEnvio DESC";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, grupoID);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Mensajes_Grupo anuncio = new Mensajes_Grupo();
                    anuncio.setMensajeID(rs.getInt("MensajeID"));
                    anuncio.setGrupoID(rs.getInt("GrupoID"));
                    anuncio.setRemitenteID(rs.getInt("RemitenteID"));
                    anuncio.setContenido(rs.getString("Contenido"));
                    anuncio.setFechaEnvio(rs.getTimestamp("FechaEnvio").toLocalDateTime());
                    anuncio.setEsAnuncio(rs.getBoolean("EsAnuncio"));
                    
                    anuncios.add(anuncio);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al recuperar anuncios del grupo", ex);
        }
        
        return anuncios;
    }
}
