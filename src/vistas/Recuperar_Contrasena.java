package vistas;

import cocochat.CocoChat;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Recuperar_Contrasena extends JDialog {
    private JTextField txtEmail;
    private JPasswordField txtNuevaContrasena;
    private JPasswordField txtConfirmarContrasena;
    private JButton btnRestablecer;
    private JButton btnCancelar;
    
    public Recuperar_Contrasena(Frame parent) {
        super(parent, "Recuperar Contraseña", true);
        initComponents();
    }
    
    private void initComponents() {
        setSize(400, 250);
        setLocationRelativeTo(getOwner());
        
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        panel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        panel.add(txtEmail);
        
        panel.add(new JLabel("Nueva contraseña:"));
        txtNuevaContrasena = new JPasswordField();
        panel.add(txtNuevaContrasena);
        
        panel.add(new JLabel("Confirmar contraseña:"));
        txtConfirmarContrasena = new JPasswordField();
        panel.add(txtConfirmarContrasena);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnRestablecer = new JButton("Restablecer");
        btnCancelar = new JButton("Cancelar");
        
        btnRestablecer.addActionListener(e -> restablecerContrasena());
        btnCancelar.addActionListener(e -> dispose());
        
        buttonPanel.add(btnRestablecer);
        buttonPanel.add(btnCancelar);
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void restablecerContrasena() {
        String email = txtEmail.getText().trim();
        String nuevaContrasena = new String(txtNuevaContrasena.getPassword());
        String confirmarContrasena = new String(txtConfirmarContrasena.getPassword());
        
        // Validar campos
        if (email.isEmpty() || nuevaContrasena.isEmpty() || confirmarContrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor complete todos los campos.", 
                "Campos incompletos", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Validar que las contraseñas coincidan
        if (!nuevaContrasena.equals(confirmarContrasena)) {
            JOptionPane.showMessageDialog(this, 
                "Las contraseñas no coinciden.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Verificar si el email existe
        if (CocoChat.usuarioController.existeEmail(email)) {
            // Actualizar la contraseña en la base de datos
            if (CocoChat.usuarioController.actualizarContrasena(email, nuevaContrasena)) {
                JOptionPane.showMessageDialog(this, 
                    "La contraseña ha sido actualizada correctamente.", 
                    "Contraseña actualizada", 
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Ha ocurrido un error al actualizar la contraseña.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "No se encontró ninguna cuenta asociada a ese correo electrónico.", 
                "Correo no encontrado", 
                JOptionPane.ERROR_MESSAGE);
        }
        // ======== MONITOR ========
        StringBuilder monitor = new StringBuilder();
        monitor.append(email);
        monitor.append(": cambio su contraseña ");

        System.out.println(monitor);
    }
}