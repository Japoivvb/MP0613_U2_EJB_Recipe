package cat.xtec.ioc.dawm07eac2receptari;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Cristòfol-Lluís
 */

@MultipartConfig()
public class Receptari extends HttpServlet {

    private List<Recepta> receptes = new ArrayList<Recepta>();

    //Directori on es guarden les imatges
    private static final String UPLOAD_DIR = "img";

    @Override
    public void init(ServletConfig config) throws ServletException {

    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }


    protected void listReceptes(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    }

    protected void addReceptaPuntuacions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }


    protected void createRecepta(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    private boolean isValidFileName(String paramName, String fileName){
        return false; //Posa't per que no doni error l'IDE
    }


    private void addReceptaToSession(HttpServletRequest request, Recepta recepta) {

    }


    private boolean checkReceptaPuntuadaSession(HttpServletRequest request, Recepta recepta)  {
        return false; //Posa't per que no doni error l'IDE
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
