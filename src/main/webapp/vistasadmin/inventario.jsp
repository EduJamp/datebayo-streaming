<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Admin - Inventario | Datebayo</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/inventario.css">
</head>
<body data-theme="dark">
<button class="mobile-menu-btn" id="mobileMenuBtn" type="button" aria-label="Abrir menú"><i class="fas fa-bars"></i></button>

<nav class="sidebar" id="sidebar">
  <a href="${pageContext.request.contextPath}/admin/dashboard" class="sidebar-brand">DATEBAYO ADMIN</a>
  <ul class="nav nav-pills flex-column mb-auto">
    <li><a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link"><i class="fas fa-chart-line"></i> Dashboard</a></li>
    <li><a href="${pageContext.request.contextPath}/admin/pedidos" class="nav-link"><i class="fas fa-shopping-basket"></i> Pedidos</a></li>
    <li><a href="${pageContext.request.contextPath}/admin/catalogo" class="nav-link"><i class="fas fa-box-open"></i> Catálogo</a></li>
    <li><a href="${pageContext.request.contextPath}/admin/inventario" class="nav-link active"><i class="fas fa-warehouse"></i> Inventario</a></li>
    <li><a href="${pageContext.request.contextPath}/admin/cupones" class="nav-link"><i class="fas fa-ticket-alt"></i> Cupones</a></li>
    <li><a href="${pageContext.request.contextPath}/admin/empleados" class="nav-link"><i class="fas fa-users"></i> Empleados</a></li>
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
  <div class="d-flex justify-content-between align-items-center mb-4">
    <h2>Gestión de <span class="accent-text">Inventario</span></h2>
  </div>

  <c:if test="${not empty sessionScope.mensajeExito}">
    <div class="alert alert-success"><c:out value="${sessionScope.mensajeExito}"/></div>
    <c:remove var="mensajeExito" scope="session"/>
  </c:if>
  <c:if test="${not empty sessionScope.errorMsg}">
    <div class="alert alert-danger"><c:out value="${sessionScope.errorMsg}"/></div>
    <c:remove var="errorMsg" scope="session"/>
  </c:if>

  <div class="row g-4">
    <!-- COLUMNA IZQUIERDA: FORMULARIO AGREGAR -->
    <div class="col-lg-4">
      <section class="admin-card">
        <h5 class="mb-4">Registrar cuenta madre</h5>
        <form action="${pageContext.request.contextPath}/admin/inventario" method="POST" id="formRegistrarCuenta">
          <input type="hidden" name="action" value="registrarCuenta">

          <div class="mb-3">
            <label for="idProducto" class="form-label">Producto</label>
            <select id="idProducto" name="idProducto" class="form-select form-control-custom" required>
              <option value="" disabled selected>Selecciona un producto</option>
              <c:forEach var="producto" items="${productosInventario}">
                <option value="${producto.id}"><c:out value="${producto.nombre}"/></option>
              </c:forEach>
            </select>
          </div>

          <div class="mb-3">
            <label for="correoCuenta" class="form-label">Correo (Cuenta madre)</label>
            <input id="correoCuenta" type="email" name="correoCuenta" class="form-control form-control-custom" required>
          </div>

          <div class="mb-3">
            <label for="claveCuenta" class="form-label">Contraseña principal</label>
            <input id="claveCuenta" type="password" name="claveCuenta" class="form-control form-control-custom" required autocomplete="new-password">
            <button type="button" class="btn btn-sm text-info mt-1 p-0 border-0 bg-transparent" data-toggle-password="claveCuenta" style="font-size: 0.8rem;">Mostrar contraseña</button>
          </div>

          <hr class="border-secondary my-4">
          <div class="d-flex justify-content-between align-items-center mb-3">
            <h6 class="m-0">Perfiles a generar</h6>
            <span class="badge bg-secondary" id="contadorPerfiles">1 / 5</span>
          </div>

          <div id="perfilesContainer">
            <!-- Perfil 1 Fijo -->
            <div class="row g-2 mb-3">
              <div class="col-7">
                <input id="nombrePerfil1" type="text" name="nombrePerfil1" class="form-control form-control-custom" placeholder="Nombre perfil 1" value="Perfil 1" required>
              </div>
              <div class="col-5">
                <input id="pinPerfil1" type="text" name="pinPerfil1" class="form-control form-control-custom" placeholder="PIN (Opcional)">
              </div>
            </div>
          </div>

          <button type="button" id="btnAgregarPerfil" class="btn btn-outline-info btn-sm w-100 mb-3 border-dashed">+ Agregar otro perfil</button>
          <button type="submit" class="btn btn-primary-custom w-100"><i class="fas fa-save me-2"></i> Generar stock</button>
        </form>
      </section>
    </div>

    <!-- COLUMNA DERECHA: TABLA LISTAR -->
    <div class="col-lg-8">
      <section class="admin-card h-100">
        <h5 class="mb-4">Cuentas en Inventario</h5>
        <div class="table-responsive">
          <table class="table table-custom table-hover">
            <thead>
            <tr>
              <th>ID</th>
              <th>Producto</th>
              <th>Correo Madre</th>
              <th>Perfiles</th>
              <th>Estado</th>
              <th class="text-end">Acciones</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="cuenta" items="${cuentasInventario}">
              <tr>
                <td>#${cuenta.idCuenta}</td>
                <td>${cuenta.nombreProducto}</td>
                <td>${cuenta.correoCuenta}</td>
                <td>${cuenta.capacidadPerfiles}</td>
                <td><span class="badge ${cuenta.estado == 'disponible' ? 'bg-success' : 'bg-warning'}">${cuenta.estado}</span></td>
                <td class="text-end">
                  <button class="btn btn-sm btn-outline-info me-1" onclick="abrirModalEditar('${cuenta.idCuenta}', '${cuenta.correoCuenta}', '${cuenta.estado}')" title="Editar"><i class="fas fa-pen"></i></button>
                  <button class="btn btn-sm btn-outline-danger" onclick="abrirModalEliminar('${cuenta.idCuenta}')" title="Eliminar"><i class="fas fa-trash"></i></button>
                </td>
              </tr>
            </c:forEach>

            <!-- Mensaje cuando no hay datos -->
            <c:if test="${empty cuentasInventario}">
              <tr>
                <td colspan="6" class="text-center text-muted py-4">
                  <i class="fas fa-box-open fs-4 mb-2 d-block"></i>
                  No hay cuentas registradas en el inventario.
                </td>
              </tr>
            </c:if>
            </tbody>
          </table>
        </div>
      </section>
    </div>
  </div>
</main>

<!-- Modal Editar -->
<div class="modal fade" id="modalEditarCuenta" tabindex="-1">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content modal-custom border-0 shadow-lg">
      <div class="modal-header border-bottom-0">
        <h5 class="modal-title">Editar Cuenta</h5>
        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
      </div>
      <form action="${pageContext.request.contextPath}/admin/inventario" method="POST">
        <div class="modal-body">
          <input type="hidden" name="action" value="editarCuenta">
          <input type="hidden" name="idCuentaEdit" id="idCuentaEdit">

          <div class="mb-3">
            <label class="form-label">Correo de la cuenta</label>
            <input type="email" name="correoEdit" id="correoEdit" class="form-control form-control-custom" required>
          </div>
          <div class="mb-3">
            <label class="form-label">Nueva Contraseña (Dejar en blanco para no cambiar)</label>
            <input type="password" name="claveEdit" class="form-control form-control-custom">
          </div>
          <div class="mb-3">
            <label class="form-label">Estado</label>
            <select name="estadoEdit" id="estadoEdit" class="form-select form-control-custom">
              <option value="disponible">Disponible</option>
              <option value="ocupada">Ocupada</option>
              <option value="suspendida">Suspendida</option>
              <option value="agotada">Agotada</option>
            </select>
          </div>
        </div>
        <div class="modal-footer border-top-0 pt-0">
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
          <button type="submit" class="btn btn-primary-custom">Guardar cambios</button>
        </div>
      </form>
    </div>
  </div>
</div>

<!-- Modal Eliminar -->
<div class="modal fade" id="modalEliminarCuenta" tabindex="-1">
  <div class="modal-dialog modal-dialog-centered modal-sm">
    <div class="modal-content modal-custom border-0 shadow-lg text-center">
      <div class="modal-body p-4">
        <i class="fas fa-exclamation-triangle text-danger mb-3" style="font-size: 3rem;"></i>
        <h5 class="mb-3">¿Eliminar cuenta?</h5>
        <p class="text-muted small">Esta acción no se puede deshacer. Los perfiles asociados también serán eliminados si no han sido entregados.</p>
        <form action="${pageContext.request.contextPath}/admin/inventario" method="POST" class="d-flex gap-2 mt-4">
          <input type="hidden" name="action" value="eliminarCuenta">
          <input type="hidden" name="idCuentaDelete" id="idCuentaDelete">
          <button type="button" class="btn btn-secondary w-50" data-bs-dismiss="modal">Cancelar</button>
          <button type="submit" class="btn btn-danger w-50">Sí, eliminar</button>
        </form>
      </div>
    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/javascript/inventario.js"></script>
</body>
</html>