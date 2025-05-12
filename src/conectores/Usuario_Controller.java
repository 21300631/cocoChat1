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
import modelos.Usuarios;

public class Usuario_Controller extends Base_Datos
{
    private static final Logger LOGGER = Logger.getLogger(Usuario_Controller.class.getName());
    
    public Usuario_Controller()
    {
        super();
    }

    // Método para agregar un nuevo usuario
    public int add(Usuarios user)
    {
        Connection conn = super.getREF();
        
        // Verificar si la conexión es nula
        if (conn == null) 
        {
            LOGGER.severe("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
            return -1;
        }
        
        // Verificar si el email ya está registrado
        if (existeEmail(user.getEmail())) {
            LOGGER.log(Level.WARNING, "No se puede crear el usuario. El email ya está registrado: {0}", user.getEmail());
            return -1;
        }

        // Preparar la consulta SQL para insertar un nuevo usuario
        String sql = "INSERT INTO Usuarios(Nombre, Apellido, Telefono, Email, FechaRegistro, UltimaConexion, " +
                     "FotoPerfil, Estado, CuentaVerificada, FechaNacimiento, Genero) VALUES (?, ?, ?, ?, GETDATE(), GETDATE(), ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            ps.setString(1, user.getNombre());
            ps.setString(2, user.getApellido());
            ps.setString(3, user.getTelefono());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getFoto_perfil());
            ps.setString(6, user.getEstado());
            ps.setBoolean(7, user.isCuenta_verificada());
            // Fecha de nacimiento puede ser null
            if (user.getFecha_nacimiento() != null) {
                ps.setDate(8, java.sql.Date.valueOf(user.getFecha_nacimiento()));
            } else {
                ps.setNull(8, java.sql.Types.DATE);
            }
            ps.setString(9, String.valueOf(user.getGenero()));
            
            int filasAfectadas = ps.executeUpdate();
            
            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int nuevoId = generatedKeys.getInt(1);
                        LOGGER.log(Level.INFO, "Usuario agregado correctamente con ID: {0}", nuevoId);
                        return nuevoId;
                    }
                }
            }
        }
        catch (SQLException ex) 
        {
            LOGGER.log(Level.SEVERE, "Error al agregar usuario", ex);
        }
        
        return -1;
    }
    
    // Método para verificar si un email ya existe en la base de datos
    public boolean existeEmail(String email) {
        Connection conn = super.getREF();
        
        if (conn == null) {
            LOGGER.severe("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
            return false;
        }
        
        String sql = "SELECT COUNT(*) FROM Usuarios WHERE Email = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al verificar email", ex);
        }
        
        return false;
    }
    
    // Método para obtener un usuario por su ID
    public Usuarios getById(int usuarioID) {
        Connection conn = super.getREF();
        
        if (conn == null) {
            LOGGER.severe("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
            return null;
        }
        
        String sql = "SELECT * FROM Usuarios WHERE UsuarioID = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioID);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuarioDesdeResultSet(rs);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener usuario por ID", ex);
        }
        
        return null;
    }
    
    // Método para obtener un usuario por su email
    public Usuarios getByEmail(String email) {
        Connection conn = super.getREF();
        
        if (conn == null) {
            LOGGER.severe("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
            return null;
        }
        
        String sql = "SELECT * FROM Usuarios WHERE Email = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuarioDesdeResultSet(rs);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener usuario por email", ex);
        }
        
        return null;
    }
    
    // Método para eliminar un usuario por su ID
    public boolean delete(int usuarioID) {
        Connection conn = super.getREF();
        
        if (conn == null) {
            LOGGER.severe("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
            return false;
        }
        
        String sql = "DELETE FROM Usuarios WHERE UsuarioID = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioID);
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al eliminar usuario", ex);
        }
        
        return false;
    }

    // Método para actualizar un usuario
    public boolean update(Usuarios user) {
        Connection conn = super.getREF();
        
        if (conn == null) {
            LOGGER.severe("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
            return false;
        }
        
        String sql = "UPDATE Usuarios SET Nombre = ?, Apellido = ?, Telefono = ?, Email = ?, " +
                    "FotoPerfil = ?, Estado = ?, CuentaVerificada = ?, FechaNacimiento = ?, Genero = ? " +
                    "WHERE UsuarioID = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getNombre());
            ps.setString(2, user.getApellido());
            ps.setString(3, user.getTelefono());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getFoto_perfil());
            ps.setString(6, user.getEstado());
            ps.setBoolean(7, user.isCuenta_verificada());
            
            if (user.getFecha_nacimiento() != null) {
                ps.setDate(8, java.sql.Date.valueOf(user.getFecha_nacimiento()));
            } else {
                ps.setNull(8, java.sql.Types.DATE);
            }
            
            ps.setString(9, String.valueOf(user.getGenero()));
            ps.setInt(10, user.getUsuario_Id());
            
            int filasAfectadas = ps.executeUpdate();
            
            if (filasAfectadas > 0) {
                LOGGER.log(Level.INFO, "Usuario actualizado correctamente, ID: {0}", user.getUsuario_Id());
                return true;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al actualizar usuario", ex);
        }
        
        return false;
    }
    
    // Método para actualizar la última conexión de un usuario
    public boolean actualizarUltimaConexion(int usuarioID) {
        Connection conn = super.getREF();
        
        if (conn == null) {
            LOGGER.severe("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
            return false;
        }
        
        String sql = "UPDATE Usuarios SET UltimaConexion = GETDATE() WHERE UsuarioID = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioID);
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al actualizar última conexión", ex);
        }
        
        return false;
    }
    
    // Método para verificar la cuenta de un usuario
    public boolean verificarCuenta(int usuarioID, boolean verificada) {
        Connection conn = super.getREF();
        
        if (conn == null) {
            LOGGER.severe("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
            return false;
        }
        
        String sql = "UPDATE Usuarios SET CuentaVerificada = ? WHERE UsuarioID = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, verificada);
            ps.setInt(2, usuarioID);
            
            int filasAfectadas = ps.executeUpdate();
            
            if (filasAfectadas > 0) {
                LOGGER.log(Level.INFO, "Estado de verificación actualizado para usuario ID: {0}", usuarioID);
                return true;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al verificar cuenta", ex);
        }
        
        return false;
    }
    
    // Método para buscar usuarios por nombre o apellido
    public List<Usuarios> buscarUsuarios(String busqueda) {
        List<Usuarios> resultados = new ArrayList<>();
        Connection conn = super.getREF();
        
        if (conn == null) {
            LOGGER.severe("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
            return resultados;
        }
        
        String sql = "SELECT * FROM Usuarios WHERE Nombre LIKE ? OR Apellido LIKE ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + busqueda + "%");
            ps.setString(2, "%" + busqueda + "%");
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultados.add(mapearUsuarioDesdeResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al buscar usuarios", ex);
        }
        
        return resultados;
    }
    
    // Método auxiliar para mapear un ResultSet a un objeto Usuarios
    private Usuarios mapearUsuarioDesdeResultSet(ResultSet rs) throws SQLException {
        Usuarios usuario = new Usuarios();
        
        usuario.setUsuarioID(rs.getInt("UsuarioID"));
        usuario.setNombre(rs.getString("Nombre"));
        usuario.setApellido(rs.getString("Apellido"));
        usuario.setTelefono(rs.getString("Telefono"));
        usuario.setEmail(rs.getString("Email"));
        
        // Manejar fechas que pueden ser null
        java.sql.Timestamp fechaReg = rs.getTimestamp("FechaRegistro");
        if (fechaReg != null) {
            usuario.setFecha_registro(fechaReg.toLocalDateTime());
        }
        
        java.sql.Timestamp ultimaConexion = rs.getTimestamp("UltimaConexion");
        if (ultimaConexion != null) {
            usuario.setUltima_conexion(ultimaConexion.toLocalDateTime());
        }
        
        usuario.setFoto_perfil(rs.getString("FotoPerfil"));
        usuario.setEstado(rs.getString("Estado"));
        usuario.setCuenta_verificada(rs.getBoolean("CuentaVerificada"));
        
        java.sql.Date fechaNac = rs.getDate("FechaNacimiento");
        if (fechaNac != null) {
            usuario.setFecha_nacimiento(fechaNac.toLocalDate());
        }
        
        String generoStr = rs.getString("Genero");
        if (generoStr != null && !generoStr.isEmpty()) {
            usuario.setGenero(generoStr.charAt(0));
        }
        
        return usuario;
    }
}
