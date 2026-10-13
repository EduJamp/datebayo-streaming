package com.utp.controller.servlet;

import com.utp.controller.facade.AdminFacade;
import com.utp.controller.facade.CustomerPurchasesFacade;
import com.utp.controller.facade.interfaces.ICustomerPurchaseFacade;
import com.utp.model.dto.CredentialDeliveredDTO;
import com.utp.model.dto.UserSesionDTO;
import com.utp.model.entity.SaleEntity;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ServletClientePerfil", urlPatterns = {"/cliente/perfil", "/perfil"})
public class ServletClientePerfil extends HttpServlet {

    private AdminFacade adminFacade;
    private ICustomerPurchaseFacade compraFacade;

    @Override
    public void init() throws ServletException {
        adminFacade = new AdminFacade();
        compraFacade = new CustomerPurchasesFacade();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        UserSesionDTO usuario = (session != null) ? (UserSesionDTO) session.getAttribute("usuarioLogueado") : null;

        String contextPath = request.getContextPath();

        // Validar si el usuario ha iniciado sesión
        if (usuario == null) {
            response.sendRedirect(contextPath + "/index.jsp");
            return;
        }

        try {
            // Obtener el historial de compras del cliente
            List<SaleEntity> misCompras = compraFacade.viewPurchaseHistory(usuario.getIdUser());
            request.setAttribute("misCompras", misCompras);

            // Obtener las credenciales activas de los productos digitales asignados
            List<CredentialDeliveredDTO> misCredenciales = adminFacade.getCredentialsByClient(usuario.getIdUser());
            request.setAttribute("misCredenciales", misCredenciales);

            // Redirigir a la vista de perfil del cliente
            request.getRequestDispatcher("/vistascliente/perfil.jsp").forward(request, response);

        } catch (Exception e) {
            getServletContext().log("Error al cargar el perfil del cliente ID: " + usuario.getIdUser(), e);
            if (!response.isCommitted()) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ocurrió un error al cargar tu perfil.");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
