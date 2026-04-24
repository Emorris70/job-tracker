<%@ include file="WEB-INF/jsp/includes/taglib.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<jsp:include page="auth_head.jsp"/>
<body>
<main class="container">
    <h1 class="m-h">Start Tracking!</h1>

    <div class="errorMsg">
        <c:if test="${not empty sessionScope.error}">
            <p class="error-msg">${sessionScope.error}</p>
            <c:remove var="error" scope="session"/>
        </c:if>
    </div>
    <form method="POST" action="${pageContext.request.contextPath}/auth" id="auth-form">
        <div class="con-wrapper">
            <label for="first_name">First Name</label>
            <div class="input-wrapper">
                <img src="images/user.png" alt="avatar logo" />
                <input
                        type="text"
                        name="first_name"
                        id="first_name"
                        placeholder="First name"
                        required
                />
            </div>
        </div>
        <div class="con-wrapper">
            <label for="email">Email</label>
            <div class="input-wrapper">
                <input
                        type="email"
                        name="email"
                        id="email"
                        placeholder="name@host.com"
                        required
                />
                <img src="images/mail.png" alt="mail icon" />
            </div>
        </div>
        <div class="con-wrapper">
            <label for="password">Password</label>
            <div class="input-wrapper">
                <img src="images/lock-p.png" alt="lock icon" />
                <input
                        type="password"
                        name="password"
                        id="password"
                        placeholder="Password"
                        pattern="(?=.*[0-9])(?=.*[^A-Za-z0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}"
                        title="Must be 8+ characters and include uppercase, lowercase, a number, and a special character."
                        required
                />
            </div>
        </div>

        <%-- Collapsible password requirements --%>
        <div class="pw-req-wrap">
            <button type="button" class="pw-req-toggle" id="pwReqToggle" aria-expanded="false" aria-controls="pwReqList">
                <svg class="pw-req-chevron" xmlns="http://www.w3.org/2000/svg" viewBox="0 -960 960 960">
                    <path d="M480-344 240-584l56-56 184 184 184-184 56 56-240 240Z"/>
                </svg>
                Password requirements
            </button>
            <ul class="pw-req-list" id="pwReqList" aria-hidden="true">
                <li class="pw-req-item" id="req-length">
                    <svg class="pw-req-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 -960 960 960">
                        <path d="M480-80q-83 0-156-31.5T197-197q-54-54-85.5-127T80-480q0-83 31.5-156T197-763q54-54 127-85.5T480-880q83 0 156 31.5T763-763q54 54 85.5 127T880-480q0 83-31.5 156T763-197q-54 54-127 85.5T480-80Zm0-80q134 0 227-93t93-227q0-134-93-227t-227-93q-134 0-227 93t-93 227q0 134 93 227t227 93Zm0-320Z"/>
                    </svg>
                    8 or more characters
                </li>
                <li class="pw-req-item" id="req-uppercase">
                    <svg class="pw-req-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 -960 960 960">
                        <path d="M480-80q-83 0-156-31.5T197-197q-54-54-85.5-127T80-480q0-83 31.5-156T197-763q54-54 127-85.5T480-880q83 0 156 31.5T763-763q54 54 85.5 127T880-480q0 83-31.5 156T763-197q-54 54-127 85.5T480-80Zm0-80q134 0 227-93t93-227q0-134-93-227t-227-93q-134 0-227 93t-93 227q0 134 93 227t227 93Zm0-320Z"/>
                    </svg>
                    At least 1 uppercase letter
                </li>
                <li class="pw-req-item" id="req-lowercase">
                    <svg class="pw-req-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 -960 960 960">
                        <path d="M480-80q-83 0-156-31.5T197-197q-54-54-85.5-127T80-480q0-83 31.5-156T197-763q54-54 127-85.5T480-880q83 0 156 31.5T763-763q54 54 85.5 127T880-480q0 83-31.5 156T763-197q-54 54-127 85.5T480-80Zm0-80q134 0 227-93t93-227q0-134-93-227t-227-93q-134 0-227 93t-93 227q0 134 93 227t227 93Zm0-320Z"/>
                    </svg>
                    At least 1 lowercase letter
                </li>
                <li class="pw-req-item" id="req-number">
                    <svg class="pw-req-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 -960 960 960">
                        <path d="M480-80q-83 0-156-31.5T197-197q-54-54-85.5-127T80-480q0-83 31.5-156T197-763q54-54 127-85.5T480-880q83 0 156 31.5T763-763q54 54 85.5 127T880-480q0 83-31.5 156T763-197q-54 54-127 85.5T480-80Zm0-80q134 0 227-93t93-227q0-134-93-227t-227-93q-134 0-227 93t-93 227q0 134 93 227t227 93Zm0-320Z"/>
                    </svg>
                    At least 1 number
                </li>
                <li class="pw-req-item" id="req-special">
                    <svg class="pw-req-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 -960 960 960">
                        <path d="M480-80q-83 0-156-31.5T197-197q-54-54-85.5-127T80-480q0-83 31.5-156T197-763q54-54 127-85.5T480-880q83 0 156 31.5T763-763q54 54 85.5 127T880-480q0 83-31.5 156T763-197q-54 54-127 85.5T480-80Zm0-80q134 0 227-93t93-227q0-134-93-227t-227-93q-134 0-227 93t-93 227q0 134 93 227t227 93Zm0-320Z"/>
                    </svg>
                    At least 1 special character
                </li>
            </ul>
        </div>

        <p class="dir-deco">
            Already have an account?<a href="auth?action=login">Login</a>
        </p>
        <div class="btn-container">
            <button type="submit"
                    name="action"
                    value="signUp"
                    class="btn-submit">
                Sign Up
            </button>
        </div>
    </form>
</main>

<script src="${pageContext.request.contextPath}/js/signup.js" defer></script>
</body>
</html>
