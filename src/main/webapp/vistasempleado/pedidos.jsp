<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Portal empleado - Pedidos | Datebayo</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/empleado.css">
</head>
<body data-theme="dark">

<div class="layout">
    <aside class="sidebar" id="sidebar">
        <a class="brand" href="${pageContext.request.contextPath}/empleado/dashboard">DATEBAYO EMPLEADO</a>
        <nav class="navlinks">
            <a href="${pageContext.request.contextPath}/empleado/dashboard" class="NAVD">
                <i class="fa-solid fa-gauge-high"></i> Dashboard
            </a>
            <a href="${pageContext.request.contextPath}/empleado/pedidos" class="NAVP active">
                <i class="fa-solid fa-bag-shopping"></i> Mis pedidos
            </a>
            <a href="${pageContext.request.contextPath}/empleado/perfil" class="NAVF">
                <i class="fa-solid fa-user"></i> Mi perfil
            </a>
        </nav>
        <a class="logout" href="${pageContext.request.contextPath}/auth?action=logout">
            <i class="fa-solid fa-right-from-bracket"></i> Cerrar sesión
        </a>
    </aside>

    <main class="main">
        <div class="topbar">
            <button class="menu-btn" id="menuBtn" type="button" aria-label="Abrir menú">
                <i class="fa-solid fa-bars"></i>
            </button>
            <h1>Gestión de <span class="accent-text">Pedidos Asignados</span></h1>
            <span class="muted"><c:out value="${sessionScope.usuarioLogueado.nameUser}"/></span>
        </div>

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

        <!-- TABLA 1: PAGOS PENDIENTES DE VALIDACIÓN -->
        <section class="card mb-4">
            <h4 class="mb-3 text-warning"><i class="fas fa-clock me-2"></i> Pagos Pendientes de Validación</h4>
            <p class="muted">Pedidos que requieren tu revisión de pago y asignación de cuentas.</p>

            <div class="table-wrap">
                <table class="table align-middle">
                    <thead>
                    <tr>
                        <th>N.º venta</th>
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
                        <c:if test="${pedido.payStatus() == 'en_revision'}">
                            <tr>
                                <td><c:out value="${pedido.saleNumber()}"/></td>
                                <td>
                                    <strong><c:out value="${pedido.clientName()}"/></strong><br>
                                    <small class="text-muted"><c:out value="${pedido.clienteEmail()}"/></small>
                                </td>
                                <td class="total-cell">S/ <c:out value="${pedido.total()}"/></td>
                                <td><span class="badge bg-secondary text-uppercase"><c:out value="${pedido.paymentMethod()}"/></span></td>
                                <td>
                                    <button type="button" class="btn btn-sm btn-outline-info" data-bs-toggle="modal" data-bs-target="#modalVoucher" onclick="verComprobante('${pageContext.request.contextPath}/uploads/default.jpg')">
                                        <i class="fas fa-image me-1"></i> Ver foto
                                    </button>
                                </td>
                                <td><span class="badge bg-warning text-dark"><c:out value="${pedido.payStatus()}"/></span></td>
                                <td class="text-end">
                                    <form action="${pageContext.request.contextPath}/empleado/pedidos" method="POST" class="d-inline-flex gap-2">
                                        <input type="hidden" name="action" value="validarPago">
                                        <input type="hidden" name="idVenta" value="${pedido.idSale()}">
                                        <input type="hidden" name="idPago" value="${pedido.idSale()}">

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
                        <tr><td colspan="7" class="empty text-center py-4">No hay pagos pendientes de revisión.</td></tr>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </section>

        <!-- TABLA 2: HISTORIAL DE PEDIDOS PROCESADOS -->
        <section class="card">
            <h4 class="mb-3 text-info"><i class="fas fa-history me-2"></i> Historial de Pedidos Procesados</h4>
            <p class="muted">Lista de pedidos que ya fueron aprobados o rechazados anteriormente.</p>

            <div class="table-wrap">
                <table class="table align-middle">
                    <thead>
                    <tr>
                        <th>N.º venta</th>
                        <th>Cliente</th>
                        <th>Total</th>
                        <th>Método</th>
                        <th>Estado venta</th>
                        <th>Estado pago</th>
                        <th>Fecha</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="pedido" items="${pedidos}">
                        <c:if test="${pedido.payStatus() != 'en_revision'}">
                            <tr>
                                <td><c:out value="${pedido.saleNumber()}"/></td>
                                <td>
                                    <strong><c:out value="${pedido.clientName()}"/></strong><br>
                                    <small class="text-muted"><c:out value="${pedido.clienteEmail()}"/></small>
                                </td>
                                <td class="total-cell">S/ <c:out value="${pedido.total()}"/></td>
                                <td><span class="badge bg-secondary text-uppercase"><c:out value="${pedido.paymentMethod()}"/></span></td>
                                <td><span class="badge bg-success text-uppercase"><c:out value="${pedido.salesStatus()}"/></span></td>
                                <td><span class="badge ${pedido.payStatus() == 'aprobado' ? 'bg-success' : 'bg-danger'}"><c:out value="${pedido.payStatus()}"/></span></td>
                                <td><c:out value="${pedido.creationDate()}"/></td>
                            </tr>
                        </c:if>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </section>
    </main>
</div>

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
<script src="${pageContext.request.contextPath}/javascript/empleado.js" defer></script>
<script>
    function verComprobante(url) {
        document.getElementById('imgComprobante').src = url;
    }
</script>
</body>
</html>