(() => {
 const themeButton = document.getElementById('themeToggleBtn');
 const themeIcon = document.getElementById('themeIcon');
 const body = document.body;
 const menuButton = document.getElementById('mobileMenuBtn');
 const sidebar = document.getElementById('sidebar');

 const applyTheme = (theme) => {
  body.setAttribute('data-theme', theme);
  if (themeIcon) {
   themeIcon.className = theme === 'dark' ? 'fa-solid fa-sun' : 'fa-solid fa-moon';
  }
 };

 // Aplicar tema guardado al cargar
 applyTheme(localStorage.getItem('datebayo-theme') || 'dark');

 // Botón para alternar tema
 if (themeButton) {
  themeButton.addEventListener('click', () => {
   const next = body.getAttribute('data-theme') === 'dark' ? 'light' : 'dark';
   applyTheme(next);
   localStorage.setItem('datebayo-theme', next);
  });
 }

 // Menú hamburguesa para versión móvil
 if (menuButton && sidebar) {
  menuButton.addEventListener('click', () => {
   sidebar.classList.toggle('active');
  });
 }
})();