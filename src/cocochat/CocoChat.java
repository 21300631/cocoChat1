package cocochat;

import vistas.Coco;
import conectores.Base_Datos;
import conectores.Usuario_Controller;
import modelos.Usuarios;

public class CocoChat {

    public static void main(String[] args) {
        // Prueba Backend
        Base_Datos bd;
        Usuario_Controller usr_control = new Usuario_Controller();
        Usuarios user1 = new Usuarios("Juan", "Alberto", "1234567890", "si@ejemplo.com", null, null, null, null, false, null, 'M');
        
        
        // Logica FrontEnd
        Coco coco = new Coco();
        coco.setVisible(true);
        // TODO code application logic here
    }
    
}
