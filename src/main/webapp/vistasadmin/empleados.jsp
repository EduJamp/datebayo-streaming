<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Empleados | Datebayo Admin</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet">

    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/styles/empleados-admin.css">
</head>

<body data-theme="dark">

<button class="mobile-menu-btn" id="mobileMenuBtn"
        type="button" aria-label="Abrir menú">
    <i class="fas fa-bars"></i>
</button>

<!-- MENÚ LATERAL -->
<nav class="sidebar" id="sidebar">

    <a class="sidebar-brand"
       href="${pageContext.request.contextPath}/admin/dashboard">
        DATEBAYO ADMIN
    </a>

    <ul class="nav nav-pills flex-column mb-auto">

        <li class="nav-item">
            <a class="nav-link"
               href="${pageContext.request.contextPath}/admin/dashboard">
                <i class="fas fa-chart-line"></i> Dashboard
            </a>
        </li>

        <li class="nav-item">
            <a class="nav-link"
               href="${pageContext.request.contextPath}/admin/pedidos">
                <i class="fas fa-shopping-basket"></i> Pedidos
            </a>
        </li>

        <li class="nav-item">
            <a class="nav-link"
               href="${pageContext.request.contextPath}/admin/catalogo">
                <i class="fas fa-box-open"></i> Catálogo
            </a>
        </li>

        <li class="nav-item">
            <a class="nav-link"
               href="${pageContext.request.contextPath}/admin/inventario">
                <i class="fas fa-warehouse"></i> Inventario
            </a>
        </li>

        <li class="nav-item">
            <a class="nav-link"
               href="${pageContext.request.contextPath}/admin/cupones">
                <i class="fas fa-ticket-alt"></i> Cupones
            </a>
        </li>

        <li class="nav-item">
            <a class="nav-link active"
               href="${pageContext.request.contextPath}/admin/empleados">
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

<!-- CONTENIDO -->
<main class="main-content">

    <header class="page-header">
        <div>
            <h1>Gestión de <span>Empleados</span></h1>
            <p class="subtitle">
                Administra las cuentas y los datos de acceso de tus empleados.
            </p>
        </div>

        <button id="showCreate" class="btn-primary-custom" type="button">
            <i class="fa-solid fa-plus"></i> Nuevo empleado
        </button>
    </header>

    <!-- MENSAJES DEL SERVIDOR -->
    <c:if test="${not empty sessionScope.mensajeExito}">
        <div class="notice success">
            <i class="fa-solid fa-circle-check"></i>
            <c:out value="${sessionScope.mensajeExito}"/>
        </div>
        <c:remove var="mensajeExito" scope="session"/>
    </c:if>

    <c:if test="${not empty sessionScope.errorMsg}">
        <div class="notice error">
            <i class="fa-solid fa-circle-exclamation"></i>
            <c:out value="${sessionScope.errorMsg}"/>
        </div>
        <c:remove var="errorMsg" scope="session"/>
    </c:if>

    <!-- TABLA -->
    <section class="admin-card employee-panel">

        <div class="table-heading">
            <div>
                <h2>Empleados registrados</h2>
                <p>Consulta, busca y administra los empleados.</p>
            </div>

            <div class="search-container">
                <i class="fa-solid fa-magnifying-glass"></i>
                <input id="searchEmployees"
                       type="search"
                       placeholder="Buscar empleado..."
                       aria-label="Buscar empleados">
            </div>
        </div>

        <div class="table-responsive">
            <table class="employees-table" id="employeesTable">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Empleado</th>
                    <th>Correo</th>
                    <th>Teléfono</th>
                    <th>Código</th>
                    <th>Ingreso</th>
                    <th>Estado laboral</th>
                    <th>Usuario</th>
                    <th>Acciones</th>
                </tr>
                </thead>

                <tbody>
                <c:forEach var="e" items="${empleados}">
                    <tr>
                        <td><c:out value="${e.idEmpleado}"/></td>

                        <td>
                            <div class="employee-name">
                                <strong>
                                    <c:out value="${e.nombres}"/>
                                    <c:out value="${e.apellidos}"/>
                                </strong>
                                <small>
                                    Usuario #<c:out value="${e.idUsuario}"/>
                                </small>
                            </div>
                        </td>

                        <td><c:out value="${e.correo}"/></td>
                        <td><c:out value="${e.telefono}"/></td>
                        <td><c:out value="${e.codigo}"/></td>
                        <td><c:out value="${e.fechaIngreso}"/></td>

                        <td>
                            <span class="status-badge">
                                <c:out value="${e.estadoLaboral}"/>
                            </span>
                        </td>

                        <td>
                            <span class="status-badge user-status">
                                <c:out value="${e.estadoUsuario}"/>
                            </span>
                        </td>

                        <td>
                            <div class="action-buttons">

                                <button type="button"
                                        class="action-btn edit-btn"
                                        title="Editar empleado"
                                        aria-label="Editar empleado"

                                        data-id="<c:out value='${e.idEmpleado}'/>"
                                        data-nombres="<c:out value='${e.nombres}'/>"
                                        data-apellidos="<c:out value='${e.apellidos}'/>"
                                        data-correo="<c:out value='${e.correo}'/>"
                                        data-telefono="<c:out value='${e.telefono}'/>"
                                        data-codigo="<c:out value='${e.codigo}'/>"
                                        data-estado-laboral="<c:out value='${e.estadoLaboral}'/>"
                                        data-estado-usuario="<c:out value='${e.estadoUsuario}'/>">
                                    <i class="fa-solid fa-pen"></i>
                                </button>

                                <button type="button"
                                        class="action-btn delete-btn"
                                        title="Desactivar empleado"
                                        aria-label="Desactivar empleado"
                                        data-id="<c:out value='${e.idEmpleado}'/>"
                                        data-nombre="<c:out value='${e.nombres} ${e.apellidos}'/>">
                                    <i class="fa-solid fa-user-slash"></i>
                                </button>

                            </div>
                        </td>
                    </tr>
                </c:forEach>

                <c:if test="${empty empleados}">
                    <tr class="empty-row">
                        <td colspan="9">
                            <i class="fa-solid fa-users"></i>
                            <p>No hay empleados registrados todavía.</p>
                        </td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>

        <div id="noResults" class="no-results" hidden>
            <i class="fa-solid fa-magnifying-glass"></i>
            <p>No se encontraron empleados con esa búsqueda.</p>
        </div>

    </section>
</main>


<!-- MODAL CREAR -->
<div class="modal-overlay" id="createModal" aria-hidden="true">
    <section class="employee-modal"
             role="dialog"
             aria-modal="true"
             aria-labelledby="createTitle">

        <div class="modal-heading">
            <div class="modal-title-group">
                <span class="modal-icon">
                    <i class="fa-solid fa-user-plus"></i>
                </span>
                <div>
                    <h2 id="createTitle">Nuevo empleado</h2>
                    <p>Completa los datos del empleado.</p>
                </div>
            </div>

            <button type="button" class="modal-close" data-close-modal
                    aria-label="Cerrar">
                <i class="fa-solid fa-xmark"></i>
            </button>
        </div>

        <form action="${pageContext.request.contextPath}/admin/empleados"
              method="post">

            <input type="hidden" name="action" value="crearEmpleado">

            <div class="form-grid">
                <label>
                    Nombres
                    <input name="nombres" maxlength="100"
                           autocomplete="given-name" required>
                </label>

                <label>
                    Apellidos
                    <input name="apellidos" maxlength="100"
                           autocomplete="family-name" required>
                </label>

                <label>
                    Correo electrónico
                    <input name="correo" type="email" maxlength="180"
                           autocomplete="email" required>
                </label>

                <label>
                    Teléfono
                    <input name="telefono" type="tel" autocomplete="tel">
                </label>

                <label>
                    Código de empleado
                    <input name="codigo" maxlength="40" required>
                </label>

                <label>
                    Contraseña inicial
                    <div class="password-container">
                        <input name="clave" type="password" minlength="8"
                               autocomplete="new-password" required>
                        <button type="button" class="toggle-pass"
                                aria-label="Mostrar contraseña">
                            <i class="fa-solid fa-eye"></i>
                        </button>
                    </div>
                </label>
            </div>

            <p class="form-hint">
                <i class="fa-solid fa-circle-info"></i>
                El servidor debe crear la cuenta y asignarle el rol EMPLEADO.
            </p>

            <div class="modal-actions">
                <button type="button" class="btn-secondary-custom"
                        data-close-modal>Cancelar</button>
                <button type="submit" class="btn-primary-custom">
                    <i class="fa-solid fa-user-plus"></i> Crear empleado
                </button>
            </div>
        </form>
    </section>
</div>


<!-- MODAL EDITAR -->
<div class="modal-overlay" id="editModal" aria-hidden="true">
    <section class="employee-modal"
             role="dialog"
             aria-modal="true"
             aria-labelledby="editTitle">

        <div class="modal-heading">
            <div class="modal-title-group">
                <span class="modal-icon">
                    <i class="fa-solid fa-user-pen"></i>
                </span>
                <div>
                    <h2 id="editTitle">Editar empleado</h2>
                    <p>Actualiza la información del empleado seleccionado.</p>
                </div>
            </div>

            <button type="button" class="modal-close" data-close-modal
                    aria-label="Cerrar">
                <i class="fa-solid fa-xmark"></i>
            </button>
        </div>

        <form action="${pageContext.request.contextPath}/admin/empleados"
              method="post">

            <input type="hidden" name="action" value="editarEmpleado">
            <input type="hidden" name="idEmpleado" id="editId">

            <div class="form-grid">
                <label>
                    Nombres
                    <input name="nombres" id="editNombres"
                           maxlength="100" required>
                </label>

                <label>
                    Apellidos
                    <input name="apellidos" id="editApellidos"
                           maxlength="100" required>
                </label>

                <label>
                    Correo electrónico
                    <input name="correo" id="editCorreo"
                           type="email" maxlength="180" required>
                </label>

                <label>
                    Teléfono
                    <input name="telefono" id="editTelefono" type="tel">
                </label>

                <label>
                    Código de empleado
                    <input name="codigo" id="editCodigo"
                           maxlength="40" required>
                </label>

                <label>
                    Estado laboral
                    <select name="estadoLaboral" id="editEstadoLaboral">
                        <option value="activo">Activo</option>
                        <option value="suspendido">Suspendido</option>
                        <option value="retirado">Retirado</option>
                    </select>
                </label>

                <label>
                    Estado de usuario
                    <select name="estadoUsuario" id="editEstadoUsuario">
                        <option value="activo">Activo</option>
                        <option value="inactivo">Inactivo</option>
                    </select>
                </label>
            </div>

            <div class="modal-actions">
                <button type="button" class="btn-secondary-custom"
                        data-close-modal>Cancelar</button>
                <button type="submit" class="btn-primary-custom">
                    <i class="fa-solid fa-floppy-disk"></i> Guardar cambios
                </button>
            </div>
        </form>
    </section>
</div>


<!-- MODAL CONFIRMAR DESACTIVACIÓN -->
<div class="modal-overlay" id="deleteModal" aria-hidden="true">
    <section class="employee-modal confirm-modal"
             role="dialog"
             aria-modal="true"
             aria-labelledby="deleteTitle">

        <button type="button" class="modal-close" data-close-modal
                aria-label="Cerrar">
            <i class="fa-solid fa-xmark"></i>
        </button>

        <div class="warning-icon">
            <i class="fa-solid fa-triangle-exclamation"></i>
        </div>

        <h2 id="deleteTitle">¿Desactivar empleado?</h2>

        <p>
            Vas a desactivar a
            <strong id="deleteEmployeeName">este empleado</strong>.
            ¿Deseas continuar?
        </p>

        <form action="${pageContext.request.contextPath}/admin/empleados"
              method="post" id="deleteForm">

            <input type="hidden" name="action" value="eliminarEmpleado">
            <input type="hidden" name="idEmpleado" id="deleteId">

            <div class="modal-actions centered">
                <button type="button" class="btn-secondary-custom"
                        data-close-modal>Cancelar</button>
                <button type="submit" class="btn-danger-custom">
                    <i class="fa-solid fa-user-slash"></i> Sí, desactivar
                </button>
            </div>
        </form>
    </section>
</div>

<script src="${pageContext.request.contextPath}/javascript/empleados-admin.js"
        defer></script>
</body>
</html>