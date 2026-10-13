package com.utp.controller.servlet;

import com.utp.controller.facade.AdminFacade;
import com.utp.controller.facade.interfaces.IAdminFacade;
import com.utp.model.dto.SaleDTO;
import com.utp.model.dto.UserSesionDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@WebServlet(name = "EmpleadoServlet", urlPatterns = {
        "/empleado", "/empleado/dashboard", "/empleado/pedidos", "/empleado/perfil"
})
public class EmpleadoServlet extends HttpServlet {
    private IAdminFacade adminFacade;

    @Override
    public void init() throws ServletException {
        adminFacade = new AdminFacade();
    }

    private UserSesionDTO validarEmpleado(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        UserSesionDTO usuario = session == null ? null : (UserSesionDTO) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return null;
        }
        if (!usuario.getRolesUser().contains("EMPLEADO")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso exclusivo para empleados.");
            return null;
        }
        return usuario;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserSesionDTO usuario = validarEmpleado(request, response);
        if (usuario == null) return;

        String path = request.getServletPath();
        if ("/empleado".equals(path)) {
            response.sendRedirect(request.getContextPath() + "/empleado/dashboard");
            return;
        }

        if ("/empleado/perfil".equals(path)) {
            request.getRequestDispatcher("/vistasempleado/perfil.jsp").forward(request, response);
            return;
        }

        List<SaleDTO> pedidos = Collections.emptyList();
        try {
            // Obtenemos los pedidos asignados o pendientes para el empleado
            pedidos = adminFacade.getSalesForEmployee(usuario.getIdUser());
        } catch (RuntimeException ex) {
            request.getSession().setAttribute("errorMsg", "No se pudieron cargar los pedidos. Verifica el vínculo entre usuario y empleado.");
        }

        request.setAttribute("pedidos", pedidos);

        if ("/empleado/pedidos".equals(path)) {
            request.getRequestDispatcher("/vistasempleado/pedidos.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/vistasempleado/dashboard.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserSesionDTO usuario = validarEmpleado(request, response);
        if (usuario == null) return;

        String action = request.getParameter("action");

        if ("validarPago".equals(action)) {
            try {
                int idVenta = Integer.parseInt(request.getParameter("idVenta"));
                int idPago = Integer.parseInt(request.getParameter("idPago"));
                String resultado = request.getParameter("resultado"); // "aprobado" o "rechazado"

                boolean exito = adminFacade.validatePaymentAndShip(idPago, idVenta, usuario.getIdUser(), resultado);

                if (exito) {
                    request.getSession().setAttribute("mensajeExito", "Pago " + resultado + " y procesado correctamente.");
                } else {
                    request.getSession().setAttribute("errorMsg", "No se pudo procesar la validación del pago.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                request.getSession().setAttribute("errorMsg", "Ocurrió un error inesperado al validar el pago.");
            }

            response.sendRedirect(request.getContextPath() + "/empleado/pedidos");
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida.");
        }
    }
}
