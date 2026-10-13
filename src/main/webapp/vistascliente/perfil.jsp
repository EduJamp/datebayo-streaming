
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <title>Datebayo - Mi Perfil</title>

  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
        rel="stylesheet">

  <link rel="stylesheet"
        href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">

  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/styles/perfil.css">
</head>

<body data-theme="dark">

<nav class="navbar navbar-expand-lg navbar-custom">
  <div class="container">
    <a class="navbar-brand"
       href="${pageContext.request.contextPath}/tienda">
      DATEBAYO
    </a>

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
      <ul class="navbar-nav ms-auto align-items-center gap-lg-2">
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

<main class="container profile-container">

  <c:if test="${not empty mensajeExito}">
    <div class="alert alert-success">
      <i class="fas fa-check-circle"></i>
      <c:out value="${mensajeExito}"/>
    </div>
  </c:if>

  <c:if test="${not empty errorPago}">
    <div class="alert alert-danger">
      <i class="fas fa-exclamation-circle"></i>
      <c:out value="${errorPago}"/>
    </div>
  </c:if>

  <!-- ENCABEZADO DEL PERFIL -->
  <section class="profile-header">
    <div class="profile-avatar">
      <i class="fas fa-user"></i>
    </div>

    <div class="profile-info">
      <h1>
        Hola,
        <span class="highlight">
            <c:out value="${sessionScope.usuarioLogueado.nameUser}"
                   default="Cliente"/>
        </span>
      </h1>

      <p>
        <i class="fas fa-envelope"></i>
        <c:out value="${sessionScope.usuarioLogueado.emailUser}"/>
      </p>

      <span class="profile-badge">
                <i class="fas fa-shield-alt"></i> Mi cuenta
            </span>
    </div>
  </section>

  <div class="row g-4">

    <!-- CREDENCIALES -->
    <div class="col-lg-6">
      <section class="section-card h-100">

        <h2 class="section-title">
          <i class="fas fa-key"></i>
          Mis credenciales activas
        </h2>

        <p class="section-description">
          Aquí aparecerán las credenciales de las cuentas
          que te hayan sido asignadas.
        </p>

        <c:choose>
          <c:when test="${not empty misCredenciales}">

            <c:forEach var="cred" items="${misCredenciales}">
              <article class="credential-box">

                <h3 class="credential-title">
                  <c:out value="${cred.nombreProducto}"/>

                  <c:if test="${not empty cred.numeroVenta}">
                                        <span class="order-reference">
                                            Pedido:
                                            <c:out value="${cred.numeroVenta}"/>
                                        </span>
                  </c:if>
                </h3>

                <div class="credential-data">
                  <p>
                    <strong>Correo/usuario:</strong>

                    <c:choose>
                      <c:when test="${not empty cred.correoCuenta}">
                        <button type="button"
                                class="data-copy"
                                data-copy="${cred.correoCuenta}"
                                title="Copiar usuario">
                          <c:out value="${cred.correoCuenta}"/>
                          <i class="far fa-copy"></i>
                        </button>
                      </c:when>
                      <c:otherwise>
                                                <span class="text-muted">
                                                    No disponible
                                                </span>
                      </c:otherwise>
                    </c:choose>
                  </p>

                  <p>
                    <strong>Contraseña de cuenta:</strong>
                    <c:choose>
                      <c:when test="${not empty cred.contrasenaCuenta}">
                        <button type="button"
                                class="data-copy"
                                data-copy="${cred.contrasenaCuenta}"
                                title="Copiar contraseña de cuenta">
                          <c:out value="${cred.contrasenaCuenta}"/>
                          <i class="far fa-copy"></i>
                        </button>
                      </c:when>
                      <c:otherwise>
                        <span class="text-muted">No disponible</span>
                      </c:otherwise>
                    </c:choose>
                  </p>

                  <p>
                    <strong>Contraseña/PIN:</strong>

                    <c:choose>
                      <c:when test="${not empty cred.pinOContrasenaPerfil}">
                        <button type="button"
                                class="data-copy"
                                data-copy="${cred.pinOContrasenaPerfil}"
                                title="Copiar contraseña o PIN">
                          <c:out value="${cred.pinOContrasenaPerfil}"/>
                          <i class="far fa-copy"></i>
                        </button>
                      </c:when>
                      <c:otherwise>
                                                <span class="text-muted">
                                                    No disponible
                                                </span>
                      </c:otherwise>
                    </c:choose>
                  </p>

                  <p class="mb-0">
                    <strong>Perfil asignado:</strong>
                    <c:choose>
                      <c:when test="${not empty cred.nombrePerfil}">
                        <c:out value="${cred.nombrePerfil}"/>
                      </c:when>
                      <c:otherwise>
                        No especificado
                      </c:otherwise>
                    </c:choose>
                  </p>
                </div>
              </article>
            </c:forEach>

          </c:when>

          <c:otherwise>
            <div class="empty-state">
              <i class="fas fa-key"></i>
              <h3>Sin credenciales</h3>
              <p>
                Todavía no tienes credenciales asignadas.
                Cuando se activen, aparecerán en esta sección.
              </p>
            </div>
          </c:otherwise>
        </c:choose>

      </section>
    </div>

    <!-- HISTORIAL DE COMPRAS -->
    <div class="col-lg-6">
      <section class="section-card h-100">

        <h2 class="section-title">
          <i class="fas fa-history"></i>
          Historial de compras
        </h2>

        <p class="section-description">
          Consulta tus pedidos y el estado de cada compra.
        </p>

        <c:choose>
          <c:when test="${not empty misCompras}">

            <div class="table-responsive">
              <table class="table table-custom align-middle">
                <thead>
                <tr>
                  <th>Pedido</th>
                  <th>Fecha</th>
                  <th>Total</th>
                  <th>Estado</th>
                </tr>
                </thead>

                <tbody>
                <c:forEach var="venta" items="${misCompras}">
                  <tr>
                    <td class="order-number">
                      <c:out value="${venta.salesNumber}"/>
                    </td>

                    <td>
                      <c:out value="${venta.creationDate}"/>
                    </td>

                    <td class="purchase-total">
                      S/
                      <c:out value="${venta.total}"/>
                    </td>

                    <td>
                      <c:choose>
                        <c:when test="${venta.saleStatus != null and (venta.saleStatus.toString().toLowerCase() == 'entregada' or venta.saleStatus.toString().toLowerCase() == 'pago_en_revision' or venta.saleStatus.toString().toLowerCase() == 'en_proceso')}">
                          <span class="badge-status bg-entregada">
                              <c:out value="${venta.saleStatus}"/>
                          </span>
                        </c:when>

                        <c:when test="${venta.saleStatus != null and (venta.saleStatus.toString().toLowerCase() == 'pendiente' or venta.saleStatus.toString().toLowerCase() == 'rechazado')}">
                          <span class="badge-status bg-pendiente">
                              <c:out value="${venta.saleStatus}"/>
                          </span>
                        </c:when>

                        <c:otherwise>
                          <span class="badge-status bg-otro">
                              <c:out value="${venta.saleStatus}"/>
                          </span>
                        </c:otherwise>
                      </c:choose>
                    </td>
                  </tr>
                </c:forEach>
                </tbody>
              </table>
            </div>

          </c:when>

          <c:otherwise>
            <div class="empty-state">
              <i class="fas fa-shopping-bag"></i>
              <h3>Sin compras</h3>
              <p>
                Todavía no tienes compras registradas.
              </p>

              <a class="btn btn-custom"
                 href="${pageContext.request.contextPath}/tienda">
                Explorar catálogo
              </a>
            </div>
          </c:otherwise>
        </c:choose>

      </section>
    </div>

  </div>
</main>

<div id="copyToast" class="copy-toast" role="status"
     aria-live="polite"></div>

<script src="${pageContext.request.contextPath}/javascript/perfil.js"></script>

</body>
</html>
