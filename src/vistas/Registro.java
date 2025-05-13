package vistas;

import cocochat.CocoChat;
import java.awt.*;
import javax.swing.*;
import modelos.Usuarios;

public class Registro extends JDialog {
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtEmail;
    private JTextField txtTelefono;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JComboBox<String> cmbGenero;
    private JButton btnRegistrar;
    private JButton btnCancelar;
    
    public Registro(Frame parent) {
        super(parent, "Registro de Usuario", true);
        initComponents();
    }
    
    private void initComponents() {
        setSize(400, 400);
        setLocationRelativeTo(getOwner());
        
        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        panel.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panel.add(txtNombre);
        
        panel.add(new JLabel("Apellido:"));
        txtApellido = new JTextField();
        panel.add(txtApellido);
        
        panel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        panel.add(txtEmail);
        
        panel.add(new JLabel("Teléfono:"));
        txtTelefono = new JTextField();
        panel.add(txtTelefono);
        
        panel.add(new JLabel("Contraseña:"));
        txtPassword = new JPasswordField();
        panel.add(txtPassword);
        
        panel.add(new JLabel("Confirmar contraseña:"));
        txtConfirmPassword = new JPasswordField();
        panel.add(txtConfirmPassword);
        
        panel.add(new JLabel("Género:"));
        cmbGenero = new JComboBox<>(new String[]{"Masculino", "Femenino", "Otro"});
        panel.add(cmbGenero);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnRegistrar = new JButton("Registrar");
        btnCancelar = new JButton("Cancelar");
        
        btnRegistrar.addActionListener(e -> registrarUsuario());
        btnCancelar.addActionListener(e -> dispose());
        
        buttonPanel.add(btnRegistrar);
        buttonPanel.add(btnCancelar);
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void registrarUsuario() {
        // Validar campos
        if (txtNombre.getText().trim().isEmpty() || 
            txtApellido.getText().trim().isEmpty() ||
            txtEmail.getText().trim().isEmpty() ||
            new String(txtPassword.getPassword()).isEmpty()) {
            
            JOptionPane.showMessageDialog(this, 
                "Por favor complete todos los campos obligatorios.", 
                "Datos incompletos", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Validar que las contraseñas coincidan
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, 
                "Las contraseñas no coinciden.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validar que el email no esté en uso
        if (CocoChat.usuarioController.existeEmail(txtEmail.getText())) {
            JOptionPane.showMessageDialog(this, 
                "El email ya está registrado.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Crear el usuario
        Usuarios nuevoUsuario = new Usuarios();
        nuevoUsuario.setNombre(txtNombre.getText().trim());
        nuevoUsuario.setApellido(txtApellido.getText().trim());
        nuevoUsuario.setEmail(txtEmail.getText().trim());
        nuevoUsuario.setTelefono(txtTelefono.getText().trim());
        nuevoUsuario.setPassword(password); // En un sistema real, se debería hashear
        nuevoUsuario.setGenero(cmbGenero.getSelectedItem().toString().charAt(0));
        nuevoUsuario.setEstado("Disponible");
        nuevoUsuario.setCuenta_verificada(false);
        
        // Guardar el usuario en la base de datos
        int resultado = CocoChat.usuarioController.add(nuevoUsuario);
        
        if (resultado > 0) {
            // Añadir el usuario al grupo Global (ID = 2)
            boolean agregadoAGrupoGlobal = CocoChat.gruposController.agregarMiembroGrupo(2, resultado, false);
            
            if (!agregadoAGrupoGlobal) {
                System.out.println("Advertencia: No se pudo añadir al usuario al grupo Global");
            }
            
            // ======== MONITOR ========
            StringBuilder usuMonitor = new StringBuilder();
            usuMonitor.append(txtNombre.getText()); // Corregido para obtener el texto del campo
            usuMonitor.append(": se registro ");
            System.out.println(usuMonitor);
            
            JOptionPane.showMessageDialog(this, 
                "Usuario registrado correctamente. ¡Bienvenido a CocoChat!",
                "Registro exitoso", 
                JOptionPane.INFORMATION_MESSAGE);
            
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Ocurrió un error al registrar el usuario.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
