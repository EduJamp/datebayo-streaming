<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Datebayo | Detalle del producto</title>

  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
        rel="stylesheet">
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"
        rel="stylesheet">
  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/styles/tienda.css">
</head>

<body data-theme="dark">

<nav class="navbar navbar-expand-lg navbar-custom">
  <div class="container">
    <a class="navbar-brand"
       href="${pageContext.request.contextPath}/tienda">DATEBAYO</a>

    <button class="navbar-toggler"
            type="button"
            data-bs-toggle="collapse"
            data-bs-target="#navbarNav"
            aria-controls="navbarNav"
            aria-expanded="false"
            aria-label="Abrir navegación">
      <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarNav">
      <ul class="navbar-nav ms-auto align-items-lg-center gap-lg-2">
        <li class="nav-item">
          <a class="nav-link"
             href="${pageContext.request.contextPath}/tienda">
            <i class="fas fa-store"></i> Tienda
          </a>
        </li>
        <li class="nav-item">
          <a class="nav-link"
             href="${pageContext.request.contextPath}/carrito">
            <i class="fas fa-shopping-cart"></i> Carrito
          </a>
        </li>
        <li class="nav-item">
          <a class="nav-link"
             href="${pageContext.request.contextPath}/perfil">
            <i class="fas fa-user-circle"></i> Mi perfil
          </a>
        </li>
        <li class="nav-item">
          <button type="button"
                  class="theme-toggle-btn"
                  id="themeToggleBtn"
                  aria-label="Cambiar tema"
                  title="Cambiar tema">
            <i class="fa-solid fa-sun" id="themeIcon"></i>
          </button>
        </li>
        <li class="nav-item">
          <a class="btn btn-outline-danger btn-sm rounded-pill px-3"
             href="${pageContext.request.contextPath}/auth?action=logout">
            <i class="fas fa-sign-out-alt"></i> Salir
          </a>
        </li>
      </ul>
    </div>
  </div>
</nav>

<main class="container product-detail-container">

  <a class="back-link"
     href="${pageContext.request.contextPath}/tienda">
    <i class="fas fa-arrow-left"></i> Volver al catálogo
  </a>

  <section class="product-detail-card">
    <div class="product-detail-image">
      <c:choose>
        <c:when test="${not empty producto.imageUrl}">
          <img src="<c:out value='${producto.imageUrl}'/>"
               alt="<c:out value='${producto.name}'/>">
        </c:when>
        <c:otherwise>
          <div class="product-placeholder">
            <i class="fas fa-cube"></i>
            <span>DATEBAYO</span>
          </div>
        </c:otherwise>
      </c:choose>
    </div>

    <div class="product-detail-info">
            <span class="product-category">
                <c:out value="${producto.category}"/>
            </span>

      <h1><c:out value="${producto.name}"/></h1>

      <p class="detail-description">
        <c:out value="${producto.description}" default="Consulta la información y disponibilidad de este producto antes de realizar tu compra."/>
      </p>

      <div class="detail-price">
        S/ <c:out value="${producto.price}"/>
      </div>

      <c:choose>
        <c:when test="${producto.stockAvailable > 0}">
          <p class="stock-available">
            <i class="fas fa-check-circle"></i>
            Producto disponible
          </p>

          <form action="${pageContext.request.contextPath}/carrito"
                method="post">
            <input type="hidden" name="action" value="agregar">
            <input type="hidden" name="idProducto"
                   value="<c:out value='${producto.idProduct}'/>">
            <input type="hidden" name="nombreProducto"
                   value="<c:out value='${producto.name}'/>">
            <input type="hidden" name="precio"
                   value="<c:out value='${producto.price}'/>">

            <button type="submit" class="btn-custom">
              <i class="fas fa-cart-plus me-2"></i>
              Agregar al carrito
            </button>
          </form>
        </c:when>

        <c:otherwise>
          <p class="stock-unavailable">
            <i class="fas fa-times-circle"></i>
            Producto sin stock
          </p>

          <button type="button" class="btn-custom" disabled>
            No disponible para comprar
          </button>
        </c:otherwise>
      </c:choose>
    </div>
  </section>
</main>

<footer class="footer-custom">
  <div class="container">
    <p>&copy; <span id="currentYear"></span> Datebayo.
      Todos los derechos reservados.</p>
  </div>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/javascript/tienda.js"></script>

</body>
</html>
