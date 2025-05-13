package vistas;

import cocochat.CocoChat;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import modelos.*;

public class Opciones extends JPanel {
    private Coco coco_main;
    
    private JButton addFriendBtn;
    private JButton createGroupBtn;
    private JButton addPersonBtn;
    private JButton sendMessageBtn;
    private JButton tagPersonBtn;
    private JButton attachFileBtn;
    private JButton tempMessageBtn;
    private JButton settingsBtn;
    private JButton chatWithFriendBtn;
    
    public Opciones(Coco coco_main) {
        this.coco_main = coco_main;
        setLayout(new GridLayout(5, 2, 5, 5));
        setBackground(new Color(220, 240, 220));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        initComponents();
    }
    
    private void initComponents() {
        // Crear botones
        addFriendBtn = createStyledButton("(1) Agregar Amigo", new Color(0, 150, 0));
        createGroupBtn = createStyledButton("(2) Crear Grupo", new Color(0, 130, 0));
        addPersonBtn = createStyledButton("(3) Agregar persona", new Color(150, 150, 150));
        sendMessageBtn = createStyledButton("(4) Enviar Mensaje", new Color(0, 140, 0));
        tagPersonBtn = createStyledButton("(5) Etiquetar Persona", new Color(0, 120, 0));
        attachFileBtn = createStyledButton("(6) Adjuntar archivo", new Color(0, 130, 0));
        tempMessageBtn = createStyledButton("(7) Mensaje Temporal", new Color(0, 110, 0));
        settingsBtn = createStyledButton("(8) Opciones", new Color(0, 100, 0));
        chatWithFriendBtn = createStyledButton("(9) Chatear con amigo", new Color(0, 160, 0));
        
        // Configurar botón addPersonBtn (inicialmente deshabilitado)
        addPersonBtn.setToolTipText("Deshabilitado en chat global");
        addPersonBtn.setEnabled(false);
        
        // Añadir ActionListeners
        addFriendBtn.addActionListener(this::btnAgregarAmigoActionPerformed);
        createGroupBtn.addActionListener(this::btnCrearGrupoActionPerformed);
        sendMessageBtn.addActionListener(e -> {
            // Este botón redirige al componente Chat
            Chat Chat = (Chat)coco_main.getComponent(1);
            Chat.enviarMensaje();
        });
        
        // Añadir botones al panel
        add(addFriendBtn);
        add(createGroupBtn);
        add(addPersonBtn);
        add(sendMessageBtn);
        add(tagPersonBtn);
        add(attachFileBtn);
        add(tempMessageBtn);
        add(settingsBtn);
        add(chatWithFriendBtn);
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setMargin(new Insets(5, 5, 5, 5));
        button.setFocusPainted(false);
        button.setBackground(color);
        return button;
    }
    
    public void habilitarBotonesChat(boolean enabled) {
        // Habilita o deshabilita botones relacionados con chat activo
        addPersonBtn.setEnabled(enabled);
    }
    
    private void btnAgregarAmigoActionPerformed(ActionEvent evt) {
        System.out.println("Usuario inició proceso de agregar amigo");
        String email = JOptionPane.showInputDialog(coco_main, "Email del usuario a agregar:");
        
        if (email != null && !email.trim().isEmpty()) {
            Usuarios nuevoAmigo = CocoChat.usuarioController.getByEmail(email);
            
            if (nuevoAmigo != null) {
                // No permitir agregarse a sí mismo
                if (nuevoAmigo.getUsuario_Id() == CocoChat.usuarioActual.getUsuario_Id()) {
                    JOptionPane.showMessageDialog(coco_main, "No puedes agregarte a ti mismo como amigo.");
                    return;
                }
                
                // Crear contacto bidireccional
                Contactos contactoDirecto = new Contactos(
                    0, // o el ID correspondiente si aplica
                    CocoChat.usuarioActual.getUsuario_Id(),
                    nuevoAmigo.getUsuario_Id(),
                    LocalDateTime.now(),
                    false
                );

                Contactos contactoReverso = new Contactos(
                    0, // o el ID correspondiente si aplica
                    nuevoAmigo.getUsuario_Id(),
                    CocoChat.usuarioActual.getUsuario_Id(),
                    LocalDateTime.now(),
                    false
                );
                
                // Guardar en BD
                CocoChat.contactosController.add(contactoDirecto);
                CocoChat.contactosController.add(contactoReverso);
                
                JOptionPane.showMessageDialog(coco_main, 
                    "Has agregado a " + nuevoAmigo.getNombre() + " a tus contactos.",
                    "Contacto agregado", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Actualizar panel de contactos
                coco_main.getContactosPanel().cargarContactos();
            } else {
                JOptionPane.showMessageDialog(coco_main, "No se encontró ningún usuario con ese email.");
            }
        }
    }
    
    private void btnCrearGrupoActionPerformed(ActionEvent evt) {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        
        JTextField nombreField = new JTextField(10);
        JTextArea descripcionArea = new JTextArea(3, 20);
        descripcionArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(descripcionArea);
        
        panel.add(new JLabel("Nombre del grupo:"));
        panel.add(nombreField);
        panel.add(new JLabel("Descripción:"));
        panel.add(scrollPane);
        
        int result = JOptionPane.showConfirmDialog(coco_main, panel, "Crear Nuevo Grupo",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String nombre = nombreField.getText().trim();
            String descripcion = descripcionArea.getText().trim();
            
            // Validar nombre
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(coco_main, 
                    "Por favor ingrese un nombre para el grupo.",
                    "Nombre requerido", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            try {
                // Crear grupo
                Grupos nuevoGrupo = new Grupos();
                nuevoGrupo.setNombre(nombre);
                nuevoGrupo.setDescripcion(descripcion);
                nuevoGrupo.setCreadorID(CocoChat.usuarioActual.getUsuario_Id());
                nuevoGrupo.setFechaCreacion(java.sql.Timestamp.valueOf(LocalDateTime.now()));
                
                // Guardar grupo
                CocoChat.gruposController.add(nuevoGrupo);
                
                // Obtener ID del grupo recién creado
                int grupoId = CocoChat.gruposController.getUltimoGrupoCreado(CocoChat.usuarioActual.getUsuario_Id());
                
                if (grupoId > 0) {
                    // Agregar usuario como miembro
                    CocoChat.gruposController.agregarMiembroGrupo(grupoId, 
                                                                CocoChat.usuarioActual.getUsuario_Id(), 
                                                                true);
                    
                    JOptionPane.showMessageDialog(coco_main, 
                        "Grupo '" + nombre + "' creado correctamente.",
                        "Grupo creado", 
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    // Actualizar lista de grupos
                    coco_main.getContactosPanel().cargarGrupos();
                } else {
                    JOptionPane.showMessageDialog(coco_main, 
                        "El grupo se creó, pero no se pudo agregar como miembro.",
                        "Advertencia", 
                        JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(coco_main, 
                    "Error al crear el grupo: " + e.getMessage(),
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
        
        // Actualizar lista de grupos - SOLUCIÓN CORREGIDA
        coco_main.getContactosPanel().cargarGrupos();
    }
    
    private void btnEnviarMensajeActionPerformed(ActionEvent evt) {
        // Implementación del nuevo código
        // Verificar que haya un chat activo y un mensaje
        // Resto del código...
    }
}