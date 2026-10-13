
"use strict";

document.addEventListener("DOMContentLoaded", () => {
    const themeButton = document.getElementById("themeToggleBtn");
    const themeIcon = document.getElementById("themeIcon");
    const body = document.body;
    const paymentForm = document.getElementById("paymentForm");
    const imageInput = document.getElementById("comprobanteImg");

    // Tema oscuro y claro
    function applyTheme(theme) {
        const validTheme = theme === "light" ? "light" : "dark";

        body.setAttribute("data-theme", validTheme);

        if (themeIcon) {
            themeIcon.className = validTheme === "dark"
                ? "fa-solid fa-sun"
                : "fa-solid fa-moon";
        }
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

    // Validar el comprobante de pago
    if (imageInput) {
        imageInput.addEventListener("change", () => {
            const file = imageInput.files[0];

            if (!file) {
                return;
            }

            const allowedTypes = [
                "image/jpeg",
                "image/png",
                "image/webp"
            ];

            const maxSize = 5 * 1024 * 1024; // 5 MB

            if (!allowedTypes.includes(file.type)) {
                alert("Selecciona una imagen JPG, PNG o WEBP.");
                imageInput.value = "";
                return;
            }

            if (file.size > maxSize) {
                alert("La imagen no debe superar los 5 MB.");
                imageInput.value = "";
            }
        });
    }

    // Confirmación visual antes de enviar el pago
    if (paymentForm) {
        paymentForm.addEventListener("submit", (event) => {
            if (!paymentForm.checkValidity()) {
                event.preventDefault();
                paymentForm.reportValidity();
                return;
            }

            const confirmar = window.confirm(
                "¿Deseas confirmar el envío de tu pago?"
            );

            if (!confirmar) {
                event.preventDefault();
            }
        });
    }
});

document.addEventListener("DOMContentLoaded", () => {
    const btnAbrirModal = document.getElementById("btnAbrirModalPago");
    const paymentForm = document.getElementById("paymentForm");
    const btnAceptarModal = document.getElementById("btnAceptarPagoModal");

    if (btnAbrirModal && paymentForm) {
        const modalElement = document.getElementById("modalConfirmarPago");
        const modalConfirmacion = new bootstrap.Modal(modalElement);

        btnAbrirModal.addEventListener("click", (e) => {
            e.preventDefault();

            // Valida que el formulario cumpla con los campos obligatorios (método de pago y archivo)
            if (paymentForm.checkValidity()) {
                modalConfirmacion.show();
            } else {
                paymentForm.reportValidity(); // Muestra las alertas nativas de campos requeridos si faltan llenar
            }
        });

        if (btnAceptarModal) {
            btnAceptarModal.addEventListener("click", () => {
                paymentForm.submit(); // Envía el formulario cuando el usuario acepta en el modal
            });
        }
    }
});
