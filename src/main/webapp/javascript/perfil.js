
document.addEventListener("DOMContentLoaded", () => {
    const body = document.body;
    const themeButton = document.getElementById("themeToggleBtn");
    const themeIcon = document.getElementById("themeIcon");
    const copyToast = document.getElementById("copyToast");

    // -----------------------------
    // CAMBIO DE TEMA
    // -----------------------------

    function applyTheme(theme) {
        const validTheme = theme === "light" ? "light" : "dark";

        body.setAttribute("data-theme", validTheme);

        if (themeIcon) {
            themeIcon.className = validTheme === "dark"
                ? "fa-solid fa-sun"
                : "fa-solid fa-moon";
        }
    }

    let savedTheme = "dark";

    try {
        savedTheme = localStorage.getItem("datebayo-theme") || "dark";
    } catch (error) {
        console.warn("No se pudo recuperar el tema guardado.");
    }

    applyTheme(savedTheme);

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

    // -----------------------------
    // COPIAR CREDENCIALES
    // -----------------------------

    function showToast(message) {
        if (!copyToast) {
            return;
        }

        copyToast.textContent = message;
        copyToast.classList.add("show");

        if (showToast.timeoutId) {
            clearTimeout(showToast.timeoutId);
        }

        showToast.timeoutId = setTimeout(() => {
            copyToast.classList.remove("show");
        }, 1800);
    }

    async function copyText(text) {
        if (navigator.clipboard && window.isSecureContext) {
            await navigator.clipboard.writeText(text);
            return;
        }

        // Alternativa para navegadores sin Clipboard API.
        const temporaryInput = document.createElement("textarea");

        temporaryInput.value = text;
        temporaryInput.setAttribute("readonly", "");
        temporaryInput.style.position = "fixed";
        temporaryInput.style.left = "-9999px";

        document.body.appendChild(temporaryInput);
        temporaryInput.select();

        const copied = document.execCommand("copy");
        temporaryInput.remove();

        if (!copied) {
            throw new Error("No se pudo copiar el texto.");
        }
    }

    document.querySelectorAll(".data-copy").forEach((button) => {
        button.addEventListener("click", async () => {
            const text = button.getAttribute("data-copy");

            if (!text) {
                showToast("No hay información para copiar.");
                return;
            }

            try {
                await copyText(text);
                showToast("¡Copiado al portapapeles!");
            } catch (error) {
                showToast("No se pudo copiar. Inténtalo nuevamente.");
            }
        });
    });
});
