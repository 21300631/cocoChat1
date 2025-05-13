package vistas;

import cocochat.CocoChat;
import java.awt.*;
import javax.swing.*;
import modelos.Usuarios;

public class LogIn extends JFrame {
    private JTextField username;
    private JPasswordField password;
    private JButton loginButton;
    private JButton registerButton;
    private JButton forgotPasswordButton;
    
    public LogIn() {
        init();
    }
    
    private void init() {
        // Configuración de la ventana
        setTitle("CocoChat - Login");
        setSize(350, 250);  // Aumentar el tamaño de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel de campos
        JPanel fieldsPanel = new JPanel(new GridLayout(2, 2, 5, 15));
        
        JLabel usernameLabel = new JLabel("Email del usuario:");
        username = new JTextField();
        username.setPreferredSize(new Dimension(150, 30));  // Establecer tamaño preferido
        
        JLabel passwordLabel = new JLabel("Contraseña:");
        password = new JPasswordField();
        password.setPreferredSize(new Dimension(150, 30));  // Establecer tamaño preferido
        
        fieldsPanel.add(usernameLabel);
        fieldsPanel.add(username);
        fieldsPanel.add(passwordLabel);
        fieldsPanel.add(password);
        
        // Panel de botones con más espacio entre botones
        JPanel buttonsPanel = new JPanel(new GridLayout(3, 1, 0, 10));
        
        loginButton = new JButton("Iniciar Sesion");
        registerButton = new JButton("Registrarse");
        forgotPasswordButton = new JButton("Olvidé mi contraseña");
        
        // Establecer altura mínima para los botones
        loginButton.setPreferredSize(new Dimension(loginButton.getPreferredSize().width, 30));
        registerButton.setPreferredSize(new Dimension(registerButton.getPreferredSize().width, 30));
        forgotPasswordButton.setPreferredSize(new Dimension(forgotPasswordButton.getPreferredSize().width, 30));
        
        // Añadir ActionListeners a los botones
        loginButton.addActionListener(e -> onClickLogin());
        registerButton.addActionListener(e -> onClickRegister());
        forgotPasswordButton.addActionListener(e -> onClickForgotPassword());
        
        buttonsPanel.add(loginButton);
        buttonsPanel.add(registerButton);
        buttonsPanel.add(forgotPasswordButton);
        
        mainPanel.add(fieldsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void onClickLogin() {
        String user = username.getText();
        String pass = new String(password.getPassword());
        
        // Autenticar usuario
        Usuarios usuario = CocoChat.usuarioController.autenticarUsuario(user, pass);
        
        if (usuario != null) {
            // Actualizar última conexion
            CocoChat.usuarioController.actualizarUltimaConexion(usuario.getUsuario_Id());
            
            // Guardar el usuario autenticado
            CocoChat.usuarioActual = usuario;
            
            // Mostrar la ventana principal
            Coco coco = new Coco();
            coco.setVisible(true);
            
            // Cerrar esta ventana
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Usuario o contraseña incorrectos", 
                "Error de autenticación", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Método para manejar el registro de nuevos usuarios
    private void onClickRegister() {
        // Crear y mostrar el formulario de registro
        Registro registroForm = new Registro(this);
        registroForm.setVisible(true);
    }
    
    // Método para manejar la recuperación de contraseña
    private void onClickForgotPassword() {
        Recuperar_Contrasena recuperarForm = new Recuperar_Contrasena(this);
        recuperarForm.setVisible(true);
    }
}
