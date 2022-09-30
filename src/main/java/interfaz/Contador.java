package interfaz;

/**
 * Clase que servirá para tomar cada valor de la tabla hash y transformarlo en un objeto para ser insertado
 * en la Data Grid View donde se mostrarán los resultados.
 */
public class Contador
{
    private String nombre;
    private int cantidad;

    public Contador(String nombre, int cantidad){
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

}
