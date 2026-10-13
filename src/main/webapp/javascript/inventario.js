(() => {
    // ---- 1. Lógica Global (Tema, Menú, Passwords) ----
    const themeButton = document.getElementById("themeToggleBtn");
    const themeIcon = document.getElementById("themeIcon");
    const body = document.body;
    const mobileMenuButton = document.getElementById("mobileMenuBtn");
    const sidebar = document.getElementById("sidebar");

    function applyTheme(theme) {
        body.setAttribute("data-theme", theme);
        if (themeIcon) themeIcon.className = theme === "dark" ? "fa-solid fa-sun" : "fa-solid fa-moon";
    }
    applyTheme(localStorage.getItem("datebayo-theme") || "dark");

    if (themeButton) themeButton.addEventListener("click", () => {
        const nextTheme = body.getAttribute("data-theme") === "dark" ? "light" : "dark";
        applyTheme(nextTheme);
        localStorage.setItem("datebayo-theme", nextTheme);
    });

    document.querySelectorAll("[data-toggle-password]").forEach(button => {
        button.addEventListener("click", () => {
            const input = document.getElementById(button.dataset.togglePassword);
            if (!input) return;
            input.type = input.type === "password" ? "text" : "password";
            button.textContent = input.type === "password" ? "Ocultar contraseña" : "Mostrar contraseña";
        });
    });

    if (mobileMenuButton && sidebar) {
        mobileMenuButton.addEventListener("click", () => sidebar.classList.toggle("active"));
    }

    // ---- 2. Lógica Dinámica: Agregar hasta 5 Perfiles ----
    const MAX_PERFILES = 5;
    let numPerfiles = 1;
    const perfilesContainer = document.getElementById('perfilesContainer');
    const btnAgregarPerfil = document.getElementById('btnAgregarPerfil');
    const contadorBadge = document.getElementById('contadorPerfiles');

    if (btnAgregarPerfil && perfilesContainer) {
        btnAgregarPerfil.addEventListener('click', () => {
            if (numPerfiles < MAX_PERFILES) {
                numPerfiles++;

                const row = document.createElement('div');
                row.className = 'row g-2 mb-3 profile-row align-items-center';
                row.innerHTML = `
                    <div class="col-6">
                        <input type="text" name="nombrePerfil${numPerfiles}" class="form-control form-control-custom" placeholder="Nombre perfil ${numPerfiles}" required>
                    </div>
                    <div class="col-5">
                        <input type="text" name="pinPerfil${numPerfiles}" class="form-control form-control-custom" placeholder="PIN (Opcional)">
                    </div>
                    <div class="col-1 text-end">
                        <button type="button" class="btn btn-sm btn-outline-danger btn-remove-profile border-0"><i class="fas fa-times"></i></button>
                    </div>
                `;
                perfilesContainer.appendChild(row);
                actualizarVistaPerfiles();
            }
        });

        // Event Delegation para eliminar perfiles creados dinámicamente
        perfilesContainer.addEventListener('click', (e) => {
            const btnRemove = e.target.closest('.btn-remove-profile');
            if (btnRemove) {
                btnRemove.closest('.profile-row').remove();
                reindexarPerfiles();
            }
        });
    }

    // Reasigna los Nombres (name=) para asegurar que se envíen en orden secuencial al Servlet
    function reindexarPerfiles() {
        const rows = perfilesContainer.querySelectorAll('.profile-row');
        numPerfiles = 1; // El primero es fijo (no tiene botón de borrar)

        rows.forEach(row => {
            numPerfiles++;
            const nameInput = row.querySelector('input[type="text"][name^="nombrePerfil"]');
            const pinInput = row.querySelector('input[type="text"][name^="pinPerfil"]');

            if(nameInput) {
                nameInput.name = `nombrePerfil${numPerfiles}`;
                nameInput.placeholder = `Nombre perfil ${numPerfiles}`;
            }
            if(pinInput) {
                pinInput.name = `pinPerfil${numPerfiles}`;
                pinInput.placeholder = `PIN (Opcional)`;
            }
        });
        actualizarVistaPerfiles();
    }

    function actualizarVistaPerfiles() {
        contadorBadge.textContent = `${numPerfiles} / ${MAX_PERFILES}`;
        btnAgregarPerfil.style.display = numPerfiles >= MAX_PERFILES ? 'none' : 'block';
    }

})();

// ---- 3. Lógica para Poblar Modales (CRUD) ----
function abrirModalEditar(id, correo, estado) {
    document.getElementById('idCuentaEdit').value = id;
    document.getElementById('correoEdit').value = correo;
    document.getElementById('estadoEdit').value = estado.toLowerCase();

    const modal = new bootstrap.Modal(document.getElementById('modalEditarCuenta'));
    modal.show();
}

function abrirModalEliminar(id) {
    document.getElementById('idCuentaDelete').value = id;

    const modal = new bootstrap.Modal(document.getElementById('modalEliminarCuenta'));
    modal.show();
}