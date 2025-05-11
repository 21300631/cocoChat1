package conectores;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelos.Tokens_Sesion;

public class Token_Sesion_Controller extends Base_Datos {
    
    private static final Logger LOGGER = Logger.getLogger(Token_Sesion_Controller.class.getName());
    
    public Token_Sesion_Controller() {
        super();
    }
    
    // Método para generar un nuevo token de sesión
    public Tokens_Sesion generarToken(int usuarioID, String dispositivo, String direccionIP, int duracionHoras) {
        Connection conn = super.getREF();
        
        if (conn == null) {
            LOGGER.severe("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
            return null;
        }
        
        // Generar un token único usando UUID
        String token = UUID.randomUUID().toString();
        
        // Establecer fecha de expiración
        LocalDateTime fechaExpiracion = LocalDateTime.now().plusHours(duracionHoras);
        
        // Crear objeto de token
        Tokens_Sesion nuevoToken = new Tokens_Sesion(usuarioID, token, fechaExpiracion, dispositivo, direccionIP);
        
        String sql = "INSERT INTO TokensSesion (UsuarioID, Token, FechaCreacion, FechaExpiracion, Dispositivo, DireccionIP, Activa) " +
                     "VALUES (?, ?, GETDATE(), ?, ?, ?, 1)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, usuarioID);
            pstmt.setString(2, token);
            pstmt.setTimestamp(3, Timestamp.valueOf(fechaExpiracion));
            pstmt.setString(4, dispositivo);
            pstmt.setString(5, direccionIP);
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                // Recuperar el ID generado
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        nuevoToken.setSesionID(generatedKeys.getInt(1));
                        LOGGER.log(Level.INFO, "Token generado correctamente para el usuario ID: {0}", usuarioID);
                        return nuevoToken;
                    }
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al generar token", ex);
        }
        
        return null;
    }
    
    // Método para validar un token de sesión
    public Tokens_Sesion validarToken(String tokenString) {
        Connection conn = super.getREF();
        
        if (conn == null) {
            LOGGER.severe("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
            return null;
        }
        
        String sql = "SELECT * FROM TokensSesion WHERE Token = ? AND Activa = 1 AND FechaExpiracion > GETDATE()";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tokenString);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Tokens_Sesion token = new Tokens_Sesion();
                    token.setSesionID(rs.getInt("SesionID"));
                    token.setUsuarioID(rs.getInt("UsuarioID"));
                    token.setToken(rs.getString("Token"));
                    token.setFechaCreacion(rs.getTimestamp("FechaCreacion").toLocalDateTime());
                    token.setFechaExpiracion(rs.getTimestamp("FechaExpiracion").toLocalDateTime());
                    token.setDispositivo(rs.getString("Dispositivo"));
                    token.setDireccionIP(rs.getString("DireccionIP"));
                    token.setActiva(rs.getBoolean("Activa"));
                    
                    LOGGER.log(Level.INFO, "Token validado correctamente para el usuario ID: {0}", token.getUsuarioID());
                    return token;
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al validar token", ex);
        }
        
        LOGGER.log(Level.WARNING, "Token inválido o expirado: {0}", tokenString);
        return null;
    }
    
    // Método para revocar (invalidar) un token específico
    public boolean revocarToken(String tokenString) {
        Connection conn = super.getREF();
        
        if (conn == null) {
            LOGGER.severe("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
            return false;
        }
        
        String sql = "UPDATE TokensSesion SET Activa = 0 WHERE Token = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tokenString);
            
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                LOGGER.log(Level.INFO, "Token revocado correctamente: {0}", tokenString);
                return true;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al revocar token", ex);
        }
        
        return false;
    }
    
    // Método para revocar todos los tokens de un usuario
    public int revocarTodosLosTokens(int usuarioID) {
        Connection conn = super.getREF();
        
        if (conn == null) {
            LOGGER.severe("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
            return 0;
        }
        
        String sql = "UPDATE TokensSesion SET Activa = 0 WHERE UsuarioID = ? AND Activa = 1";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, usuarioID);
            
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                LOGGER.log(Level.INFO, "Revocados {0} tokens para el usuario ID: {1}", new Object[]{filasAfectadas, usuarioID});
            }
            return filasAfectadas;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al revocar todos los tokens", ex);
        }
        
        return 0;
    }
    
    // Método para extender la validez de un token
    public boolean extenderToken(String tokenString, int horasAdicionales) {
        Connection conn = super.getREF();
        
        if (conn == null) {
            LOGGER.severe("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
            return false;
        }
        
        String sql = "UPDATE TokensSesion SET FechaExpiracion = DATEADD(hour, ?, FechaExpiracion) " +
                     "WHERE Token = ? AND Activa = 1 AND FechaExpiracion > GETDATE()";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, horasAdicionales);
            pstmt.setString(2, tokenString);
            
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                LOGGER.log(Level.INFO, "Token extendido correctamente por {0} horas: {1}", new Object[]{horasAdicionales, tokenString});
                return true;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al extender token", ex);
        }
        
        return false;
    }
    
    // Método para obtener todos los tokens activos de un usuario
    public List<Tokens_Sesion> getTokensActivosByUsuario(int usuarioID) {
        List<Tokens_Sesion> tokens = new ArrayList<>();
        Connection conn = super.getREF();
        
        if (conn == null) {
            LOGGER.severe("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
            return tokens;
        }
        
        String sql = "SELECT * FROM TokensSesion WHERE UsuarioID = ? AND Activa = 1 AND FechaExpiracion > GETDATE()";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, usuarioID);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Tokens_Sesion token = new Tokens_Sesion();
                    token.setSesionID(rs.getInt("SesionID"));
                    token.setUsuarioID(rs.getInt("UsuarioID"));
                    token.setToken(rs.getString("Token"));
                    token.setFechaCreacion(rs.getTimestamp("FechaCreacion").toLocalDateTime());
                    token.setFechaExpiracion(rs.getTimestamp("FechaExpiracion").toLocalDateTime());
                    token.setDispositivo(rs.getString("Dispositivo"));
                    token.setDireccionIP(rs.getString("DireccionIP"));
                    token.setActiva(rs.getBoolean("Activa"));
                    
                    tokens.add(token);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener tokens activos", ex);
        }
        
        return tokens;
    }
}
