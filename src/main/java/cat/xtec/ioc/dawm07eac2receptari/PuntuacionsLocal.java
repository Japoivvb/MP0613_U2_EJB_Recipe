package cat.xtec.ioc.dawm07eac2receptari;

import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Cristòfol-Lluís
 */
@Local
public interface PuntuacionsLocal {
    public List<Recepta> getReceptesPuntuades();
    public void setReceptesPuntuades(List<Recepta> receptesPuntuades);
}
