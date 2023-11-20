/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import dao.ProductoJpaController;
import dto.Producto;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
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
@WebServlet(name = "GenerarPDF", urlPatterns = {"/generarPDF"})
public class GenerarPDF extends HttpServlet {

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
        response.setContentType("application/pdf"); // Configura el tipo de contenido como PDF
        response.setHeader("Content-Disposition", "inline; filename=reporte.pdf");

        try ( OutputStream out = response.getOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, out);

            document.open();
            ProductoJpaController usuDAO = new ProductoJpaController();

            List<Producto> resultado = usuDAO.findProductoEntities();

            // Crear una tabla
            PdfPTable table = new PdfPTable(7); // 7 columnas para tus datos

            // Añadir estilo a la tabla
            table.setWidthPercentage(100);
            table.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

            // Encabezados de la tabla
            String[] headers = {"Código", "Nombre", "Marca", "Categoría", "Forma", "Precio", "Stock"};
            for (String header : headers) {
                PdfPCell headerCell = new PdfPCell(new Phrase(header));
                headerCell.setBackgroundColor(BaseColor.GRAY);
                table.addCell(headerCell);
            }

            // Datos de la base de datos
            for (Producto i : resultado) {
                table.addCell(i.getCodiProd().toString());
                table.addCell(i.getNombProd());
                table.addCell(i.getMarcProd());
                table.addCell(i.getCateProd());
                table.addCell(i.getFormProd());
                table.addCell(i.getPrecProd().toString());
                table.addCell(i.getStocProd().toString());
            }

            document.add(table); // Agrega la tabla al PDF
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
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
