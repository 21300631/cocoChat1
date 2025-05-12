package cocochat;

//Import Vistas (frontend)
import vistas.Coco;

// Import Conectores (backend base de datos)
import conectores.Base_Datos;
import conectores.Usuario_Controller;

// Inport Modelos (Tablas base de datos)
import modelos.Usuarios;

public class CocoChat {
    public static void main(String[] args) {
        Coco coco = new Coco();
        coco.setVisible(true);
        
        //Base_Datos bd = new Base_Datos();
        Usuario_Controller usr_control = new Usuario_Controller();
        Usuarios user1 = new Usuarios("Juan", "Alberto", "1234567890", "si@ejemplo.com", null, null, null, null, false, null, 'M');
        
    }
    
}
