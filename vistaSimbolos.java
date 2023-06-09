/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compiladoresumg;

import java.util.HashMap;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author MAALBALE
 */
public class vistaSimbolos extends javax.swing.JFrame {
private HashMap<String, Simbolo> simbolos;
 /**
 * Creates new form vistaSimbolos
     * @param Simbolos
 */
 public vistaSimbolos(HashMap<String, Simbolo> Simbolos) {
 this.simbolos = Simbolos;
 initComponents();
 cargarTablaSimbolos();
 }
public static void main(String args[]) {
 /* Set the Nimbus look and feel */
 //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
 /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
 * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
 */
 try {
 for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
 if ("Nimbus".equals(info.getName())) {
 javax.swing.UIManager.setLookAndFeel(info.getClassName());
break;
 }
 }
 } catch (ClassNotFoundException ex) {
 java.util.logging.Logger.getLogger(vistaSimbolos.class.getName()).log(java.util.logging.Level.SEVERE, 
null, ex);
 } catch (InstantiationException ex) {
 java.util.logging.Logger.getLogger(vistaSimbolos.class.getName()).log(java.util.logging.Level.SEVERE, 
null, ex);
 } catch (IllegalAccessException ex) {
 java.util.logging.Logger.getLogger(vistaSimbolos.class.getName()).log(java.util.logging.Level.SEVERE, 
null, ex);
 } catch (javax.swing.UnsupportedLookAndFeelException ex) {
 java.util.logging.Logger.getLogger(vistaSimbolos.class.getName()).log(java.util.logging.Level.SEVERE, 
null, ex);
 }
 //</editor-fold>
 /* Create and display the form */
 java.awt.EventQueue.invokeLater(() -> {
     //new vistaSimbolos().setVisible(true);
 });
 }
 
 private void cargarTablaSimbolos(){
 DefaultTableModel model = (DefaultTableModel) Jt_Simbolos.getModel();
 model.setRowCount(0);
 Object[] row;
 
 
 try {
 for (String sim: simbolos.keySet()) {
 String posiciones = ""; 
 Simbolo contador = simbolos.get(sim);
 row = new Object[4];
 row[0] = contador.numero;
 row[1] = contador.variable;
 row[2] = contador.nombre;
 
 if(contador.posicion.size() > 0){
 for (int i = 0; i < contador.posicion.size(); i++) {
 posiciones = posiciones +contador.posicion.get(i) + " ";
 } 
 }
 else{
 posiciones = "null";
 }
 row[3] = posiciones;
 
 model.addRow(row);
 }
 
 } catch (Exception e) {}
 }
}