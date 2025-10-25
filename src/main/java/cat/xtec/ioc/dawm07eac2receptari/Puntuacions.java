package cat.xtec.ioc.dawm07eac2receptari;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateful;

/**
 *
 * @author Cristòfol-Lluís
 */
@Stateful
public class Puntuacions implements PuntuacionsLocal {
    // initialize to avoid errors if access points without any point
    private List<Recepta> receptesPuntuades = new ArrayList<>();

    @Override
    public List<Recepta> getReceptesPuntuades() {
        return receptesPuntuades;
    }

    @Override
    public void setReceptesPuntuades(List<Recepta> receptesPuntuades) {
        this.receptesPuntuades = receptesPuntuades;
    }

}
