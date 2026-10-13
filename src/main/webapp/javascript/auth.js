document.addEventListener("DOMContentLoaded", () => {
    const container = document.getElementById("containerAuth");
    const themeButton = document.getElementById("themeToggleBtn");
    const themeIcon = document.getElementById("themeIcon");
    const body = document.body;

    const showLogin = () => container?.classList.remove("right-panel-active");
    const showRegister = () => container?.classList.add("right-panel-active");

    document.getElementById("signUpBtn")?.addEventListener("click", showRegister);
    document.getElementById("signInBtn")?.addEventListener("click", showLogin);
    document.getElementById("mobileSignUpBtn")?.addEventListener("click", (event) => {
        event.preventDefault();
        showRegister();
        document.getElementById("containerAuth")?.scrollIntoView({ behavior: "smooth", block: "start" });
    });
    document.getElementById("mobileSignInBtn")?.addEventListener("click", (event) => {
        event.preventDefault();
        showLogin();
        document.getElementById("containerAuth")?.scrollIntoView({ behavior: "smooth", block: "start" });
    });

    const applyTheme = (theme) => {
        const selected = theme === "light" ? "light" : "dark";
        body.setAttribute("data-theme", selected);
        if (themeIcon) {
            themeIcon.classList.toggle("fa-sun", selected === "dark");
            themeIcon.classList.toggle("fa-moon", selected === "light");
        }
        themeButton?.setAttribute("aria-label", selected === "dark" ? "Cambiar a tema claro" : "Cambiar a tema oscuro");
    };

    let savedTheme = "dark";
    try { savedTheme = localStorage.getItem("datebayo-theme") || "dark"; } catch (_) {}
    applyTheme(savedTheme);

    themeButton?.addEventListener("click", () => {
        const nextTheme = body.getAttribute("data-theme") === "dark" ? "light" : "dark";
        applyTheme(nextTheme);
        try { localStorage.setItem("datebayo-theme", nextTheme); } catch (_) {}
    });

    // Mostrar u ocultar las contraseñas de inicio de sesión y registro.
    document.querySelectorAll("[data-toggle-password]").forEach((button) => {
        button.addEventListener("click", () => {
            const input = document.getElementById(button.dataset.togglePassword);
            if (!input) return;
            const visible = input.type === "password";
            input.type = visible ? "text" : "password";
            button.setAttribute("aria-label", visible ? "Ocultar contraseña" : "Mostrar contraseña");
            button.setAttribute("aria-pressed", String(visible));
            const icon = button.querySelector("i");
            if (icon) icon.className = visible ? "fa-regular fa-eye-slash" : "fa-regular fa-eye";
        });
    });

    document.querySelectorAll("[data-social]").forEach((button) => {
        button.addEventListener("click", () => {
            const provider = button.getAttribute("data-social");
            alert("El acceso con " + provider + " requiere configurar OAuth y las credenciales de la aplicación.");
        });
    });

    document.getElementById("forgotPasswordBtn")?.addEventListener("click", () => {
        const emailInput = document.querySelector('input[name="correo"]');
        const email = emailInput?.value.trim();
        if (!email) {
            alert("Escribe tu correo electrónico en el campo de inicio de sesión y vuelve a intentarlo.");
            emailInput?.focus();
            return;
        }
        alert("La solicitud para " + email + " está pendiente de implementar en el servidor. Para recuperar la contraseña se necesita un flujo seguro de restablecimiento.");
    });
});
