(() => {
    const body = document.body;
    const themeButton = document.getElementById('themeToggleBtn');
    const themeIcon = document.getElementById('themeIcon');
    const themeLabel = document.getElementById('themeLabel');
    const menuButton = document.getElementById('mobileMenuBtn');
    const sidebar = document.getElementById('sidebar');

    const applyTheme = (theme) => {
        body.setAttribute('data-theme', theme);
        if (themeIcon) themeIcon.className = theme === 'dark' ? 'fa-solid fa-sun' : 'fa-solid fa-moon';
        if (themeLabel) themeLabel.textContent = theme === 'dark' ? 'Tema claro' : 'Tema oscuro';
    };
    let savedTheme = 'dark';
    try { savedTheme = localStorage.getItem('datebayo-theme') || 'dark'; } catch (e) {}
    applyTheme(savedTheme);

    if (themeButton) {
        themeButton.addEventListener('click', () => {
            const next = body.getAttribute('data-theme') === 'dark' ? 'light' : 'dark';
            applyTheme(next);
            try { localStorage.setItem('datebayo-theme', next); } catch (e) {}
        });
    }
    if (menuButton && sidebar) {
        menuButton.addEventListener('click', () => sidebar.classList.toggle('active'));
        document.addEventListener('click', (event) => {
            if (window.innerWidth <= 992 && sidebar.classList.contains('active')
                && !sidebar.contains(event.target) && !menuButton.contains(event.target)) {
                sidebar.classList.remove('active');
            }
        });
    }

    const modalEditar = document.getElementById('modalEditarProducto');
    if (modalEditar) {
        modalEditar.addEventListener('show.bs.modal', (event) => {
            const button = event.relatedTarget;
            if (!button) return;
            document.getElementById('editarId').value = button.dataset.id || '';
            document.getElementById('editarNombre').value = button.dataset.nombre || '';
            document.getElementById('editarDescripcion').value = button.dataset.descripcion || '';
            document.getElementById('editarImagen').value = button.dataset.imagen || '';
            document.getElementById('editarPrecio').value = button.dataset.precio || '';
            document.getElementById('editarCategoria').value = button.dataset.categoria || '';
            document.getElementById('editarTipo').value = button.dataset.tipo || '';
        });
    }
    const modalEliminar = document.getElementById('modalEliminarProducto');
    if (modalEliminar) {
        modalEliminar.addEventListener('show.bs.modal', (event) => {
            const button = event.relatedTarget;
            if (!button) return;
            document.getElementById('eliminarId').value = button.dataset.id || '';
            document.getElementById('eliminarNombre').textContent = button.dataset.nombre || 'este producto';
        });
    }

    const search = document.getElementById('buscarProducto');
    const rows = Array.from(document.querySelectorAll('#tablaProductos tbody tr.product-row'));
    const noResults = document.getElementById('sinResultados');
    if (search) {
        search.addEventListener('input', () => {
            const term = search.value.trim().toLocaleLowerCase('es');
            let visible = 0;
            rows.forEach((row) => {
                const match = row.textContent.toLocaleLowerCase('es').includes(term);
                row.classList.toggle('d-none', !match);
                if (match) visible++;
            });
            if (noResults) noResults.classList.toggle('d-none', visible !== 0 || rows.length === 0);
        });
    }
})();
