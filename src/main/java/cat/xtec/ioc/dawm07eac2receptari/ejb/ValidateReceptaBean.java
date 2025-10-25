/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package cat.xtec.ioc.dawm07eac2receptari.ejb;

import javax.ejb.Stateless;

/**
 *
 * @author JoseAlbertoPortugalO
 */
@Stateless
public class ValidateReceptaBean implements ValidateReceptaBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    @Override
    public Boolean isValidFileImageName(String articleName, String fileImageName) {

        // get name from file
        int dotIndex = fileImageName.lastIndexOf(".");
        String nameWithoutExtension = fileImageName.substring(0, dotIndex);
        
        // check recipe name is same as file uplodaded
        return nameWithoutExtension.equalsIgnoreCase(articleName);
    }
}
