package vistas;

import cocochat.CocoChat;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import modelos.*;

public class Usuarios_Linea extends JPanel {
    
    public Usuarios_Linea() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("En Línea"));
        setPreferredSize(new Dimension(150, 0));
        setBackground(new Color(230, 255, 230));
        
        // Usuarios en línea se cargarán después
    }
    
    public void cargarUsuariosEnLinea() {
        // Obtener usuarios en línea
        List<Usuarios> usuariosEnLinea = CocoChat.usuarioController.getUsuariosEnLinea();
        
        // Limpiar panel
        removeAll();
        
        // Mostrar usuarios
        for (Usuarios usuario : usuariosEnLinea) {
            // No mostrar al usuario actual
            if (usuario.getUsuario_Id() != CocoChat.usuarioActual.getUsuario_Id()) {
                JLabel usuarioLabel = new JLabel("• " + usuario.getNombre());
                usuarioLabel.setForeground(new Color(0, 100, 0));
                usuarioLabel.setBorder(new EmptyBorder(3, 5, 3, 5));
                add(usuarioLabel);
            }
        }
        
        revalidate();
        repaint();
    }
    
    public void actualizarUsuariosEnLinea() {
        try {
            // Obtener usuarios en línea (últimos 5 minutos)
            List<Usuarios> usuariosEnLinea = CocoChat.usuarioController.getUsuariosEnLinea();
            
            // Depuración
            System.out.println("Usuarios en línea encontrados: " + usuariosEnLinea.size());
            
            // Limpiar panel
            removeAll();
            
            // Mostrar cada usuario
            for (Usuarios usuario : usuariosEnLinea) {
                // No mostrar al usuario actual
                if (usuario.getUsuario_Id() != CocoChat.usuarioActual.getUsuario_Id()) {
                    JLabel lblUsuario = new JLabel("• " + usuario.getNombre() + " (online)");
                    lblUsuario.setForeground(new Color(0, 150, 0));
                    add(lblUsuario);
                }
            }
            
            revalidate();
            repaint();
            
            // Actualizar última conexión del usuario actual
            CocoChat.usuarioController.actualizarUltimaConexion(CocoChat.usuarioActual.getUsuario_Id());
        } catch (Exception e) {
            System.err.println("Error al actualizar usuarios en línea: " + e.getMessage());
        }
    }
}