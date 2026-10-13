<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <title>Datebayo - Carrito de compras</title>

  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
        rel="stylesheet">

  <link rel="stylesheet"
        href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">

  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/styles/carrito.css">
</head>

<body data-theme="dark">

<c:set var="carrito" value="${sessionScope.carrito}"/>

<!-- NAVBAR -->
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
            aria-label="Abrir menú">
      <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarNav">
      <ul class="navbar-nav ms-auto align-items-center">
        <li class="nav-item">
          <a class="nav-link"
             href="${pageContext.request.contextPath}/tienda">
            <i class="fas fa-store"></i>
            Seguir comprando
          </a>
        </li>

        <li class="nav-item">
          <a class="nav-link"
             href="${pageContext.request.contextPath}/perfil">
            <i class="fas fa-user-circle"></i>
            Mi perfil
          </a>
        </li>

        <li class="nav-item ms-lg-3">
          <button type="button"
                  class="theme-toggle-btn"
                  id="themeToggleBtn"
                  aria-label="Cambiar tema">
            <i class="fa-solid fa-sun" id="themeIcon"></i>
          </button>
        </li>
      </ul>
    </div>
  </div>
</nav>

<main class="container mb-5">

  <h2 class="page-title">
    Tu <span>carrito</span>
  </h2>

  <!-- Mensajes -->
  <c:if test="${not empty errorPago}">
    <div class="alert alert-danger" role="alert">
      <c:out value="${errorPago}"/>
    </div>
  </c:if>

  <c:if test="${not empty mensajeExito}">
    <div class="alert alert-success" role="alert">
      <c:out value="${mensajeExito}"/>
    </div>
  </c:if>

  <div class="row g-4">

    <!-- TABLA DE PRODUCTOS -->
    <div class="col-lg-8">
      <section class="cart-container">

        <div class="table-responsive">
          <table class="table table-custom">
            <thead>
            <tr>
              <th>Producto</th>
              <th>Precio</th>
              <th>Cantidad</th>
              <th>Subtotal</th>
              <th>Acción</th>
            </tr>
            </thead>

            <tbody>
            <c:choose>
              <c:when test="${not empty carrito.items}">

                <c:forEach var="item"
                           items="${carrito.items}">

                  <tr>
                    <td class="product-name">
                      <c:out value="${item.name}"/>
                    </td>

                    <td>
                      S/
                      <c:out value="${item.unitPrice}"/>
                    </td>

                    <td>
                      <c:out value="${item.quantity}"/>
                    </td>

                    <td class="product-subtotal">
                      S/
                      <c:out value="${item.subtotal}"/>
                    </td>

                    <td>
                      <form action="${pageContext.request.contextPath}/carrito"
                            method="post"
                            class="remove-form">

                        <input type="hidden"
                               name="action"
                               value="eliminar">

                        <input type="hidden"
                               name="idProducto"
                               value="${item.idProduct}">

                        <button type="submit"
                                class="btn btn-sm btn-outline-danger"
                                aria-label="Eliminar producto">
                          <i class="fas fa-trash"></i>
                        </button>
                      </form>
                    </td>
                  </tr>

                </c:forEach>

              </c:when>

              <c:otherwise>
                <tr>
                  <td colspan="5" class="empty-cart">
                    <i class="fas fa-shopping-cart"></i>

                    <p>Tu carrito está vacío.</p>

                    <a href="${pageContext.request.contextPath}/tienda">
                      Explorar catálogo
                    </a>
                  </td>
                </tr>
              </c:otherwise>
            </c:choose>
            </tbody>
          </table>
        </div>

      </section>
    </div>

    <!-- RESUMEN Y PAGO -->
    <div class="col-lg-4">
      <section class="summary-box">

        <h4 class="summary-title">Resumen de compra</h4>

        <div class="summary-line">
          <span>Subtotal</span>
          <strong>
            S/
            <c:out value="${not empty carrito ? carrito.total : 0}"/>
          </strong>
        </div>

        <div class="summary-line total-line">
          <span>Total a pagar</span>
          <strong>
            S/
            <c:out value="${not empty carrito ? carrito.total : 0}"/>
          </strong>
        </div>

        <c:choose>
          <c:when test="${not empty carrito.items}">

            <!-- FORMULARIO PARA APLICAR CUPÓN              -->
            <form action="${pageContext.request.contextPath}/carrito" method="post" class="mb-4 pb-4 border-bottom border-secondary">
              <input type="hidden" name="action" value="aplicarCupon">

              <label for="codigoCupon" class="form-label text-muted small">
                <i class="fas fa-ticket-alt"></i> ¿Tienes un código de descuento?
              </label>

              <div class="input-group">
                <input type="text" id="codigoCupon" name="codigoCupon" class="form-control form-control-custom" placeholder="Ingresa tu cupón" required>
                <button class="btn btn-outline-warning" type="submit">Aplicar</button>
              </div>

              <!-- Mensajes de validación del cupón -->
              <c:if test="${not empty sessionScope.mensajeCupon}">
                <small class="text-success mt-2 d-block">
                  <i class="fas fa-check-circle"></i> <c:out value="${sessionScope.mensajeCupon}"/>
                </small>
                <c:remove var="mensajeCupon" scope="session" />
              </c:if>

              <c:if test="${not empty sessionScope.errorCupon}">
                <small class="text-danger mt-2 d-block">
                  <i class="fas fa-exclamation-circle"></i> <c:out value="${sessionScope.errorCupon}"/>
                </small>
                <c:remove var="errorCupon" scope="session" />
              </c:if>
            </form>

            <!-- FORMULARIO DE PAGO -->
            <form action="${pageContext.request.contextPath}/carrito"
                  method="post"
                  enctype="multipart/form-data"
                  id="paymentForm">

              <input type="hidden"
                     name="action"
                     value="pagar">

              <div class="mb-3">
                <label for="metodoPago" class="form-label">
                  Método de pago
                </label>

                <select id="metodoPago"
                        name="metodoPago"
                        class="form-select form-control-custom"
                        required>
                  <option value="" disabled selected>
                    Selecciona una opción
                  </option>
                  <option value="yape">Yape</option>
                  <option value="plin">Plin</option>
                  <option value="efectivo">
                    Efectivo
                  </option>
                </select>
              </div>

              <div class="mb-3">
                <label for="comprobanteImg" class="form-label">
                  Comprobante de pago
                </label>

                <input type="file"
                       id="comprobanteImg"
                       name="comprobanteImg"
                       class="form-control form-control-custom"
                       accept="image/jpeg,image/png,image/webp"
                       required>

                <small class="form-help">
                  Sube una imagen JPG, PNG o WEBP.
                </small>
              </div>

              <div class="mb-4">
                <label for="datoExtra" class="form-label">
                  ID del jugador o perfil (si aplica)
                </label>

                <input type="text"
                       id="datoExtra"
                       name="datoExtra"
                       class="form-control form-control-custom"
                       maxlength="100"
                       placeholder="Ej.: 123456789">
              </div>

              <button type="button" class="btn-custom" id="btnAbrirModalPago">
                Confirmar pago
                <i class="fas fa-check-circle ms-2"></i>
              </button>

            </form>

          </c:when>

          <c:otherwise>
            <a href="${pageContext.request.contextPath}/tienda"
               class="btn-custom continue-button">
              Ir a comprar
            </a>
          </c:otherwise>
        </c:choose>

      </section>
    </div>

  </div>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<script src="${pageContext.request.contextPath}/javascript/carrito.js"></script>

<div class="modal fade" id="modalConfirmarPago" tabindex="-1" aria-labelledby="modalPagoLabel" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content border-0 shadow-lg" style="background-color: #11151a; color: #fffaf5; border-radius: 12px; border: 1px solid #252a32 !important;">
      <div class="modal-header border-bottom-0 pb-0">
        <h5 class="modal-title fw-bold" id="modalPagoLabel">
          <i class="fas fa-question-circle text-warning me-2"></i> Confirmar Compra
        </h5>
        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Cerrar"></button>
      </div>
      <div class="modal-body text-center py-4">
        <p class="mb-1 fs-6">¿Estás seguro de que deseas confirmar este pago?</p>
        <small style="color: #a3a6b0;">Asegúrate de haber subido el comprobante correcto.</small>
      </div>
      <div class="modal-footer border-top-0 pt-0 pb-4 justify-content-center gap-2">
        <button type="button" class="btn btn-secondary px-4 py-2 rounded-pill" data-bs-dismiss="modal" style="background-color: #252a32; border: none; color: #fffaf5;">
          Cancelar
        </button>
        <button type="button" class="btn px-4 py-2 rounded-pill fw-bold" id="btnAceptarPagoModal" style="background-color: #ff7900; color: #111111; border: none;">
          Sí, confirmar
        </button>
      </div>
    </div>
  </div>
</div>

</body>
</html>