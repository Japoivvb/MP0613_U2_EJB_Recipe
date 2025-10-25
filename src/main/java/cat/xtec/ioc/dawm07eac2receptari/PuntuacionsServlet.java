package cat.xtec.ioc.dawm07eac2receptari;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Cristòfol-Lluís
 */
@WebServlet(name = "PuntuacionsServlet", urlPatterns = {"/puntuacions"})
public class PuntuacionsServlet extends HttpServlet {

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
        String action = request.getParameter("action");

        switch (action.toLowerCase()) {
            case "receptespuntuadeslist":
                receptesList(request, response);
                break;
            case "deleterecepta":
                deleteRecepta(request, response);
                break;

        }

    }

    private void receptesList(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        PuntuacionsLocal puntuacionsBean = (PuntuacionsLocal) session.getAttribute("puntuacionsbean");

        if (puntuacionsBean == null) {
            puntuacionsBean = new Puntuacions();
            session.setAttribute("puntuacionsbean", puntuacionsBean);
        }

        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();

        try {
            for (Recepta r : puntuacionsBean.getReceptesPuntuades()) { 
                JSONObject obj = new JSONObject();
                obj.put("name", r.getName());
                obj.put("puntuacio", r.getPuntuacio());
                array.put(obj);
            }

            json.put("jsonArray", array);
        } catch (JSONException ex) {
            Logger.getLogger(PuntuacionsServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.print(json.toString());
        } catch (IOException ex) {
            Logger.getLogger(PuntuacionsServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void deleteRecepta(HttpServletRequest request, HttpServletResponse response) {
        String nomRecepta = request.getParameter("recepta");
        HttpSession session = request.getSession();
        PuntuacionsLocal puntuacionsBean = (PuntuacionsLocal) session.getAttribute("puntuacionsbean");

        List<Recepta> llista = puntuacionsBean.getReceptesPuntuades();
        // search recipe to remove
        for (int i = llista.size() - 1; i >= 0; i--) {
            Recepta r = llista.get(i);
            if (r.getName().equalsIgnoreCase(nomRecepta)) {
                // remove from puntuaciones bean
                r.setPuntuacio(0.0);// initialize points
                llista.remove(i);
                break; 
            }
        }
        puntuacionsBean.setReceptesPuntuades(llista); // update bean

        // return json response
        JSONObject json = new JSONObject();
        try {
            json.put("resposta", "OK");
        } catch (JSONException ex) {
            Logger.getLogger(PuntuacionsServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.print(json.toString());
        } catch (IOException ex) {
            Logger.getLogger(PuntuacionsServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

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
