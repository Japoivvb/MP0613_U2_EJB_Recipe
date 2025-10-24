package cat.xtec.ioc.dawm07eac2receptari;


/**
 *
 * @author Cristòfol-Lluís
 */
public class Recepta {
    private String name;
    private Double puntuacio;

    public Recepta(String name, Double puntuacio) {
        this.name = name;
        this.puntuacio = puntuacio;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPuntuacio() {
        return puntuacio;
    }

    public void setPuntuacio(Double puntuacio) {
        this.puntuacio = puntuacio;
    }

}
