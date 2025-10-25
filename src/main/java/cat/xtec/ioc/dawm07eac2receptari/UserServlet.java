package cat.xtec.ioc.dawm07eac2receptari;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Cristòfol-Lluís
 */
@WebServlet(name = "UserServlet", urlPatterns = {"/user"})
public class UserServlet extends HttpServlet {

    @Resource
    Validator validator;

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
        // get action from request
        String action = request.getParameter("action");

        // redirect based on action
        switch (action) {
            case "formUser":
                formUser(request, response);
                break;

            case "newUser":
                newUser(request, response);
                break;
        }
    }

    private void formUser(HttpServletRequest request, HttpServletResponse response) {
        // get session
        HttpSession session = request.getSession();

        // get user bean from session
        UserLocal user = (UserLocal) session.getAttribute("userbean");

        // if it does not exist, create a new one
        if (user == null) {
            user = new User();
            user.setUser("");
            user.setName("");
            user.setLastname("");
            session.setAttribute("userbean", user);

        }

        // build json with user values
        JSONObject json = new JSONObject();
        try {
            json.put("user", user.getUser() != null ? user.getUser() : "");
            json.put("name", user.getName() != null ? user.getName() : "");
            json.put("lastname", user.getLastname() != null ? user.getLastname() : "");
        } catch (JSONException ex) {
            Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        // return JSON as response
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.print(json.toString());
        } catch (IOException ex) {
            Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void newUser(HttpServletRequest request, HttpServletResponse response) {
        // get bean from session
        HttpSession session = request.getSession();
        UserLocal userBean = (UserLocal) session.getAttribute("userbean");

        // get parameters from request
        String userParam = request.getParameter("user");
        String nameParam = request.getParameter("name");
        String lastnameParam = request.getParameter("lastname");
        userBean.setUser(userParam);
        userBean.setName(nameParam);
        userBean.setLastname(lastnameParam);

        JSONObject json = new JSONObject();

        // VALIDATION
        Set<ConstraintViolation<UserLocal>> violations = validator.validate(userBean);

        try {
            // if there is any error
            if (!violations.isEmpty()) {
                // get first violation message
                String msg = violations.iterator().next().getMessage();
                json.put("resposta", msg);

            } else {
                json.put("resposta", "OK");
            }
        } catch (JSONException ex) {
            Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Devolvemos JSON como respuesta
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.print(json.toString());
        } catch (IOException ex) {
            Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //
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
