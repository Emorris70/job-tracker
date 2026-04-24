<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%-- Sidebar --%>
<div class="sidebar" id="sidebar" aria-hidden="true">
    <div class="sidebar-top">
        <div class="sidebar-brand">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 -960 960 960" class="sidebar-brand-icon">
                <path d="M160-120q-33 0-56.5-23.5T80-200v-560q0-33 23.5-56.5T160-840h640q33 0 56.5 23.5T880-760v560q0 33-23.5 56.5T800-120H160Zm0-80h640v-480H160v480Zm160-80v-80h320v80H320Zm0-160v-80h320v80H320Z"/>
            </svg>
            <span>Job Tracker</span>
        </div>
        <button class="sidebar-close" id="sidebarClose" aria-label="Close menu">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 -960 960 960">
                <path d="m256-200-56-56 224-224-224-224 56-56 224 224 224-224 56 56-224 224 224 224-56 56-224-224-224 224Z"/>
            </svg>
        </button>
    </div>

    <nav class="sidebar-nav">
        <p class="sidebar-section-label">Navigation</p>
        <a href="${pageContext.request.contextPath}/home" class="sidebar-link">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 -960 960 960">
                <path d="M240-200h120v-240h240v240h120v-360L480-740 240-560v360Zm-80 80v-480l320-240 320 240v480H520v-240h-80v240H160Zm320-350Z"/>
            </svg>
            My Applications
        </a>
        <a href="${pageContext.request.contextPath}/settings" class="sidebar-link">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 -960 960 960">
                <path d="m370-80-16-128q-13-5-24.5-12T307-235l-119 50L78-375l103-78q-1-7-1-13.5v-27q0-6.5 1-13.5L78-585l110-190 119 50q11-8 23-15t24-12l16-128h220l16 128q13 5 24.5 12t22.5 15l119-50 110 190-103 78q1 7 1 13.5v27q0 6.5-2 13.5l103 78-110 190-118-50q-11 8-23 15t-24 12L590-80H370Zm70-80h79l14-106q31-8 57.5-23.5T639-327l99 41 39-68-86-65q5-14 7-29.5t2-31.5q0-16-2-31.5t-7-29.5l86-65-39-68-99 42q-22-23-48.5-38.5T533-694l-13-106h-79l-14 106q-31 8-57.5 23.5T321-633l-99-41-39 68 86 65q-5 14-7 29.5t-2 31.5q0 16 2 31.5t7 29.5l-86 65 39 68 99-42q22 23 48.5 38.5T427-266l13 106Zm42-180q58 0 99-41t41-99q0-58-41-99t-99-41q-59 0-99.5 41T342-480q0 58 40.5 99t99.5 41Zm-2-140Z"/>
            </svg>
            Account Settings
        </a>
    </nav>

    <div class="sidebar-footer">
        <div class="sidebar-user">
            <img src="${pageContext.request.contextPath}/images/home-user.png" alt="user avatar" class="img-d">
            <div class="sidebar-user-info">
                <span class="sidebar-user-name">${fn:escapeXml(sessionScope.user.firstName)}</span>
                <span class="sidebar-email">${fn:escapeXml(sessionScope.user.email)}</span>
            </div>
        </div>
        <a href="${pageContext.request.contextPath}/logout" class="sidebar-logout-link">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 -960 960 960">
                <path d="M200-120q-33 0-56.5-23.5T120-200v-560q0-33 23.5-56.5T200-840h280v80H200v560h280v80H200Zm440-160-55-58 102-102H360v-80h327L585-622l55-58 200 200-200 200Z"/>
            </svg>
            Logout
        </a>
    </div>
</div>
<div class="sidebar-overlay" id="sidebarOverlay"></div>

<header>
    <h1 class="main-header">Job Tracker</h1>
    <div class="user-content">
        <div class="user-info">
            <img src="${pageContext.request.contextPath}/images/home-user.png" alt="user icon" class="img-d">
            <a href="${pageContext.request.contextPath}/settings" class="user-email-d user-email-link">${fn:escapeXml(sessionScope.user.email)}</a>
        </div>
        <div class="user-logout">
            <svg xmlns="http://www.w3.org/2000/svg"
                 class="img-d svg-color-c"
                 viewBox="0 -960 960 960"
                 fill="#e3e3e3">
                <path d="M200-120q-33 0-56.5-23.5T120-200v-560q0-33 23.5-56.5T200-840h280v80H200v560h280v80H200Zm440-160-55-58 102-102H360v-80h327L585-622l55-58 200 200-200 200Z"/></svg>
            <a href="${pageContext.request.contextPath}/logout">Logout</a>
        </div>
    </div>
    <button class="hamburger" id="hamburgerBtn" aria-label="Open menu">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 -960 960 960">
            <path d="M120-240v-80h720v80H120Zm0-200v-80h720v80H120Zm0-200v-80h720v80H120Z"/>
        </svg>
    </button>
</header>
