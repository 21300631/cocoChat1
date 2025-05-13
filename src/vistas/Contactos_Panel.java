package vistas;

import cocochat.CocoChat;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import modelos.*;

public class Contactos_Panel extends JPanel {
    private Coco coco_main;
    private JPanel panelAmigos;
    private JPanel gruposPanel;
    private JTextField buscadorCampo;
    private JFrame parent;
    
    public Contactos_Panel(Coco coco_main) {
        this.coco_main = coco_main;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(220, 0));
        setBackground(new Color(230, 255, 230));
        
        initComponents();
    }
    
    private void initComponents() {
        // Panel de búsqueda
        JPanel buscadorPanel = new JPanel(new BorderLayout());
        buscadorPanel.setBackground(new Color(220, 255, 220));
        buscadorPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        
        buscadorCampo = new JTextField();
        buscadorCampo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Buscador"),
            BorderFactory.createEmptyBorder(3, 5, 3, 5)
        ));
        buscadorCampo.setToolTipText("Buscar amigos o grupos");
        buscadorCampo.setPreferredSize(new Dimension(buscadorCampo.getPreferredSize().width, 25));
        buscadorCampo.setFont(new Font("Arial", Font.PLAIN, 12));
        buscadorPanel.add(buscadorCampo, BorderLayout.CENTER);
        
        // Panel contenedor de contactos y grupos
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
        
        // Sección de Grupos
        gruposPanel = new JPanel();
        gruposPanel.setName("gruposPanel");
        gruposPanel.setLayout(new BoxLayout(gruposPanel, BoxLayout.Y_AXIS));
        gruposPanel.setBorder(new EmptyBorder(15, 15, 5, 5));
        gruposPanel.setBackground(new Color(230, 255, 230));
        
        JLabel gruposLabel = new JLabel("Grupos");
        gruposLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gruposLabel.setForeground(new Color(0, 100, 0));
        gruposLabel.setBorder(new EmptyBorder(5, 0, 5, 0));
        gruposPanel.add(gruposLabel);
        gruposPanel.add(Box.createVerticalStrut(5));
        
        contactsContainer.add(panelAmigos);
        contactsContainer.add(gruposPanel);
        
        JScrollPane scrollPane = new JScrollPane(contactsContainer);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        add(buscadorPanel);
        add(Box.createVerticalStrut(10));
        add(scrollPane);
    }
    
    public void cargarContactos() {
        // Obtener lista de contactos
        List<Usuarios> contactos = CocoChat.contactosController.getContactosDetalladosByUsuario(
                                  CocoChat.usuarioActual.getUsuario_Id());
        
        // Limpiar panel
        panelAmigos.removeAll();
        
        // Título de sección
        JLabel amigosLabel = new JLabel("Amigos");
        amigosLabel.setFont(new Font("Arial", Font.BOLD, 14));
        amigosLabel.setForeground(new Color(0, 100, 0));
        amigosLabel.setBorder(new EmptyBorder(5, 0, 5, 0));
        panelAmigos.add(amigosLabel);
        panelAmigos.add(Box.createVerticalStrut(5));
        
        // Agregar cada contacto
        for (Usuarios contacto : contactos) {
            JPanel panelContacto = crearPanelContacto(contacto);
            panelAmigos.add(panelContacto);
        }
        
        panelAmigos.revalidate();
        panelAmigos.repaint();
    }
    
    private JPanel crearPanelContacto(Usuarios contacto) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(230, 255, 230));
        panel.setBorder(new EmptyBorder(3, 5, 3, 5));
        
        JLabel labelNombre = new JLabel("• " + contacto.getNombre() + " " + contacto.getApellido());
        labelNombre.setForeground(new Color(0, 80, 0));
        panel.add(labelNombre, BorderLayout.CENTER);
        
        // Hacer el panel interactivo
        panel.setOpaque(true);
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(200, 230, 200));
                panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(new Color(230, 255, 230));
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                coco_main.setContactoActual(contacto);
            }
        });
        
        return panel;
    }
    
    public void cargarGrupos() {
        // Obtener lista de grupos
        List<modelos.Grupos> grupos = CocoChat.gruposController.getGruposByUsuario(
                             CocoChat.usuarioActual.getUsuario_Id());
        
        // Limpiar panel de grupos (excepto el título)
        while (gruposPanel.getComponentCount() > 2) {
            gruposPanel.remove(2);
        }
        
        // Añadir grupos con listeners
        for (modelos.Grupos grupo : grupos) {
            crearPanelGrupo(grupo);
        }
        
        gruposPanel.revalidate();
        gruposPanel.repaint();
    }
    
    private void crearPanelGrupo(Grupos grupo) {
        JPanel grupoPanel = new JPanel(new BorderLayout());
        grupoPanel.setBackground(new Color(230, 255, 230));
        grupoPanel.setBorder(new EmptyBorder(3, 5, 3, 5));
        
        JLabel grupoLabel = new JLabel("✧ " + grupo.getNombre());
        grupoLabel.setForeground(new Color(0, 120, 0));
        grupoPanel.add(grupoLabel, BorderLayout.CENTER);
        
        // Añadir menú contextual
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem abrirItem = new JMenuItem("Abrir chat");
        JMenuItem agregarUsuariosItem = new JMenuItem("Añadir usuarios");
        JMenuItem verMiembrosItem = new JMenuItem("Ver miembros");
        
        abrirItem.addActionListener(e -> abrirChatGrupo(grupo));
        agregarUsuariosItem.addActionListener(e -> mostrarDialogoAgregarUsuarios(grupo));
        verMiembrosItem.addActionListener(e -> mostrarMiembrosGrupo(grupo));
        
        popupMenu.add(abrirItem);
        popupMenu.add(agregarUsuariosItem);
        popupMenu.add(verMiembrosItem);
        
        // Añadir listener para mostrar menú contextual
        grupoPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger() || SwingUtilities.isRightMouseButton(e)) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger() || SwingUtilities.isRightMouseButton(e)) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    abrirChatGrupo(grupo);
                }
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                grupoPanel.setBackground(new Color(200, 230, 200));
                grupoPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                grupoPanel.setBackground(new Color(230, 255, 230));
            }
        });
        
        gruposPanel.add(grupoPanel);
    }

    private void mostrarDialogoAgregarUsuarios(Grupos grupo) {
        // Crear diálogo para añadir usuarios
        JDialog dialog = new JDialog(parent, "Añadir Usuarios a " + grupo.getNombre(), true);
        dialog.setSize(350, 400);
        dialog.setLocationRelativeTo(parent);
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

    private void mostrarMiembrosGrupo(Grupos grupo) {
        // Obtener miembros del grupo con detalles
        List<Object[]> miembrosConDetalles = CocoChat.gruposController.getMiembrosConDetalles(grupo.getGrupoID());
        
        // Crear diálogo para mostrar los miembros
        JDialog dialog = new JDialog(parent, "Miembros de " + grupo.getNombre(), true);
        dialog.setSize(300, 350);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout());
        
        // Panel para la lista de miembros
        JPanel miembrosPanel = new JPanel();
        miembrosPanel.setLayout(new BoxLayout(miembrosPanel, BoxLayout.Y_AXIS));
        
        // Añadir miembros
        if (miembrosConDetalles.isEmpty()) {
            miembrosPanel.add(new JLabel("No hay miembros en este grupo"));
        } else {
            for (Object[] item : miembrosConDetalles) {
                Usuarios usuario = (Usuarios) item[0];
                boolean esAdmin = (boolean) item[1];
                
                String nombre = usuario.getNombre() + " " + usuario.getApellido();
                String rol = esAdmin ? " (Administrador)" : "";
                
                JPanel memberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                memberPanel.add(new JLabel(nombre + rol));
                
                miembrosPanel.add(memberPanel);
            }
        }
        
        JScrollPane scrollPane = new JScrollPane(miembrosPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton closeButton = new JButton("Cerrar");
        closeButton.addActionListener(e -> dialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        buttonPanel.add(closeButton);
        
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void abrirChatGrupo(Grupos grupo) {
        // Modificar esta función para usar métodos en lugar de índices directos
        try {
            // Informar a la ventana principal que estamos en un chat de grupo (no individual)
            coco_main.setContactoActual(null);
            
            // Usar el método específico para abrir chat de grupo
            if (coco_main.getChatPanel() != null) {
                coco_main.getChatPanel().mostrarChatGrupo(grupo);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error: No se pudo acceder al panel de chat", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al abrir chat de grupo: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}