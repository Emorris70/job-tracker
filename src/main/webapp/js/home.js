"use strict";
/**
 * Handles the initialization of the page
 * @returns {void}
 */
const initialization = () => {
    handleSidebar();
    handleAddButton();
    keepFormOpenOnError();
}
/**
 * Handles the sidebar and overlay
 * @returns {void}
 */
const handleSidebar = () => {
    const sidebar    = document.getElementById('sidebar');
    const overlay    = document.getElementById('sidebarOverlay');
    const openBtn    = document.getElementById('hamburgerBtn');
    const closeBtn   = document.getElementById('sidebarClose');

    const open = () => {
        sidebar.classList.add('open');
        overlay.classList.add('active');
        sidebar.setAttribute('aria-hidden', 'false');
        document.body.classList.add('sidebar-open');
    };

    const close = () => {
        sidebar.classList.remove('open');
        overlay.classList.remove('active');
        sidebar.setAttribute('aria-hidden', 'true');
        document.body.classList.remove('sidebar-open');
    };

    openBtn.addEventListener('click', open);
    closeBtn.addEventListener('click', close);
    overlay.addEventListener('click', close);

    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape') close();
    });
};

/**
 * Keeps the form open if there is an error
 * @returns {void}
 */
const keepFormOpenOnError = () => {
    const error = document.querySelector('.errorMsg');
    if (error) {
        document.querySelector('.container').classList.add('form-open');
    }
}

/**
 * Handles the add button
 * @returns {void}
 */
const handleAddButton = () => {
    const container = document.querySelector('.container');
    const toggleBtn = document.querySelector('.main-btn');
    if (!toggleBtn || !container) return;

    toggleBtn.addEventListener('click', () => {
        container.classList.toggle('form-open');
    });
}

window.addEventListener('DOMContentLoaded', initialization);
