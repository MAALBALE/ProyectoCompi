/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compiladoresumg;

/**
 *
 * @author MAALBALE
 */
public class Peticion {

    int linea;
    int numPeticion;
    public int go_to;
    public String org;
    String variable;

    public Peticion(int NumPeticion, String Variable, int Linea) {
        this.numPeticion = NumPeticion;
        this.variable = Variable;
        this.linea = Linea;
    }

    public int getPeticion() {
        return this.numPeticion;
    }

    public String getVariable() {
        return this.variable;
    }

}
