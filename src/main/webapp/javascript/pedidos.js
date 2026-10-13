(() => {
    const themeButton = document.getElementById("themeToggleBtn");
    const themeIcon = document.getElementById("themeIcon");
    const body = document.body;
    const mobileMenuButton = document.getElementById("mobileMenuBtn");
    const sidebar = document.getElementById("sidebar");

    function applyTheme(theme) {
        body.setAttribute("data-theme", theme);
        if (themeIcon) themeIcon.className = theme === "dark" ? "fa-solid fa-sun" : "fa-solid fa-moon";
    }

    // Recuperar el tema guardado
    let savedTheme = "dark";

    try {
        savedTheme = localStorage.getItem("datebayo-theme") || "dark";
    } catch (error) {
        console.warn("No se pudo recuperar el tema guardado.");
    }

    applyTheme(savedTheme);

    // Cambiar el tema
    if (themeButton) {
        themeButton.addEventListener("click", () => {
            const currentTheme = body.getAttribute("data-theme");
            const nextTheme = currentTheme === "dark" ? "light" : "dark";

            applyTheme(nextTheme);

            try {
                localStorage.setItem("datebayo-theme", nextTheme);
            } catch (error) {
                console.warn("No se pudo guardar el tema.");
            }
        });
    }

    applyTheme(localStorage.getItem("datebayo-theme") || "dark");

    if (themeButton) themeButton.addEventListener("click", () => {
        const nextTheme = body.getAttribute("data-theme") === "dark" ? "light" : "dark";
        applyTheme(nextTheme);
        localStorage.setItem("datebayo-theme", nextTheme);
    });
    if (mobileMenuButton && sidebar) mobileMenuButton.addEventListener("click", () => sidebar.classList.toggle("active"));
})();

function cargarComprobante(idVenta, idPago, rutaComprobante) {
    const ventaInput = document.getElementById("hiddenIdVenta");
    const pagoInput = document.getElementById("hiddenIdPago");
    const image = document.getElementById("imgComprobante");
    const noReceipt = document.getElementById("sinComprobante");
    if (ventaInput) ventaInput.value = idVenta || "";
    if (pagoInput) pagoInput.value = idPago || "";
    if (rutaComprobante && rutaComprobante.trim() !== "") {
        image.src = rutaComprobante;
        image.hidden = false;
        noReceipt.hidden = true;
    } else {
        image.removeAttribute("src");
        image.hidden = true;
        noReceipt.hidden = false;
    }
}
