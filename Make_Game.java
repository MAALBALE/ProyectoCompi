/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compiladoresumg;

/**
 *
 * @author MAALBALE
 */
public class Make_Game extends javax.swing.JFrame {

    JFileChooser seleccionar = new JFileChooser();
    File Archivo;
    FileInputStream Entrada;
    FileOutputStream Salida;
    AnalizadorLexico anl = new AnalizadorLexico();
    AnalizadorSintactico ans;
    vistaLexica resAnLex;
    vistaSimbolos resSimbol;
    Totito tablero;
    String resultado = "EMPATE";
    String salidaErrores = "";
    LinkedList<Peticion> instrucciones = new LinkedList<>();
    LinkedList<Posicion> Posiciones;
    boolean error = false;
    boolean enPos = false;
    int numInstruccion = 0;
    String valor = "";
    String variable = "";
    int tipo = 0;

    /**
     * Creates new form Make_Game
     */
    public Make_Game(Totito Tablero) {
        this.tablero = Tablero;
        initComponents();
        btnEnter.setEnabled(false);
    }

    private void jbtn_startGameMouseClicked(java.awt.event.MouseEvent evt) {
        Consola.setText("");
        error = false;
        salidaErrores = "";
        anl.Escaner(txtEntrada.getText());
        ans = new AnalizadorSintactico(anl.getToken());
        ans.AnalizarSintaxis();
        AgregarErrorLexico();
        AgregarErrorSintactico();
        AgregarErrorSemantico();
        instrucciones = ans.getPeticiones();

        if (!error) {
            cargarTablero();
            cbx_columna.removeAllItems();
            cbx_fila.removeAllItems();
            enPos = false;
            Consola.setForeground(Color.white);
            numInstruccion = 0;
            ejecutar();
            tablero.Reiniciar();
        } else {
            Consola.setForeground(Color.red);
            Consola.setText("Compilación Fallida!");
        }

    }

    /*BOTON DE ENTER
 Aqui se programan las entradas solicitadas por el sistema
     */
    private void btnEnterActionPerformed(java.awt.event.ActionEvent evt) {

        if (enPos) {
            String fila = String.valueOf(cbx_fila.getSelectedItem());
            String columna = String.valueOf(cbx_columna.getSelectedItem());

            valor = fila + columna;
            cargarPosiciones();
            enPos = false;

            if (Posiciones.size() <= 0) {
                valor = "";
            }
            removerPosicion(fila, columna);

            //Comprovamos si ya existe un ganador
            if (tablero.ganador.equals("EMPATE")) {
                tablero.comprovarGanador(variable);
                System.out.println(variable);
                if (!tablero.ganador.equals("EMPATE")) {
                    tablero.lb_ganador.setText(ans.getValoSimbolo(tablero.ganador));
                    resultado = "GANADOR: !" + tablero.lb_ganador.getText() + "!";
                }
            }
        } else {
            valor = EntradaTeclado.getText();
            EntradaTeclado.setText("");
        }

        ans.AsignarValor(variable, valor, tipo);
        Consola.setText(Consola.getText() + valor + "\n");
        btnEnter.setEnabled(false);
        ejecutar();
    }

    /*Abre la ventana para la TABLA DE TOKNES*/
    private void op_tablaTokenActionPerformed(java.awt.event.ActionEvent evt) {
        resAnLex = new vistaLexica(anl.getToken(), anl.contadorPalabras);
        resAnLex.setVisible(true);
    }

    /*Abre la ventana para la TABLA DE SIMBOLOS*/
    private void op_tablaSimbolosActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            resSimbol = new vistaSimbolos(ans.tablaSimbolos);
            resSimbol.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ejecute un codigo valido para ver la tabla de simbolos");
        }

    }

    /*Carga los Items para el comboBox de Filas*/
    private void cbx_filaItemStateChanged(java.awt.event.ItemEvent evt) {
        cargarColumna(String.valueOf(cbx_fila.getSelectedItem()));
    }

    /*Abre la ventana para el TABLERO DE TOTITO*/
    private void op_tableroActionPerformed(java.awt.event.ActionEvent evt) {
        tablero.setVisible(true);
    }

    /*Abre la ventana para Exportar el archivo CSV*/
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {
        if (seleccionar.showDialog(null, "Guardar CSV") == JFileChooser.APPROVE_OPTION) {
            Archivo = seleccionar.getSelectedFile();

            if (Archivo.getName().endsWith("txt") || Archivo.getName().endsWith("csv")) {
                String documento = salidaErrores;
                String mensaje = GuardarArchivo(Archivo, documento);
                if (mensaje != null) {
                    JOptionPane.showMessageDialog(null, mensaje);
                } else {
                    JOptionPane.showMessageDialog(null, "Archivo no guardado");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Archivo no guardado, recuerde\nusar extensión .txt o .csv");
            }
        }
    }

    /*Cierra la ventana pricipal desde el menu de archivo*/
    private void opSalirActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
    }

    /*
 Abre la ventana para guardar archivo de codigo
     */
    private void opGuardarActionPerformed(java.awt.event.ActionEvent evt) {
        if (seleccionar.showDialog(null, "Guardar") == JFileChooser.APPROVE_OPTION) {
            Archivo = seleccionar.getSelectedFile();
            if (Archivo.getName().endsWith("txt")) {
                String documento = txtEntrada.getText();
                String mensaje = GuardarArchivo(Archivo, documento);
                if (mensaje != null) {
                    JOptionPane.showMessageDialog(null, mensaje);
                } else {
                    JOptionPane.showMessageDialog(null, "Archivo no guardado");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Archivo no guardado, recuerde\nusar extension .txt");
            }
        }
    }

    /*
 Abre la ventana para arbir archivo de codigo
     */
    private void opAbrirActionPerformed(java.awt.event.ActionEvent evt) {
        if (seleccionar.showDialog(null, "Abrir") == JFileChooser.APPROVE_OPTION) {
            Archivo = seleccionar.getSelectedFile();
            if (Archivo.canRead()) {
                String documento = AbrirArchivo(Archivo);
                txtEntrada.setText(documento);
            } else {
                JOptionPane.showMessageDialog(null, "Archivo No Compatible");
            }
        }
    }

    /*cargamos la tabla de erroes Lexicos*/
    public void AgregarErrorLexico() {
        DefaultTableModel model = (DefaultTableModel) jt_erorrLexico.getModel();
        model.setNumRows(0);
        Object[] row = new Object[4];
        LinkedList<Error> listaErrores = anl.getErrores();

        if (listaErrores.size() > 0) {
            check_lex.setBackground(Color.red);
            error = true;
            salidaErrores += "ERRORES LEXICOS\n";
            salidaErrores += "NUMERO;ERROR;FILA;COLUMNA\n";

        } else {
            check_lex.setBackground(Color.green);
        }

        for (int i = 0; i < listaErrores.size(); i++) {

            row[0] = i + 1;
            row[1] = listaErrores.get(i).getError();
            row[2] = listaErrores.get(i).getFila();
            row[3] = listaErrores.get(i).getColumna();
            salidaErrores += row[0].toString() + ";" + row[1].toString() + ";" + row[2].toString() + ";"
                    + row[3].toString() + "\n";
            model.addRow(row);
        }
    }

    /*cargamos la tabla de erroes semtnaticos*/
    public void AgregarErrorSintactico() {
        DefaultTableModel model = (DefaultTableModel) jt_errorSintactico.getModel();
        model.setNumRows(0);
        Object[] row = new Object[4];
        LinkedList<Error> listaErrores = ans.getErroresSintax();

        if (listaErrores.size() > 0) {
            check_sin.setBackground(Color.red);
            error = true;
            salidaErrores += "ERRORES SINTACTICOS\n";
            salidaErrores += "NUMERO;ERROR;FILA;COLUMNA\n";
        } else {
            check_sin.setBackground(Color.green);
        }

        for (int i = 0; i < listaErrores.size(); i++) {

            row[0] = i + 1;
            row[1] = listaErrores.get(i).getError();
            row[2] = listaErrores.get(i).getFila();
            row[3] = listaErrores.get(i).getColumna();
            salidaErrores += row[0].toString() + ";" + row[1].toString() + ";" + row[2].toString() + ";"
                    + row[3].toString() + "\n";
            model.addRow(row);
        }
    }

    /*cargamos la tabla de errores Semanticos*/
    public void AgregarErrorSemantico() {
        DefaultTableModel model = (DefaultTableModel) jt_errorSemantico.getModel();
        model.setNumRows(0);
        Object[] row = new Object[4];
        LinkedList<Error> listaErrores = ans.getErroresSeman();

        if (listaErrores.size() > 0) {
            check_sem.setBackground(Color.red);
            error = true;
            salidaErrores += "ERRORES SEMANTICOS\n";
            salidaErrores += "NUMERO;ERROR;FILA;COLUMNA\n";
        } else {
            check_sem.setBackground(Color.green);
        }

        for (int i = 0; i < listaErrores.size(); i++) {

            row[0] = i + 1;
            row[1] = listaErrores.get(i).getError();
            row[2] = listaErrores.get(i).getFila();
            row[3] = listaErrores.get(i).getColumna();
            salidaErrores += row[0].toString() + ";" + row[1].toString() + ";" + row[2].toString() + ";"
                    + row[3].toString() + "\n";
            model.addRow(row);
        }
    }

    /*Metodod para Guardar un archivo*/
    public String AbrirArchivo(File archivo) {
        String documento = "";
        try {
            Entrada = new FileInputStream(archivo);
            int ascci;
            while ((ascci = Entrada.read()) != -1) {
                char caracter = (char) ascci;
                documento += caracter;
            }
        } catch (Exception e) {
        }
        return documento;
    }

    /*Metodo para Almacenar un Archivo*/
    public String GuardarArchivo(File archivo, String documento) {
        String mensaje = null;
        try {
            Salida = new FileOutputStream(archivo);
            byte[] bytxt = documento.getBytes();
            Salida.write(bytxt);
            mensaje = "¡Guardado con exito!";
        } catch (Exception e) {
        }
        return mensaje;
    }

    /**
     * ***********************TABLERO**************************
     */

    /*
 En este metodo llenamos una linked list de posiciones que
 posteriormente se iran cargando al tablero real cuando se 
 vayan solicitando las posciones.
     */
    public void cargarTablero() {
        LinkedList<String> ColumnaA = new LinkedList<>();
        LinkedList<String> ColumnaB = new LinkedList<>();
        LinkedList<String> ColumnaC = new LinkedList<>();
        ColumnaA.add("1");
        ColumnaA.add("2");
        ColumnaA.add("3");
        ColumnaB.add("1");
        ColumnaB.add("2");
        ColumnaB.add("3");
        ColumnaC.add("1");
        ColumnaC.add("2");
        ColumnaC.add("3");
        Posiciones = new LinkedList<>();
        Posiciones.add(new Posicion("A", ColumnaA));
        Posiciones.add(new Posicion("B", ColumnaB));
        Posiciones.add(new Posicion("C", ColumnaC));
    }

    /*Se cargan las filas*/
    public void cargarFilas() {
        cbx_fila.removeAllItems();
        for (int i = 0; i < Posiciones.size(); i++) {
            cbx_fila.addItem(Posiciones.get(i).fila);
        }
    }

    /*Cargamos las columnas, por filas*/
    public void cargarColumna(String Fila) {
        cbx_columna.removeAllItems();
        for (int i = 0; i < Posiciones.size(); i++) {
            if (Posiciones.get(i).fila.equals(Fila)) {
                for (int j = 0; j < Posiciones.get(i).columna.size(); j++) {
                    cbx_columna.addItem(Posiciones.get(i).columna.get(j));
                }
            }
        }
    }

    /*Removemos las posiciones de la linked list*/
    public void removerPosicion(String Fila, String Columna) {
        if (Posiciones.size() > 0) {
            //comprovamos que exitan filas
            for (int i = 0; i < Posiciones.size(); i++) {
                //recorremos las filas
                if (Posiciones.get(i).fila.equals(Fila)) {
                    //buscamos la coincidencia de la fila
                    for (int j = 0; j < Posiciones.get(i).columna.size(); j++) {
                        //recorremos las columnas de la fila
                        if (Posiciones.get(i).columna.get(j).equals(Columna)) {
                            //removemos la posicion.
                            Posiciones.get(i).columna.remove(j);
                        }
                        if (Posiciones.get(i).columna.size() <= 0) {
                            //verificamos si la fiila ya no cuenta con columnas para removerla
                            Posiciones.remove(i);
                            break;
                        }
                    }
                }
            }
        } else {
            cargarTablero();
            tablero.Reiniciar();
        }
    }

    /*Cargamos la posicion al tablero*/
    public void cargarPosiciones() {
        switch (valor) {
            case "A1" -> {
                tablero.casillas[0].setText(variable);
            }
            case "A2" -> {
                tablero.casillas[1].setText(variable);
            }
            case "A3" -> {
                tablero.casillas[2].setText(variable);
            }
            case "B1" -> {
                tablero.casillas[3].setText(variable);
            }
            case "B2" -> {
                tablero.casillas[4].setText(variable);
            }
            case "B3" -> {
                tablero.casillas[5].setText(variable);
            }
            case "C1" -> {
                tablero.casillas[6].setText(variable);
            }
            case "C2" -> {
                tablero.casillas[7].setText(variable);
            }
            case "C3" -> {
                tablero.casillas[8].setText(variable);
            }
        }
    }

    /**
     * ******************************************************************************************
     */
    /*
 Aqui es donde se ejecuta el set de instrucciones 
 proporcionado por el analizador sintactico y semantico
     */
    /**
     * ********************************SET DE INSTRUCCION****************************************
     */

    /*Metodod para Ejecutar una instrccion*/
    public void ejecutar() {
        if (numInstruccion < instrucciones.size()) {
            run_instruct(instrucciones.get(numInstruccion).numPeticion,
                    instrucciones.get(numInstruccion).getVariable());

        } else {
            Consola.setText(Consola.getText() + "\nEjecucion finalizada....");
            check_lex.setBackground(Color.yellow);
            check_sem.setBackground(Color.yellow);
            check_sin.setBackground(Color.yellow);
        }

    }

    /*Metodo para poner el programa en un estado 
 especifico para procesar una instruccion en 
 particular*/
    public void run_instruct(int ins, String variable) {
        switch (ins) {
            case 1 -> {/*Solicitar nombres de los jugadores*/
                Consola.setText(Consola.getText() + "Ingrese el nombre del jugador para " + variable + ": \n");
                this.variable = variable;
                this.tipo = 2;
                numInstruccion++;
                EntradaTeclado.setEnabled(true);
                btnEnter.setEnabled(true);
            }
            case 2 -> {/*Solicitar poscisiones a marcar*/

                this.variable = variable;
                this.tipo = 1;
                cargarFilas();
                enPos = true;
                numInstruccion++;
                EntradaTeclado.setEnabled(false);
                btnEnter.setEnabled(true);
            }
            case 3 -> {/*Imprimir a pantalla (Mensaje)*/
                Consola.setText(Consola.getText() + variable);
                numInstruccion++;
                ejecutar();
            }
            case 4 -> {/*"Imprimir a pantalla" (nombreJugador)*/
                Consola.setText(Consola.getText() + ans.getValoSimbolo(variable));
                numInstruccion++;
                ejecutar();
            }
            case 5 -> {/*Muestra el resultado del Juego*/
                Consola.setText(Consola.getText() + variable + resultado + "\n");
                resultado = "EMPATE";
                numInstruccion++;
                ejecutar();
            }
            case 6 -> {/*Iniciar iteracion"*/
                int iteracion = Integer.parseInt(instrucciones.get(numInstruccion).variable);
                int temp = 0;
                if (iteracion <= 0) {
                    instrucciones.get(numInstruccion).variable = instrucciones.get(numInstruccion).org;
                    numInstruccion = instrucciones.get(numInstruccion).go_to;
                } else {
                    iteracion--;
                    instrucciones.get(numInstruccion).variable = String.valueOf(iteracion);
                    numInstruccion++;
                    temp++;
                }
                ejecutar();
            }
            case 7 -> {/*Retorno de iteracion*/
                numInstruccion = instrucciones.get(numInstruccion).go_to;
                ejecutar();
            }
        }
    }
}
