/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import com.google.gson.Gson;
import dao.ProductoJpaController;
import dto.Producto;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import security.SHA256;

/**
 *
 * @author User
 */
@WebServlet(name = "AgregarProductos", urlPatterns = {"/agregarProductos"})
public class AgregarProductos extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        
        try ( PrintWriter out = response.getWriter()) {
            try {
            String json = "";
            String token = request.getParameter("token");
            String logi= request.getParameter("logi");
            String hashlogin = SHA256.getSHA256(logi) ;
            if (hashlogin.equals(token) ) {
                Gson g = new Gson();
                ProductoJpaController usuDAO= new ProductoJpaController();
                String codiProd = request.getParameter("codiProd");
                int codiintProd = Integer.parseInt(codiProd);
                String nombProd = request.getParameter("nombProd");
                String marcProd = request.getParameter("marcProd");
                String cateProd = request.getParameter("cateProd");
                String formProd = request.getParameter("formProd");
                String precProd = request.getParameter("precProd");
                String stocProd = request.getParameter("stocProd");
                Producto i = new Producto(codiintProd, nombProd, marcProd, cateProd, formProd, precProd,stocProd);
                try {
                String resultado = g.toJson(usuDAO.create(i));
                out.print(resultado);
                if (resultado!=null) {
                
                json = "{\"resultado\":\"ok\"}";
            } else {
                json = "{\"resultado\":\"error\"}";
            }
                out.print(json);
                } catch (Exception e) {
                    json = "{\"resultado\":\"error\"}";
                    out.println(json);
                    e.printStackTrace();
                }
                
                
            }else{
                out.print("{\"resultado\":\"error\"}");
            }
            } catch (Exception e) {
                out.print("{\"resultado\":\""+e+"\"}");
            }
            
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
