package soporte;

import negocio.Conteo;

import java.io.*;

public class ProcesarTexto {
    private File file;

    /**
     * Contructor de la clase que recibe como parametro un archivo que lo asignará a su atributo
     * Siendo el archivo que luego será procesado en sus métodos
     * @param archivo objeto del tipo File que será el archivo a procesar
     */
    public ProcesarTexto(File archivo)
    {
        this.file = archivo;
    }

    /**
     * Recorre un archivo y procesa cada línea de este. Por cada línea separa sus datos a partir
     * de la coma que hay entre ellos. A partir de ello filtra aquellos que poseen en su campo
     * número 6, que representa de la jurisdicción de aplicación, sea "Córdoba". Si lo es, lo
     * busca en una tabla hash utilizando como clave el nombre del departamento. En caso de
     * encontrarlo, toma el objeto de su value el cual contiene un objeto Conteo que posee como
     * atributos tres tablas hash: uno por cada contador. A través del método Comparación suma en
     * las tablas correspondiente utilizando el valor del campo como clave para ingresar en la
     * tabla hash. Ello lo hace con cada departamento y con una clave TODOS que almacena la
     * contabilidad de todos los departamentos. Una vez finalizada la lectura del archivo, retorna
     * la tabla hash que posee como clave los departamentos y como valor un objeto Conteo.
     * @return Tabla hash que posee como clave los departamentos y como valor un objeto Conteo
     */

    public TSBHashtable identificarVacunas() {

        //Donde se almacenarán los campos de cada línea
        String campos[];

        //Tabla hash donde se almacenarán los objetos Conteo que contendrá los conteos
        TSBHashtable tabla = new TSBHashtable(15);

        //Try Catch para manejar el error que surge en caso de que no se seleccione el archivo
        try{
            BufferedReader lector = new BufferedReader(new InputStreamReader( new FileInputStream(this.file.getAbsolutePath()), "utf-8"));
              String linea = lector.readLine();

            while (linea != null){
                campos = linea.split(",");

                /*Línea de código que sirve para realizar el procesamiento de los campos
                Quitando las comas, pero aumenta los tiempos de procesamiento
                (En las últimas líneas se encuentra el resto del código)*/

                //String campos[] = procesar(campos_sp);

                //Filtrar solo las líneas que corresponden a Córdoba
                if(campos[6].compareTo("\"Córdoba\"") == 0)
                {
                    //Se verifica si ya existe una entrada en la tabla, tanto para la clave
                    //TODOS como con cada departamento. Si no existe se crea una nueva entrada
                    // y si existe se incrementa el contador según corresponda
                    if(!tabla.containsKey("TODOS"))
                    {
                        Conteo vacunacion = new Conteo(campos[13],campos[11],campos[0]);
                        tabla.put("TODOS", vacunacion);
                    }
                    else
                    {
                        Conteo todas_vacunacion = (Conteo) tabla.get("TODOS");
                        comparacion(campos[13], todas_vacunacion.getVacunas());
                        comparacion(campos[11], todas_vacunacion.getMarcas());
                        comparacion(campos[0], todas_vacunacion.getSexos());

                    }
                    if(tabla.containsKey(campos[8]))
                    {
                        Conteo vacunacion = (Conteo) tabla.get(campos[8]);
                        comparacion(campos[13], vacunacion.getVacunas());
                        comparacion(campos[11], vacunacion.getMarcas());
                        comparacion(campos[0], vacunacion.getSexos());

                    }
                    else
                    {
                        Conteo vacunacion = new Conteo(campos[13],campos[11],campos[0]);
                        tabla.put(campos[8], vacunacion);
                    }

                }
                linea = lector.readLine();
            }
        }
        catch (FileNotFoundException e)
        {
            System.err.print("No se encontró el archivo " + file);
        }

        catch (Exception e)
        {
            System.out.println("Ocurrio un error");
        }

        return tabla;
    }

    /**
     * Toma el valor de la tabla hash pasada como parametro utilizando como clave el
     * parametro atributo. Al valor obtenido lo incrementa en uno y lo cambia en la tabla.
     * En el caso de que no exista ninguna entrada con dicha clave, la crea e inicia el contador
     * en 1.
     * @param atributo String que servirá como clave para la tabla hash
     * @param contador tabla hash que contiene alguno de los contadores necesarios para el proceso
     */

    public void comparacion(String atributo, TSBHashtable contador)
    {
        if (contador.containsKey(atributo))
        {
            int vac = (int) contador.get(atributo);
            vac += 1;
            contador.put(atributo, vac);
        }
        else
        {
            contador.put(atributo, 1);
        }
    }

    /*
    *
    * A través de este metodo se pueden quitar las comillas de los datos levantados de la tabla
    * Pero no se lo utiliza debido a que aumentan los tiempos de procesamiento NOTABLEMENTE.
    * */
//    private String[] procesar(String [] campos){
//        String result[] = new String[campos.length];
//
//        for (int i=0;i<result.length;i++){
//            result[i] = campos[i].replaceAll("\"", "").replaceAll("\"", "");
//        }
//        return result;
//
//    }


}
