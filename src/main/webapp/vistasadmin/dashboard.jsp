<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Dashboard | Datebayo Admin</title>

    <!-- Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet">

    <!-- Font Awesome -->
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">

    <!-- CSS del dashboard -->
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/styles/admin-dashboard.css">
</head>

<body data-theme="dark">

<!-- Botón del menú para dispositivos móviles -->
<button class="mobile-menu-btn"
        id="mobileMenuBtn"
        type="button"
        aria-label="Abrir menú">
    <i class="fas fa-bars"></i>
</button>

<!-- Menú lateral -->
<nav class="sidebar" id="sidebar">

    <a class="sidebar-brand"
       href="${pageContext.request.contextPath}/admin/dashboard">
        DATEBAYO ADMIN
    </a>

    <ul class="nav nav-pills flex-column mb-auto">

        <!-- Dashboard -->
        <li class="nav-item">
            <a class="nav-link active"
               href="${pageContext.request.contextPath}/admin/dashboard">
                <i class="fas fa-chart-line"></i>
                Dashboard
            </a>
        </li>

        <!-- Pedidos -->
        <li class="nav-item">
            <a class="nav-link"
               href="${pageContext.request.contextPath}/admin/pedidos">
                <i class="fas fa-shopping-basket"></i>
                Pedidos
            </a>
        </li>

        <!-- Catálogo -->
        <li class="nav-item">
            <a class="nav-link"
               href="${pageContext.request.contextPath}/admin/catalogo">
                <i class="fas fa-box-open"></i>
                Catálogo
            </a>
        </li>

        <!-- Inventario -->
        <li class="nav-item">
            <a class="nav-link"
               href="${pageContext.request.contextPath}/admin/inventario">
                <i class="fas fa-warehouse"></i>
                Inventario
            </a>
        </li>

        <!-- Cupones -->
        <li class="nav-item">
            <a class="nav-link"
               href="${pageContext.request.contextPath}/admin/cupones">
                <i class="fas fa-ticket-alt"></i>
                Cupones
            </a>
        </li>

        <!-- Empleados -->
        <li class="nav-item">
            <a class="nav-link"
               href="${pageContext.request.contextPath}/admin/empleados">
                <i class="fas fa-users"></i>
                Empleados
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

<!-- Contenido principal -->
<main class="main-content">

    <h1>
        Panel de <span>Administración</span>
    </h1>

    <p class="subtitle">
        Gestiona las operaciones de Datebayo Streaming desde aquí.
    </p>

    <!-- Tarjetas del dashboard -->
    <div class="dashboard-grid">

        <!-- Pedidos -->
        <a class="admin-card"
           href="${pageContext.request.contextPath}/admin/pedidos">

            <i class="fas fa-shopping-basket"></i>

            <h2>Pedidos</h2>

            <p>
                Revisa las ventas y gestiona la validación de pagos.
            </p>

            <span>Ir a pedidos →</span>
        </a>

        <!-- Catálogo -->
        <a class="admin-card"
           href="${pageContext.request.contextPath}/admin/catalogo">

            <i class="fas fa-box-open"></i>

            <h2>Catálogo</h2>

            <p>
                Registra nuevos productos para la tienda.
            </p>

            <span>Administrar catálogo →</span>
        </a>

        <!-- Inventario -->
        <a class="admin-card"
           href="${pageContext.request.contextPath}/admin/inventario">

            <i class="fas fa-warehouse"></i>

            <h2>Inventario</h2>

            <p>
                Registra y administra las cuentas digitales
                y los perfiles disponibles.
            </p>

            <span>Administrar inventario →</span>
        </a>

        <!-- Cupones -->
        <a class="admin-card"
           href="${pageContext.request.contextPath}/admin/cupones">

            <i class="fas fa-ticket-alt"></i>

            <h2>Cupones</h2>

            <p>
                Crea y administra campañas de descuentos.
            </p>

            <span>Administrar cupones →</span>
        </a>

        <!-- Empleados -->
        <a class="admin-card"
           href="${pageContext.request.contextPath}/admin/empleados">

            <i class="fas fa-users"></i>

            <h2>Empleados</h2>

            <p>
                Registra, consulta y administra los empleados
                de Datebayo Streaming.
            </p>

            <span>Administrar empleados →</span>
        </a>

        <a class="admin-card"
           href="${pageContext.request.contextPath}/admin/clientes">

            <i class="fas fa-users"></i>

            <h2>Clientes</h2>

            <p>
                Registra, consulta y administra los clientes
                de Datebayo Streaming.
            </p>

            <span>Administrar clientes →</span>
        </a>

    </div>

</main>

<!-- JavaScript del dashboard -->
<script src="${pageContext.request.contextPath}/javascript/admin-dashboard.js"></script>

</body>
</html>
