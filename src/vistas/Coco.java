package vistas;

import cocochat.CocoChat;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import modelos.*;

public class Coco extends JFrame {
    // Paneles principales
    private JPanel mainPanel;
    private Contactos_Panel leftPanel;
    private Chat chatPanel;
    private Usuarios_Linea onlinePanel;
    private Opciones Opciones;
    
    // Timer para actualizaciones
    private Timer timer;
    private Timer messageUpdateTimer;
    
    // Usuario con el que se está chateando actualmente
    private Usuarios contactoActual;
    
    public Coco() {
        setTitle("CocoChat - " + CocoChat.usuarioActual.getNombre());
        initComponents();
        layoutComponents();
        configureWindow();
        initTimers();
        
        // Verificar si hay un usuario actual
        if (CocoChat.usuarioActual == null) {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this, "No hay sesión activa", 
                                             "Error", JOptionPane.ERROR_MESSAGE);
                this.dispose();
            });
            return;
        }
        
        // Cargar datos iniciales
        SwingUtilities.invokeLater(() -> cargarDatosIniciales());
    }
    
    public Coco(Usuarios usuario) {
        setTitle("CocoChat - " + CocoChat.usuarioActual.getNombre());
        initComponents();
        layoutComponents();
        configureWindow();
        initTimers();
        
        // Verificar si hay un usuario actual
        if (CocoChat.usuarioActual == null) {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this, "No hay sesión activa", 
                                             "Error", JOptionPane.ERROR_MESSAGE);
                this.dispose();
            });
            return;
        }
        
        // Cargar datos iniciales
        SwingUtilities.invokeLater(() -> cargarDatosIniciales());
        
        SwingUtilities.invokeLater(() -> {
            // Buscar el grupo Global (ID = 2)
            for (Grupos grupo : CocoChat.gruposController.getGruposByUsuario(CocoChat.usuarioActual.getUsuario_Id())) {
                if (grupo.getGrupoID() == 2) {
                    chatPanel.mostrarChatGrupo(grupo);
                    break;
                }
            }
        });
    }
    
    private void initComponents() {
        // Panel principal
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(240, 255, 240));
        
        // Crear los paneles específicos
        leftPanel = new Contactos_Panel(this);
        chatPanel = new Chat(this);
        onlinePanel = new Usuarios_Linea();
        Opciones = new Opciones(this);
    }
    
    private void layoutComponents() {
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(chatPanel, BorderLayout.CENTER);
        mainPanel.add(onlinePanel, BorderLayout.EAST);
        mainPanel.add(Opciones, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void configureWindow() {
        setPreferredSize(new Dimension(720, 480));
        setMinimumSize(new Dimension(720, 480));
        setSize(720, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Manejar el cierre de la ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose(); // Usar dispose() en lugar de System.exit(0)
            }
        });
    }
    
    private void initTimers() {
        // Timer para actualizar usuarios en línea
        timer = new Timer(30000, e -> onlinePanel.actualizarUsuariosEnLinea());
        timer.start();
        
        // Timer para actualizar mensajes
        messageUpdateTimer = new Timer(3000, e -> chatPanel.actualizarMensajesChat());
        messageUpdateTimer.start();
    }
    
    private void stopTimers() {
        // Actualizar estado a offline antes de cerrar
        if (CocoChat.usuarioActual != null) {
            try {
                // Cambiar estado a offline (0)
                CocoChat.usuarioController.actualizarEstadoUsuario(CocoChat.usuarioActual.getUsuario_Id(), 0);
                System.out.println("Usuario marcado como offline");
            } catch (Exception ex) {
                System.err.println("Error al actualizar estado de usuario: " + ex.getMessage());
            }
        }
        
        // Detener timers
        if (timer != null) timer.stop();
        if (messageUpdateTimer != null) messageUpdateTimer.stop();
    }
    
    /**
     * Actualiza el estado del usuario en la base de datos
     * @param usuarioId ID del usuario a actualizar
     * @param estado Nuevo estado (0=offline, 1=online)
     */
    public void actualizarEstadoUsuario(int usuarioId, int estado) {
        try {
            CocoChat.usuarioController.actualizarEstadoUsuario(usuarioId, estado);
            System.out.println("Estado de usuario actualizado a: " + (estado == 1 ? "online" : "offline"));
        } catch (Exception ex) {
            System.err.println("Error al actualizar estado de usuario: " + ex.getMessage());
        }
    }
    
    public void cargarDatosIniciales() {
        leftPanel.cargarContactos();
        leftPanel.cargarGrupos();
        onlinePanel.cargarUsuariosEnLinea();
    }
    
    // Métodos de acceso para la comunicación entre paneles
    public void setContactoActual(Usuarios contacto) {
        this.contactoActual = contacto;
        chatPanel.setContactoActual(contacto);
        Opciones.habilitarBotonesChat(contacto != null);
    }
    
    public Usuarios getContactoActual() {
        return contactoActual;
    }
    
    /**
     * Método de acceso al panel de chat
     * @return El panel de chat
     */
    public Chat getChatPanel() {
        return this.chatPanel;
    }
    
    /**
     * Método de acceso al panel de contactos
     * @return El panel de contactos
     */
    public Contactos_Panel getContactosPanel() {
        return this.leftPanel;
    }
    
    @Override
    public void dispose() {
        stopTimers(); // Esto actualizará el estado a offline y detendrá los timers
        super.dispose();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Coco form = new Coco();
            form.setVisible(true);
        });
    }
}