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
public class Simbolo {

    public int numero;
    public String variable;
    public String nombre;
    public LinkedList<String> posicion;

    public Simbolo() {
        this.nombre = "null";
        posicion = new LinkedList<>();
    }
}
