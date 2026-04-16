"use strict";

/**
 * Initializes the application
 * @returns {void}
 */
const initialization = () => {
    handleAddButton();
}

/**
 * Handles the click event on the Add button
 * @returns {void}
 */
const handleAddButton = () => {
    const container = document.querySelector('.container');
    const toggleBtn = document.querySelector('.main-btn');

    toggleBtn.addEventListener('click', () => {

        container.classList.toggle('form-open');
    });
}

window.addEventListener('DOMContentLoaded', initialization);