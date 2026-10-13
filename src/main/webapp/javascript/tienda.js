document.addEventListener("DOMContentLoaded", function () {

    const themeButton = document.getElementById("themeToggleBtn");
    const themeIcon = document.getElementById("themeIcon");
    const body = document.body;
    const currentYear = document.getElementById("currentYear");

    // Mostrar el año actual.
    if (currentYear) {
        currentYear.textContent = new Date().getFullYear();
    }

    // Aplicar el tema seleccionado.
    function applyTheme(theme) {
        const selectedTheme = theme === "light" ? "light" : "dark";

        body.setAttribute("data-theme", selectedTheme);

        if (themeIcon) {
            if (selectedTheme === "dark") {
                themeIcon.className = "fa-solid fa-sun";
            } else {
                themeIcon.className = "fa-solid fa-moon";
            }
        }
    }

    // Recuperar el tema guardado.
    let savedTheme = "dark";

    try {
        savedTheme = localStorage.getItem("datebayo-theme") || "dark";
    } catch (error) {
        savedTheme = "dark";
    }

    applyTheme(savedTheme);

    // Cambiar el tema al pulsar el botón.
    if (themeButton) {
        themeButton.addEventListener("click", function () {

            const currentTheme = body.getAttribute("data-theme");

            const nextTheme =
                currentTheme === "dark" ? "light" : "dark";

            applyTheme(nextTheme);

            try {
                localStorage.setItem("datebayo-theme", nextTheme);
            } catch (error) {
                // El tema seguirá funcionando durante esta visita.
            }
        });
    }

});