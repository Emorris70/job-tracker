<%@ include file="WEB-INF/jsp/includes/taglib.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<jsp:include page="auth_head.jsp"/>
<body>
<main class="container override-animation">
    <h1 class="m-h">Reset your Password</h1>
    <p class="p-deco s-p s-p-2">Enter the code sent to your email and choose a new password.</p>
    <c:if test="${not empty sessionScope.error}">
        <div class="errorMsg">
            <p class="error-msg">${sessionScope.error}</p>
        </div>
        <c:remove var="error" scope="session"/>
    </c:if>
    <form method="POST" action="${pageContext.request.contextPath}/auth" id="auth-exists">
        <div class="con-wrapper">
            <label for="v-code">Verification code</label>
            <div class="input-wrapper">
                <input type="text" name="v-code" id="v-code" placeholder="Enter code" required />
            </div>
        </div>
        <div class="con-wrapper">
            <label for="password">New password</label>
            <div class="input-wrapper">
                <img src="${pageContext.request.contextPath}/images/lock-p.png" alt="lock icon" />
                <input type="password" name="password" id="password" placeholder="New password"
                       pattern="(?=.*[0-9])(?=.*[^A-Za-z0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}"
                       title="Must be 8+ characters and include uppercase, lowercase, a number, and a special character."
                       required />
            </div>
        </div>
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

        <div class="btn-container sw-dir">
            <button type="submit" name="action" value="confirmForgotPassword" class="btn-submit">
                Confirm
            </button>
            <a href="auth?action=reset-pass" class="btn-submit back-btn">Back</a>
        </div>
    </form>
</main>
<script src="${pageContext.request.contextPath}/js/signup.js" defer></script>
</body>
</html>
