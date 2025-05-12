package vistas;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 * Si quieres integrar la parte de la base de datos lo que buscas son las funciones onClickLogin(), onClickRegister() y quiza onClickForgotPassword()
 * Cualquier duda o cosa q quieran cambiar sientanse libres o mandenme un DM :D
 * deje unos cuantos comentarios a lo largo del codigo, ojala se entienda lmao
 * @author babalito01
 */
public class LogIn extends JFrame{
    
    JLabel usernameLabel, passwordLabel;
    JTextField username;
    JPasswordField password;
    JButton login, register, forgotPassword;
    
    public LogIn(){
        super();
        init();
    }
    
    private void init(){
        setTitle("CocoChat - Login");
        //ADVERTENCIA: la vdd no estoy 100% seguro de que operacion de cerrado debe utilizar esta ventana, 
        // ya que ps en teoria nomas es un popup, entonces si da error calen a moverle a esta linea
        setDefaultCloseOperation(EXIT_ON_CLOSE); 
        
        // aqui se inicializan los botones y establezco las funciones cuando se clickean
        login = new JButton("Iniciar Sesion");
        login.addActionListener(a -> {
            onClickLogin();
        });
        register = new JButton("Registrarse");
        register.addActionListener(a -> {
            onClickRegister();
        });
        forgotPassword = new JButton("Olvide mi contraseña");
        forgotPassword.addActionListener(a -> {
            onClickForgotPassword();
        });
        
        // se inicializan los campos de texto y las etiquetas para que sepan lo que es cada una
        usernameLabel = new JLabel("Nombre de usuario:");
        username = new JTextField();
        passwordLabel = new JLabel("Contraseña:");
        password = new JPasswordField();
        
        GroupLayout gl = new GroupLayout(getContentPane());
        gl.setAutoCreateContainerGaps(true);
        gl.setAutoCreateGaps(true);
        gl.linkSize(SwingConstants.HORIZONTAL, login, register, forgotPassword);//esto sirve pa q los botones tengan la misma anchura ;)
        
        gl.setHorizontalGroup(
            gl.createParallelGroup()
                .addComponent(usernameLabel)
                .addComponent(username)
                .addComponent(passwordLabel)
                .addComponent(password)
                .addGroup(
                    gl.createSequentialGroup()//el chiste de hacer un grupo secuencial y luego otro paralelo es que los botones midan menos que los campos de texto y esten centrados
                        .addGap(50)// <--  para eso se agregan estas gaps
                        .addGroup(
                            gl.createParallelGroup()
                                .addComponent(login)
                                .addComponent(register)
                                .addComponent(forgotPassword)
                        )
                        .addGap(50)
                )
        );
        gl.setVerticalGroup(
            gl.createSequentialGroup()
                .addComponent(usernameLabel)
                .addComponent(username)
                .addComponent(passwordLabel)
                .addComponent(password)
                .addGap(15)
                .addComponent(login)
                .addComponent(register)
                .addComponent(forgotPassword)
        );
        
        setLayout(gl);
        pack();

        setResizable(false);// para que no puedan cambiar el tamaño de la ventana y se vaya alv
        setLocationRelativeTo(null);// esto es para que la ventana aparezca en el centro de la pantalla
    }
    
    private void onClickLogin(){
        String user = username.getText().trim();//extrae el nombre de usuario y contraseña de los campos de texto
        String pass = new String(password.getPassword()).trim();

        // el siguiente if detecta si hay campos vacios y crea un popup de error
        if(user.isEmpty() || pass.isEmpty()) {
            String mensajeError = "Error: Tiene que rellenar todos los campos para iniciar sesion";
            JOptionPane.showMessageDialog(
                this, 
                mensajeError, 
                "Error al iniciar sesion", 
                JOptionPane.ERROR_MESSAGE
            );
        } else {
            //AQUI VA LA FUNCION PARA CUANDO ESTEN LOS CAMPOS LLENOS
        }
    }
    
    private void onClickRegister(){
        //AQUI VA LA FUNCION PARA ABRIR LA VENTANA DE REGISTRO
    }
    
private void onClickForgotPassword() {
    JDialog recoveryDialog = new JDialog(this, "Recuperar Contraseña", true);
    recoveryDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    recoveryDialog.setResizable(false);

    // Componentes de la ventana
    JLabel usernameLabel = new JLabel("Nombre de usuario:");
    JTextField usernameField = new JTextField(15);
    JButton submitButton = new JButton("Enviar");
    JButton backButton = new JButton("Volver al Login");

    // Acción para el botón de enviar
    submitButton.addActionListener(e -> {
        String username = usernameField.getText().trim();
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(
                recoveryDialog,
                "Por favor ingresa tu nombre de usuario.",
                "Campo vacío",
                JOptionPane.ERROR_MESSAGE
            );
        } else {
            // Lógica para recuperar contraseña (simulada)
            JOptionPane.showMessageDialog(
                recoveryDialog,
                "Se ha enviado un enlace de recuperación al correo asociado a " + username,
                "Instrucciones enviadas",
                JOptionPane.INFORMATION_MESSAGE
            );
            recoveryDialog.dispose();
        }
    });

    // Acción para el botón de volver
    backButton.addActionListener(e -> recoveryDialog.dispose());

    // Configuración del layout
    GroupLayout gl = new GroupLayout(recoveryDialog.getContentPane());
    gl.setAutoCreateContainerGaps(true);
    gl.setAutoCreateGaps(true);
    gl.linkSize(SwingConstants.HORIZONTAL, submitButton, backButton);

    gl.setHorizontalGroup(
        gl.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addGroup(gl.createSequentialGroup()
                .addGroup(gl.createParallelGroup()
                    .addComponent(usernameLabel)
                    .addComponent(usernameField)
                    .addGroup(gl.createSequentialGroup()
                        .addComponent(submitButton)
                        .addComponent(backButton)
                )
            )
                    )
    );

    gl.setVerticalGroup(
        gl.createSequentialGroup()
            .addComponent(usernameLabel)
            .addComponent(usernameField)
            .addGap(20)
            .addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(submitButton)
                .addComponent(backButton))
    );

    recoveryDialog.getContentPane().setLayout(gl);
    recoveryDialog.pack();
    recoveryDialog.setLocationRelativeTo(this);
    recoveryDialog.setVisible(true);
} 
}
