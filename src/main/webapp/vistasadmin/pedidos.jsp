<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Admin - Pedidos | Datebayo</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/pedidos.css">
</head>
<body data-theme="dark">
<button class="mobile-menu-btn" id="mobileMenuBtn" type="button" aria-label="Abrir menú"><i class="fas fa-bars"></i></button>
<nav class="sidebar" id="sidebar">
  <a href="${pageContext.request.contextPath}/admin/dashboard" class="sidebar-brand">DATEBAYO ADMIN</a>
  <ul class="nav nav-pills flex-column mb-auto">
    <li><a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link"><i class="fas fa-chart-line"></i> Dashboard</a></li>
    <li><a href="${pageContext.request.contextPath}/admin/pedidos" class="nav-link active"><i class="fas fa-shopping-basket"></i> Pedidos</a></li>
    <li><a href="${pageContext.request.contextPath}/admin/catalogo" class="nav-link"><i class="fas fa-box-open"></i> Catálogo</a></li>
    <li><a href="${pageContext.request.contextPath}/admin/inventario" class="nav-link"><i class="fas fa-warehouse"></i> Inventario</a></li>
    <li><a href="${pageContext.request.contextPath}/admin/cupones" class="nav-link"><i class="fas fa-ticket-alt"></i> Cupones</a></li>
    <li><a href="${pageContext.request.contextPath}/admin/empleados" class="nav-link"><i class="fas fa-user-tie"></i> Empleados</a></li>
    <li><a href="${pageContext.request.contextPath}/admin/clientes" class="nav-link"><i class="fas fa-users"></i> Clientes</a></li>
    <li class="nav-item">
      <a class="nav-link" href="${pageContext.request.contextPath}/admin/empleados">
        <i class="fas fa-users"></i> Empleados
      </a>
    </li>
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
  <c:if test="${not empty sessionScope.errorMsg}">
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
      <c:out value="${sessionScope.errorMsg}"/>
      <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Cerrar"></button>
    </div>
    <c:remove var="errorMsg" scope="session"/>
  </c:if>

  <div class="d-flex justify-content-between align-items-center mb-4">
    <h2>Gestión de <span class="accent-text">Pedidos</span></h2>
  </div>

  <c:if test="${not empty sessionScope.mensajeExito}">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
      <c:out value="${sessionScope.mensajeExito}"/>
      <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Cerrar"></button>
    </div>
    <c:remove var="mensajeExito" scope="session"/>
  </c:if>

  <!-- TABLA 1: PEDIDOS PENDIENTES DE VALIDACIÓN -->
  <section class="admin-card mb-5">
    <h4 class="mb-3"><i class="fas fa-clock text-warning me-2"></i> Pagos Pendientes de Validación</h4>
    <div class="table-responsive">
      <table class="table table-custom table-hover align-middle">
        <thead>
        <tr>
          <th>ID Venta</th>
          <th>Cliente</th>
          <th>Total</th>
          <th>Método</th>
          <th>Comprobante</th>
          <th>Estado pago</th>
          <th class="text-end">Acciones</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="pedido" items="${pedidos}">
          <c:if test="${pedido.payStatus == 'en_revision'}">
            <tr>
              <td><c:out value="${pedido.saleNumber}"/></td>
              <td><strong><c:out value="${pedido.clientName}"/></strong><br><small class="text-muted"><c:out value="${pedido.clienteEmail}"/></small></td>
              <td class="total-cell">S/ <c:out value="${pedido.total}"/></td>
              <td><span class="badge bg-secondary text-uppercase"><c:out value="${pedido.paymentMethod}"/></span></td>
              <td>
                <button type="button" class="btn btn-sm btn-outline-info" data-bs-toggle="modal" data-bs-target="#modalVoucher" onclick="verComprobante('${pageContext.request.contextPath}/uploads/${pedido.voucherFileName}')">
                  <i class="fas fa-image me-1"></i> Ver foto
                </button>
              </td>
              <td><span class="badge bg-warning text-dark"><c:out value="${pedido.payStatus}"/></span></td>
              <td class="text-end">
                <form action="${pageContext.request.contextPath}/admin/pedidos" method="POST" class="d-inline-flex gap-2">
                  <input type="hidden" name="action" value="validarPago">
                  <input type="hidden" name="idVenta" value="${pedido.idSale}">
                  <input type="hidden" name="idPago" value="${pedido.idSale}">

                  <button type="submit" name="resultado" value="aprobado" class="btn btn-sm btn-success" title="Aprobar pago">
                    <i class="fas fa-check"></i> Aprobar
                  </button>

                  <button type="submit" name="resultado" value="rechazado" class="btn btn-sm btn-danger" title="Rechazar pago">
                    <i class="fas fa-times"></i> Rechazar
                  </button>
                </form>
              </td>
            </tr>
          </c:if>
        </c:forEach>
        <c:if test="${empty pedidos}">
          <tr><td colspan="7" class="text-center py-4 text-muted">No hay pedidos pendientes de revisión.</td></tr>
        </c:if>
        </tbody>
      </table>
    </div>
  </section>

  <!-- TABLA 2: HISTORIAL DE PEDIDOS PROCESADOS -->
  <section class="admin-card">
    <h4 class="mb-3"><i class="fas fa-history text-info me-2"></i> Historial de Pedidos Procesados</h4>
    <div class="table-responsive">
      <table class="table table-custom table-hover align-middle">
        <thead>
        <tr>
          <th>ID Venta</th>
          <th>Cliente</th>
          <th>Total</th>
          <th>Método</th>
          <th>Estado Venta</th>
          <th>Estado Pago</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="pedido" items="${pedidos}">
          <c:if test="${pedido.payStatus != 'en_revision'}">
            <tr>
              <td><c:out value="${pedido.saleNumber}"/></td>
              <td><strong><c:out value="${pedido.clientName}"/></strong><br><small class="text-muted"><c:out value="${pedido.clienteEmail}"/></small></td>
              <td class="total-cell">S/ <c:out value="${pedido.total}"/></td>
              <td><span class="badge bg-secondary text-uppercase"><c:out value="${pedido.paymentMethod}"/></span></td>
              <td><span class="badge bg-success text-uppercase"><c:out value="${pedido.salesStatus}"/></span></td>
              <td><span class="badge ${pedido.payStatus == 'aprobado' ? 'bg-success' : 'bg-danger'}"><c:out value="${pedido.payStatus}"/></span></td>
            </tr>
          </c:if>
        </c:forEach>
        </tbody>
      </table>
    </div>
  </section>
</main>

<!-- Modal para ver la foto del comprobante -->
<div class="modal fade" id="modalVoucher" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content modal-custom border-0 shadow-lg" style="background: #101216; color: #fff;">
      <div class="modal-header border-bottom-0">
        <h5 class="modal-title">Comprobante de Pago</h5>
        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Cerrar"></button>
      </div>
      <div class="modal-body text-center">
        <img id="imgComprobante" src="" alt="Comprobante" class="img-fluid rounded" style="max-height: 400px;">
      </div>
    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
  function verComprobante(url) {
    document.getElementById('imgComprobante').src = url;
  }
  (() => {
    const themeButton = document.getElementById("themeToggleBtn");
    const themeIcon = document.getElementById("themeIcon");
    const body = document.body;
    const mobileMenuButton = document.getElementById("mobileMenuBtn");
    const sidebar = document.getElementById("sidebar");

    function applyTheme(theme) {
      body.setAttribute("data-theme", theme);
      if (themeIcon) {
        themeIcon.className = theme === "dark" ? "fa-solid fa-sun" : "fa-solid fa-moon";
      }
      // Ajuste para el botón cerrar del modal en modo claro
      body.style.setProperty('--invert-icon', theme === 'dark' ? '1' : '0');
    }

    // Cargar el tema guardado al iniciar
    let savedTheme = localStorage.getItem("datebayo-theme") || "dark";
    applyTheme(savedTheme);

    // Un solo evento de clic para el botón
    if (themeButton) {
      themeButton.addEventListener("click", () => {
        const currentTheme = body.getAttribute("data-theme");
        const nextTheme = currentTheme === "dark" ? "light" : "dark";
        applyTheme(nextTheme);
        localStorage.setItem("datebayo-theme", nextTheme);
      });
    }

    // Lógica del menú móvil
    if (mobileMenuButton && sidebar) {
      mobileMenuButton.addEventListener("click", () => sidebar.classList.toggle("active"));
    }
  })();
</script>
</body>
</html>