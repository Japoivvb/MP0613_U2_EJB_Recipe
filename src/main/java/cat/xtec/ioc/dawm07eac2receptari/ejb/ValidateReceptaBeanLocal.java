/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package cat.xtec.ioc.dawm07eac2receptari.ejb;

import javax.ejb.Local;

/**
 *
 * @author JoseAlbertoPortugalO
 */
@Local
public interface ValidateReceptaBeanLocal {
    public Boolean isValidFileImageName(String receptaName, String fileImageName);
    
}
