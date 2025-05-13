package cocochat;

import conectores.*;
import modelos.*;
import vistas.LogIn;

public class CocoChat {
    // Controladores como estáticos para acceso global
    public static Usuario_Controller usuarioController;
    public static Contactos_Controller contactosController;
    public static Mensajes_Individuales_Controller mensajesController;
    public static Grupos_Controller gruposController;
    public static Mensajes_Grupo_Controller mensajesGrupoController;
    public static Mensajes_Mejores_Amigos_Controller mensajesMejoresAmigosController;
    public static Mejores_Amigos_Controller mejoresAmigosController;
    
    // Variable para guardar el usuario actual
    public static Usuarios usuarioActual;
    
    public static void main(String[] args) {
        // Inicializar controladores
        usuarioController = new Usuario_Controller();
        contactosController = new Contactos_Controller();
        mensajesController = new Mensajes_Individuales_Controller();
        gruposController = new Grupos_Controller();
        mensajesGrupoController = new Mensajes_Grupo_Controller();
        mensajesMejoresAmigosController = new Mensajes_Mejores_Amigos_Controller();
        mejoresAmigosController = new Mejores_Amigos_Controller();
        
        // Mostrar ventana de login
        java.awt.EventQueue.invokeLater(() -> {
            new LogIn().setVisible(true);
        });
    }
}
