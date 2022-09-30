package negocio;

import soporte.TSBHashtable;

/**
 * Clase que almacenará cada uno de los contadores según los distintos criterios.
 * Cada contador será una tabla hash que tendrá como clave los datos según cada criterio
 * (nombre de la marca de la vacuna, número de orden o sexo) y en su valor la cantidad de
 * apariciones. Será utilizada para almacenar como valor en otra tabla hash que tendrá
 * como clave cada departamento.
 */
public class Conteo {
    private TSBHashtable vacunas;
    private TSBHashtable marcas;
    private TSBHashtable sexos;

    public Conteo(String vacuna, String marca, String sexo)
    {
        vacunas = new TSBHashtable();
        vacunas.put(vacuna, 1);
        marcas = new TSBHashtable();
        marcas.put(marca, 1);
        sexos = new TSBHashtable();
        sexos.put(sexo, 1);

    }

    public TSBHashtable getVacunas() {
        return vacunas;
    }

    public TSBHashtable getMarcas() {
        return marcas;
    }

    public TSBHashtable getSexos() {
        return sexos;
    }

}
