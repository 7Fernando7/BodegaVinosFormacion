package bodegavininho;

public class RegionVino {

    String nombre;
    String pais;

    void mostrarRegion() {

        if (nombre.equalsIgnoreCase("Rioja")) {
            System.out.println("Vino tinto mayoritariamente");
        } else if (nombre.equalsIgnoreCase("Ribera")) {
            System.out.println("Vinos de alta graduación");
        } else {
            System.out.println("Otra región vinícola");
        }
    }

}
