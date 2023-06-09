/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compiladoresumg;

import java.util.LinkedList;

/**
 *
 * @author MAALBALE
 */
public class Posicion {

    public String fila;
    public LinkedList<String> columna;

    public Posicion(String Fila, LinkedList<String> Columna) {
        this.fila = Fila;
        this.columna = Columna;
    }

}
