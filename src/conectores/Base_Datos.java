package conectores;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Base_Datos 
{
    private Connection ref;
    
    public Base_Datos() 
    {
        try 
        {
            // Cargar el driver JDBC para SQL Server
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            
            // Conexión con autenticación SQL Server usando usuario App
            //String url = "jdbc:sqlserver://localhost;database=CocoChat;trustServerCertificate=true";
            String url = "jdbc:sqlserver://192.168.38.141;database=CocoChat;trustServerCertificate=true";
            String user = "App";
            String password = "S3rV!d0&";
            
            ref = DriverManager.getConnection(url, user, password);
            
            if (ref != null) 
            {
                System.out.println("Conexion a SQL Server establecida correctamente con usuario App");
            }
        } 
        catch (ClassNotFoundException e) 
        {
            Logger.getLogger(Base_Datos.class.getName()).log(Level.SEVERE, 
                "Error al cargar el driver JDBC", e);
            System.out.println("Error al cargar el driver JDBC: " + e.getMessage());
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Base_Datos.class.getName()).log(Level.SEVERE, 
                "Error al conectar a la base de datos", ex);
            System.out.println("Error al conectar a la base de datos: " + ex.getMessage());
        }
    }

    public Connection getREF() 
    {
        return ref;
    }
}
