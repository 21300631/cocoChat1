package conectores;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelos.Contactos;
import modelos.Usuarios;

public class Contactos_Controller extends Base_Datos 
{
    private static final Logger LOGGER = Logger.getLogger(Contactos_Controller.class.getName());
    
    public Contactos_Controller() 
    {
        super();
    }

    // Método para agregar un contacto
    public boolean add(Contactos contacto) 
    {
        Connection conn = super.getREF();
        
        if (conn == null) 
        {
            LOGGER.severe("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
            return false;
        }
        
        // Verificar si ya existe el contacto
        if (esContacto(contacto.getUsuarioID(), contacto.getContactoUsuarioID())) 
        {
            LOGGER.info("El contacto ya existe para este usuario");
            return false;
        }
        
        String sql = "INSERT INTO Contactos (UsuarioID, ContactoUsuarioID, FechaAgregado, EsBloqueado) "
                   + "VALUES (?, ?, GETDATE(), ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) 
        {
            pstmt.setInt(1, contacto.getUsuarioID());
            pstmt.setInt(2, contacto.getContactoUsuarioID());
            pstmt.setBoolean(3, contacto.isEsBloqueado());
            
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) 
            {
                LOGGER.info("Contacto agregado correctamente");
                return true;
            }
        } 
        catch (SQLException ex) 
        {
            LOGGER.log(Level.SEVERE, "Error al agregar contacto", ex);
        }
        
        return false;
    }
    
    // Obtener todos los contactos de un usuario
    public List<Contactos> getContactosByUsuario(int usuarioID) 
    {
        List<Contactos> listaContactos = new ArrayList<>();
        Connection conn = super.getREF();
        
        if (conn == null) 
        {
            System.err.println("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
            return listaContactos;
        }
        
        String sql = "SELECT * FROM Contactos WHERE UsuarioID = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) 
        {
            pstmt.setInt(1, usuarioID);
            
            try (ResultSet rs = pstmt.executeQuery()) 
            {
                while (rs.next()) 
                {
                    int contactoID = rs.getInt("ContactoID");
                    int contactoUsuarioID = rs.getInt("ContactoUsuarioID");
                    LocalDateTime fechaAgregado = rs.getObject("FechaAgregado", LocalDateTime.class);
                    boolean esBloqueado = rs.getBoolean("EsBloqueado");
                    
                    Contactos contacto = new Contactos(contactoID, usuarioID, contactoUsuarioID, 
                                                      fechaAgregado, esBloqueado);
                    listaContactos.add(contacto);
                }
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Contactos_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listaContactos;
    }
    
    // Obtener información detallada de contactos con datos de usuario
    public List<Usuarios> getContactosDetalladosByUsuario(int usuarioID) 
    {
        List<Usuarios> listaUsuarios = new ArrayList<>();
        Connection conn = super.getREF();
        
        if (conn == null) 
        {
            System.err.println("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
            return listaUsuarios;
        }
        
        String sql = "SELECT u.* FROM Usuarios u " +
                     "INNER JOIN Contactos c ON u.UsuarioID = c.ContactoUsuarioID " +
                     "WHERE c.UsuarioID = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) 
        {
            pstmt.setInt(1, usuarioID);
            
            try (ResultSet rs = pstmt.executeQuery()) 
            {
                while (rs.next()) 
                {
                    Usuarios usuario = new Usuarios();
                    usuario.setUsuarioID(rs.getInt("UsuarioID"));
                    usuario.setNombre(rs.getString("Nombre"));
                    usuario.setApellido(rs.getString("Apellido"));
                    usuario.setTelefono(rs.getString("Telefono"));
                    usuario.setEmail(rs.getString("Email"));
                    usuario.setFoto_perfil(rs.getString("FotoPerfil"));
                    usuario.setEstado(rs.getString("Estado"));
                    
                    listaUsuarios.add(usuario);
                }
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Contactos_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listaUsuarios;
    }
    
    // Actualizar un contacto (principalmente para bloquear/desbloquear)
    public boolean actualizarEstadoBloqueo(int contactoID, boolean nuevoEstadoBloqueo) 
    {
        Connection conn = super.getREF();
        
        if (conn == null) 
        {
            System.err.println("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
            return false;
        }
        
        String sql = "UPDATE Contactos SET EsBloqueado = ? WHERE ContactoID = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) 
        {
            pstmt.setBoolean(1, nuevoEstadoBloqueo);
            pstmt.setInt(2, contactoID);
            
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) 
            {
                System.out.println("Estado de bloqueo actualizado correctamente");
                return true;
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Contactos_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    // Eliminar un contacto
    public boolean eliminarContacto(int contactoID) 
    {
        Connection conn = super.getREF();
        
        if (conn == null) 
        {
            System.err.println("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
            return false;
        }
        
        String sql = "DELETE FROM Contactos WHERE ContactoID = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) 
        {
            pstmt.setInt(1, contactoID);
            
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Contacto eliminado correctamente");
                return true;
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Contactos_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    // Verificar si un usuario es contacto de otro
    public boolean esContacto(int usuarioID, int posibleContactoID) 
    {
        Connection conn = super.getREF();
        
        if (conn == null) 
        {
            System.err.println("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
            return false;
        }
        
        String sql = "SELECT COUNT(*) FROM Contactos WHERE UsuarioID = ? AND ContactoUsuarioID = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) 
        {
            pstmt.setInt(1, usuarioID);
            pstmt.setInt(2, posibleContactoID);
            
            try (ResultSet rs = pstmt.executeQuery()) 
            {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Contactos_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }

    // Buscar contactos por nombre o apellido
    public List<Usuarios> buscarContactos(int usuarioID, String busqueda) 
    {
        List<Usuarios> resultados = new ArrayList<>();
        Connection conn = super.getREF();
        
        if (conn == null) 
        {
            LOGGER.severe("Error: No se puede realizar la operación. Conexión a la base de datos no disponible.");
            return resultados;
        }
        
        String sql = "SELECT u.* FROM Usuarios u " +
                     "INNER JOIN Contactos c ON u.UsuarioID = c.ContactoUsuarioID " +
                     "WHERE c.UsuarioID = ? AND (u.Nombre LIKE ? OR u.Apellido LIKE ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) 
        {
            pstmt.setInt(1, usuarioID);
            pstmt.setString(2, "%" + busqueda + "%");
            pstmt.setString(3, "%" + busqueda + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) 
            {
                while (rs.next()) 
                {
                    Usuarios usuario = new Usuarios();
                    usuario.setUsuarioID(rs.getInt("UsuarioID"));
                    usuario.setNombre(rs.getString("Nombre"));
                    usuario.setApellido(rs.getString("Apellido"));
                    usuario.setTelefono(rs.getString("Telefono"));
                    usuario.setEmail(rs.getString("Email"));
                    usuario.setFoto_perfil(rs.getString("FotoPerfil"));
                    usuario.setEstado(rs.getString("Estado"));
                    
                    resultados.add(usuario);
                }
            }
        } 
        catch (SQLException ex) 
        {
            LOGGER.log(Level.SEVERE, "Error al buscar contactos", ex);
        }
        
        return resultados;
    }
}
