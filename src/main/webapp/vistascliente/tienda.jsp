<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Datebayo | Catálogo</title>

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
            <div class="badge-premium ms-lg-3 my-2 my-lg-0">
                <span class="dot"></span>
                STREAMING PREMIUM
            </div>

            <ul class="navbar-nav ms-auto align-items-lg-center gap-lg-1">
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
                        <span class="badge bg-danger rounded-pill">
                            <c:choose>
                                <c:when test="${not empty sessionScope.carrito}">
                                    <c:out value="${sessionScope.carrito.items.size()}"/>
                                </c:when>
                                <c:otherwise>0</c:otherwise>
                            </c:choose>
                        </span>
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

                <li class="nav-item ms-lg-2">
                    <a class="btn btn-outline-danger btn-sm rounded-pill px-3"
                       href="${pageContext.request.contextPath}/auth?action=logout">
                        <i class="fas fa-sign-out-alt"></i> Salir
                    </a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<section class="hero-section">
    <div class="container">
        <span class="hero-label">TU TIENDA DIGITAL</span>
        <h1>
            Encuentra tu próximo
            <span class="text-primary-custom">favorito</span>
        </h1>
        <p>
            Explora nuestro catálogo de productos digitales y consulta
            sus detalles antes de realizar tu compra.
        </p>
    </div>
</section>

<main class="container mb-5">
    <div class="catalog-header">
        <div>
            <h2>Explora nuestro catálogo</h2>
            <p>Elige un producto para conocer más información.</p>
        </div>
    </div>

    <c:if test="${not empty mensajeExito}">
        <div class="alert alert-success">
            <c:out value="${mensajeExito}"/>
        </div>
    </c:if>

    <c:if test="${not empty mensajeError}">
        <div class="alert alert-danger">
            <c:out value="${mensajeError}"/>
        </div>
    </c:if>

    <c:choose>
        <c:when test="${not empty productos}">
            <div class="row row-cols-1 row-cols-sm-2 row-cols-lg-3 row-cols-xl-4 g-4">

                <c:forEach var="prod" items="${productos}">
                    <div class="col">
                        <article class="product-card">

                            <div class="product-img-wrapper">
                                <c:choose>
                                    <c:when test="${not empty prod.imageUrl}">
                                        <img src="<c:out value='${prod.imageUrl}'/>"
                                             alt="<c:out value='${prod.name}'/>"
                                             loading="lazy">
                                    </c:when>
                                    <c:otherwise>
                                        <div class="product-placeholder">
                                            <i class="fas fa-cube"></i>
                                            <span>DATEBAYO</span>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <div class="product-body">
                                <span class="product-category">
                                    <c:out value="${prod.category}"/>
                                </span>

                                <h3 class="product-title">
                                    <c:out value="${prod.name}"/>
                                </h3>

                                <div class="product-price">
                                    S/ <c:out value="${prod.price}"/>
                                </div>

                                <c:choose>
                                    <c:when test="${prod.stockAvailable > 0}">
                                        <p class="stock-available">
                                            <i class="fas fa-check-circle"></i>
                                            Disponible
                                        </p>
                                    </c:when>
                                    <c:otherwise>
                                        <p class="stock-unavailable">
                                            <i class="fas fa-times-circle"></i>
                                            Sin stock disponible
                                        </p>
                                    </c:otherwise>
                                </c:choose>

                                <a class="btn-custom text-center text-decoration-none"
                                   href="${pageContext.request.contextPath}/producto?id=${prod.idProduct}">
                                    <i class="fas fa-eye me-2"></i>
                                    Ver detalle
                                </a>
                            </div>
                        </article>
                    </div>
                </c:forEach>

            </div>
        </c:when>

        <c:otherwise>
            <div class="empty-catalog">
                <i class="fas fa-box-open"></i>
                <h3>No hay productos disponibles</h3>
                <p>Vuelve más tarde para descubrir nuestros productos.</p>
            </div>
        </c:otherwise>
    </c:choose>
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
