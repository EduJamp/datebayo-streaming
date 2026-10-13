package com.utp.controller.servlet;

import com.utp.config.ConnectionPostgresSQL;
import com.utp.controller.facade.AdminFacade;
import com.utp.controller.facade.interfaces.IAdminFacade;
import com.utp.model.dto.CuponDTO;
import com.utp.model.dto.SaleDTO;
import com.utp.model.dto.UserSesionDTO;
import com.utp.model.entity.*;
import com.utp.persistence.dao.UserDAO;
import com.utp.util.enums.DigitalAccountStatus;
import com.utp.util.enums.DigitalProfileStatus;
import com.utp.util.enums.ProductStatus;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "AdminServlet", urlPatterns = {
        "/admin/dashboard",
        "/admin/pedidos",
        "/admin/inventario",
        "/admin/catalogo",
        "/admin/cupones",
        "/admin/empleados",
        "/admin/clientes"
})
public class AdminServlet extends HttpServlet {

    private IAdminFacade adminFacade;

    @Override
    public void init() throws ServletException {
        adminFacade = new AdminFacade();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        UserSesionDTO usuario = (session != null)
                ? (UserSesionDTO) session.getAttribute("usuarioLogueado")
                : null;

        if (usuario == null || usuario.getRolesUser() == null
                || !usuario.getRolesUser().contains("ADMIN")) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        String path = request.getServletPath();

        switch (path) {
            case "/admin/dashboard":
                request.getRequestDispatcher(
                        "/vistasadmin/dashboard.jsp").forward(request, response);
                break;

            case "/admin/pedidos":
                request.setAttribute("pedidos", adminFacade.getOrderTry());
                request.getRequestDispatcher(
                        "/vistasadmin/pedidos.jsp").forward(request, response);
                break;

            case "/admin/inventario":
                cargarProductosInventario(request);
                // NUEVO: Cargar las cuentas para mostrarlas en la tabla de inventario
                request.setAttribute("cuentasInventario", adminFacade.listarCuentasInventario());
                request.getRequestDispatcher(
                        "/vistasadmin/inventario.jsp").forward(request, response);
                break;

            case "/admin/catalogo":
                cargarCatalogo(request);
                request.getRequestDispatcher(
                        "/vistasadmin/catalogo.jsp").forward(request, response);
                break;

            case "/admin/cupones":
                try {
                    request.setAttribute("campanias", adminFacade.listarCampanias());
                    request.setAttribute("cupones", adminFacade.listarCupones());
                } catch (Exception e) {
                    request.setAttribute("errorMsg", "No se pudieron cargar los cupones y campañas.");
                }
                request.getRequestDispatcher(
                        "/vistasadmin/cupones.jsp").forward(request, response);
                break;

            case "/admin/empleados":
                try {
                    request.setAttribute("empleados", new UserDAO().listEmployees());
                } catch (Exception e) {
                    request.setAttribute(
                            "errorMsg", "No se pudo cargar la lista de empleados.");
                }

                request.getRequestDispatcher(
                        "/vistasadmin/empleados.jsp").forward(request, response);
                break;

            case "/admin/clientes":
                request.setAttribute("clientes", adminFacade.listarClientes());
                request.getRequestDispatcher("/vistasadmin/clientes.jsp").forward(request, response);
                break;

            default:
                response.sendRedirect(
                        request.getContextPath() + "/admin/dashboard");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);
        UserSesionDTO usuario = (session != null) ? (UserSesionDTO) session.getAttribute("usuarioLogueado") : null;

        if (usuario == null || !usuario.getRolesUser().contains("ADMIN")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
            return;
        }

        try {
            if ("validarPago".equals(action)) {
                procesarValidacionPago(request, response, usuario);
            } else if ("registrarCuenta".equals(action)) {
                procesarRegistroCuenta(request, response, usuario);
            } else if ("editarCuenta".equals(action)) {        // NUEVO
                procesarEdicionCuenta(request, response);
            } else if ("eliminarCuenta".equals(action)) {      // NUEVO
                procesarEliminacionCuenta(request, response);
            } else if ("crearProducto".equals(action)) {
                procesarCreacionProducto(request, response, usuario);
            } else if ("editarProducto".equals(action)) {
                procesarEdicionProducto(request, response);
            } else if ("eliminarProducto".equals(action)) {
                procesarEliminacionProducto(request, response);
            } else if ("editarCliente".equals(action)) {
                procesarEdicionCliente(request, response);
            } else if ("bloquearCliente".equals(action)) {
                procesarBloqueoCliente(request, response);
            } else if ("crearEmpleado".equals(action)) {
            }else if ("crearCampania".equals(action)) {
                procesarCreacionCampania(request, response, usuario);
            } else if ("editarCampania".equals(action)) {
                procesarEdicionCampania(request, response);
            } else if ("eliminarCampania".equals(action)) {
                procesarEliminacionCampania(request, response);
            } else if ("generarCupon".equals(action)) {
                procesarGeneracionCupon(request, response, usuario);
            } else if ("editarCupon".equals(action)) {
                procesarEdicionCupon(request, response);
            } else if ("eliminarCupon".equals(action)) {
                procesarEliminacionCupon(request, response);
            } else if ("crearEmpleado".equals(action)) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMsg", "Ocurrió un error al procesar la solicitud: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        }
    }


    private void procesarValidacionPago(HttpServletRequest request, HttpServletResponse response, UserSesionDTO usuario) throws IOException {
        int idPago = Integer.parseInt(request.getParameter("idPago"));
        int idVenta = Integer.parseInt(request.getParameter("idVenta"));
        String resultado = request.getParameter("resultado");

        boolean exito = adminFacade.validatePaymentAndShip(idPago, idVenta, usuario.getIdUser(), resultado);

        if (exito) {
            request.getSession().setAttribute("mensajeExito", "Pago " + resultado + " y procesado correctamente.");
        } else {
            request.getSession().setAttribute("errorMsg", "Ocurrió un error al procesar el pago o despachar el producto.");
        }
        response.sendRedirect(request.getContextPath() + "/admin/pedidos");
    }

    private void procesarRegistroCuenta(HttpServletRequest request, HttpServletResponse response, UserSesionDTO usuario) throws IOException {
        int idProducto = Integer.parseInt(request.getParameter("idProducto"));
        String correoCuenta = request.getParameter("correoCuenta");
        String claveCuenta = request.getParameter("claveCuenta");

        DigitalAccountEntity cuenta = new DigitalAccountEntity();
        cuenta.setIdProduct(idProducto);
        cuenta.setAccountEmail(correoCuenta);
        cuenta.setPaswordHash(claveCuenta);
        cuenta.setCreatedBy(usuario.getIdUser());
        cuenta.setStatus(DigitalAccountStatus.valueOf("disponible"));

        List<DigitalProfileEntity> perfiles = new ArrayList<>();

        // Iterar hasta 5 perfiles posibles
        for (int i = 1; i <= 5; i++) {
            String nombrePerfil = request.getParameter("nombrePerfil" + i);
            String pinPerfil = request.getParameter("pinPerfil" + i);

            if (nombrePerfil != null && !nombrePerfil.trim().isEmpty()) {
                DigitalProfileEntity perfil = new DigitalProfileEntity();
                perfil.setProfileName(nombrePerfil.trim());
                perfil.setEncryptedPasswordProfile(pinPerfil != null ? pinPerfil.trim() : "");
                perfil.setDigitalProfileStatus(DigitalProfileStatus.valueOf("disponible"));
                perfiles.add(perfil);
            }
        }

        cuenta.setProfileCapacity(perfiles.size());

        boolean exito = adminFacade.registerMainAccount(cuenta, perfiles);

        if (exito) {
            request.getSession().setAttribute("mensajeExito", "Cuenta madre y " + perfiles.size() + " perfiles generados con éxito.");
        } else {
            request.getSession().setAttribute("errorMsg", "Error al registrar la cuenta madre en el inventario.");
        }
        response.sendRedirect(request.getContextPath() + "/admin/inventario");
    }

    //Métodos para Editar y Eliminar
    private void procesarEdicionCuenta(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            DigitalAccountEntity accountEdit = new DigitalAccountEntity();
            accountEdit.setIdDigitalAcount(Integer.parseInt(request.getParameter("idCuentaEdit")));
            accountEdit.setAccountEmail(request.getParameter("correoEdit"));
            accountEdit.setPaswordHash(request.getParameter("claveEdit")); // Puede venir vacío si no la cambian

            String estado = request.getParameter("estadoEdit");
            if (estado != null) {
                estado = estado.toLowerCase();
                if (estado.equals("pausado")) estado = "suspendida";
                accountEdit.setStatus(DigitalAccountStatus.valueOf(estado));
            }

            boolean exito = adminFacade.editarCuentaInventario(accountEdit);
            if (exito) {
                request.getSession().setAttribute("mensajeExito", "Cuenta actualizada correctamente.");
            } else {
                request.getSession().setAttribute("errorMsg", "Error al actualizar la cuenta.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMsg", "Datos inválidos para editar la cuenta.");
        }
        response.sendRedirect(request.getContextPath() + "/admin/inventario");
    }

    private void procesarEliminacionCuenta(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int idCuenta = Integer.parseInt(request.getParameter("idCuentaDelete"));
            boolean exito = adminFacade.eliminarCuentaInventario(idCuenta);

            if (exito) {
                request.getSession().setAttribute("mensajeExito", "Cuenta eliminada (o suspendida por seguridad) correctamente.");
            } else {
                request.getSession().setAttribute("errorMsg", "No se pudo eliminar la cuenta.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMsg", "Error al procesar la eliminación.");
        }
        response.sendRedirect(request.getContextPath() + "/admin/inventario");
    }

    private void procesarCreacionProducto(HttpServletRequest request, HttpServletResponse response, UserSesionDTO usuario) throws IOException {
        ProductEntity producto = new ProductEntity();
        producto.setIdCategory(Integer.parseInt(request.getParameter("idCategoria")));
        producto.setIdProductType(Integer.parseInt(request.getParameter("idTipoProducto")));
        producto.setName(request.getParameter("nombre"));
        producto.setDescription(request.getParameter("descripcion"));
        producto.setUrlImage(request.getParameter("imagenUrl"));
        producto.setPrice(Double.parseDouble(request.getParameter("precio")));
        producto.setProductStatus(ProductStatus.valueOf("publicado"));
        producto.setCreatedBy(usuario.getIdUser());

        boolean exito = adminFacade.addProduct(producto);

        if (exito) {
            request.getSession().setAttribute("mensajeExito", "Producto agregado al catálogo correctamente.");
        } else {
            request.getSession().setAttribute("errorMsg", "Error al crear el producto.");
        }
        response.sendRedirect(request.getContextPath() + "/admin/catalogo");
    }

    private void cargarCatalogo(HttpServletRequest request) {
        try {
            List<ProductEntity> productos = adminFacade.getAllProductsForAdmin();
            List<CategoryEntity> categorias = adminFacade.getCategories();
            List<ProductTypeEntity> tiposProducto = adminFacade.getProductTypes();

            request.setAttribute("productos", productos);
            request.setAttribute("categorias", categorias);
            request.setAttribute("tiposProducto", tiposProducto);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMsg", "No se pudo cargar el catálogo.");
            request.setAttribute("productos", new ArrayList<ProductEntity>());
            request.setAttribute("categorias", new ArrayList<CategoryEntity>());
            request.setAttribute("tiposProducto", new ArrayList<ProductTypeEntity>());
        }
    }

    private void cargarProductosInventario(HttpServletRequest request) {
        try {
            List<ProductEntity> productos = adminFacade.getAllProductsForAdmin();
            List<Map<String, Object>> productosInventario = new ArrayList<>();

            for (ProductEntity producto : productos) {
                if (producto.getProductStatus() == ProductStatus.publicado) {
                    Map<String, Object> item = new LinkedHashMap<>();
                    item.put("id", producto.getIdProduct());
                    item.put("nombre", producto.getName());
                    productosInventario.add(item);
                }
            }
            request.setAttribute("productosInventario", productosInventario);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMsg", "No se pudieron cargar los productos publicados.");
            request.setAttribute("productosInventario", new ArrayList<Map<String, Object>>());
        }
    }

    private void procesarEdicionProducto(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        try {
            int idProducto = Integer.parseInt(request.getParameter("idProducto"));
            int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
            int idTipoProducto = Integer.parseInt(request.getParameter("idTipoProducto"));
            String nombre = request.getParameter("nombre");
            String descripcion = request.getParameter("descripcion");
            String imagenUrl = request.getParameter("imagenUrl");
            double precio = Double.parseDouble(request.getParameter("precio"));

            if (nombre == null || nombre.trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del producto es obligatorio.");
            }
            if (!Double.isFinite(precio) || precio < 0) {
                throw new IllegalArgumentException("El precio debe ser un número válido mayor o igual a cero.");
            }

            ProductEntity producto = new ProductEntity();
            producto.setIdProduct(idProducto);
            producto.setIdCategory(idCategoria);
            producto.setIdProductType(idTipoProducto);
            producto.setName(nombre.trim());
            producto.setDescription(descripcion == null ? null : descripcion.trim());
            producto.setUrlImage(imagenUrl == null || imagenUrl.trim().isEmpty() ? null : imagenUrl.trim());
            producto.setPrice(precio);

            List<ProductEntity> productos = adminFacade.getAllProductsForAdmin();
            ProductEntity actual = productos.stream()
                    .filter(p -> p.getIdProduct() == idProducto)
                    .findFirst()
                    .orElse(null);

            if (actual == null) {
                session.setAttribute("errorMsg", "No se encontró el producto.");
            } else {
                producto.setProductStatus(actual.getProductStatus());
                producto.setAllowsMiltiplePurchases(actual.isAllowsMiltiplePurchases());
                producto.setMinimumQuantity(actual.getMinimumQuantity());
                producto.setMacximumQuantity(actual.getMacximumQuantity());

                boolean ok = adminFacade.updateProduct(producto);

                session.setAttribute(ok ? "mensajeExito" : "errorMsg",
                        ok ? "Producto actualizado correctamente." : "No se pudo actualizar el producto.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMsg", "No se pudo editar el producto. Verifica los datos ingresados.");
        }
        response.sendRedirect(request.getContextPath() + "/admin/catalogo");
    }

    private void procesarEliminacionProducto(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        try {
            int idProducto = Integer.parseInt(request.getParameter("idProducto"));
            boolean ok = adminFacade.pauseProduct(idProducto);
            session.setAttribute(ok ? "mensajeExito" : "errorMsg",
                    ok ? "Producto pausado y retirado del catálogo." : "No se encontró el producto o no se pudo pausar.");
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMsg", "No se pudo retirar el producto del catálogo.");
        }
        response.sendRedirect(request.getContextPath() + "/admin/catalogo");
    }

    private void procesarCreacionCampania(HttpServletRequest request, HttpServletResponse response, UserSesionDTO usuario) throws IOException {
        String nombreCampania = request.getParameter("nombreCampania");
        double porcentaje = Double.parseDouble(request.getParameter("porcentaje"));

        boolean exito = adminFacade.createDiscountCampaing(nombreCampania, porcentaje, usuario.getIdUser());

        if (exito) {
            request.getSession().setAttribute("mensajeExito", "Campaña de descuento activada correctamente.");
        } else {
            request.getSession().setAttribute("errorMsg", "Error al generar la campaña.");
        }
        response.sendRedirect(request.getContextPath() + "/admin/cupones");
    }

    private void procesarEdicionCampania(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int idCampania = Integer.parseInt(request.getParameter("idCampaniaEdit"));
            String nombre = request.getParameter("nombreCampaniaEdit");
            double porcentaje = Double.parseDouble(request.getParameter("porcentajeEdit"));
            String estado = request.getParameter("estadoEdit");

            boolean exito = adminFacade.editarCampania(idCampania, nombre, porcentaje, estado);
            request.getSession().setAttribute(exito ? "mensajeExito" : "errorMsg",
                    exito ? "Campaña actualizada correctamente." : "Error al actualizar la campaña.");
        } catch (Exception e) {
            request.getSession().setAttribute("errorMsg", "Datos inválidos para editar la campaña.");
        }
        response.sendRedirect(request.getContextPath() + "/admin/cupones");
    }

    private void procesarEliminacionCampania(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int idCampania = Integer.parseInt(request.getParameter("idCampaniaDelete"));
            boolean exito = adminFacade.eliminarCampania(idCampania);
            request.getSession().setAttribute(exito ? "mensajeExito" : "errorMsg",
                    exito ? "Campaña inactivada correctamente." : "No se pudo inactivar la campaña.");
        } catch (Exception e) {
            request.getSession().setAttribute("errorMsg", "Error al procesar la eliminación.");
        }
        response.sendRedirect(request.getContextPath() + "/admin/cupones");
    }

    private void procesarGeneracionCupon(HttpServletRequest request, HttpServletResponse response, UserSesionDTO usuario) throws IOException {
        try {
            int idCampania = Integer.parseInt(request.getParameter("idCampania"));
            String codigo = request.getParameter("codigoCupon");
            String limiteStr = request.getParameter("limiteUsos");
            Integer limiteUsos = (limiteStr != null && !limiteStr.trim().isEmpty()) ? Integer.parseInt(limiteStr) : null;

            boolean exito = adminFacade.generarCupon(idCampania, codigo, limiteUsos, usuario.getIdUser());
            request.getSession().setAttribute(exito ? "mensajeExito" : "errorMsg",
                    exito ? "Cupón generado exitosamente." : "Error al generar cupón. Puede que el código ya exista.");
        } catch (Exception e) {
            request.getSession().setAttribute("errorMsg", "Datos inválidos al generar el cupón.");
        }
        response.sendRedirect(request.getContextPath() + "/admin/cupones");
    }

    private void procesarEdicionCupon(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int idCupon = Integer.parseInt(request.getParameter("idCuponEdit"));
            String codigo = request.getParameter("codigoEdit");
            String limiteStr = request.getParameter("limiteUsosEdit");
            Integer limiteUsos = (limiteStr != null && !limiteStr.trim().isEmpty()) ? Integer.parseInt(limiteStr) : null;
            String estado = request.getParameter("estadoEdit");

            boolean exito = adminFacade.editarCupon(idCupon, codigo, limiteUsos, estado);
            request.getSession().setAttribute(exito ? "mensajeExito" : "errorMsg",
                    exito ? "Cupón actualizado." : "Error al actualizar cupón.");
        } catch (Exception e) {
            request.getSession().setAttribute("errorMsg", "Datos inválidos para editar el cupón.");
        }
        response.sendRedirect(request.getContextPath() + "/admin/cupones");
    }

    private void procesarEliminacionCupon(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int idCupon = Integer.parseInt(request.getParameter("idCuponDelete"));
            boolean exito = adminFacade.eliminarCupon(idCupon);
            request.getSession().setAttribute(exito ? "mensajeExito" : "errorMsg",
                    exito ? "Cupón inactivado." : "Error al inactivar el cupón.");
        } catch (Exception e) {
            request.getSession().setAttribute("errorMsg", "Error al procesar.");
        }
        response.sendRedirect(request.getContextPath() + "/admin/cupones");
    }

    private void procesarEdicionCliente(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int idCliente = Integer.parseInt(request.getParameter("idCliente"));
            String nombres = request.getParameter("nombres");
            String apellidos = request.getParameter("apellidos");
            String correo = request.getParameter("correo");
            String telefono = request.getParameter("telefono");
            String estado = request.getParameter("estado");

            boolean exito = adminFacade.editarCliente(idCliente, nombres, apellidos, correo, telefono, estado);
            request.getSession().setAttribute(exito ? "mensajeExito" : "errorMsg",
                    exito ? "Cliente actualizado correctamente." : "Error al actualizar los datos del cliente.");
        } catch (Exception e) {
            request.getSession().setAttribute("errorMsg", "Datos inválidos para editar al cliente.");
        }
        response.sendRedirect(request.getContextPath() + "/admin/clientes");
    }

    private void procesarBloqueoCliente(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int idCliente = Integer.parseInt(request.getParameter("idClienteDelete"));
            boolean exito = adminFacade.bloquearCliente(idCliente);
            request.getSession().setAttribute(exito ? "mensajeExito" : "errorMsg",
                    exito ? "Cliente bloqueado del sistema." : "No se pudo bloquear al cliente.");
        } catch (Exception e) {
            request.getSession().setAttribute("errorMsg", "Error al procesar el bloqueo.");
        }
        response.sendRedirect(request.getContextPath() + "/admin/clientes");
    }
}
