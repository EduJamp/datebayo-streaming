(() => {
    "use strict";

    document.addEventListener("DOMContentLoaded", () => {

        /* =========================================
           TEMA COMPARTIDO CON EL DASHBOARD
        ========================================= */

        const body = document.body;
        const themeButton = document.getElementById("themeToggleBtn");
        const themeIcon = document.getElementById("themeIcon");
        const themeLabel = document.getElementById("themeLabel");

        const applyTheme = (theme) => {
            const selectedTheme = theme === "light" ? "light" : "dark";

            body.setAttribute("data-theme", selectedTheme);

            if (themeIcon) {
                themeIcon.className =
                    selectedTheme === "dark"
                        ? "fa-solid fa-sun"
                        : "fa-solid fa-moon";
            }

            if (themeLabel) {
                themeLabel.textContent =
                    selectedTheme === "dark"
                        ? "Modo oscuro"
                        : "Modo claro";
            }

            if (themeButton) {
                themeButton.setAttribute(
                    "aria-label",
                    selectedTheme === "dark"
                        ? "Cambiar a modo claro"
                        : "Cambiar a modo oscuro"
                );
            }
        };

        const getSavedTheme = () => {
            try {
                const savedTheme = localStorage.getItem("datebayo-theme");

                return savedTheme === "light" ? "light" : "dark";
            } catch (error) {
                return "dark";
            }
        };

        applyTheme(getSavedTheme());

        if (themeButton) {
            themeButton.addEventListener("click", () => {
                const currentTheme = body.getAttribute("data-theme");
                const nextTheme =
                    currentTheme === "dark" ? "light" : "dark";

                applyTheme(nextTheme);

                try {
                    localStorage.setItem("datebayo-theme", nextTheme);
                } catch (error) {
                    // El tema cambia en la página aunque el almacenamiento
                    // del navegador no esté disponible.
                }
            });
        }

        // Sincroniza el tema si cambia desde otra pestaña.
        window.addEventListener("storage", (event) => {
            if (event.key === "datebayo-theme") {
                applyTheme(event.newValue === "light" ? "light" : "dark");
            }
        });


        /* =========================================
           MENÚ RESPONSIVE
        ========================================= */

        const menuButton = document.getElementById("mobileMenuBtn");
        const sidebar = document.getElementById("sidebar");

        if (menuButton && sidebar) {
            menuButton.addEventListener("click", () => {
                sidebar.classList.toggle("active");

                const isOpen = sidebar.classList.contains("active");

                menuButton.setAttribute(
                    "aria-label",
                    isOpen ? "Cerrar menú" : "Abrir menú"
                );

                menuButton.innerHTML = isOpen
                    ? '<i class="fas fa-times"></i>'
                    : '<i class="fas fa-bars"></i>';
            });

            document.addEventListener("click", (event) => {
                if (
                    window.innerWidth <= 992 &&
                    sidebar.classList.contains("active") &&
                    !sidebar.contains(event.target) &&
                    !menuButton.contains(event.target)
                ) {
                    sidebar.classList.remove("active");
                    menuButton.innerHTML = '<i class="fas fa-bars"></i>';
                    menuButton.setAttribute("aria-label", "Abrir menú");
                }
            });
        }


        /* =========================================
           MODALES
        ========================================= */

        const createModal = document.getElementById("createModal");
        const editModal = document.getElementById("editModal");
        const deleteModal = document.getElementById("deleteModal");

        const modals = [createModal, editModal, deleteModal].filter(Boolean);

        let previousFocus = null;

        const openModal = (modal) => {
            if (!modal) return;

            closeAllModals();

            previousFocus = document.activeElement;

            modal.classList.add("is-open");
            modal.setAttribute("aria-hidden", "false");
            body.classList.add("modal-open");

            const focusable = modal.querySelector(
                "input:not([type='hidden']), select, button"
            );

            if (focusable) {
                window.setTimeout(() => focusable.focus(), 50);
            }
        };

        const closeModal = (modal) => {
            if (!modal) return;

            modal.classList.remove("is-open");
            modal.setAttribute("aria-hidden", "true");

            if (!modals.some((item) => item.classList.contains("is-open"))) {
                body.classList.remove("modal-open");

                if (previousFocus && typeof previousFocus.focus === "function") {
                    previousFocus.focus();
                }
            }
        };

        function closeAllModals() {
            modals.forEach((modal) => {
                modal.classList.remove("is-open");
                modal.setAttribute("aria-hidden", "true");
            });

            body.classList.remove("modal-open");
        }

        document.querySelectorAll("[data-close-modal]").forEach((button) => {
            button.addEventListener("click", () => {
                closeModal(button.closest(".modal-overlay"));
            });
        });

        modals.forEach((modal) => {
            modal.addEventListener("click", (event) => {
                if (event.target === modal) {
                    closeModal(modal);
                }
            });
        });

        document.addEventListener("keydown", (event) => {
            if (event.key === "Escape") {
                const activeModal = modals.find((modal) =>
                    modal.classList.contains("is-open")
                );

                if (activeModal) {
                    closeModal(activeModal);
                }
            }
        });


        /* =========================================
           CREAR EMPLEADO
        ========================================= */

        const showCreate = document.getElementById("showCreate");

        if (showCreate && createModal) {
            showCreate.addEventListener("click", () => {
                const form = createModal.querySelector("form");

                if (form) {
                    form.reset();

                    const password = form.querySelector("[name='clave']");
                    const toggle = form.querySelector(".toggle-pass");

                    if (password) {
                        password.type = "password";
                    }

                    if (toggle) {
                        toggle.innerHTML = '<i class="fa-solid fa-eye"></i>';
                        toggle.setAttribute(
                            "aria-label",
                            "Mostrar contraseña"
                        );
                    }
                }

                openModal(createModal);
            });
        }


        /* =========================================
           MOSTRAR / OCULTAR CONTRASEÑA
        ========================================= */

        document.querySelectorAll(".toggle-pass").forEach((button) => {
            button.addEventListener("click", () => {
                const container = button.closest(".password-container");
                const input = container?.querySelector("input");

                if (!input) return;

                const wasPassword = input.type === "password";

                input.type = wasPassword ? "text" : "password";

                button.innerHTML = wasPassword
                    ? '<i class="fa-solid fa-eye-slash"></i>'
                    : '<i class="fa-solid fa-eye"></i>';

                button.setAttribute(
                    "aria-label",
                    wasPassword
                        ? "Ocultar contraseña"
                        : "Mostrar contraseña"
                );
            });
        });


        /* =========================================
           EDITAR EMPLEADO
        ========================================= */

        const editFields = {
            id: document.getElementById("editId"),
            nombres: document.getElementById("editNombres"),
            apellidos: document.getElementById("editApellidos"),
            correo: document.getElementById("editCorreo"),
            telefono: document.getElementById("editTelefono"),
            codigo: document.getElementById("editCodigo"),
            estadoLaboral: document.getElementById("editEstadoLaboral"),
            estadoUsuario: document.getElementById("editEstadoUsuario")
        };

        document.querySelectorAll(".edit-btn").forEach((button) => {
            button.addEventListener("click", () => {
                if (!editModal) return;

                editFields.id.value = button.dataset.id || "";
                editFields.nombres.value = button.dataset.nombres || "";
                editFields.apellidos.value = button.dataset.apellidos || "";
                editFields.correo.value = button.dataset.correo || "";
                editFields.telefono.value = button.dataset.telefono || "";
                editFields.codigo.value = button.dataset.codigo || "";

                setSelectValue(
                    editFields.estadoLaboral,
                    button.dataset.estadoLaboral
                );

                setSelectValue(
                    editFields.estadoUsuario,
                    button.dataset.estadoUsuario
                );

                openModal(editModal);
            });
        });

        function setSelectValue(select, value) {
            if (!select) return;

            const normalized = (value || "").trim().toLowerCase();

            const option = Array.from(select.options).find(
                (item) => item.value.toLowerCase() === normalized
            );

            if (option) {
                select.value = option.value;
            } else {
                select.selectedIndex = 0;
            }
        }


        /* =========================================
           CONFIRMAR DESACTIVACIÓN
        ========================================= */

        const deleteId = document.getElementById("deleteId");
        const deleteName = document.getElementById("deleteEmployeeName");

        document.querySelectorAll(".delete-btn").forEach((button) => {
            button.addEventListener("click", () => {
                if (!deleteModal || !deleteId) return;

                deleteId.value = button.dataset.id || "";

                if (deleteName) {
                    deleteName.textContent =
                        button.dataset.nombre || "este empleado";
                }

                openModal(deleteModal);
            });
        });


        /* =========================================
           BÚSQUEDA DE EMPLEADOS
        ========================================= */

        const searchInput = document.getElementById("searchEmployees");
        const table = document.getElementById("employeesTable");
        const noResults = document.getElementById("noResults");

        if (searchInput && table) {
            const rows = Array.from(
                table.querySelectorAll("tbody tr")
            ).filter((row) => !row.classList.contains("empty-row"));

            searchInput.addEventListener("input", () => {
                const query = normalize(searchInput.value);
                let visibleRows = 0;

                rows.forEach((row) => {
                    const matches = normalize(row.textContent).includes(query);

                    row.hidden = !matches;

                    if (matches) {
                        visibleRows++;
                    }
                });

                if (noResults) {
                    noResults.hidden =
                        visibleRows > 0 || rows.length === 0;
                }
            });
        }

        function normalize(value) {
            return (value || "")
                .toLocaleLowerCase("es")
                .normalize("NFD")
                .replace(/[\u0300-\u036f]/g, "")
                .trim();
        }
    });
})();