"use strict";
/**
 * Handles the delete confirmation
 * @returns {void}
 */
(function () {
    var toggle  = document.getElementById("deleteToggle");
    var confirm = document.getElementById("deleteConfirm");
    var cancel  = document.getElementById("cancelDelete");

    if (!toggle || !confirm || !cancel) return;

    toggle.addEventListener("click", function () {
        confirm.classList.add("open");
        toggle.disabled = true;
        toggle.style.opacity = "0.4";
    });

    cancel.addEventListener("click", function () {
        confirm.classList.remove("open");
        toggle.disabled = false;
        toggle.style.opacity = "";
    });
})();
