package com.utp.controller.servlet;

import com.utp.controller.facade.AuthFacade;
import com.utp.controller.facade.interfaces.IAuthFacade;
import com.utp.model.dto.UserSesionDTO;
import com.utp.model.entity.UserEntity;
import com.utp.util.enums.UserStatus;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "AuthServlet", urlPatterns = {"/auth"})
public class AuthServlet extends HttpServlet {

    private IAuthFacade authFacade;

    @Override
    public void init() throws ServletException {
        authFacade = new AuthFacade();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if ("logout".equals(action)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("login".equals(action)) {
            procesarLogin(request, response);
        } else if ("register".equals(action)) {
            procesarRegistro(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        }
    }

    private void procesarLogin(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String correo = request.getParameter("correo");
        String clave = request.getParameter("clave");

        UserSesionDTO usuario = authFacade.login(correo, clave);

        if (usuario != null) {
            HttpSession session = request.getSession();
            session.setAttribute("usuarioLogueado", usuario);

            // Redirección basada en roles
            if (usuario.getRolesUser().contains("ADMIN")) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else if (usuario.getRolesUser().contains("EMPLEADO")) {
                response.sendRedirect(request.getContextPath() + "/empleado/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/tienda"); // Cliente
            }
        } else {
            // Mandamos un mensaje de error al JSP
            request.setAttribute("errorLogin", "Correo o contraseña incorrectos.");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }

    private void procesarRegistro(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UserEntity nuevoUsuario = new UserEntity();
        nuevoUsuario.setName(request.getParameter("nombres"));
        nuevoUsuario.setLastname(request.getParameter("apellidos"));
        nuevoUsuario.setEmail(request.getParameter("correoRegistro"));
        nuevoUsuario.setPasswordHash(request.getParameter("claveRegistro"));
        nuevoUsuario.setCellphone(request.getParameter("telefono"));
        nuevoUsuario.setUserStatus(UserStatus.activo);

        boolean registrado = authFacade.registerClient(nuevoUsuario);

        if (registrado) {
            request.setAttribute("exitoRegistro", "¡Cuenta creada con éxito! Por favor inicia sesión.");
            request.setAttribute("showLogin", true);
        } else {
            request.setAttribute("errorRegistro", "Error al crear la cuenta. El correo podría estar en uso.");
            request.setAttribute("showRegister", true);
        }
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}