
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Datebayo - Acceso</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/auth.css">
</head>
<body data-theme="dark">
<button class="theme-toggle-btn" id="themeToggleBtn" type="button"
        title="Cambiar tema" aria-label="Cambiar tema">
    <i class="fa-solid fa-sun" id="themeIcon" aria-hidden="true"></i>
</button>

<header class="mobile-header">
    <h2 class="logo-text">DATEBAYO</h2>
    <div class="badge-premium"><span class="dot"></span>SISTEMA DE STREAMING PREMIUM</div>
</header>

<main class="page-shell">
    <section class="container-auth <c:if test='${showRegister}'>right-panel-active</c:if>"
             id="containerAuth" aria-label="Acceso a Datebayo">
        <div class="form-container sign-up-container">
            <form action="${pageContext.request.contextPath}/auth" method="post" accept-charset="UTF-8">
                <input type="hidden" name="action" value="register">
                <h1>Crear cuenta</h1>

                <c:if test="${not empty errorRegistro}">
                    <div class="alert alert-danger form-alert" role="alert">
                        <c:out value="${errorRegistro}"/>
                    </div>
                </c:if>

                <span>Ingresa tus datos para empezar tu aventura</span>
                <input type="text" name="nombres" placeholder="Nombres" autocomplete="given-name" required>
                <input type="text" name="apellidos" placeholder="Apellidos" autocomplete="family-name" required>
                <input type="email" name="correoRegistro" placeholder="Correo electrónico" autocomplete="email" required>

                <div class="password-field">
                    <input id="claveRegistro" type="password" name="claveRegistro"
                           placeholder="Contraseña" autocomplete="new-password"
                           minlength="8" required>
                    <button class="password-toggle" type="button"
                            data-toggle-password="claveRegistro"
                            aria-label="Mostrar contraseña" aria-pressed="false">
                        <i class="fa-regular fa-eye" aria-hidden="true"></i>
                    </button>
                </div>

                <input type="tel" name="telefono" placeholder="WhatsApp (opcional)" autocomplete="tel">
                <button class="btn-custom" type="submit">Registrarse</button>

                <p class="mobile-switch">
                    ¿Ya tienes una cuenta?
                    <a href="#" id="mobileSignInBtn">Inicia sesión</a>
                </p>
            </form>
        </div>

        <div class="form-container sign-in-container">
            <form action="${pageContext.request.contextPath}/auth" method="post" accept-charset="UTF-8">
                <input type="hidden" name="action" value="login">
                <h1>Iniciar sesión</h1>

                <c:if test="${not empty errorLogin}">
                    <div class="alert alert-danger form-alert" role="alert">
                        <c:out value="${errorLogin}"/>
                    </div>
                </c:if>

                <c:if test="${not empty exitoRegistro}">
                    <div class="alert alert-success form-alert" role="status">
                        <c:out value="${exitoRegistro}"/>
                    </div>
                </c:if>

                <div class="social-container">
                    <button type="button" class="social-button" data-social="Google" aria-label="Acceder con Google">
                        <i class="fa-brands fa-google"></i>
                    </button>
                    <button type="button" class="social-button" data-social="Discord" aria-label="Acceder con Discord">
                        <i class="fa-brands fa-discord"></i>
                    </button>
                </div>

                <span>O usa tu cuenta registrada</span>
                <input type="email" name="correo" placeholder="Correo electrónico" autocomplete="email" required>

                <div class="password-field">
                    <input id="claveLogin" type="password" name="clave"
                           placeholder="Contraseña" autocomplete="current-password" required>
                    <button class="password-toggle" type="button"
                            data-toggle-password="claveLogin"
                            aria-label="Mostrar contraseña" aria-pressed="false">
                        <i class="fa-regular fa-eye" aria-hidden="true"></i>
                    </button>
                </div>

                <button type="button" class="forgot-link" id="forgotPasswordBtn">
                    ¿Olvidaste tu contraseña?
                </button>
                <button class="btn-custom" type="submit">Ingresar</button>

                <p class="mobile-switch">
                    ¿No tienes una cuenta?
                    <a href="#" id="mobileSignUpBtn">Regístrate</a>
                </p>
            </form>
        </div>

        <div class="overlay-container" aria-hidden="true">
            <div class="overlay">
                <div class="overlay-panel overlay-left">
                    <h2 class="logo-text overlay-logo">DATEBAYO</h2>
                    <h1>¡Qué bueno verte!</h1>
                    <p>Inicia sesión para ver tus compras y recargas.</p>
                    <button class="btn-custom btn-ghost" id="signInBtn" type="button">
                        Iniciar sesión
                    </button>
                </div>

                <div class="overlay-panel overlay-right">
                    <h2 class="logo-text overlay-logo">DATEBAYO</h2>
                    <div class="badge-premium"><span class="dot"></span>SISTEMA DE STREAMING PREMIUM</div>
                    <p>Regístrate y descubre nuestras ofertas de streaming y recargas gaming.</p>
                    <button class="btn-custom btn-ghost" id="signUpBtn" type="button">
                        Crear cuenta
                    </button>
                </div>
            </div>
        </div>
    </section>
</main>

<script src="${pageContext.request.contextPath}/javascript/auth.js" defer></script>
</body>
</html>
