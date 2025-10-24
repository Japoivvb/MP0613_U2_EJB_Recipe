package cat.xtec.ioc.dawm07eac2receptari;

import java.util.List;
import javax.ejb.Stateful;

/**
 *
 * @author Cristòfol-Lluís
 */
@Stateful
public class Puntuacions implements PuntuacionsLocal {
    private List<Recepta> receptesPuntuades;

    @Override
    public List<Recepta> getReceptesPuntuades() {
        return receptesPuntuades;
    }

    @Override
    public void setReceptesPuntuades(List<Recepta> receptesPuntuades) {
        this.receptesPuntuades = receptesPuntuades;
    }

}
