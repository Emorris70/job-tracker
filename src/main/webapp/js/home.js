"use strict";

/**
 * Initializes the application
 * @returns {void}
 */
const initialization = () => {
    handleAddButton();
    keepFormOpenOnError();
}

/**
 * Keeps the form open if an error message is present on the page.
 * @returns {void}
 */
const keepFormOpenOnError = () => {
    const error = document.querySelector('.errorMsg');
    if (error) {
        document.querySelector('.container').classList.add('form-open');
    }
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