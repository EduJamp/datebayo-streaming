<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Admin - Cupones | Datebayo</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cupones-admin.css">
</head>
<body data-theme="dark">

<button class="mobile-menu-btn" id="mobileMenuBtn" type="button" aria-label="Abrir menú">
    <i class="fas fa-bars"></i>
</button>

<nav class="sidebar" id="sidebar">
    <a href="${pageContext.request.contextPath}/admin/dashboard" class="sidebar-brand">DATEBAYO ADMIN</a>
    <ul class="nav nav-pills flex-column mb-auto">
        <li><a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link"><i class="fas fa-chart-line"></i> Dashboard</a></li>
        <li><a href="${pageContext.request.contextPath}/admin/pedidos" class="nav-link"><i class="fas fa-shopping-basket"></i> Pedidos</a></li>
        <li><a href="${pageContext.request.contextPath}/admin/catalogo" class="nav-link"><i class="fas fa-box-open"></i> Catálogo</a></li>
        <li><a href="${pageContext.request.contextPath}/admin/inventario" class="nav-link"><i class="fas fa-warehouse"></i> Inventario</a></li>
        <li><a href="${pageContext.request.contextPath}/admin/cupones" class="nav-link active"><i class="fas fa-ticket-alt"></i> Cupones</a></li>
        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin/empleados"><i class="fas fa-users"></i> Empleados</a></li>
        <li><a href="${pageContext.request.contextPath}/admin/clientes" class="nav-link "><i class="fas fa-users"></i> Clientes</a></li>
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

    <!-- Alertas de éxito o error -->
    <c:if test="${not empty sessionScope.errorMsg}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <c:out value="${sessionScope.errorMsg}"/>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Cerrar"></button>
        </div>
        <c:remove var="errorMsg" scope="session"/>
    </c:if>

    <c:if test="${not empty sessionScope.mensajeExito}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <c:out value="${sessionScope.mensajeExito}"/>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Cerrar"></button>
        </div>
        <c:remove var="mensajeExito" scope="session"/>
    </c:if>

    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>Gestión de <span class="accent-text">Campañas y Cupones</span></h2>
    </div>

    <!-- ========================================== -->
    <!-- SECCIÓN 1: CAMPAÑAS DE DESCUENTO           -->
    <!-- ========================================== -->
    <section class="admin-card mb-5">
        <h4 class="mb-4"><i class="fas fa-bullhorn text-primary me-2"></i> Campañas Base</h4>

        <!-- Formulario Crear Campaña -->
        <form action="${pageContext.request.contextPath}/admin/cupones" method="post" class="mb-4 pb-4 border-bottom border-secondary">
            <input type="hidden" name="action" value="crearCampania">
            <div class="row g-3">
                <div class="col-md-5">
                    <label class="form-label text-muted small">Nombre de la campaña</label>
                    <input class="form-control form-control-custom" name="nombreCampania" placeholder="Ej. Black Friday" required maxlength="100">
                </div>
                <div class="col-md-4">
                    <label class="form-label text-muted small">Descuento (%)</label>
                    <input class="form-control form-control-custom" name="porcentaje" type="number" min="0.01" max="100" step="0.01" placeholder="Ej. 15.50" required>
                </div>
                <div class="col-md-3 d-flex align-items-end">
                    <button class="btn btn-primary-custom w-100" type="submit"><i class="fas fa-plus me-1"></i> Crear Campaña</button>
                </div>
            </div>
        </form>

        <!-- Tabla de Campañas -->
        <div class="table-responsive">
            <table class="table table-custom table-hover align-middle">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Descuento</th>
                    <th>Estado</th>
                    <th class="text-end">Acciones</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="camp" items="${campanias}">
                    <tr>
                        <td>#<c:out value="${camp.idCampania}" /></td>
                        <td><strong><c:out value="${camp.nombre}" /></strong></td>
                        <td><c:out value="${camp.porcentajeDescuento}" />%</td>
                        <td>
                            <span class="badge ${camp.estado == 'activa' ? 'bg-success' : (camp.estado == 'inactiva' ? 'bg-danger' : 'bg-warning')} text-uppercase">
                              <c:out value="${camp.estado}" />
                            </span>
                        </td>
                        <td class="text-end">
                            <!-- Botón Editar -->
                            <button type="button" class="btn btn-sm btn-outline-info" data-bs-toggle="modal" data-bs-target="#modalEditCamp${camp.idCampania}"><i class="fas fa-edit"></i></button>

                            <!-- Botón Eliminar (Abre el Modal) -->
                            <button type="button" class="btn btn-sm btn-outline-danger" data-bs-toggle="modal" data-bs-target="#modalDeleteCamp${camp.idCampania}"><i class="fas fa-trash"></i></button>

                            <!-- Modal Editar Campaña -->
                            <div class="modal fade text-start" id="modalEditCamp${camp.idCampania}" tabindex="-1" aria-hidden="true">
                                <div class="modal-dialog modal-dialog-centered">
                                    <div class="modal-content modal-custom border-0 shadow-lg">
                                        <form action="${pageContext.request.contextPath}/admin/cupones" method="POST">
                                            <div class="modal-header border-bottom-0">
                                                <h5 class="modal-title">Editar Campaña</h5>
                                                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                                            </div>
                                            <div class="modal-body">
                                                <input type="hidden" name="action" value="editarCampania">
                                                <input type="hidden" name="idCampaniaEdit" value="${camp.idCampania}">

                                                <div class="mb-3">
                                                    <label class="form-label">Nombre</label>
                                                    <input type="text" name="nombreCampaniaEdit" class="form-control form-control-custom" value="${camp.nombre}" required>
                                                </div>
                                                <div class="mb-3">
                                                    <label class="form-label">Descuento (%)</label>
                                                    <input type="number" name="porcentajeEdit" class="form-control form-control-custom" value="${camp.porcentajeDescuento}" min="0.01" max="100" step="0.01" required>
                                                </div>
                                                <div class="mb-3">
                                                    <label class="form-label">Estado</label>
                                                    <select name="estadoEdit" class="form-select form-control-custom">
                                                        <option value="activa" ${camp.estado == 'activa' ? 'selected' : ''}>Activa</option>
                                                        <option value="borrador" ${camp.estado == 'borrador' ? 'selected' : ''}>Borrador</option>
                                                        <option value="inactiva" ${camp.estado == 'inactiva' ? 'selected' : ''}>Inactiva</option>
                                                        <option value="vencida" ${camp.estado == 'vencida' ? 'selected' : ''}>Vencida</option>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="modal-footer border-top-0">
                                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                                                <button type="submit" class="btn btn-primary-custom">Guardar Cambios</button>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>

                            <!-- Modal Eliminar Campaña -->
                            <div class="modal fade text-start" id="modalDeleteCamp${camp.idCampania}" tabindex="-1" aria-hidden="true">
                                <div class="modal-dialog modal-dialog-centered">
                                    <div class="modal-content modal-custom border-0 shadow-lg">
                                        <form action="${pageContext.request.contextPath}/admin/cupones" method="POST">
                                            <div class="modal-header border-bottom-0">
                                                <h5 class="modal-title text-danger"><i class="fas fa-exclamation-triangle me-2"></i> Confirmar Acción</h5>
                                                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                                            </div>
                                            <div class="modal-body">
                                                <input type="hidden" name="action" value="eliminarCampania">
                                                <input type="hidden" name="idCampaniaDelete" value="${camp.idCampania}">
                                                <p class="mb-2">¿Estás seguro de que deseas inactivar la campaña <strong><c:out value="${camp.nombre}"/></strong>?</p>
                                                <small class="text-muted">No se podrán generar nuevos cupones asociados a esta campaña.</small>
                                            </div>
                                            <div class="modal-footer border-top-0">
                                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                                                <button type="submit" class="btn btn-danger">Sí, inactivar</button>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty campanias}">
                    <tr><td colspan="5" class="text-center py-4 text-muted">No hay campañas registradas.</td></tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </section>

    <!-- ========================================== -->
    <!-- SECCIÓN 2: CUPONES GENERADOS               -->
    <!-- ========================================== -->
    <section class="admin-card mb-5">
        <h4 class="mb-4"><i class="fas fa-tags text-warning me-2"></i> Códigos de Cupón</h4>

        <!-- Formulario Crear Cupón -->
        <form action="${pageContext.request.contextPath}/admin/cupones" method="post" class="mb-4 pb-4 border-bottom border-secondary">
            <input type="hidden" name="action" value="generarCupon">
            <div class="row g-3">
                <div class="col-md-4">
                    <label class="form-label text-muted small">Campaña Base</label>
                    <select name="idCampania" class="form-select form-control-custom" required>
                        <option value="" disabled selected>Selecciona una campaña...</option>
                        <c:forEach var="camp" items="${campanias}">
                            <option value="${camp.idCampania}">${camp.nombre} (-${camp.porcentajeDescuento}%)</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-3">
                    <label class="form-label text-muted small">Código</label>
                    <input type="text" name="codigoCupon" class="form-control form-control-custom" placeholder="INVIERNO20" style="text-transform: uppercase;" required>
                </div>
                <div class="col-md-3">
                    <label class="form-label text-muted small">Límite de usos</label>
                    <input type="number" name="limiteUsos" class="form-control form-control-custom" min="1" placeholder="(Ilimitado)">
                </div>
                <div class="col-md-2 d-flex align-items-end">
                    <button class="btn btn-warning w-100 fw-bold" type="submit"><i class="fas fa-plus me-1"></i> Generar</button>
                </div>
            </div>
        </form>

        <!-- Tabla de Cupones -->
        <div class="table-responsive">
            <table class="table table-custom table-hover align-middle">
                <thead>
                <tr>
                    <th>Código</th>
                    <th>Campaña Asignada</th>
                    <th>Dscto</th>
                    <th>Usos Disp.</th>
                    <th>Estado</th>
                    <th class="text-end">Acciones</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="cupon" items="${cupones}">
                    <tr>
                        <td><strong class="text-warning"><c:out value="${cupon.codigo}" /></strong></td>
                        <td><c:out value="${cupon.nombreCampania}" /></td>
                        <td><c:out value="${cupon.porcentajeDescuento}" />%</td>
                        <td><c:out value="${cupon.limiteUsos == null ? 'Ilimitado' : cupon.limiteUsos}" /></td>
                        <td>
                            <span class="badge ${cupon.estado == 'activo' ? 'bg-success' : 'bg-danger'} text-uppercase">
                                <c:out value="${cupon.estado}" />
                            </span>
                        </td>
                        <td class="text-end">
                            <!-- Botón Editar -->
                            <button type="button" class="btn btn-sm btn-outline-info" data-bs-toggle="modal" data-bs-target="#modalEditCupon${cupon.idCupon}"><i class="fas fa-edit"></i></button>

                            <!-- Botón Eliminar (Abre el Modal) -->
                            <button type="button" class="btn btn-sm btn-outline-danger" data-bs-toggle="modal" data-bs-target="#modalDeleteCupon${cupon.idCupon}"><i class="fas fa-trash"></i></button>

                            <!-- Modal Editar Cupón -->
                            <div class="modal fade text-start" id="modalEditCupon${cupon.idCupon}" tabindex="-1" aria-hidden="true">
                                <div class="modal-dialog modal-dialog-centered">
                                    <div class="modal-content modal-custom border-0 shadow-lg">
                                        <form action="${pageContext.request.contextPath}/admin/cupones" method="POST">
                                            <div class="modal-header border-bottom-0">
                                                <h5 class="modal-title">Editar Cupón</h5>
                                                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                                            </div>
                                            <div class="modal-body">
                                                <input type="hidden" name="action" value="editarCupon">
                                                <input type="hidden" name="idCuponEdit" value="${cupon.idCupon}">

                                                <div class="mb-3">
                                                    <label class="form-label">Código</label>
                                                    <input type="text" name="codigoEdit" class="form-control form-control-custom" value="${cupon.codigo}" style="text-transform: uppercase;" required>
                                                </div>
                                                <div class="mb-3">
                                                    <label class="form-label">Límite de usos (Vacío = Ilimitado)</label>
                                                    <input type="number" name="limiteUsosEdit" class="form-control form-control-custom" value="${cupon.limiteUsos}" min="1">
                                                </div>
                                                <div class="mb-3">
                                                    <label class="form-label">Estado</label>
                                                    <select name="estadoEdit" class="form-select form-control-custom">
                                                        <option value="activo" ${cupon.estado == 'activo' ? 'selected' : ''}>Activo</option>
                                                        <option value="inactivo" ${cupon.estado == 'inactivo' ? 'selected' : ''}>Inactivo</option>
                                                        <option value="vencido" ${cupon.estado == 'vencido' ? 'selected' : ''}>Vencido</option>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="modal-footer border-top-0">
                                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                                                <button type="submit" class="btn btn-warning">Guardar Cambios</button>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>

                            <!-- Modal Eliminar Cupón -->
                            <div class="modal fade text-start" id="modalDeleteCupon${cupon.idCupon}" tabindex="-1" aria-hidden="true">
                                <div class="modal-dialog modal-dialog-centered">
                                    <div class="modal-content modal-custom border-0 shadow-lg">
                                        <form action="${pageContext.request.contextPath}/admin/cupones" method="POST">
                                            <div class="modal-header border-bottom-0">
                                                <h5 class="modal-title text-danger"><i class="fas fa-exclamation-triangle me-2"></i> Confirmar Acción</h5>
                                                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                                            </div>
                                            <div class="modal-body">
                                                <input type="hidden" name="action" value="eliminarCupon">
                                                <input type="hidden" name="idCuponDelete" value="${cupon.idCupon}">
                                                <p class="mb-2">¿Estás seguro de que deseas inactivar el código de cupón <strong><c:out value="${cupon.codigo}"/></strong>?</p>
                                                <small class="text-muted">Los clientes ya no podrán usar este código para obtener el descuento.</small>
                                            </div>
                                            <div class="modal-footer border-top-0">
                                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                                                <button type="submit" class="btn btn-danger">Sí, inactivar</button>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty cupones}">
                    <tr><td colspan="6" class="text-center py-4 text-muted">No hay cupones generados.</td></tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </section>

</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/javascript/cupones-admin.js"></script>
</body>
</html>