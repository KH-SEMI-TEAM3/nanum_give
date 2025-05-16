// header.js
document.addEventListener('DOMContentLoaded', function() {
    // Toggle dropdown menu
    const dropdownToggle = document.querySelector('.dropdown-toggle');
    const dropdownMenu = document.querySelector('.dropdown-menu');
    
    if (dropdownToggle && dropdownMenu) {
        // Click event for dropdown
        dropdownToggle.addEventListener('click', function(e) {
            e.stopPropagation();
            dropdownMenu.classList.toggle('show');
        });
        
        // Close dropdown when clicking outside
        document.addEventListener('click', function() {
            if (dropdownMenu.classList.contains('show')) {
                dropdownMenu.classList.remove('show');
            }
        });
        
        // Prevent dropdown from closing when clicking inside it
        dropdownMenu.addEventListener('click', function(e) {
            e.stopPropagation();
        });
    }
    
    // Active link highlighting
    const navLinks = document.querySelectorAll('.nav-item');
    const currentPath = window.location.pathname;
    
    navLinks.forEach(link => {
        const href = link.getAttribute('href');
        // Remove context path if necessary
        const path = href.includes('://') ? new URL(href).pathname : href;
        
        if (currentPath === path || 
            (path !== '/' && currentPath.startsWith(path))) {
            link.classList.add('active');
        }
    });
    
    // Search functionality
    const searchForm = document.querySelector('.search-input');
    const searchInput = searchForm ? searchForm.querySelector('input') : null;
    const searchButton = searchForm ? searchForm.querySelector('.search-button') : null;
    
    if (searchForm && searchInput && searchButton) {
        searchButton.addEventListener('click', function() {
            if (searchInput.value.trim()) {
                // Replace with your actual search endpoint
                window.location.href = `/search?q=${encodeURIComponent(searchInput.value.trim())}`;
            }
        });
        
        // Allow search on Enter key
        searchInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter' && searchInput.value.trim()) {
                e.preventDefault();
                window.location.href = `/search?q=${encodeURIComponent(searchInput.value.trim())}`;
            }
        });
    }
});