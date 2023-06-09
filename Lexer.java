/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compiladoresumg;

/**
 *
 * @author MAALBALE
 */
import java.util.HashMap;
import java.util.LinkedList;

public class Lexer {

    private LinkedList<Token> tokens;
    private LinkedList<Error> errores;
    private int Estado_Actual;
    private String Aux_Lex;
    private int Linea;
    private int Columna;
    public HashMap<String, LinkedList<Token>> contadorPalabras;

    /*
    Este procedimiento simula el AFD creado para detectar todos los lexemas válidos
    y almacenarlos en la tabla de tokens, adicionalmente almacena la posición en número
    de fila y columna.
     */
    public void Escaner(String Entrada) {
        tokens = new LinkedList<>();
        errores = new LinkedList<>();
        contadorPalabras = new HashMap<String, LinkedList<Token>>();
        Entrada = Entrada + "#";
        Estado_Actual = 0;
        Linea = 1;
        Columna = 1;
        Aux_Lex = "";
        char C;

        for (int i = 0; i < Entrada.length(); i++) {
            C = Entrada.charAt(i);

            if (C == '\n' && Estado_Actual == 0) {
                Linea++;
                Columna = 0;
            }
            if (C == '\t' && Estado_Actual == 0) {
                Columna += 8;
            }
            switch (Estado_Actual) {
                case 0 -> {
                    if (Character.isLetter(C)) {
                        Estado_Actual = 1;
                        Aux_Lex += C;
                    } else if (Character.isDigit(C)) {
                        Estado_Actual = 2;
                        Aux_Lex += C;
                    } else if (C != ' ' && C != '\n' && C != '\t') {
                        switch (C) {
                            case '"' -> {
                                Estado_Actual = 3;
                                Columna++;
                            }
                            case '(' -> {
                                Estado_Actual = 4;
                                Aux_Lex += C;
                            }
                            case ')' -> {
                                Estado_Actual = 5;
                                Aux_Lex += C;
                            }
                            case ',' -> {
                                Estado_Actual = 6;
                                Aux_Lex += C;
                            }
                            case '#' -> {
                                if (i < Entrada.length() - 1) {
                                    Aux_Lex += C;
                                    String error = "Palabra no reconocida '" + Aux_Lex + "'";
                                    agregarError(new Error(error, Linea, Columna));
                                } else {
                                    break;
                                }
                            }
                            default -> {
                                Aux_Lex += C;
                                String error = "Palabra no reconocida '" + Aux_Lex + "'";
                                agregarError(new Error(error, Linea, Columna));
                            }
                        }
                    }
                }

                case 1 -> {
                    if (Character.isLetterOrDigit(C)) {
                        Aux_Lex += C;
                    } else {
                        switch (Aux_Lex) {
                            case "Jugador" -> {
                                agregarToken(new Token(7, "pr_jugador", Aux_Lex, Linea, Columna));
                            }
                            case "crearJugador" -> {
                                agregarToken(new Token(8, "pr_crearJugador", Aux_Lex, Linea, Columna));
                            }
                            case "Imprimir" -> {
                                agregarToken(new Token(9, "pr_imprimir", Aux_Lex, Linea, Columna));
                            }
                            case "Inicio" -> {
                                agregarToken(new Token(10, "pr_inicio", Aux_Lex, Linea, Columna));
                            }
                            case "Fin" -> {
                                agregarToken(new Token(11, "pr_fin", Aux_Lex, Linea, Columna));
                            }
                            case "Resultado" -> {
                                agregarToken(new Token(12, "pr_resultado", Aux_Lex, Linea, Columna));
                            }
                            case "Repetir" -> {
                                agregarToken(new Token(13, "pr_repetir", Aux_Lex, Linea, Columna));
                            }
                            case "finRepetir" -> {
                                agregarToken(new Token(14, "pr_finRepetir", Aux_Lex, Linea, Columna));
                            }
                            case "leerPosicion" -> {
                                agregarToken(new Token(15, "pr_leerPos", Aux_Lex, Linea, Columna));
                            }
                            default -> {
                                agregarToken(new Token(1, "id", Aux_Lex, Linea, Columna));
                            }
                        }
                        i--;
                        Columna--;
                        Aux_Lex = "";
                        Estado_Actual = 0;
                    }
                }

                case 2 -> {
                    if (Character.isDigit(C)) {
                        Aux_Lex += C;
                    } else {
                        agregarToken(new Token(2, "entero", Aux_Lex, Linea, Columna));
                        i--;
                        Columna--;
                        Aux_Lex = "";
                        Estado_Actual = 0;
                    }
                }
                case 3 -> {
                    if (C != '"' && i < Entrada.length() - 1) {
                        if (C == '\n') {
                            Linea++;
                            Columna = 0;
                        }
                        Aux_Lex += C;
                    } else {
                        if (i == Entrada.length() - 1) {
                            String error = "Falta cierre de comillas dobles '" + Aux_Lex;
                            agregarError(new Error(error, Linea, Columna));
                        } else {
                            agregarToken(new Token(3, "cadena", Aux_Lex, Linea, Columna));
                        }
                        Aux_Lex = "";
                        Estado_Actual = 0;
                    }
                }
                case 4 -> {
                    agregarToken(new Token(4, "pabierto", Aux_Lex, Linea, Columna));
                    i--;
                    Columna--;
                    Aux_Lex = "";
                    Estado_Actual = 0;
                }
                case 5 -> {
                    agregarToken(new Token(5, "pcerrado", Aux_Lex, Linea, Columna));
                    i--;
                    Columna--;
                    Aux_Lex = "";
                    Estado_Actual = 0;
                }
                case 6 -> {
                    agregarToken(new Token(6, "coma", Aux_Lex, Linea, Columna));
                    i--;
                    Columna--;
                    Aux_Lex = "";
                    Estado_Actual = 0;
                }
            }
            Columna++;
        }
        tokens.add(new Token(0, "$", "Fin_Pila", 0, 0));
    }

    /*
Método para agregar el token a la tabla,
y también agregamos los mismos a un mapa de hash
para ir contando las palabras que se van ingresando.
     */
    public void agregarToken(Token newToken) {

        if (contadorPalabras.containsKey(Aux_Lex)) {
            contadorPalabras.get(Aux_Lex).add(newToken);
        } else {
            LinkedList<Token> contador = new LinkedList<>();
            contador.add(newToken);
            contadorPalabras.put(Aux_Lex, contador);
        }

        tokens.add(newToken);
        Aux_Lex = "";
        Estado_Actual = 0;
    }

    /*
 Almacena los errores detectados en el analsis Lexico
     */
    private void agregarError(Error newError) {
        errores.add(newError);
        Aux_Lex = "";
        Estado_Actual = 0;
    }

    /*
 Devuleve la lista de tokens
     */
    public LinkedList<Token> getToken() {
        return tokens;
    }

    /*
 Devuelve la lista de errores
     */
    public LinkedList<Error> getErrores() {
        return errores;
    }
}
