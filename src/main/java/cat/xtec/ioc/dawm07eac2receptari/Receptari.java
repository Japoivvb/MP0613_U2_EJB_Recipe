package cat.xtec.ioc.dawm07eac2receptari;

import cat.xtec.ioc.dawm07eac2receptari.ejb.ValidateReceptaBeanLocal;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Cristòfol-Lluís
 */
// get any request from Receptari goes to this servlet
@WebServlet(name = "Receptari", urlPatterns = {"/Receptari", "/Recepta"})
@MultipartConfig(location = "C:\\Users\\Jose\\tmp") // expect MIME multipart/form-data and set location file
public class Receptari extends HttpServlet {

    @Inject
    private ValidateReceptaBeanLocal validator;

    private List<Recepta> receptes = new ArrayList<Recepta>();

    //Directori on es guarden les imatges
    private static final String UPLOAD_DIR = "img";

    @Override
    public void init(ServletConfig config) throws ServletException {
        //load recipes from web.xml in list of servlet
        super.init(config);

        java.util.Enumeration<String> paramsXML = config.getInitParameterNames();

        while (paramsXML.hasMoreElements()) {
            String name = paramsXML.nextElement();
            String value = config.getInitParameter(name);
            Double points = Double.valueOf(value);
            receptes.add(new Recepta(name, points));
        }

        log("Recipes loaded " + receptes.size() + " from file web.xml");

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
        // based on action goes to different methods of servlet
        String action = request.getParameter("action");

        switch (action) {
            case "listReceptes":
                listReceptes(request, response);
                break;
            case "addReceptaPuntuacions":
                addReceptaPuntuacions(request, response);
                break;
            case "createRecepta":
                createRecepta(request, response);
                break;
        }

    }

    /**
     * Read recipes and return as JSON.
     */
    protected void listReceptes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();

        // for each recipe return its name and points as JSON
        for (Recepta r : receptes) {
            LinkedHashMap<String, String> jsonOrderedMap = new LinkedHashMap<>();
            jsonOrderedMap.put("name", r.getName());
            jsonOrderedMap.put("puntuacio", String.valueOf(r.getPuntuacio()));
            // check if recipe exist in puntuacionsBeans
            if (checkReceptaPuntuadaSession(request, r)) {
                jsonOrderedMap.put("afegit", "SI");
            } else {
                jsonOrderedMap.put("afegit", "NO");
            }

            JSONObject member = new JSONObject(jsonOrderedMap);
            array.put(member);
        }

        try {
            // build response json with name/puntuacio
            json.put("jsonArray", array);
        } catch (JSONException ex) {
            Logger.getLogger(Receptari.class.getName()).log(Level.SEVERE, null, ex);
        }
        out.print(json.toString());

    }

    /**
     * Add points to recipe selected
     */
    protected void addReceptaPuntuacions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // get parameters from url request
        // "Receptari?action=addReceptaPuntuacions&recepta=" + recepta + "&puntuacio=" + puntuacio;.
        String name = request.getParameter("recepta");
        Double points = Double.valueOf(request.getParameter("puntuacio"));

        JSONObject json = new JSONObject();

        // search for recipe to set points
        for (Recepta r : receptes) {
            if (r.getName().equalsIgnoreCase(name)) {
                r.setPuntuacio(points);
                addReceptaToSession(request, r);

                try {
                    // build response json with keys
                    json.put("receptaPuntuada", r.getName());
                    json.put("puntuacioRecepta", r.getPuntuacio());

                    out.print(json.toString());
                    out.close();
                    return;
                } catch (JSONException e) {
                    Logger.getLogger(Receptari.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }

    }

    protected void createRecepta(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // GET INPUTS
        String name = request.getParameter("fpname");
        Part filePart = request.getPart("fileImageName");
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

        // VALIDATIONS
        if (!isValidFileName(name, fileName)) {
            response.sendRedirect("index.html");
            return;
        }

        // UPLOAD FILE       
        // save in @MultipartConfig
        filePart.write(fileName);

        // copy to project img folder
        String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
        Path source = Paths.get("C:\\Users\\Jose\\tmp", fileName);
        Path target = Paths.get(uploadPath, fileName);
        Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);

        // SAVE RECIPE
        // Create new recipe
        Recepta nova = new Recepta(name, 0.0);
        receptes.add(nova);

        // Redirect html
        response.sendRedirect("index.html");

    }

    private boolean isValidFileName(String paramName, String fileName) {
        // Validate name recipe and name file
        if (!validator.isValidFileImageName(paramName, fileName)) {
            return false;
        }
        // Validate if exist in current recipe list
        for (Recepta r : receptes) {
            if (r.getName().equalsIgnoreCase(paramName)) {
                return false;
            }
        }
        return true;
    }

    private void addReceptaToSession(HttpServletRequest request, Recepta recepta) {
        PuntuacionsLocal puntuacionsBean = (PuntuacionsLocal) request.getSession().getAttribute("puntuacionsbean");
        if (puntuacionsBean == null) {
            // to avoid null pointer in first time
            puntuacionsBean = new Puntuacions();
            puntuacionsBean.setReceptesPuntuades(new ArrayList<>());
            request.getSession().setAttribute("puntuacionsbean", puntuacionsBean);
        }
        puntuacionsBean.getReceptesPuntuades().add(recepta);

    }

    private boolean checkReceptaPuntuadaSession(HttpServletRequest request, Recepta recepta) {
        PuntuacionsLocal puntuacionsBean = (PuntuacionsLocal) request.getSession().getAttribute("puntuacionsbean");

        // read points in case bean exist and there is any point
        if (puntuacionsBean != null && puntuacionsBean.getReceptesPuntuades() != null) {
            // read all recipes added in puntuacionsBean
            for (Recepta r : puntuacionsBean.getReceptesPuntuades()) {
                if (r.getName().equalsIgnoreCase(recepta.getName())) {
                    return true; // recipe found in puntuacionsBean
                }
            }
        }

        // if not found return false
        return false;
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
