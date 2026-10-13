package com.utp.controller.servlet;

import com.utp.controller.facade.AdminFacade;
import com.utp.controller.facade.CustomerPurchasesFacade;
import com.utp.controller.facade.StoreFacade;
import com.utp.controller.facade.interfaces.IAdminFacade;
import com.utp.controller.facade.interfaces.ICustomerPurchaseFacade;
import com.utp.controller.facade.interfaces.IStoreFacade;
import com.utp.model.dto.CredentialDeliveredDTO;
import com.utp.model.dto.ProductCatalogDTO;
import com.utp.model.dto.UserSesionDTO;
import com.utp.model.entity.SaleEntity;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@WebServlet(
        name = "TiendaServlet",
        urlPatterns = {"/tienda", "/producto"}
)
public class TiendaServlet extends HttpServlet {

    private IStoreFacade tiendaFacade;
    private ICustomerPurchaseFacade compraFacade;
    private IAdminFacade adminFacade;

    @Override
    public void init() throws ServletException {
        tiendaFacade = new StoreFacade();
        compraFacade = new CustomerPurchasesFacade();
        adminFacade = new AdminFacade();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        UserSesionDTO usuario = (session != null)
                ? (UserSesionDTO) session.getAttribute("usuarioLogueado")
                : null;

        String contextPath = request.getContextPath();

        if (usuario == null) {
            response.sendRedirect(contextPath + "/index.jsp");
            return;
        }

        String path = request.getServletPath();

        try {

            if ("/tienda".equals(path)) {

                List<ProductCatalogDTO> productos =
                        tiendaFacade.getCatalogAvailable();

                request.setAttribute("productos", productos);

                request.getRequestDispatcher(
                        "/vistascliente/tienda.jsp"
                ).forward(request, response);

                // Perfil e historial de compras
            } else if ("/perfil".equals(path)) {

                List<SaleEntity> misCompras =
                        compraFacade.viewPurchaseHistory(
                                usuario.getIdUser()
                        );

                request.setAttribute("misCompras", misCompras);

                List<CredentialDeliveredDTO> misCredenciales =
                        adminFacade.getCredentialsByClient(
                                usuario.getIdUser()
                        );

                request.setAttribute("misCredenciales", misCredenciales);

                request.getRequestDispatcher(
                        "/vistascliente/perfil.jsp"
                ).forward(request, response);

            } else if ("/producto".equals(path)) {

                String idParam = request.getParameter("id");

                if (idParam == null || idParam.isBlank()) {
                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "Falta el identificador del producto."
                    );
                    return;
                }

                final int idProduct;

                try {
                    idProduct = Integer.parseInt(idParam);

                    if (idProduct <= 0) {
                        response.sendError(
                                HttpServletResponse.SC_BAD_REQUEST,
                                "El identificador del producto debe ser positivo."
                        );
                        return;
                    }

                } catch (NumberFormatException e) {
                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "El identificador del producto no es válido."
                    );
                    return;
                }

                ProductCatalogDTO producto =
                        tiendaFacade.showProductDetail(idProduct);

                // Si no existe o no está disponible, devolver 404.
                if (producto == null) {
                    response.sendError(
                            HttpServletResponse.SC_NOT_FOUND,
                            "El producto no existe o no está disponible."
                    );
                    return;
                }

                request.setAttribute("producto", producto);

                request.getRequestDispatcher(
                        "/vistascliente/detalleproducto.jsp"
                ).forward(request, response);

            } else {

                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND
                );
            }

        } catch (Exception e) {

            getServletContext().log(
                    "Error en TiendaServlet procesando la ruta: " + path,
                    e
            );

            if (!response.isCommitted()) {
                response.sendError(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Ocurrió un error al cargar la página."
                );
            }
        }
    }
}