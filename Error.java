/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compiladoresumg;

/**
 *
 * @author MAALBALE
 */
public class Error {

    static void add(Error error) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private String error;
    private int fila;
    private int columna;

    public Error(String Error, int Fila, int Columna) {
        this.error = Error;
        this.fila = Fila;
        this.columna = Columna;
    }

    public String getError() {
        return this.error;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }
}
