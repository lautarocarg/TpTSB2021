package interfaz;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import negocio.Conteo;
import soporte.TSBHashtable;
import soporte.ProcesarTexto;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class Controlador_vacunacion {

    @FXML
    private TableView dgv_tabla;

    @FXML
    private ComboBox cmb_depto;

    @FXML
    private ComboBox cmb_criterio;

    @FXML
    private Label lbl_nombre;

    @FXML
    private Button btn_buscar;

    @FXML
    private Button btn_abrir;

    //Tabla hash que se cargará según el departamento elegido
    private TSBHashtable contadores;

    public void initialize()
    {
        /*Se inicializan los objetos opciones y se los carga en el combo*/

        Opciones op1 = new Opciones("Cantidad de dosis por orden", 1);
        Opciones op2 = new Opciones("Cantidad de dosis por sexo", 2);
        Opciones op3 = new Opciones("Cantidad de dosis por vacuna", 3);


        ObservableList criterios = FXCollections.observableArrayList();

        criterios.addAll(op1, op2, op3);

        cmb_criterio.getItems().addAll(criterios);

    }

    @FXML
    void click_btn_abrir(ActionEvent event) throws FileNotFoundException {
        /*
        Comienza iniciando el selector de archivo, que serán del tipo .csv para ser procesado.
        Luego muestra su nombre en un Label en la pantalla.
         */

        FileChooser selectorArchivo = new FileChooser();
        FileChooser.ExtensionFilter filtro = new FileChooser.ExtensionFilter("Archivos CSV", "*.csv");
        selectorArchivo.getExtensionFilters().add(filtro);
        selectorArchivo.setTitle("Seleccione el archivo a procesar");
        List listaArchivos= selectorArchivo.showOpenMultipleDialog(null);

        File archivo = (File) listaArchivos.get(0);

        lbl_nombre.setVisible(true);
        lbl_nombre.setText("Archivo: " + archivo.getName());

        /*
        Crea una clase que será la encargada de recorrer el archivo y realizar el conteo de la información
         */
        ProcesarTexto arc_contadores = new ProcesarTexto(archivo);

        //Tabla hash que tendrá una entrada por departamento y en cada valor tendrá un objeto del tipo
        //Conteo que llevará la información de cada criterio
        contadores = arc_contadores.identificarVacunas();

        //Cargar los datos de las tablas en el combo. Se extraen las llaves de cada tabla,
        //ya que son los departamentos encontrados en el archivo.
        ObservableList ol = FXCollections.observableArrayList();

        Set<String> keys = contadores.keySet();

        for (String key : keys)
        {
            ol.add(key);
        }

        cmb_depto.getItems().addAll(ol);

        //Se habilita la elección del departamento
        cmb_depto.setDisable(false);

        //Se deshabilita la elección de archivo.
        btn_abrir.setDisable(true);

    }

    @FXML
    void click_cmb_depto(ActionEvent event)
    {
        //Una vez elegido el departamento, se habilita la elección del criterio
        cmb_criterio.setDisable(false);
    }

    @FXML
    void click_cmb_criterio(ActionEvent event)
    {
        //Ya elegido el departamento y el criterio se habilita la búsqueda da la información solicitada
        btn_buscar.setDisable(false);
    }

    @FXML
    void click_btn_buscar(ActionEvent event)
    {
        //Limpiar todo lo que anteriormente tenia la grilla
        dgv_tabla.getColumns().clear();
        dgv_tabla.getItems().clear();

        //Lista qúe tendrá aquello que se mostrará en la grilla
        ObservableList<Contador> contador = FXCollections.observableArrayList();

        //Opción elegida
        Opciones op = (Opciones) cmb_criterio.getValue();

        //Objeto del tipo Conteo que tendrá las tablas con los contadores. Según la
        //opción elegida, se cargará con una u ottra.
        Conteo tablas;

        switch (op.getNumero()){
            case 1:
                //Obtengo de la tabla hash de todos los departamentos junto a sus contadores
                //el objeto Conteo del departamento elegido. De dicho departamento obtengo
                //la tabla cque contiene la información acerca de las dosis
                tablas = (Conteo) contadores.get(cmb_depto.getValue());
                TSBHashtable vacunas_v = tablas.getVacunas();

                //Extraigo de la tabla sus datos y los coloco en la grilla
                carga(vacunas_v, contador, "Orden de dosis");
                break;
            case 2:
                //Obtengo de la tabla hash de todos los departamentos junto a sus contadores
                //el objeto Conteo del departamento elegido. De dicho departamento obtengo
                //la tabla cque contiene la información acerca de las vacunas por sexo
                tablas = (Conteo) contadores.get(cmb_depto.getValue());
                TSBHashtable vacunas_s = tablas.getSexos();

                //Extraigo de la tabla sus datos y los coloco en la grilla
                carga(vacunas_s, contador, "Sexo");
                break;
            case 3:
                //Obtengo de la tabla hash de todos los departamentos junto a sus contadores
                //el objeto Conteo del departamento elegido. De dicho departamento obtengo
                //la tabla cque contiene la información acerca de las vacunas según su marca.
                tablas = (Conteo) contadores.get(cmb_depto.getValue());
                TSBHashtable vacunas_m = tablas.getMarcas();

                //Extraigo de la tabla sus datos y los coloco en la grilla
                carga(vacunas_m, contador, "Marca");
                break;
        }

    }

    /**
     * Carga la grilla con los datos pasados como parámetros. La tabla hash pasada como parámetro
     * contendrá como llave en cada una de sus entradas la descripción de aquello que se quiere contar
     * como por ejemplo "Pfizer". A partir de cada entrada genero un objeto Contador que tendrá la descripción
     * y la cantidad. Estos objetos Contador son almacenados en una lista pasada como parámetro que luego
     * serán los items de la grilla. Además se pasa como parámetro el nombre que tendrá la columna
     * @param campo tabla hash con los contadores de un departamento
     * @param contador lista que se utilizará para cargar la grilla
     * @param desc nombre que tendrá la columna en la grilla
     */
    private void carga(TSBHashtable campo, ObservableList<Contador> contador, String desc)
    {
        /*
        Obtener las llaves y sus valores
         */
        Set<String> keys = campo.keySet();

        for (String key : keys)
        {
            Contador c = new Contador(key, (int)campo.get(key));
            contador.add(c);
        }

        /*
        Se crean las columnas que tendrá la grilla
         */

        TableColumn<Contador, String> colDescripcion = new TableColumn<>(desc);
        TableColumn<Contador, Integer> colNumero = new TableColumn<>("Cantidad");

        /*
        Se establece de donde tomarán los datos las columnas
         */
        colNumero.setCellValueFactory(new PropertyValueFactory<Contador, Integer>("cantidad"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Contador, String>("nombre"));

        dgv_tabla.getColumns().addAll(colDescripcion, colNumero);
        dgv_tabla.getItems().addAll(contador);

    }


    /**
     * Clase interna que tendrá la información de cada opción sirviendo para cargar los combos
     * donde se mostraran y para determinar que opción fue elegida.
     */
    private class Opciones
    {
        //Descripción de la opción
        private String opcion;
        //Número que representa la opción.
        private int numero;

        public Opciones(String opcion, int numero)
        {
            this.opcion = opcion;
            this.numero = numero;
        }

        public int getNumero() {
            return numero;
        }

        @Override
        public String toString() {
            return opcion;
        }
    }



}
