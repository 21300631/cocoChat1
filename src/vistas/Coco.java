package vistas;

import cocochat.CocoChat;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import modelos.*;

public class Coco extends JFrame {
    private JPanel mainPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel chatPanel;
    private JPanel onlinePanel;
    private JPanel optionsPanel;
    private JPanel panelAmigos;
    private JPanel panelMensajes;
    
    private JButton addFriendBtn;
    private JButton createGroupBtn;
    private JButton addPersonBtn;
    private JButton sendMessageBtn;
    private JButton tagPersonBtn;
    private JButton attachFileBtn;
    private JButton tempMessageBtn;
    private JButton settingsBtn;
    private JButton chatWithFriendBtn;
    
    private JTextField searchField;
    private JTextArea chatArea;
    private JTextField messageField;
    
    private Usuarios contactoActual; // Variable para el contacto actual
    private JTextField txtMensaje; // Campo de texto para el mensaje
    private JLabel lblChatActual; // Etiqueta para el título del chat
    private Timer timer; // Timer para actualizar usuarios en línea
    private Timer messageUpdateTimer; // Declarar como variable de clase
    
    public Coco() {
        initComponents();
        layoutComponents();
        setPreferredSize(new Dimension(720, 480));
        setMinimumSize(new Dimension(720, 480)); // Tamaño mínimo para evitar ventana demasiado pequeña
        setSize(720, 480);                       // Establecer tamaño inicial
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Esta línea asegura que se cierre el programa
        setLocationRelativeTo(null);             // Centrar en la pantalla
        setResizable(true);
        
        // Agregar un listener para manejar el cierre correctamente
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Detener el timer para liberar recursos
                if (timer != null) {
                    timer.stop();
                }
                if (messageUpdateTimer != null) {
                    messageUpdateTimer.stop();
                }
                // Cerrar la aplicación
                System.exit(0);
            }
        });
        
        // Verificar si hay un usuario actual
        if (CocoChat.usuarioActual == null) {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this, "No hay sesión activa", 
                                             "Error", JOptionPane.ERROR_MESSAGE);
                this.dispose();
            });
            return;
        }
        
        // Cargar datos iniciales después de que el constructor termine
        SwingUtilities.invokeLater(() -> cargarDatosIniciales());
        
        // Configurar timer para actualizar usuarios en línea periódicamente
        timer = new Timer(30000, e -> actualizarUsuariosEnLinea());
        timer.start();
        
        // Configurar timer para actualizar mensajes si hay un chat activo (cada 3 segundos)
        messageUpdateTimer = new Timer(3000, e -> actualizarMensajesChat());
        messageUpdateTimer.start();
    }
    
    private void initComponents() {
    // Panel principal con fondo verde claro
    mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
    mainPanel.setBackground(new Color(240, 255, 240));

    // ========== PANEL IZQUIERDO ==========
    // (Esta es la seccion de los contactos y grupos)
    leftPanel = new JPanel();
    leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
    leftPanel.setPreferredSize(new Dimension(220, 0));
    leftPanel.setBackground(new Color(230, 255, 230));

    // 1->  BUSCADOR DE AMIXES
    JPanel buscadorPanel = new JPanel(new BorderLayout());
    buscadorPanel.setBackground(new Color(220, 255, 220));
    buscadorPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
    
    JTextField buscadorCampo = new JTextField();
    buscadorCampo.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder("Buscador"),
        BorderFactory.createEmptyBorder(3, 5, 3, 5)
    ));
    buscadorCampo.setToolTipText("Buscar amigos o grupos");
    buscadorCampo.setPreferredSize(new Dimension(buscadorCampo.getPreferredSize().width, 25));
    buscadorCampo.setFont(new Font("Arial", Font.PLAIN, 12));
    buscadorPanel.add(buscadorCampo, BorderLayout.CENTER);

    // 2-> LISTA DE CONTACTOS
    JPanel contactsContainer = new JPanel();
    contactsContainer.setLayout(new BoxLayout(contactsContainer, BoxLayout.Y_AXIS));
    contactsContainer.setBackground(new Color(230, 255, 230));

    // Sección de Amigos
    panelAmigos = new JPanel();
    panelAmigos.setLayout(new BoxLayout(panelAmigos, BoxLayout.Y_AXIS));
    panelAmigos.setBorder(new EmptyBorder(5, 15, 5, 5));
    panelAmigos.setBackground(new Color(230, 255, 230));

    JLabel amigosLabel = new JLabel("Amigos");
    amigosLabel.setFont(new Font("Arial", Font.BOLD, 14));
    amigosLabel.setForeground(new Color(0, 100, 0));
    amigosLabel.setBorder(new EmptyBorder(5, 0, 5, 0));
    panelAmigos.add(amigosLabel);
    panelAmigos.add(Box.createVerticalStrut(5));

    String[] amigos = {"Etienne", "Uriel", "Rebeca"};
    for (String amix : amigos) {
        JLabel amigoLabel = new JLabel("• " + amix);
        amigoLabel.setBorder(new EmptyBorder(3, 5, 3, 5));
        amigoLabel.setOpaque(true);
        amigoLabel.setBackground(new Color(230, 255, 230));
        amigoLabel.setForeground(new Color(0, 80, 0));
        amigoLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                amigoLabel.setBackground(new Color(200, 230, 200));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                amigoLabel.setBackground(new Color(230, 255, 230));
            }
        });
        panelAmigos.add(amigoLabel);
    }

    // Sección de Grupos
    JPanel gruposPanel = new JPanel();
    gruposPanel.setLayout(new BoxLayout(gruposPanel, BoxLayout.Y_AXIS));
    gruposPanel.setBorder(new EmptyBorder(15, 15, 5, 5));
    gruposPanel.setBackground(new Color(230, 255, 230));

    JLabel gruposLabel = new JLabel("Grupos");
    gruposLabel.setFont(new Font("Arial", Font.BOLD, 14));
    gruposLabel.setForeground(new Color(0, 100, 0));
    gruposLabel.setBorder(new EmptyBorder(5, 0, 5, 0));
    gruposPanel.add(gruposLabel);
    gruposPanel.add(Box.createVerticalStrut(5));

    String[] grupos = {"Osciloscopios", "Potaxies", "Equipo Dinamita"};
    for (String group : grupos) {
        JLabel grupoLabel = new JLabel("✧ " + group);
        grupoLabel.setBorder(new EmptyBorder(3, 5, 3, 5));
        grupoLabel.setOpaque(true);
        grupoLabel.setBackground(new Color(230, 255, 230));
        grupoLabel.setForeground(new Color(0, 120, 0));
        grupoLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                grupoLabel.setBackground(new Color(200, 230, 200));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                grupoLabel.setBackground(new Color(230, 255, 230));
            }
        });
        gruposPanel.add(grupoLabel);
    }

    contactsContainer.add(panelAmigos);
    contactsContainer.add(gruposPanel);

    JScrollPane scrollPane = new JScrollPane(contactsContainer);
    scrollPane.setBorder(null);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    leftPanel.add(buscadorPanel);
    leftPanel.add(Box.createVerticalStrut(10));
    leftPanel.add(scrollPane);

    // ========== PANEL CENTRAL ==========
    // (Esta es la parte que contiene el chat)
    rightPanel = new JPanel(new BorderLayout());
    rightPanel.setBackground(new Color(240, 255, 240));

    // Encabezado del chat
    JPanel chatHeaderPanel = new JPanel(new BorderLayout());
    chatHeaderPanel.setBackground(new Color(220, 240, 220));
    
    lblChatActual = new JLabel("Chat Global");
    lblChatActual.setFont(new Font("Arial", Font.BOLD, 14));
    lblChatActual.setForeground(new Color(0, 100, 0));
    chatHeaderPanel.add(lblChatActual, BorderLayout.WEST);
    
    searchField = new JTextField("Buscar en [Nombre de amigo o grupo]", 20);
    searchField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    chatHeaderPanel.add(searchField, BorderLayout.EAST);
    
    rightPanel.add(chatHeaderPanel, BorderLayout.NORTH);

    // Área de mensajes
    chatPanel = new JPanel(new BorderLayout());
    chatPanel.setBackground(new Color(240, 255, 240));

    panelMensajes = new JPanel();
    panelMensajes.setLayout(new BoxLayout(panelMensajes, BoxLayout.Y_AXIS));
    panelMensajes.setBorder(new EmptyBorder(10, 10, 10, 10));
    panelMensajes.setBackground(new Color(240, 255, 240));

    JScrollPane chatScrollPane = new JScrollPane(panelMensajes);
    chatScrollPane.setBorder(null);

    chatPanel.add(chatScrollPane, BorderLayout.CENTER);
    rightPanel.add(chatPanel, BorderLayout.CENTER);

    // Panel para escribir los mensajes
    JPanel messageInputPanel = new JPanel(new BorderLayout());
    messageInputPanel.setBackground(new Color(220, 240, 220));
    messageInputPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

    txtMensaje = new JTextField();
    JButton sendBtn = new JButton("Enviar");
    sendBtn.setBackground(new Color(0, 150, 0));
    sendBtn.setForeground(Color.WHITE);
    sendBtn.setFocusPainted(false);
    sendBtn.addActionListener(this::btnEnviarMensajeActionPerformed);

    messageInputPanel.add(txtMensaje, BorderLayout.CENTER);
    messageInputPanel.add(sendBtn, BorderLayout.EAST);
    rightPanel.add(messageInputPanel, BorderLayout.SOUTH);

    // ========== PANEL DERECHO ==========
    // Aqui van los usuarios que estan en linea
    onlinePanel = new JPanel();
    onlinePanel.setLayout(new BoxLayout(onlinePanel, BoxLayout.Y_AXIS));
    onlinePanel.setBorder(BorderFactory.createTitledBorder("En Linea"));
    onlinePanel.setPreferredSize(new Dimension(150, 0));
    onlinePanel.setBackground(new Color(230, 255, 230));

    String[] onlineUsers = {"Uriel", "Ozuna", "Mia Khalifa", "Zahid"};
    for (String user : onlineUsers) {
        JLabel userLabel = new JLabel("• " + user);
        userLabel.setForeground(new Color(0, 100, 0));
        userLabel.setBorder(new EmptyBorder(3, 5, 3, 5));
        onlinePanel.add(userLabel);
    }

    // ========== PANEL INFERIOR ===========
    // Todos los botones
    optionsPanel = new JPanel(new GridLayout(5, 2, 5, 5));
    optionsPanel.setBackground(new Color(220, 240, 220));
    optionsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

    // Aqui se crean los botones
    addFriendBtn = createStyledButton("(1) Agregar Amigo", new Color(0, 150, 0));
    addFriendBtn.addActionListener(this::btnAgregarAmigoActionPerformed);
    createGroupBtn = createStyledButton("(2) Crear Grupo", new Color(0, 130, 0));
    addPersonBtn = createStyledButton("(3) Agregar persona", new Color(150, 150, 150));
    sendMessageBtn = createStyledButton("(4) Enviar Mensaje", new Color(0, 140, 0));
    sendMessageBtn.addActionListener(this::btnEnviarMensajeActionPerformed);
    tagPersonBtn = createStyledButton("(5) Etiquetar Persona", new Color(0, 120, 0));
    attachFileBtn = createStyledButton("(6) Adjuntar archivo", new Color(0, 130, 0));
    tempMessageBtn = createStyledButton("(7) Mensaje Temporal", new Color(0, 110, 0));
    settingsBtn = createStyledButton("(8) Opciones", new Color(0, 100, 0));
    chatWithFriendBtn = createStyledButton("(9) Chatear con amigo", new Color(0, 160, 0));

    addPersonBtn.setToolTipText("Deshabilitado en chat global"); 
    addPersonBtn.setEnabled(false);

    optionsPanel.add(addFriendBtn);
    optionsPanel.add(createGroupBtn);
    optionsPanel.add(addPersonBtn);
    optionsPanel.add(sendMessageBtn);
    optionsPanel.add(tagPersonBtn);
    optionsPanel.add(attachFileBtn);
    optionsPanel.add(tempMessageBtn);
    optionsPanel.add(settingsBtn);
    optionsPanel.add(chatWithFriendBtn);
}
    
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setMargin(new Insets(5, 5, 5, 5));
        button.setFocusPainted(false);
        button.setBackground(color);
        return button;
    }
    
    private void layoutComponents() {
        // Aqui llamo a todos mis paneles
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        mainPanel.add(onlinePanel, BorderLayout.EAST);
        mainPanel.add(optionsPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    // Método para cargar datos iniciales
    // Este método se llama al iniciar la aplicación
    public void cargarDatosIniciales() {
        cargarContactos();
        cargarGrupos();
        cargarUsuariosEnLinea();
    }

    // Método para cargar los grupos
    private void cargarGrupos() {
        // Obtener lista de grupos del usuario actual
        List<Grupos> grupos = CocoChat.gruposController.getGruposByUsuario(
                              CocoChat.usuarioActual.getUsuario_Id());
        
        // Limpiar panel de grupos
        onlinePanel.removeAll();
        
        // Agregar cada grupo al panel
        for (Grupos grupo : grupos) {
            JLabel grupoLabel = new JLabel("✧ " + grupo.getNombre());
            grupoLabel.setForeground(new Color(0, 100, 0));
            grupoLabel.setBorder(new EmptyBorder(3, 5, 3, 5));
            onlinePanel.add(grupoLabel);
        }
        
        onlinePanel.revalidate();
        onlinePanel.repaint();
    }

    // Método para cargar los usuarios en línea
    private void cargarUsuariosEnLinea() {
        // Obtener lista de usuarios en línea
        List<Usuarios> usuariosEnLinea = CocoChat.usuarioController.getUsuariosEnLinea();
        
        // Limpiar panel de usuarios en línea
        onlinePanel.removeAll();
        
        // Agregar cada usuario al panel
        for (Usuarios usuario : usuariosEnLinea) {
            JLabel usuarioLabel = new JLabel("• " + usuario.getNombre());
            usuarioLabel.setForeground(new Color(0, 100, 0));
            usuarioLabel.setBorder(new EmptyBorder(3, 5, 3, 5));
            onlinePanel.add(usuarioLabel);
        }
        
        onlinePanel.revalidate();
        onlinePanel.repaint();
    }
    
    private void actualizarUsuariosEnLinea() {
        try {
            // Obtener usuarios en línea (últimos 5 minutos)
            List<Usuarios> usuariosEnLinea = CocoChat.usuarioController.getUsuariosEnLinea();
            
            // Depuración - ver cuántos usuarios están en línea
            System.out.println("Usuarios en línea encontrados: " + usuariosEnLinea.size());
            
            // Limpiar panel
            onlinePanel.removeAll();
            
            // Mostrar cada usuario en línea
            for (Usuarios usuario : usuariosEnLinea) {
                // No mostrar al usuario actual
                if (usuario.getUsuario_Id() != CocoChat.usuarioActual.getUsuario_Id()) {
                    JLabel lblUsuario = new JLabel("• " + usuario.getNombre() + " (online)");
                    lblUsuario.setForeground(new Color(0, 150, 0)); // Verde para usuarios en línea
                    onlinePanel.add(lblUsuario);
                }
            }
            
            onlinePanel.revalidate();
            onlinePanel.repaint();
        } catch (Exception e) {
            System.err.println("Error al actualizar usuarios en línea: " + e.getMessage());
        }
    }
    
    // Método para cargar los contactos
    // Este método se llama al iniciar la aplicación
    private void cargarContactos() {
        // Obtener lista de contactos del usuario actual
        List<Usuarios> contactos = CocoChat.contactosController.getContactosDetalladosByUsuario(
                                  CocoChat.usuarioActual.getUsuario_Id());
        
        // Limpiar panel de amigos
        panelAmigos.removeAll();
        
        // Agregar cada contacto al panel
        for (Usuarios contacto : contactos) {
            JPanel panelContacto = crearPanelContacto(contacto);
            panelAmigos.add(panelContacto);
        }
        
        panelAmigos.revalidate();
        panelAmigos.repaint();
    }

    //
    private JPanel crearPanelContacto(Usuarios contacto) {
        // Crear un panel para mostrar el contacto
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
        JLabel labelNombre = new JLabel(contacto.getNombre() + " " + contacto.getApellido());
        panel.add(labelNombre, BorderLayout.CENTER);
        
        // Agregar listener para abrir chat al hacer clic
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                abrirChat(contacto);
            }
        });
        
        return panel;
    }
    
    private void abrirChat(Usuarios contacto) {
        // Guardar el contacto seleccionado
        contactoActual = contacto;
        
        // Actualizar título del chat
        lblChatActual.setText("Chat con: " + contacto.getNombre() + " " + contacto.getApellido());
        
        // Habilitar botón para agregar persona al chat
        addPersonBtn.setEnabled(true);
        
        // Cargar los mensajes con el contacto
        cargarMensajesConContacto(contacto);
    }
    
    private void cargarMensajesConContacto(Usuarios contacto) {
        // Guardar el contacto actual
        this.contactoActual = contacto;
        
        // Actualizar título con el nombre del contacto
        lblChatActual.setText("Chat con: " + contacto.getNombre() + " " + contacto.getApellido());
        
        // Limpiar panel de mensajes
        panelMensajes.removeAll();
        
        // Obtener mensajes
        List<Mensajes_Individuales> mensajes = CocoChat.mensajesController.getMensajesByUsuarios(
                                             CocoChat.usuarioActual.getUsuario_Id(), 
                                             contacto.getUsuario_Id());
        
        // Mostrar cada mensaje
        for (Mensajes_Individuales msj : mensajes) {
            JPanel panelMensaje = crearPanelMensaje(msj);
            panelMensajes.add(panelMensaje);
        }
        
        panelMensajes.revalidate();
        panelMensajes.repaint();
    }
    
    private JPanel crearPanelMensaje(Mensajes_Individuales mensaje) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        boolean esMensajePropio = mensaje.getRemitenteID() == CocoChat.usuarioActual.getUsuario_Id();
        
        JLabel lblMensaje = new JLabel();
        lblMensaje.setText(mensaje.getContenido());
        lblMensaje.setOpaque(true);
        lblMensaje.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(8, 10, 8, 10),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        
        // Estilo diferente para mensajes propios y recibidos
        if (esMensajePropio) {
            lblMensaje.setBackground(new Color(220, 248, 220));
            lblMensaje.setHorizontalAlignment(SwingConstants.RIGHT);
            panel.add(lblMensaje, BorderLayout.EAST);
            panel.add(new JLabel("Tú"), BorderLayout.NORTH);
        } else {
            lblMensaje.setBackground(new Color(240, 240, 240));
            lblMensaje.setHorizontalAlignment(SwingConstants.LEFT);
            panel.add(lblMensaje, BorderLayout.WEST);
            panel.add(new JLabel(contactoActual.getNombre()), BorderLayout.NORTH);
        }
        
        return panel;
    }
    
    // Método para actualizar mensajes del chat actual
    private void actualizarMensajesChat() {
        // Solo actualizar si hay un chat activo
        if (contactoActual != null) {
            // Guardar posición de scroll actual para mantener la vista
            JScrollPane scrollPane = (JScrollPane) panelMensajes.getParent().getParent();
            int currentValue = scrollPane.getVerticalScrollBar().getValue();
            int maxValue = scrollPane.getVerticalScrollBar().getMaximum();
            
            // Obtener mensajes actualizados
            List<Mensajes_Individuales> mensajes = CocoChat.mensajesController.getMensajesByUsuarios(
                                                 CocoChat.usuarioActual.getUsuario_Id(), 
                                                 contactoActual.getUsuario_Id());
            
            // Verificar si hay mensajes nuevos (comparando con el total actual)
            if (mensajes.size() != panelMensajes.getComponentCount()) {
                // Limpiar y recargar todos los mensajes
                panelMensajes.removeAll();
                
                // Mostrar cada mensaje
                for (Mensajes_Individuales msj : mensajes) {
                    JPanel panelMensaje = crearPanelMensaje(msj);
                    panelMensajes.add(panelMensaje);
                }
                
                panelMensajes.revalidate();
                panelMensajes.repaint();
                
                // Si estaba al final del scroll, mantenerlo al final
                if (maxValue - currentValue <= 50) { // cerca del final
                    SwingUtilities.invokeLater(() -> {
                        JScrollBar vbar = scrollPane.getVerticalScrollBar();
                        vbar.setValue(vbar.getMaximum());
                    });
                } else {
                    // Sino, mantener la posición de scroll
                    SwingUtilities.invokeLater(() -> {
                        scrollPane.getVerticalScrollBar().setValue(currentValue);
                    });
                }
            }
            
            // Actualizar última conexión del usuario actual (mantenerse en línea)
            CocoChat.usuarioController.actualizarUltimaConexion(CocoChat.usuarioActual.getUsuario_Id());
        }
    }
    
    // (1) Agregar Amigo
    private void btnAgregarAmigoActionPerformed(ActionEvent evt) {
        String email = JOptionPane.showInputDialog(this, "Email del usuario a agregar:");
        
        if (email != null && !email.trim().isEmpty()) {
            Usuarios nuevoAmigo = CocoChat.usuarioController.getByEmail(email);
            
            if (nuevoAmigo != null) {
                // No permitir agregarse a sí mismo
                if (nuevoAmigo.getUsuario_Id() == CocoChat.usuarioActual.getUsuario_Id()) {
                    JOptionPane.showMessageDialog(this, "No puedes agregarte a ti mismo como amigo.");
                    return;
                }
                
                // 1. Crear contacto: Usuario actual -> Amigo nuevo
                Contactos contactoDirecto = new Contactos(
                    0, // ID se generará automáticamente 
                    CocoChat.usuarioActual.getUsuario_Id(), // UsuarioID (quien agrega)
                    nuevoAmigo.getUsuario_Id(), // ContactoUsuarioID (a quien se agrega)
                    java.time.LocalDateTime.now(),
                    false // No bloqueado
                );
                
                // 2. Crear contacto: Amigo nuevo -> Usuario actual (relación inversa)
                Contactos contactoReverso = new Contactos(
                    0, // ID se generará automáticamente
                    nuevoAmigo.getUsuario_Id(), // UsuarioID (amigo)
                    CocoChat.usuarioActual.getUsuario_Id(), // ContactoUsuarioID (usuario actual)
                    java.time.LocalDateTime.now(),
                    false // No bloqueado
                );
                
                // Agregar ambas relaciones a la BD
                CocoChat.contactosController.add(contactoDirecto);
                CocoChat.contactosController.add(contactoReverso);
                
                JOptionPane.showMessageDialog(this, 
                    "Has agregado a " + nuevoAmigo.getNombre() + " a tus contactos.",
                    "Contacto agregado", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Actualizar la lista de contactos
                cargarContactos();
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró ningún usuario con ese email.");
            }
        }
    }

    // (4) Enviar Mensaje
    private void btnEnviarMensajeActionPerformed(ActionEvent evt) {
        // Verificar que haya un chat activo y un mensaje
        String mensaje = txtMensaje.getText().trim();
        
        if (contactoActual != null && !mensaje.isEmpty()) {
            // Crear objeto mensaje
            Mensajes_Individuales nuevoMensaje = new Mensajes_Individuales();
            nuevoMensaje.setRemitenteID(CocoChat.usuarioActual.getUsuario_Id());
            nuevoMensaje.setDestinatarioID(contactoActual.getUsuario_Id());
            nuevoMensaje.setContenido(mensaje);
            nuevoMensaje.setEsArchivado(false);
            
            // Enviar mensaje
            CocoChat.mensajesController.add(nuevoMensaje);
            
            // Limpiar campo y actualizar chat
            txtMensaje.setText("");
            cargarMensajesConContacto(contactoActual);
        }
    }
    
    @Override
    public void dispose() {
        if (timer != null) {
            timer.stop();
        }
        if (messageUpdateTimer != null) {
            messageUpdateTimer.stop();
        }
        super.dispose();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Coco form = new Coco();
            form.setVisible(true);
        });
    }
}