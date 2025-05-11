/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vistas;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 *
 * @author Laraa
 */
public final class Coco extends JFrame {
    
    public Coco(){
        super();
        metodo();   
    }
    
    void metodo(){
        //todos los elementos y propiedades se usa SET
        setSize(400, 700);
        setTitle("Hola mundo!");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color (154,148,188));
        
        //----------------------------------------------------------
        JButton boton; //Referencia
        boton = new JButton(); //Obejeto
        this.add(boton); //Personalizar
        add(boton);
        boton.setText("picame");
        boton.setSize(100, 100);
        
        JTextField textField;
        textField = new JTextField();
        this.add(textField);
        add(textField);
    }
}
