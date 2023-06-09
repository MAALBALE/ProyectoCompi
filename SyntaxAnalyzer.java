/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


/**
 *
 * @author MAALBALE
 */

package compiladoresumg;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class SyntaxAnalyzer {

private HashMap<String, Simbolo> symbolTable;
private LinkedList<Token> tokens;
private LinkedList<Error> syntaxErrors;
private LinkedList<Error> semanticErrors;
private LinkedList<Peticion> requests;
private String output = "";
private int nextToken = 0;
private int symbolCount = 1;
private int line;
private String previousId = "";
private boolean error;
private boolean estado;

public SyntaxAnalyzer(LinkedList<Token> tokens) {
this.tokens = tokens;
symbolTable = new HashMap<>();
syntaxErrors = new LinkedList<>();
semanticErrors = new LinkedList<>();
requests = new LinkedList<>();
error = false;
estado = false;
symbolCount = 1;
line = 0;
}

public boolean analyzeSyntax() {
S();
return estado;
}

/*DEVUELVE TODA LA LISTA DE ERRORES SINTÁCTICOS*/
public LinkedList<Error> getSyntaxErrors() {
return syntaxErrors;
}

/*DEVUELVE TODA LA LISTA DE ERRORES SEMÁNTICOS */
public LinkedList<Error> getSemanticErrors() {
return semanticErrors;
}

/*SIMULACION DE LA REGLA
S -> pr_inicio INS pr_fin
*/
private void S() {
if ("pr_inicio".equals(tokens.get(nextToken).getNombre())) {
avanzarToken();
SENT();
if ("pr_fin".equals(tokens.get(nextToken).getNombre())) {
avanzarToken();
if ("$".equals(tokens.get(nextToken).getNombre()) && !error) {
estado = true;
} else {
agregarErrorSintax("Fin", 1);
}
} else {
agregarErrorSintax("Fin", 1);
}
} else {
agregarErrorSintax("Inicio", 1);
}
}

/*SENT*/
private void SENT() {
  switch (tokens.get(nextToken).getNombre()) {
    case "pr_jugador" -> {
      avanzarToken();
      DEC();
    }
    case "pr_crearJugador" -> {
      avanzarToken();
      ASIG();
    }
    case "pr_imprimir" -> {
      avanzarToken();
      IMPRI();
    }
    case "pr_leerPos" -> {
      avanzarToken();
      LEER();
    }
    case "pr_repetir" -> {
      avanzarToken();
      CICLO();
    }
    case "pr_resultado" -> {
      avanzarToken();
      RESUL();
    }
    case "pr_finRepetir", "pr_fin" -> {
      return;
    }
    default -> agregarErrorSintax("Jugador, crearJugador, Imprimir, Resultado, Repetir, leerPosicion", 1);
  }
}

/*
   SIMULACION DE LA REGLA
   DEC -> pr_jugador id MI INS
   Regla para la declaración de 1 a n jugadores
*/
private void DEC() {
  if ("id".equals(tokens.get(nextToken).getNombre())) {
    agregarSimbolo();
    avanzarToken();
    COM();
    SENT();
  } else {
    agregarErrorSintax("un Identificador", 2);
  }
}


/*
   SIMULACION DE LA REGLA
   MI -> coma id MI | ε
*/
private void COM() {
  if (!"coma".equals(tokens.get(nextToken).getNombre())) {
    return;
  }
  if ("coma".equals(tokens.get(nextToken).getNombre())) {
    avanzarToken();
    if ("id".equals(tokens.get(nextToken).getNombre())) {
      agregarSimbolo(); 
      avanzarToken();
      COM();
    } else {
      agregarErrorSintax("un Identificador", 2);
    }
  } else {
    agregarErrorSintax(",", 2);
  }
}


/*ASIG*/
private void ASIG() {
  if ("pabierto".equals(tokens.get(nextToken).getNombre())) {
    avanzarToken();
    if ("id".equals(tokens.get(nextToken).getNombre())) {
      // Agregamos una petición de entrada por consola.
      agregarPeticion(1, tokens.get(nextToken).getLexema());
      avanzarToken();
      if ("pcerrado".equals(tokens.get(nextToken).getNombre())) {
        avanzarToken();
        SENT();
      } else {
        agregarErrorSintax(")", 2);
      }
    } else {
      agregarErrorSintax("un Identificador", 2);
    }
  } else {
    agregarErrorSintax("(", 2);
  }
}


/*IMPRI*/
private void IMPRI() {
  if ("pabierto".equals(tokens.get(nextToken).getNombre())) {
    avanzarToken();
    MSN();
    if ("pcerrado".equals(tokens.get(nextToken).getNombre())) {
      // Agregamos una petición de impresión a pantalla.
      if (!"".equals(output)) {
        agregarPeticion(3, output);
      }
      agregarPeticion(3, "\n");
      output = "";
      avanzarToken();
      SENT();
    } else {
      agregarErrorSintax(",' ó ')", 2);
    }
  } else {
    agregarErrorSintax("(", 2);
  }
}

/*
   SIMULACION DE LA REGLA
   EX -> cadena SG | entero SG | id SG
*/
private void MSN() {
  switch (tokens.get(nextToken).getNombre()) {
    case "cadena", "entero", "id" -> {
      if ("id".equals(tokens.get(nextToken).getNombre())) {
        Simbolo simboloEncontrado = symbolTable.get(tokens.get(nextToken).getLexema());
        if (simboloEncontrado != null) {
          if (!"".equals(output)) {
            agregarPeticion(3, output);
          }
          agregarPeticion(4, symbolTable.get(tokens.get(nextToken).getLexema()).variable);
          output = "";
        } else {
          // La variable ya existe, agregamos el error de id repetido
          AgregarErrorSemantico(2);
        }
      } else {
        output = output + tokens.get(nextToken).getLexema();
      }
      avanzarToken();
      SIG();
    }
    default -> {
      agregarErrorSintax("cadena, entero o id", 2);
    }
  }
}


/*
   SIMULACION DE LA REGLA
   SG -> coma EX | ε
*/
private void SIG() {
  if ("coma".equals(tokens.get(nextToken).getNombre())) {
    avanzarToken();
    MSN();
  }
}

/*LEER*/
private void LEER() {
  if ("pabierto".equals(tokens.get(nextToken).getNombre())) {
    avanzarToken();
    if ("id".equals(tokens.get(nextToken).getNombre())) {
      agregarPeticion(2, tokens.get(nextToken).getLexema());
      avanzarToken();
      if ("pcerrado".equals(tokens.get(nextToken).getNombre())) {
        avanzarToken();
        SENT    ();
      } else {
        agregarErrorSintax(")", 2);
      }
    } else {
      agregarErrorSintax("un Identificador", 2);
    }
  } else {
    agregarErrorSintax("(", 2);
  }
}

/* CICLO*/
private void CICLO() {
  int linea_ac;
  if ("pabierto".equals(tokens.get(nextToken).getNombre())) {
    avanzarToken();
    if ("entero".equals(tokens.get(nextToken).getNombre())) {
      String i = tokens.get(nextToken).getLexema();
      avanzarToken();
      if ("pcerrado".equals(tokens.get(nextToken).getNombre())) {
        linea_ac = line;
        agregarPeticion(6, i);
        requests.get(linea_ac).org = i;
        avanzarToken();
        SENT();
        if ("pr_finRepetir".equals(tokens.get(nextToken).getNombre())) {
          agregarPeticion(7, "fin_rep");
          requests.getLast().go_to = linea_ac;
          requests.get(linea_ac).go_to = line;
          avanzarToken();
          SENT();
        } else {
          agregarErrorSintax("finRepetir", 1);
        }
      } else {
        agregarErrorSintax(")", 2);
      }
    } else {
      agregarErrorSintax("un Entero", 2);
    }
  } else {
    agregarErrorSintax("(", 2);
  }
}


/*
   SIMULACION DE LA REGLA
   RES -> pr_resultado pabierto pcerrado
*/
private void RESUL() {
  if ("pabierto".equals(tokens.get(nextToken).getNombre())) {
    avanzarToken();
    if ("pcerrado".equals(tokens.get(nextToken).getNombre())) {
      agregarPeticion(5, "RESULTADOS: ");
      avanzarToken();
      SENT();
    } else {
      agregarErrorSintax(")", 2);
    }
  } else {
    agregarErrorSintax("(", 2);
  }
}

/*
   Como usamos un LinkedList nos movemos en la lista
   según su posición y este método únicamente suma 1 a
   la variable nextTokenIndex para ir moviendo la posición.
*/
private void avanzarToken() {
  nextToken++;
}
/*
   Cuando se detecta un error sintáctico, se llama a este 
   método para agregarlo mandando dos parámetros que son 
   el token que corresponde y el tipo de error si es por 
   palabra reservada o algún otro token.
*/
private void agregarErrorSintax(String palabraReservada, int tipo) {
  switch (tipo) {
    case 1 -> {
      String error = tokens.get(nextToken).getLexema() + " Se esperaba palabra reservada: '" + palabraReservada + "'";
      syntaxErrors.add(new Error(error, tokens.get(nextToken).getFila(), tokens.get(nextToken).getColumna()));
    }
    case 2 -> {
      String error = tokens.get(nextToken).getLexema() + " Se esperaba: '" + palabraReservada + "'";
     syntaxErrors.add(new Error(error, tokens.get(nextToken).getFila(), tokens.get(nextToken).getColumna()));
    }
  }
  error = true;
  recuperacion();
}

/*
   Cuando se detecta un error semántico se llama a este
   método únicamente mandando el tipo de caso que se determine 
   el valor del id se toma de la lista de tokens global.
*/
private void AgregarErrorSemantico(int caso) {
  switch (caso) {
    case 1 -> {
      String error = "Identificador repetido: '" + tokens.get(nextToken).getLexema() + "'";
      semanticErrors.add(new Error(error, tokens.get(nextToken).getFila(), tokens.get(nextToken).getColumna()));
    }
    case 2 -> {
      String error = "Identificador no declarado: '" + tokens.get(nextToken).getLexema() + "'";
      semanticErrors.add(new Error(error, tokens.get(nextToken).getFila(), tokens.get(nextToken).getColumna()));
    }
  }
}
/*
   Cuando encontramos un error sintáctico, se necesita 
   la recuperación del error para que el análisis no se 
   detenga hasta llegar al final de la lista de tokens.
   Para esto se utiliza esta recuperación que omite todos
   los tokens después del error hasta encontrar la siguiente 
   instrucción válida.
*/
private void recuperacion() {
  while (true) {
    switch (tokens.get(nextToken).getNombre()) {
      case "pr_jugador", "pr_crearJugador", "pr_imprimir", "pr_leerPos", "pr_repetir", "pr_resultado" -> {
        SENT();
        return;
      }
      case "$", "pr_fin" -> {
        return;
      }
      default -> avanzarToken();
    }
  }
}

/*
   Se llama cuando queremos agregar un símbolo nuevo.
   Este método primero busca si este símbolo existe o no 
   en nuestra tabla. Si lo detecta, agrega un error; de lo 
   contrario, lo agrega a la tabla.
*/
private void agregarSimbolo() {
  String lexema = tokens.get(nextToken).getLexema();
  Simbolo simboloEncontrado = symbolTable.get(lexema);

  if (simboloEncontrado == null) {
    // La variable no existe, crear una nueva instancia de Simbolo y agregarla al mapa
    Simbolo nuevoSimbolo = new Simbolo();
    nuevoSimbolo.variable = lexema;
    nuevoSimbolo.numero = symbolCount;
    symbolCount++;
    symbolTable.put(lexema, nuevoSimbolo);
  } else {
    // La variable ya existe, agregamos el error de id repetido
    AgregarErrorSemantico(1);
  }
}

/*
   Cuando se le quiere asignar un valor a un id,
   se llama a este método para asignar un nombre o posición 
   a un símbolo en específico. Este método utiliza tres parámetros:
   la variable a la que se le asignará, el valor y el tipo de valor.
*/
public void AsignarValor(String variable, String valor, int tipo) {
  switch (tipo) {
    case 1 -> symbolTable.get(variable).posicion.add(valor);
    case 2 -> symbolTable.get(variable).nombre = valor;
  }
}

/*
   Función que nos ayuda a determinar si un 
   símbolo existe o no en la tabla de símbolos.
*/
private boolean buscarSimbolo() {
  Simbolo simboloEncontrado = symbolTable.get(tokens.get(nextToken).getLexema());
  return simboloEncontrado != null;
}

/*
   Función que nos devuelve el nombre asignado a 
   un símbolo en específico.
*/
public String getValorSimbolo(String variable) {
  return symbolTable.get(variable).nombre;
}

/*
   Al final de los 3 análisis obtenemos una lista de 
   peticiones o instrucciones que se irán ejecutando de 
   manera secuencial y en un orden específico. Estas 
   peticiones se van agregando en el momento que se hace
   el análisis sintáctico y semántico. 
 
   -- PETICIONES
   1 -> "Solicitar nombres de los jugadores"
   2 -> "Solicitar posiciones a marcar"
   3 -> "Imprimir a pantalla" (Mensaje)
   4 -> "Imprimir a pantalla" (nombreJugador)
   5 -> "Mostrar resultado del juego"
   6 -> "Iniciar iteración"
   7 -> "Finalizar iteración"
*/

/* Agregamos una petición a la lista con el número 
   que se solicita y la variable que lo utilizará. */

private void agregarPeticion(int numPeticion, String variable) {
  if ((numPeticion == 1 || numPeticion == 2) && !buscarSimbolo()) {
    AgregarErrorSemantico(2);
  } else {
    requests.add(new Peticion(numPeticion, variable, line));
    line++;
  }
}

/*
   Devuelve la lista de peticiones creada hacia 
   el frontend.
*/
public LinkedList<Peticion> getPeticiones() {
  return requests;
}

/*
   Método para mostrar la lista de peticiones o instrucciones por consola.
*/
public void listaP() {
  for (int i = 0; i < requests.size(); i++) {
    System.out.println("L" + requests.get(i).linea + " P" + requests.get(i).numPeticion + " " + requests.get(i).getVariable() + " goto: " + requests.get(i).go_to);
  }
}
}


