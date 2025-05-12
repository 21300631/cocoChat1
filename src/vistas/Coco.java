package vistas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Coco extends JFrame {
    private JPanel mainPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel chatPanel;
    private JPanel onlinePanel;
    private JPanel optionsPanel;
    
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
    
    public Coco() {
        super();
        setTitle("CocoChat");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 600)); //Dimension de la pantalla completa
        
        // Fondo de pantalla principal
        UIManager.put("Panel.background", new Color(240, 255, 240)); // Verde claro
        UIManager.put("TextArea.background", new Color(255, 255, 255));
        
        initComponents();
        layoutComponents();
        
        pack();
        setLocationRelativeTo(null);
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
    JPanel amigosPanel = new JPanel();
    amigosPanel.setLayout(new BoxLayout(amigosPanel, BoxLayout.Y_AXIS));
    amigosPanel.setBorder(new EmptyBorder(5, 15, 5, 5));
    amigosPanel.setBackground(new Color(230, 255, 230));

    JLabel amigosLabel = new JLabel("Amigos");
    amigosLabel.setFont(new Font("Arial", Font.BOLD, 14));
    amigosLabel.setForeground(new Color(0, 100, 0));
    amigosLabel.setBorder(new EmptyBorder(5, 0, 5, 0));
    amigosPanel.add(amigosLabel);
    amigosPanel.add(Box.createVerticalStrut(5));

    String[] amigos = {"Etienne", "Uriel", "Rebeca"};
    for (String amix : amigos) {
        JLabel amigoLabel = new JLabel("• " + amix);
        amigoLabel.setBorder(new EmptyBorder(3, 5, 3, 5));
        amigoLabel.setOpaque(true);
        amigoLabel.setBackground(new Color(230, 255, 230));
        amigoLabel.setForeground(new Color(0, 80, 0));
        amigoLabel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                amigoLabel.setBackground(new Color(200, 230, 200));
            }
            public void mouseExited(MouseEvent e) {
                amigoLabel.setBackground(new Color(230, 255, 230));
            }
        });
        amigosPanel.add(amigoLabel);
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
            public void mouseEntered(MouseEvent e) {
                grupoLabel.setBackground(new Color(200, 230, 200));
            }
            public void mouseExited(MouseEvent e) {
                grupoLabel.setBackground(new Color(230, 255, 230));
            }
        });
        gruposPanel.add(grupoLabel);
    }

    contactsContainer.add(amigosPanel);
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
    
    JLabel chatTitle = new JLabel("Chat Global");
    chatTitle.setFont(new Font("Arial", Font.BOLD, 14));
    chatTitle.setForeground(new Color(0, 100, 0));
    chatHeaderPanel.add(chatTitle, BorderLayout.WEST);
    
    searchField = new JTextField("Buscar en [Nombre de amigo o grupo]", 20);
    searchField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    chatHeaderPanel.add(searchField, BorderLayout.EAST);
    
    rightPanel.add(chatHeaderPanel, BorderLayout.NORTH);

    // Área de mensajes
    chatPanel = new JPanel(new BorderLayout());
    chatPanel.setBackground(new Color(240, 255, 240));

    JPanel messagesContainer = new JPanel();
    messagesContainer.setLayout(new BoxLayout(messagesContainer, BoxLayout.Y_AXIS));
    messagesContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
    messagesContainer.setBackground(new Color(240, 255, 240));

    // Mensaje de amigo
    JPanel friendMessagePanel = new JPanel();
    friendMessagePanel.setLayout(new BoxLayout(friendMessagePanel, BoxLayout.X_AXIS));
    friendMessagePanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
    friendMessagePanel.setOpaque(false);

    
    JTextPane friendMessage = new JTextPane();
    friendMessage.setEditable(false);
    friendMessage.setContentType("text/html");
    friendMessage.setText("<html><div style='background:#e0f0e0; padding:8px; border-radius:8px; max-width:70%; color:#005000;'>"
        + "<b style='color:#003000;'>amigo x</b><br>"
        + "Mensaje que hayan enviado largo para separar"
        + "</div></html>");
    friendMessage.setBorder(new EmptyBorder(5, 5, 5, 5));

    friendMessagePanel.add(friendMessage);
    friendMessagePanel.add(Box.createHorizontalGlue());

    // Cosa separadora
    JLabel separator = new JLabel(" ");
    separator.setBorder(new EmptyBorder(10, 0, 10, 0));

    // Mensaje del usuario
    JPanel userMessagePanel = new JPanel();
    userMessagePanel.setLayout(new BoxLayout(userMessagePanel, BoxLayout.X_AXIS));
    userMessagePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    userMessagePanel.setOpaque(false);

    JTextPane userMessage = new JTextPane();
    userMessage.setEditable(false);
    userMessage.setContentType("text/html");
    userMessage.setText("<html><div style='background:#c0e0c0; padding:8px; border-radius:8px; max-width:70%; color:#004000;'>"
        + "<b style='color:#002000;'>Tú</b><br>"
        + "Mensaje que hayas enviado largo para separar"
        + "</div></html>");
    userMessage.setBorder(new EmptyBorder(5, 5, 5, 5));

    userMessagePanel.add(Box.createHorizontalGlue());
    userMessagePanel.add(userMessage);

    messagesContainer.add(friendMessagePanel);
    messagesContainer.add(separator);
    messagesContainer.add(userMessagePanel);

    JScrollPane chatScrollPane = new JScrollPane(messagesContainer);
    chatScrollPane.setBorder(null);

    chatPanel.add(chatScrollPane, BorderLayout.CENTER);
    rightPanel.add(chatPanel, BorderLayout.CENTER);

    // Panel para escribir los mensajes
    JPanel messageInputPanel = new JPanel(new BorderLayout());
    messageInputPanel.setBackground(new Color(220, 240, 220));
    messageInputPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

    messageField = new JTextField();
    JButton sendBtn = new JButton("Enviar");
    sendBtn.setBackground(new Color(0, 150, 0));
    sendBtn.setForeground(Color.WHITE);
    sendBtn.setFocusPainted(false);

    messageInputPanel.add(messageField, BorderLayout.CENTER);
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
    createGroupBtn = createStyledButton("(2) Crear Grupo", new Color(0, 130, 0));
    addPersonBtn = createStyledButton("(3) Agregar persona", new Color(150, 150, 150));
    sendMessageBtn = createStyledButton("(4) Enviar Mensaje", new Color(0, 140, 0));
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
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Coco form = new Coco();
            form.setVisible(true);
        });
    }
}