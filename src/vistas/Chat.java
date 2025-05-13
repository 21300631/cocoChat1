package vistas;

import cocochat.CocoChat;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import modelos.*;

public class Chat extends JPanel {
    private Coco coco_main;
    private JPanel panelMensajes;
    private JScrollPane chatScrollPane;
    private JTextField txtMensaje;
    private JLabel lblChatActual;
    private JTextField searchField;
    private Usuarios contactoActual;
    private Grupos grupoActual;
    
    public Chat(Coco coco_main) {
        this.coco_main = coco_main;
        setLayout(new BorderLayout());
        setBackground(new Color(240, 255, 240));
        
        initComponents();
    }
    
    private void initComponents() {
        // Encabezado del chat
        JPanel chatHeaderPanel = new JPanel(new BorderLayout());
        chatHeaderPanel.setBackground(new Color(220, 240, 220));
        
        lblChatActual = new JLabel("Chat Global");
        lblChatActual.setFont(new Font("Arial", Font.BOLD, 14));
        lblChatActual.setForeground(new Color(0, 100, 0));
        chatHeaderPanel.add(lblChatActual, BorderLayout.WEST);
        
        searchField = new JTextField("Buscar en chat", 20);
        searchField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        chatHeaderPanel.add(searchField, BorderLayout.EAST);
        
        add(chatHeaderPanel, BorderLayout.NORTH);
        
        // Área de mensajes
        JPanel chatMainPanel = new JPanel(new BorderLayout());
        chatMainPanel.setBackground(new Color(240, 255, 240));
        
        panelMensajes = new JPanel();
        panelMensajes.setLayout(new BoxLayout(panelMensajes, BoxLayout.Y_AXIS));
        panelMensajes.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelMensajes.setBackground(new Color(240, 255, 240));
        chatScrollPane = new JScrollPane(panelMensajes);
        chatScrollPane.setBorder(null);
        
        chatMainPanel.add(chatScrollPane, BorderLayout.CENTER);
        add(chatMainPanel, BorderLayout.CENTER);
        add(chatMainPanel, BorderLayout.CENTER);
        
        // Panel para escribir mensajes
        JPanel messageInputPanel = new JPanel(new BorderLayout());
        messageInputPanel.setBackground(new Color(220, 240, 220));
        messageInputPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        txtMensaje = new JTextField();
        JButton sendBtn = new JButton("Enviar");
        sendBtn.setBackground(new Color(0, 150, 0));
        sendBtn.setForeground(Color.WHITE);
        sendBtn.setFocusPainted(false);
        sendBtn.addActionListener(e -> enviarMensaje());
        
        messageInputPanel.add(txtMensaje, BorderLayout.CENTER);
        messageInputPanel.add(sendBtn, BorderLayout.EAST);
        add(messageInputPanel, BorderLayout.SOUTH);
    }
    
    public void setContactoActual(Usuarios contacto) {
        this.contactoActual = contacto;
        this.grupoActual = null;
        
        if (contacto != null) {
            cargarMensajesConContacto(contacto);
        } else {
            // Limpiar panel de mensajes si no hay contacto
            panelMensajes.removeAll();
            panelMensajes.revalidate();
            panelMensajes.repaint();
        }
    }
    
    /**
     * Muestra el chat de un grupo específico
     * @param grupo El grupo cuyo chat se va a mostrar
     */
    public void mostrarChatGrupo(Grupos grupo) {
        // Actualizar referencias
        this.contactoActual = null;
        this.grupoActual = grupo;
        
        // Actualizar título con botón de gestión
        JPanel headerPanel = new JPanel(new BorderLayout(5, 0));
        
        JLabel titleLabel = new JLabel("Chat en grupo: " + grupo.getNombre());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        JButton adminButton = new JButton("+ Añadir usuarios");
        adminButton.setFocusPainted(false);
        adminButton.addActionListener(e -> mostrarDialogoAgregarUsuarios(grupo));
        
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(adminButton, BorderLayout.EAST);
        
        // Reemplaza el label por el panel
        lblChatActual.setVisible(false);
        ((JPanel)lblChatActual.getParent()).add(headerPanel, BorderLayout.NORTH);
        
        // Cargar mensajes del grupo
        cargarMensajesGrupo();
    }
    
    private void mostrarDialogoAgregarUsuarios(Grupos grupo) {
        // Crear diálogo para añadir usuarios
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
                                    "Añadir Usuarios a " + grupo.getNombre(), true);
        dialog.setSize(350, 400);
        dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
        dialog.setLayout(new BorderLayout(10, 10));
        
        // Panel para búsqueda
        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        JTextField searchField = new JTextField();
        JButton searchButton = new JButton("Buscar");
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        
        // Panel para resultados
        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        JScrollPane scrollPane = new JScrollPane(resultsPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Usuarios disponibles"));
        
        // Panel para botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        JButton addButton = new JButton("Añadir Seleccionados");
        JButton cancelButton = new JButton("Cancelar");
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        
        // Lista para mantener los checkboxes seleccionados
        List<JCheckBox> checkBoxList = new ArrayList<>();
        
        // Función para buscar usuarios
        ActionListener searchAction = e -> {
            String query = searchField.getText().trim();
            resultsPanel.removeAll();
            checkBoxList.clear();
            
            List<Usuarios> usuarios;
            if (query.isEmpty()) {
                // Mostrar todos los usuarios excepto el actual y los ya miembros
                usuarios = CocoChat.usuarioController.getAllUsuarios();
            } else {
                // Buscar usuarios por criterio
                usuarios = CocoChat.usuarioController.buscarUsuarios(query);
            }
            
            // Obtener miembros actuales para excluirlos
            List<Integer> miembrosIds = CocoChat.gruposController.getMiembrosIdsByGrupo(grupo.getGrupoID());
            
            // Filtrar usuarios que no son miembros ya
            for (Usuarios usuario : usuarios) {
                // No mostrar el usuario actual ni los que ya son miembros
                if (usuario.getUsuario_Id() != CocoChat.usuarioActual.getUsuario_Id() && 
                    !miembrosIds.contains(usuario.getUsuario_Id())) {
                        
                    JCheckBox checkBox = new JCheckBox(usuario.getNombre() + " " + usuario.getApellido());
                    checkBox.putClientProperty("usuario", usuario);
                    checkBoxList.add(checkBox);
                    
                    JPanel userPanel = new JPanel(new BorderLayout());
                    userPanel.add(checkBox, BorderLayout.CENTER);
                    userPanel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
                    
                    resultsPanel.add(userPanel);
                }
            }
            
            if (checkBoxList.isEmpty()) {
                resultsPanel.add(new JLabel("No se encontraron usuarios disponibles"));
            }
            
            resultsPanel.revalidate();
            resultsPanel.repaint();
        };
        
        // Configurar acciones
        searchButton.addActionListener(searchAction);
        searchField.addActionListener(searchAction);
        
        // Ejecutar búsqueda inicial para mostrar todos los usuarios
        searchField.setText("");
        searchAction.actionPerformed(new ActionEvent(searchButton, ActionEvent.ACTION_PERFORMED, null));
        
        // Acción para añadir usuarios seleccionados
        addButton.addActionListener(e -> {
            List<Usuarios> usuariosSeleccionados = new ArrayList<>();
            
            for (JCheckBox cb : checkBoxList) {
                if (cb.isSelected()) {
                    Usuarios usuario = (Usuarios) cb.getClientProperty("usuario");
                    usuariosSeleccionados.add(usuario);
                }
            }
            
            if (usuariosSeleccionados.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, 
                    "No has seleccionado ningún usuario", 
                    "Selección vacía", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Añadir todos los usuarios seleccionados al grupo
            boolean todoOk = true;
            for (Usuarios usuario : usuariosSeleccionados) {
                boolean resultado = CocoChat.gruposController.agregarMiembroGrupo(
                    grupo.getGrupoID(), usuario.getUsuario_Id(), false);
                
                if (!resultado) {
                    todoOk = false;
                }
            }
            
            if (todoOk) {
                JOptionPane.showMessageDialog(dialog, 
                    "Usuarios añadidos correctamente al grupo", 
                    "Operación exitosa", 
                    JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, 
                    "Hubo errores al añadir algunos usuarios", 
                    "Error parcial", 
                    JOptionPane.WARNING_MESSAGE);
            }
        });
        
        // Acción para cancelar
        cancelButton.addActionListener(e -> dialog.dispose());
        
        // Añadir componentes al diálogo
        dialog.add(searchPanel, BorderLayout.NORTH);
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // Mostrar el diálogo
        dialog.setVisible(true);
    }
    
    // Método para cargar mensajes con un contacto específico
    /**
     * Carga los mensajes entre el usuario actual y un contacto específico
     * @param contacto El contacto con el que se cargan los mensajes
     */
    public void cargarMensajesConContacto(Usuarios contacto) {
        // Implementación del método del nuevo código
        System.out.println("Cargando mensajes con " + contacto.getNombre());
        // Actualizar título
        lblChatActual.setText("Chat con: " + contacto.getNombre() + " " + contacto.getApellido());
        
        // Limpiar panel
        panelMensajes.removeAll();
        
        // Obtener mensajes
        List<Mensajes_Individuales> mensajes = CocoChat.mensajesController.getMensajesByUsuarios(
                                             CocoChat.usuarioActual.getUsuario_Id(), 
                                             contacto.getUsuario_Id());
        
        // Mostrar mensajes
        for (Mensajes_Individuales msj : mensajes) {
            JPanel panelMensaje = crearPanelMensaje(msj);
            panelMensajes.add(panelMensaje);
        }
        
        panelMensajes.revalidate();
        panelMensajes.repaint();
    }
    
    public JPanel crearPanelMensaje(Mensajes_Individuales mensaje) {
        // Implementación del método del nuevo código
        JPanel panel = new JPanel(new BorderLayout());
        // Resto del código...
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
    
    private JPanel crearPanelMensajeGrupo(Mensajes_Grupo mensaje, Usuarios remitente) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        boolean esMensajePropio = mensaje.getRemitenteID() == CocoChat.usuarioActual.getUsuario_Id();
        
        // Panel para el contenido del mensaje
        JLabel lblMensaje = new JLabel(mensaje.getContenido());
        lblMensaje.setOpaque(true);
        lblMensaje.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(8, 10, 8, 10),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        
        // Estilo diferente para mensajes propios y de otros
        if (esMensajePropio) {
            lblMensaje.setBackground(new Color(220, 248, 220));
            lblMensaje.setHorizontalAlignment(SwingConstants.RIGHT);
            panel.add(lblMensaje, BorderLayout.EAST);
            panel.add(new JLabel("Tú"), BorderLayout.NORTH);
        } else {
            lblMensaje.setBackground(new Color(240, 240, 240));
            lblMensaje.setHorizontalAlignment(SwingConstants.LEFT);
            panel.add(lblMensaje, BorderLayout.WEST);
            
            // Mostrar el nombre del remitente
            String nombreRemitente = remitente.getNombre() + " " + remitente.getApellido();
            panel.add(new JLabel(nombreRemitente), BorderLayout.NORTH);
        }
        
        return panel;
    }
    
    public void enviarMensaje() {
        String mensaje = txtMensaje.getText().trim();
        
        if (!mensaje.isEmpty()) {
            if (contactoActual != null) {
                // Enviar mensaje individual
                Mensajes_Individuales nuevoMensaje = new Mensajes_Individuales();
                nuevoMensaje.setRemitenteID(CocoChat.usuarioActual.getUsuario_Id());
                nuevoMensaje.setDestinatarioID(contactoActual.getUsuario_Id());
                nuevoMensaje.setContenido(mensaje);
                nuevoMensaje.setEsArchivado(false);
                
                // Enviar
                CocoChat.mensajesController.add(nuevoMensaje);
                
                // Limpiar y actualizar
                txtMensaje.setText("");
                cargarMensajesConContacto(contactoActual);
            } 
            else if (grupoActual != null) {
                // Crear mensaje de grupo
                Mensajes_Grupo nuevoMensaje = new Mensajes_Grupo();
                nuevoMensaje.setGrupoID(grupoActual.getGrupoID());
                nuevoMensaje.setRemitenteID(CocoChat.usuarioActual.getUsuario_Id());
                nuevoMensaje.setContenido(mensaje);
                nuevoMensaje.setEsAnuncio(false);
                
                // Enviar el mensaje
                int mensajeId = CocoChat.mensajesGrupoController.enviarMensaje(nuevoMensaje);
                
                if (mensajeId > 0) {
                    System.out.println("Mensaje de grupo enviado con ID: " + mensajeId);
                    // Limpiar campo de texto
                    txtMensaje.setText("");
                    // Actualizar vista de mensajes
                    cargarMensajesGrupo();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Error al enviar mensaje al grupo", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    public void actualizarMensajesChat() {
        // Guardar posición de scroll
        int currentValue = chatScrollPane.getVerticalScrollBar().getValue();
        int maxValue = chatScrollPane.getVerticalScrollBar().getMaximum();
        boolean cerca_del_final = maxValue - currentValue <= 50;
        
        if (contactoActual != null) {
            // Actualizar chat individual
            List<Mensajes_Individuales> mensajes = CocoChat.mensajesController.getMensajesByUsuarios(
                                                 CocoChat.usuarioActual.getUsuario_Id(), 
                                                 contactoActual.getUsuario_Id());
            
            // Verificar si hay mensajes nuevos
            if (mensajes.size() != panelMensajes.getComponentCount()) {
                // Recargar mensajes
                panelMensajes.removeAll();
                
                for (Mensajes_Individuales msj : mensajes) {
                    JPanel panelMensaje = crearPanelMensaje(msj);
                    panelMensajes.add(panelMensaje);
                }
                
                panelMensajes.revalidate();
                panelMensajes.repaint();
                
                // Ajustar el scroll
                ajustarScroll(cerca_del_final, currentValue);
            }
        } else if (grupoActual != null) {
            // Para grupos, simplemente recargamos los mensajes
            // Se podría optimizar comprobando si hay nuevos, pero por simplicidad...
            List<Object[]> mensajesConRemitente = 
                CocoChat.mensajesGrupoController.getMensajesConRemitente(grupoActual.getGrupoID(), 20);
                
            // Verificar si hay mensajes nuevos
            if (mensajesConRemitente.size() != panelMensajes.getComponentCount()) {
                // Recargar mensajes
                panelMensajes.removeAll();
                
                if (mensajesConRemitente.isEmpty()) {
                    JLabel infoLabel = new JLabel("No hay mensajes en este grupo aún");
                    infoLabel.setHorizontalAlignment(JLabel.CENTER);
                    panelMensajes.add(infoLabel);
                } else {
                    for (Object[] item : mensajesConRemitente) {
                        Mensajes_Grupo mensaje = (Mensajes_Grupo) item[0];
                        Usuarios remitente = (Usuarios) item[1];
                        
                        JPanel panelMensaje = crearPanelMensajeGrupo(mensaje, remitente);
                        panelMensajes.add(panelMensaje);
                    }
                }
                
                panelMensajes.revalidate();
                panelMensajes.repaint();
                
                // Ajustar el scroll
                ajustarScroll(cerca_del_final, currentValue);
            }
        }
    }
    
    // Método auxiliar para ajustar el scroll
    private void ajustarScroll(boolean cerca_del_final, int posicionAnterior) {
        SwingUtilities.invokeLater(() -> {
            JScrollBar vbar = chatScrollPane.getVerticalScrollBar();
            if (cerca_del_final) {
                // Si estaba cerca del final, ir hasta abajo
                vbar.setValue(vbar.getMaximum());
            } else {
                // Mantener la posición
                vbar.setValue(posicionAnterior);
            }
        });
    }
    
    private void cargarMensajesGrupo() {
        if (grupoActual != null) {
            // Limpiar panel de mensajes
            panelMensajes.removeAll();
            
            // Obtener los mensajes del grupo con detalles del remitente (20 mensajes más recientes)
            List<Object[]> mensajesConRemitente = 
                CocoChat.mensajesGrupoController.getMensajesConRemitente(grupoActual.getGrupoID(), 20);
            
            if (mensajesConRemitente.isEmpty()) {
                // No hay mensajes, mostrar mensaje informativo
                JLabel infoLabel = new JLabel("No hay mensajes en este grupo aún");
                infoLabel.setHorizontalAlignment(JLabel.CENTER);
                panelMensajes.add(infoLabel);
            } else {
                // Mostrar cada mensaje
                for (Object[] item : mensajesConRemitente) {
                    Mensajes_Grupo mensaje = (Mensajes_Grupo) item[0];
                    Usuarios remitente = (Usuarios) item[1];
                    
                    JPanel panelMensaje = crearPanelMensajeGrupo(mensaje, remitente);
                    panelMensajes.add(panelMensaje);
                }
            }
            
            panelMensajes.revalidate();
            panelMensajes.repaint();
            
            // Desplazar al final
            SwingUtilities.invokeLater(() -> {
                JScrollBar vbar = chatScrollPane.getVerticalScrollBar();
                vbar.setValue(vbar.getMaximum());
            });
        }
    }
}