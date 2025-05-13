package conectores;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelos.Mensajes_Individuales;

public class Mensajes_Individuales_Controller extends Base_Datos {
    
    private static final Logger LOGGER = Logger.getLogger(Mensajes_Individuales_Controller.class.getName());
    
    public Mensajes_Individuales_Controller() {
        super();
    }
    
    /**
     * Obtiene los mensajes intercambiados entre dos usuarios.
     * 
     * @param usuarioID ID del usuario actual
     * @param contactoID ID del contacto
     * @return Lista de mensajes entre los dos usuarios
     */
    public List<Mensajes_Individuales> getMensajesByUsuarios(int usuarioID, int contactoID) {
        Connection conn = super.getREF();
        List<Mensajes_Individuales> mensajes = new ArrayList<>();
        
        if (conn == null) {
            LOGGER.severe("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
            return mensajes;
        }
        
        // Consulta para obtener mensajes entre dos usuarios (en ambas direcciones)
        String sql = "SELECT * FROM MensajesIndividuales WHERE " +
                    "((RemitenteID = ? AND DestinatarioID = ?) OR " +
                    "(RemitenteID = ? AND DestinatarioID = ?)) " +
                    "ORDER BY FechaEnvio ASC";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioID);
            ps.setInt(2, contactoID);
            ps.setInt(3, contactoID);
            ps.setInt(4, usuarioID);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Mensajes_Individuales mensaje = new Mensajes_Individuales();
                    mensaje.setMensajeID(rs.getInt("MensajeID"));
                    mensaje.setRemitenteID(rs.getInt("RemitenteID"));
                    mensaje.setDestinatarioID(rs.getInt("DestinatarioID"));
                    mensaje.setContenido(rs.getString("Contenido"));
                    
                    // Manejo de valores nulos para fechas
                    if (rs.getObject("FechaEnvio") != null) {
                        mensaje.setFechaEnvio(rs.getTimestamp("FechaEnvio").toLocalDateTime());
                    }
                    if (rs.getObject("FechaLeido") != null) {
                        mensaje.setFechaLeido(rs.getTimestamp("FechaLeido").toLocalDateTime());
                    }
                    mensaje.setEsArchivado(rs.getBoolean("EsArchivado"));
                    
                    mensajes.add(mensaje);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener mensajes entre usuarios: {0}", ex.getMessage());
        }
        
        return mensajes;
    }
    
    /**
     * Añade un nuevo mensaje individual a la base de datos
     * 
     * @param mensaje El mensaje a agregar
     * @return true si se agregó correctamente, false en caso contrario
     */
    public boolean add(Mensajes_Individuales mensaje) {
        Connection conn = super.getREF();
        
        if (conn == null) {
            LOGGER.severe("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
            return false;
        }
        
        // Consulta SQL corregida - eliminada la columna Adjunto que no existe
        String sql = "INSERT INTO MensajesIndividuales (RemitenteID, DestinatarioID, Contenido, FechaEnvio, " +
                    "FechaLeido, EsArchivado) VALUES (?, ?, ?, GETDATE(), NULL, ?)";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, mensaje.getRemitenteID());
            ps.setInt(2, mensaje.getDestinatarioID());
            ps.setString(3, mensaje.getContenido());
            ps.setBoolean(4, mensaje.isEsArchivado());
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al agregar mensaje: {0}", ex.getMessage());
            return false;
        }
    }
}