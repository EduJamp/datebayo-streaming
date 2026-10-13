<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Admin - Clientes | Datebayo</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
  <!-- Reutilizamos el mismo CSS que usaste para cupones y empleados -->
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
    <li><a href="${pageContext.request.contextPath}/admin/cupones" class="nav-link"><i class="fas fa-ticket-alt"></i> Cupones</a></li>
    <li><a href="${pageContext.request.contextPath}/admin/empleados" class="nav-link"><i class="fas fa-user-tie"></i> Empleados</a></li>
    <li><a href="${pageContext.request.contextPath}/admin/clientes" class="nav-link active"><i class="fas fa-users"></i> Clientes</a></li>
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
    <h2>Gestión de <span class="accent-text">Clientes</span></h2>
  </div>

  <!-- TABLA DE CLIENTES                          -->
  <section class="admin-card">

    <div class="d-flex justify-content-between align-items-center mb-4">
      <div>
        <h4 class="mb-1"><i class="fas fa-users text-primary me-2"></i> Clientes registrados</h4>
        <p class="text-muted small mb-0">Consulta, edita y administra a los clientes de la tienda.</p>
      </div>
    </div>

    <div class="table-responsive">
      <table class="table table-custom table-hover align-middle">
        <thead>
        <tr>
          <th>ID</th>
          <th>CLIENTE</th>
          <th>CORREO</th>
          <th>TELÉFONO</th>
          <th>ESTADO</th>
          <th class="text-end">ACCIONES</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="cliente" items="${clientes}">
          <tr>
            <td><span class="text-muted">#<c:out value="${cliente.idUsuario}" /></span></td>
            <td>
              <strong><c:out value="${cliente.nombres} ${cliente.apellidos}" /></strong><br>
              <small class="text-muted">Ingreso: <c:out value="${cliente.fechaRegistro}" /></small>
            </td>
            <td><c:out value="${cliente.correo}" /></td>
            <td><c:out value="${cliente.telefono != null ? cliente.telefono : 'N/A'}" /></td>
            <td>
                            <span class="badge ${cliente.estado == 'activo' ? 'bg-success' : (cliente.estado == 'bloqueado' ? 'bg-danger' : 'bg-warning')} text-uppercase">
                                <c:out value="${cliente.estado}" />
                            </span>
            </td>
            <td class="text-end">

              <!-- Botón Detalles -->
              <button type="button" class="btn btn-sm btn-outline-secondary" data-bs-toggle="modal" data-bs-target="#modalViewCliente${cliente.idUsuario}" title="Ver detalles">
                <i class="fas fa-eye"></i>
              </button>

              <!-- Botón Editar -->
              <button type="button" class="btn btn-sm btn-outline-info ms-1" data-bs-toggle="modal" data-bs-target="#modalEditCliente${cliente.idUsuario}" title="Editar cliente">
                <i class="fas fa-pen"></i>
              </button>

              <!-- Botón Bloquear -->
              <button type="button" class="btn btn-sm btn-outline-danger ms-1" data-bs-toggle="modal" data-bs-target="#modalDeleteCliente${cliente.idUsuario}" title="Bloquear cliente">
                <i class="fas fa-user-slash"></i>
              </button>

              <!-- MODAL 1: VER DETALLES                      -->
              <div class="modal fade text-start" id="modalViewCliente${cliente.idUsuario}" tabindex="-1" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered">
                  <div class="modal-content modal-custom border-0 shadow-lg">
                    <div class="modal-header border-bottom-0">
                      <h5 class="modal-title"><i class="fas fa-address-card text-primary me-2"></i> Detalles del Cliente</h5>
                      <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                      <div class="d-flex align-items-center mb-4">
                        <div class="rounded-circle bg-secondary d-flex justify-content-center align-items-center" style="width: 60px; height: 60px; font-size: 24px;">
                          <i class="fas fa-user text-white"></i>
                        </div>
                        <div class="ms-3">
                          <h5 class="mb-0"><strong><c:out value="${cliente.nombres} ${cliente.apellidos}" /></strong></h5>
                          <span class="badge ${cliente.estado == 'activo' ? 'bg-success' : 'bg-danger'} mt-1 text-uppercase"><c:out value="${cliente.estado}" /></span>
                        </div>
                      </div>
                      <div class="row g-3">
                        <div class="col-6">
                          <small class="text-muted d-block">ID Usuario</small>
                          <span>#<c:out value="${cliente.idUsuario}" /></span>
                        </div>
                        <div class="col-6">
                          <small class="text-muted d-block">Teléfono</small>
                          <span><c:out value="${cliente.telefono != null ? cliente.telefono : 'N/A'}" /></span>
                        </div>
                        <div class="col-12">
                          <small class="text-muted d-block">Correo Electrónico</small>
                          <span><c:out value="${cliente.correo}" /></span>
                        </div>
                        <div class="col-6">
                          <small class="text-muted d-block">Fecha de Registro</small>
                          <span><c:out value="${cliente.fechaRegistro}" /></span>
                        </div>
                        <div class="col-6">
                          <small class="text-muted d-block">Último Acceso</small>
                          <span><c:out value="${cliente.ultimoAcceso != null ? cliente.ultimoAcceso : 'Nunca'}" /></span>
                        </div>
                      </div>
                    </div>
                    <div class="modal-footer border-top-0">
                      <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
                    </div>
                  </div>
                </div>
              </div>

              <!-- MODAL 2: EDITAR CLIENTE                    -->
              <div class="modal fade text-start" id="modalEditCliente${cliente.idUsuario}" tabindex="-1" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered">
                  <div class="modal-content modal-custom border-0 shadow-lg">
                    <form action="${pageContext.request.contextPath}/admin/clientes" method="POST">
                      <div class="modal-header border-bottom-0">
                        <h5 class="modal-title"><i class="fas fa-user-edit text-info me-2"></i> Editar Cliente</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                      </div>
                      <div class="modal-body">
                        <input type="hidden" name="action" value="editarCliente">
                        <input type="hidden" name="idCliente" value="${cliente.idUsuario}">

                        <div class="row g-3">
                          <div class="col-md-6">
                            <label class="form-label text-muted small">Nombres</label>
                            <input type="text" name="nombres" class="form-control form-control-custom" value="${cliente.nombres}" required>
                          </div>
                          <div class="col-md-6">
                            <label class="form-label text-muted small">Apellidos</label>
                            <input type="text" name="apellidos" class="form-control form-control-custom" value="${cliente.apellidos}" required>
                          </div>
                          <div class="col-md-12">
                            <label class="form-label text-muted small">Correo Electrónico</label>
                            <input type="email" name="correo" class="form-control form-control-custom" value="${cliente.correo}" required>
                          </div>
                          <div class="col-md-6">
                            <label class="form-label text-muted small">Teléfono</label>
                            <input type="text" name="telefono" class="form-control form-control-custom" value="${cliente.telefono}">
                          </div>
                          <div class="col-md-6">
                            <label class="form-label text-muted small">Estado de la cuenta</label>
                            <select name="estado" class="form-select form-control-custom">
                              <option value="activo" ${cliente.estado == 'activo' ? 'selected' : ''}>Activo</option>
                              <option value="bloqueado" ${cliente.estado == 'bloqueado' ? 'selected' : ''}>Bloqueado</option>
                              <option value="pendiente" ${cliente.estado == 'pendiente' ? 'selected' : ''}>Pendiente</option>
                            </select>
                          </div>
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

              <!-- MODAL 3: BLOQUEAR CLIENTE                  -->
              <div class="modal fade text-start" id="modalDeleteCliente${cliente.idUsuario}" tabindex="-1" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered">
                  <div class="modal-content modal-custom border-0 shadow-lg">
                    <form action="${pageContext.request.contextPath}/admin/clientes" method="POST">
                      <div class="modal-header border-bottom-0">
                        <h5 class="modal-title text-danger"><i class="fas fa-user-slash me-2"></i> Bloquear Cliente</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                      </div>
                      <div class="modal-body">
                        <input type="hidden" name="action" value="bloquearCliente">
                        <input type="hidden" name="idClienteDelete" value="${cliente.idUsuario}">

                        <p class="mb-2">¿Estás seguro de que deseas bloquear al cliente <strong><c:out value="${cliente.nombres} ${cliente.apellidos}"/></strong>?</p>
                        <small class="text-muted">Si lo bloqueas, el usuario no podrá acceder a su cuenta ni realizar compras hasta que un administrador lo reactive.</small>
                      </div>
                      <div class="modal-footer border-top-0">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                        <button type="submit" class="btn btn-danger">Sí, bloquear</button>
                      </div>
                    </form>
                  </div>
                </div>
              </div>

            </td>
          </tr>
        </c:forEach>

        <c:if test="${empty clientes}">
          <tr><td colspan="6" class="text-center py-5 text-muted">
            <i class="fas fa-users-slash fs-1 mb-3 d-block"></i>
            No se encontraron clientes registrados en el sistema.
          </td></tr>
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