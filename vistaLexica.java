/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compiladoresumg;

import java.util.HashMap;
import java.util.LinkedList;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author MAALBALE
 */
public class vistaLexica extends javax.swing.JFrame {
 private LinkedList<Token> tokens = new LinkedList<>();
 private HashMap<String, LinkedList<Token>> contadorPalabras;
 
 public vistaLexica(LinkedList<Token> Tokens, HashMap<String, LinkedList<Token>> ContadorPalabras) {
 this.tokens = Tokens;
 this.contadorPalabras = ContadorPalabras;
// initComponents();
 CargarTabla();
 CargarLexemas();
 }
private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) { 
 this.dispose();
 } 
 
 public void CargarTabla(){
 DefaultTableModel model = (DefaultTableModel) Jt_Toknes.getModel();
 model.setRowCount(0);
 Object[] row;
 
 try {
 
 for (int i = 0; i < tokens.size()-1; i++) {
 row = new Object [3];
 row[0] = tokens.get(i).getNum();
 row[1] = tokens.get(i).getNombre();
 row[2] = tokens.get(i).getLexema();
 
 model.addRow(row);
 }
 
 } catch (Exception e) {}
 
 }
 
 public void CargarLexemas(){
 DefaultTableModel model = (DefaultTableModel) Jt_Lexemas.getModel();
 model.setNumRows(0);
 Object[] row;
 
 try {
 for (String Lexema: contadorPalabras.keySet()) {
 LinkedList<Token> contador = contadorPalabras.get(Lexema);
 row = new Object[4];
 row[0] = contador.get(0).getNum();
 row[1] = contador.get(0).getNombre();
 row[2] = Lexema;
 row[3] = contador.size();
 
 model.addRow(row);
 }
 
 } catch (Exception e) {}
 
 
 }
}
