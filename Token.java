/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compiladoresumg;

/**
 *
 * @author MAALBALE
 */
public class Token {

    private String nombre;
    private String lexema;
    private int num;
    private int fila;
    private int columna;

    public Token(int num, String nombre, String lexema, int fila, int columna) {
        this.num = num;
        this.nombre = nombre;
        this.lexema = lexema;
        this.fila = fila;
        this.columna = columna;
    }

    public Token() {
    }

    public String getNombre() {
        return nombre;
    }

    public String getLexema() {
        return lexema;
    }

    public int getColumna() {
        return columna;
    }

    public int getFila() {
        return fila;
    }

    public int getNum() {
        return num;
    }
}
