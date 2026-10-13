<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Catálogo | Datebayo Admin</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/catalogo-admin.css">
</head>

<body data-theme="dark">

<button class="mobile-menu-btn" id="mobileMenuBtn" type="button" aria-label="Abrir menú">
    <i class="fas fa-bars"></i>
</button>

<nav class="sidebar" id="sidebar">
    <a class="sidebar-brand" href="${pageContext.request.contextPath}/admin/dashboard">
        DATEBAYO ADMIN
    </a>

    <ul class="nav nav-pills flex-column mb-auto">
        <li>
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/dashboard">
                <i class="fas fa-chart-line"></i> Dashboard
            </a>
        </li>
        <li>
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/pedidos">
                <i class="fas fa-shopping-basket"></i> Pedidos
            </a>
        </li>
        <li>
            <a class="nav-link active" href="${pageContext.request.contextPath}/admin/catalogo">
                <i class="fas fa-box-open"></i> Catálogo
            </a>
        </li>
        <li>
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/inventario">
                <i class="fas fa-warehouse"></i> Inventario
            </a>
        </li>
        <li>
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/cupones">
                <i class="fas fa-ticket-alt"></i> Cupones
            </a>
        </li>
        <li>
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/empleados">
                <i class="fas fa-users"></i> Empleados
            </a>
        </li>
        <li><a href="${pageContext.request.contextPath}/admin/clientes" class="nav-link"><i class="fas fa-users"></i> Clientes</a></li>
    </ul>

    <!-- Botón para cambiar el tema -->
    <button class="theme-toggle-btn"
            id="themeToggleBtn"
            type="button"
            aria-label="Cambiar tema">
        <i id="themeIcon" class="fa-solid fa-sun"></i>
    </button>

    <a href="${pageContext.request.contextPath}/auth?action=logout" class="nav-link text-danger mt-3">
        <i class="fas fa-sign-out-alt"></i> Salir
    </a>
</nav>

<main class="main-content">

    <div class="page-heading">
        <div>
            <h1>Gestión de <span>Catálogo</span></h1>
            <p class="subtitle">Administra los productos disponibles en Datebayo.</p>
        </div>

        <button class="btn btn-primary-custom" type="button"
                data-bs-toggle="modal" data-bs-target="#modalCrearProducto">
            <i class="fa-solid fa-plus me-2"></i>Agregar producto
        </button>
    </div>

    <!-- Mensajes de respuesta -->

    <c:if test="${not empty sessionScope.mensajeExito}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <c:out value="${sessionScope.mensajeExito}"/>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Cerrar"></button>
        </div>
        <c:remove var="mensajeExito" scope="session"/>
    </c:if>

    <c:if test="${not empty sessionScope.errorMsg}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <c:out value="${sessionScope.errorMsg}"/>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Cerrar"></button>
        </div>
        <c:remove var="errorMsg" scope="session"/>
    </c:if>

    <c:if test="${not empty requestScope.errorMsg}">
        <div class="alert alert-danger" role="alert">
            <c:out value="${requestScope.errorMsg}"/>
        </div>
    </c:if>

    <!-- Tabla del catálogo -->

    <section class="admin-card">
        <div class="table-toolbar">
            <div>
                <h2>Productos registrados</h2>
                <p>Consulta, edita o retira productos del catálogo.</p>
            </div>

            <div class="search-box">
                <i class="fa-solid fa-magnifying-glass"></i>
                <input type="search" id="buscarProducto"
                       placeholder="Buscar producto..."
                       aria-label="Buscar producto">
            </div>
        </div>

        <div class="table-responsive">
            <table class="table catalog-table align-middle" id="tablaProductos">
                <thead>
                <tr>
                    <th>Producto</th>
                    <th>Categoría</th>
                    <th>Tipo</th>
                    <th>Precio</th>
                    <th>Estado</th>
                    <th class="text-end">Acciones</th>
                </tr>
                </thead>

                <tbody>
                <c:choose>
                    <c:when test="${empty productos}">
                        <tr>
                            <td colspan="6" class="empty-state">
                                <i class="fa-solid fa-box-open"></i>
                                <p>No hay productos para mostrar.</p>
                            </td>
                        </tr>
                    </c:when>

                    <c:otherwise>
                        <c:forEach var="p" items="${productos}">
                            <tr class="product-row">

                                <!-- Nombre e imagen -->

                                <td>
                                    <div class="product-cell">
                                        <c:choose>
                                            <c:when test="${not empty p.urlImage}">
                                                <img class="product-thumb"
                                                     src="<c:out value='${p.urlImage}'/>"
                                                     alt=""
                                                     onerror="this.style.display='none'">
                                            </c:when>
                                            <c:otherwise>
                                                <div class="product-thumb product-thumb-placeholder">
                                                    <i class="fa-solid fa-image"></i>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>

                                        <div>
                                            <strong><c:out value="${p.name}"/></strong>
                                            <small>#<c:out value="${p.idProduct}"/></small>
                                        </div>
                                    </div>
                                </td>

                                <!-- Categoría y tipo -->

                                <td>
                                    <c:out value="${p.idCategory}"/>
                                </td>

                                <td>
                                    <c:out value="${p.idProductType}"/>
                                </td>

                                <!-- Precio -->

                                <td class="price-cell">
                                    S/ <c:out value="${p.price}"/>
                                </td>

                                <!-- Estado -->

                                <td>
                                        <span class="status-badge status-${p.productStatus}">
                                            <c:out value="${p.productStatus}"/>
                                        </span>
                                </td>

                                <!-- Acciones -->

                                <td>
                                    <div class="action-buttons">

                                        <button type="button"
                                                class="btn-action btn-edit"
                                                title="Editar producto"
                                                data-bs-toggle="modal"
                                                data-bs-target="#modalEditarProducto"
                                                data-id="<c:out value='${p.idProduct}'/>"
                                                data-nombre="<c:out value='${p.name}'/>"
                                                data-descripcion="<c:out value='${p.description}'/>"
                                                data-imagen="<c:out value='${p.urlImage}'/>"
                                                data-precio="<c:out value='${p.price}'/>"
                                                data-categoria="<c:out value='${p.idCategory}'/>"
                                                data-tipo="<c:out value='${p.idProductType}'/>">

                                            <i class="fa-solid fa-pen"></i>
                                        </button>

                                        <c:if test="${p.productStatus ne 'pausado'}">
                                            <button type="button"
                                                    class="btn-action btn-delete"
                                                    title="Retirar producto"
                                                    data-bs-toggle="modal"
                                                    data-bs-target="#modalEliminarProducto"
                                                    data-id="<c:out value='${p.idProduct}'/>"
                                                    data-nombre="<c:out value='${p.name}'/>">

                                                <i class="fa-solid fa-trash"></i>
                                            </button>
                                        </c:if>

                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>

        <p id="sinResultados" class="empty-search d-none">
            No se encontraron productos con esa búsqueda.
        </p>
    </section>
</main>

<!-- Modal: crear producto -->

<div class="modal fade" id="modalCrearProducto" tabindex="-1"
     aria-labelledby="tituloCrearProducto" aria-hidden="true">

    <div class="modal-dialog modal-lg modal-dialog-centered modal-dialog-scrollable">
        <div class="modal-content">

            <form action="${pageContext.request.contextPath}/admin/catalogo" method="post">
                <input type="hidden" name="action" value="crearProducto">

                <div class="modal-header">
                    <div>
                        <h5 class="modal-title" id="tituloCrearProducto">Agregar producto</h5>
                        <p class="modal-subtitle">Completa los datos del nuevo producto.</p>
                    </div>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                </div>

                <div class="modal-body">
                    <div class="row g-3">

                        <div class="col-md-8">
                            <label class="form-label" for="crearNombre">Nombre</label>
                            <input class="form-control form-control-custom"
                                   id="crearNombre" name="nombre"
                                   maxlength="120" required>
                        </div>

                        <div class="col-md-4">
                            <label class="form-label" for="crearPrecio">Precio (S/)</label>
                            <input class="form-control form-control-custom"
                                   id="crearPrecio" name="precio"
                                   type="number" min="0.01" step="0.01" required>
                        </div>

                        <div class="col-md-6">
                            <label class="form-label" for="crearCategoria">Categoría</label>
                            <select class="form-select form-control-custom"
                                    id="crearCategoria" name="idCategoria" required>
                                <option value="">Selecciona una categoría</option>

                                <c:forEach var="c" items="${categorias}">
                                    <option value="<c:out value='${c.idCategory}'/>">
                                        <c:out value="${c.nameCategory}"/>
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="col-md-6">
                            <label class="form-label" for="crearTipo">Tipo de producto</label>
                            <select class="form-select form-control-custom"
                                    id="crearTipo" name="idTipoProducto" required>
                                <option value="">Selecciona un tipo</option>

                                <c:forEach var="t" items="${tiposProducto}">
                                    <option value="<c:out value='${t.idProductType}'/>">
                                        <c:out value="${t.nameProductType}"/>
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="col-12">
                            <label class="form-label" for="crearImagen">URL de imagen</label>
                            <input class="form-control form-control-custom"
                                   id="crearImagen" name="imagenUrl"
                                   type="url" placeholder="https://...">
                        </div>

                        <div class="col-12">
                            <label class="form-label" for="crearDescripcion">Descripción</label>
                            <textarea class="form-control form-control-custom"
                                      id="crearDescripcion" name="descripcion"
                                      rows="3"></textarea>
                        </div>

                    </div>
                </div>

                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary-custom" data-bs-dismiss="modal">
                        Cancelar
                    </button>
                    <button class="btn btn-primary-custom" type="submit">
                        <i class="fa-solid fa-floppy-disk me-2"></i>Guardar producto
                    </button>
                </div>
            </form>

        </div>
    </div>
</div>

<!-- Modal: editar producto -->

<div class="modal fade" id="modalEditarProducto" tabindex="-1"
     aria-labelledby="tituloEditarProducto" aria-hidden="true">

    <div class="modal-dialog modal-lg modal-dialog-centered modal-dialog-scrollable">
        <div class="modal-content">

            <form action="${pageContext.request.contextPath}/admin/catalogo" method="post">
                <input type="hidden" name="action" value="editarProducto">
                <input type="hidden" name="idProducto" id="editarId">

                <div class="modal-header">
                    <div>
                        <h5 class="modal-title" id="tituloEditarProducto">Editar producto</h5>
                        <p class="modal-subtitle">Modifica los datos y guarda los cambios.</p>
                    </div>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                </div>

                <div class="modal-body">
                    <div class="row g-3">

                        <div class="col-md-8">
                            <label class="form-label" for="editarNombre">Nombre</label>
                            <input class="form-control form-control-custom"
                                   id="editarNombre" name="nombre"
                                   maxlength="120" required>
                        </div>

                        <div class="col-md-4">
                            <label class="form-label" for="editarPrecio">Precio (S/)</label>
                            <input class="form-control form-control-custom"
                                   id="editarPrecio" name="precio"
                                   type="number" min="0.01" step="0.01" required>
                        </div>

                        <div class="col-md-6">
                            <label class="form-label" for="editarCategoria">Categoría</label>
                            <select class="form-select form-control-custom"
                                    id="editarCategoria" name="idCategoria" required>
                                <option value="">Selecciona una categoría</option>

                                <c:forEach var="c" items="${categorias}">
                                    <option value="<c:out value='${c.idCategory}'/>">
                                        <c:out value="${c.nameCategory}"/>
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="col-md-6">
                            <label class="form-label" for="editarTipo">Tipo de producto</label>
                            <select class="form-select form-control-custom"
                                    id="editarTipo" name="idTipoProducto" required>
                                <option value="">Selecciona un tipo</option>

                                <c:forEach var="t" items="${tiposProducto}">
                                    <option value="<c:out value='${t.idProductType}'/>">
                                        <c:out value="${t.nameProductType}"/>
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="col-12">
                            <label class="form-label" for="editarImagen">URL de imagen</label>
                            <input class="form-control form-control-custom"
                                   id="editarImagen" name="imagenUrl" type="url">
                        </div>

                        <div class="col-12">
                            <label class="form-label" for="editarDescripcion">Descripción</label>
                            <textarea class="form-control form-control-custom"
                                      id="editarDescripcion" name="descripcion"
                                      rows="3"></textarea>
                        </div>

                    </div>
                </div>

                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary-custom" data-bs-dismiss="modal">
                        Cancelar
                    </button>
                    <button class="btn btn-primary-custom" type="submit">
                        <i class="fa-solid fa-check me-2"></i>Guardar cambios
                    </button>
                </div>
            </form>

        </div>
    </div>
</div>

<!-- Modal: retirar producto del catálogo -->

<div class="modal fade" id="modalEliminarProducto" tabindex="-1"
     aria-labelledby="tituloEliminarProducto" aria-hidden="true">

    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">

            <form action="${pageContext.request.contextPath}/admin/catalogo" method="post">
                <input type="hidden" name="action" value="eliminarProducto">
                <input type="hidden" name="idProducto" id="eliminarId">

                <div class="modal-header">
                    <h5 class="modal-title" id="tituloEliminarProducto">Retirar producto</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                </div>

                <div class="modal-body">
                    <div class="delete-icon">
                        <i class="fa-solid fa-triangle-exclamation"></i>
                    </div>

                    <p>
                        ¿Seguro que deseas retirar
                        <strong id="eliminarNombre"></strong>
                        del catálogo?
                    </p>

                    <p class="text-muted small mb-0">
                        El producto cambiará al estado pausado. No se eliminará físicamente
                        de la base de datos.
                    </p>
                </div>

                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary-custom" data-bs-dismiss="modal">
                        Cancelar
                    </button>
                    <button class="btn btn-danger-custom" type="submit">
                        Sí, retirar producto
                    </button>
                </div>
            </form>

        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/javascript/catalogo-admin.js"></script>

</body>
</html>