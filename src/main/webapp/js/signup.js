"use strict";

(function () {
    const CHECK_D  = 'M480-80q-83 0-156-31.5T197-197q-54-54-85.5-127T80-480q0-83 31.5-156T197-763q54-54 127-85.5T480-880q83 0 156 31.5T763-763q54 54 85.5 127T880-480q0 83-31.5 156T763-197q-54 54-127 85.5T480-80Zm-56-240 240-240-58-58-182 182-82-82-58 58 140 140Z';
    const CIRCLE_D = 'M480-80q-83 0-156-31.5T197-197q-54-54-85.5-127T80-480q0-83 31.5-156T197-763q54-54 127-85.5T480-880q83 0 156 31.5T763-763q54 54 85.5 127T880-480q0 83-31.5 156T763-197q-54 54-127 85.5T480-80Zm0-80q134 0 227-93t93-227q0-134-93-227t-227-93q-134 0-227 93t-93 227q0 134 93 227t227 93Zm0-320Z';

    const rules = [
        { id: 'req-length',    test: v => v.length >= 8 },
        { id: 'req-uppercase', test: v => /[A-Z]/.test(v) },
        { id: 'req-lowercase', test: v => /[a-z]/.test(v) },
        { id: 'req-number',    test: v => /\d/.test(v) },
        { id: 'req-special',   test: v => /[^A-Za-z0-9]/.test(v) },
    ];

    const pwInput = document.getElementById('password');
    const toggle  = document.getElementById('pwReqToggle');
    const list    = document.getElementById('pwReqList');

    const openList = () => {
        list.classList.add('open');
        toggle.setAttribute('aria-expanded', 'true');
        list.setAttribute('aria-hidden', 'false');
    };

    const closeList = () => {
        list.classList.remove('open');
        toggle.setAttribute('aria-expanded', 'false');
        list.setAttribute('aria-hidden', 'true');
    };

    const updateRules = (val) => {
        rules.forEach(({ id, test }) => {
            const item = document.getElementById(id);
            const path = item.querySelector('path');
            const met  = test(val);
            item.classList.toggle('met', met);
            item.classList.toggle('unmet', !met);
            path.setAttribute('d', met ? CHECK_D : CIRCLE_D);
        });
    };

    toggle.addEventListener('click', () => {
        list.classList.contains('open') ? closeList() : openList();
    });

    let dirty = false;
    pwInput.addEventListener('input', () => {
        if (!dirty) { dirty = true; openList(); }
        updateRules(pwInput.value);
    });

    pwInput.closest('form').addEventListener('submit', (e) => {
        const allMet = rules.every(({ test }) => test(pwInput.value));
        if (!allMet) {
            e.preventDefault();
            dirty = true;
            openList();
            updateRules(pwInput.value);
        }
    });
})();
