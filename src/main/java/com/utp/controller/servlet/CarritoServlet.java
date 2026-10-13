package com.utp.controller.servlet;

import com.utp.controller.facade.CustomerPurchasesFacade;
import com.utp.controller.facade.interfaces.ICustomerPurchaseFacade;
import com.utp.model.dto.CartDTO;
import com.utp.model.dto.ShoppingCartItemDTO;
import com.utp.model.dto.UserSesionDTO;
import com.utp.model.entity.PayEntity;
import com.utp.model.entity.VoucherPayEntity;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CarritoServlet", urlPatterns = {"/carrito"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
        maxFileSize = 1024 * 1024 * 10,       // 10MB
        maxRequestSize = 1024 * 1024 * 50     // 50MB
)
public class CarritoServlet extends HttpServlet {

    private ICustomerPurchaseFacade compraFacade;

    @Override
    public void init() throws ServletException {
        compraFacade = new CustomerPurchasesFacade();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Verificar que el usuario haya iniciado sesión.
        if (session == null
                || session.getAttribute("usuarioLogueado") == null) {

            response.sendRedirect(
                    request.getContextPath() + "/index.jsp"
            );
            return;
        }

        // Crear un carrito nuevo si todavía no existe.
        if (session.getAttribute("carrito") == null) {
            CartDTO nuevoCarrito = new CartDTO();
            nuevoCarrito.setItems(new ArrayList<>());
            session.setAttribute("carrito", nuevoCarrito);
        }

        request.getRequestDispatcher(
                "/vistascliente/carrito.jsp"
        ).forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Validar la sesión.
        if (session == null
                || session.getAttribute("usuarioLogueado") == null) {

            response.sendRedirect(
                    request.getContextPath() + "/index.jsp"
            );
            return;
        }

        String action = request.getParameter("action");

        CartDTO carrito = (CartDTO) session.getAttribute("carrito");

        UserSesionDTO usuario = (UserSesionDTO)
                session.getAttribute("usuarioLogueado");

        if ("agregar".equals(action)) {
            agregarProducto(request, response, session, carrito);
            return;
        }

        if ("eliminar".equals(action)) {
            eliminarProducto(request, response, session, carrito);
            return;
        }

        if ("pagar".equals(action)) {
            procesarPago(
                    request,
                    response,
                    session,
                    carrito,
                    usuario
            );

        if ("aplicarCupon".equals(action)) {
            aplicarCupon(request, response, session, carrito);
            return;
        }
            return;
        }

        response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "La acción solicitada no es válida."
        );
    }

    private void agregarProducto(
            HttpServletRequest request,
            HttpServletResponse response,
            HttpSession session,
            CartDTO carrito
    ) throws ServletException, IOException {

        try {
            String idProductoTexto = request.getParameter("idProducto");
            String nombre = request.getParameter("nombreProducto");
            String precioTexto = request.getParameter("precio");

            if (idProductoTexto == null
                    || nombre == null
                    || nombre.isBlank()
                    || precioTexto == null) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Faltan datos del producto."
                );
                return;
            }

            int idProducto = Integer.parseInt(idProductoTexto);
            double precio = Double.parseDouble(precioTexto);

            if (idProducto <= 0 || !Double.isFinite(precio) || precio < 0) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Los datos del producto no son válidos."
                );
                return;
            }

            if (carrito == null) {
                carrito = new CartDTO();
            }

            // Asegurar que la lista de items sea una ArrayList mutable
            if (carrito.getItems() == null) {
                carrito.setItems(new ArrayList<>());
            } else {
                try {
                    // Probamos si la lista permite agregar elementos
                    carrito.getItems().size();
                } catch (Exception e) {
                    carrito.setItems(new ArrayList<>(carrito.getItems()));
                }
            }

            ShoppingCartItemDTO item = new ShoppingCartItemDTO();
            item.setIdProduct(idProducto);
            item.setName(nombre.trim());
            item.setUnitPrice(precio);
            item.setQuantity(1);

            // Si por diseño del DTO la lista sigue siendo inmodificable, la recreamos de forma segura
            try {
                carrito.getItems().add(item);
            } catch (UnsupportedOperationException e) {
                List<ShoppingCartItemDTO> listaMutable = new ArrayList<>(carrito.getItems());
                listaMutable.add(item);
                carrito.setItems(listaMutable);
            }

            session.setAttribute("carrito", carrito);

            response.sendRedirect(
                    request.getContextPath() + "/tienda"
            );

        } catch (NumberFormatException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "El identificador o el precio del producto no son válidos."
            );
        }
    }

    private void procesarPago(
            HttpServletRequest request,
            HttpServletResponse response,
            HttpSession session,
            CartDTO carrito,
            UserSesionDTO usuario
    ) throws ServletException, IOException {

        if (carrito == null
                || carrito.getItems() == null
                || carrito.getItems().isEmpty()) {

            mostrarErrorPago(
                    request,
                    response,
                    "Tu carrito está vacío."
            );
            return;
        }

        String metodoPago = request.getParameter("metodoPago");

        if (metodoPago == null || metodoPago.isBlank()) {
            mostrarErrorPago(
                    request,
                    response,
                    "Selecciona un método de pago."
            );
            return;
        }

        String nombreArchivoEjemplo =
                "voucher_" + System.currentTimeMillis() + ".jpg";

        PayEntity pago = new PayEntity();
        pago.setPaymentMethod(metodoPago.trim());
        pago.setAmount(carrito.getTotal());

        VoucherPayEntity comprobante = new VoucherPayEntity();
        comprobante.setFileName(nombreArchivoEjemplo);
        comprobante.setFileUrl("/uploads/" + nombreArchivoEjemplo);

        try {
            boolean exito = compraFacade.makePurchase(
                    usuario.getIdUser(),
                    carrito,
                    pago,
                    comprobante,
                    0.0
            );

            if (exito) {
                // Vaciar el carrito después de registrar la compra.
                session.removeAttribute("carrito");

                // El servlet /perfil volverá a cargar el historial.
                response.sendRedirect(
                        request.getContextPath() + "/perfil"
                );

            } else {
                mostrarErrorPago(
                        request,
                        response,
                        "Hubo un error al procesar tu compra. Inténtalo nuevamente."
                );
            }

        } catch (Exception e) {
            getServletContext().log(
                    "Error al procesar la compra del usuario "
                            + usuario.getIdUser(),
                    e
            );

            mostrarErrorPago(
                    request,
                    response,
                    "Ocurrió un error al procesar tu compra."
            );
        }
    }

    private void mostrarErrorPago(
            HttpServletRequest request,
            HttpServletResponse response,
            String mensaje
    ) throws ServletException, IOException {

        request.setAttribute("errorPago", mensaje);

        request.getRequestDispatcher(
                "/vistascliente/carrito.jsp"
        ).forward(request, response);
    }

    private void eliminarProducto(
            HttpServletRequest request,
            HttpServletResponse response,
            HttpSession session,
            CartDTO carrito
    ) throws ServletException, IOException {
        try {
            String idProductoTexto = request.getParameter("idProducto");
            if (idProductoTexto != null && !idProductoTexto.isBlank()) {
                int idProducto = Integer.parseInt(idProductoTexto);
                if (carrito != null) {
                    carrito.deleteItemShoppingCart(idProducto);
                    session.setAttribute("carrito", carrito);
                }
            }
        } catch (NumberFormatException e) {
            getServletContext().log("Error al eliminar producto del carrito: ID inválido", e);
        }

        response.sendRedirect(request.getContextPath() + "/carrito");
    }

    private void aplicarCupon(
            HttpServletRequest request,
            HttpServletResponse response,
            HttpSession session,
            CartDTO carrito
    ) throws ServletException, IOException {

        String codigoCupon = request.getParameter("codigoCupon");

        if (codigoCupon != null && !codigoCupon.isBlank()) {


            if (codigoCupon.equalsIgnoreCase("DATEBAYO10")) {
                // Lógica para aplicar el descuento al DTO del carrito
                session.setAttribute("mensajeCupon", "Cupón '" + codigoCupon + "' aplicado correctamente.");
            } else {
                session.setAttribute("errorCupon", "El cupón '" + codigoCupon + "' no existe o está vencido.");
            }
        }

        // Redirigir de vuelta al carrito para mostrar el nuevo total y el mensaje
        response.sendRedirect(request.getContextPath() + "/carrito");
    }
}
