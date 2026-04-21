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
                <input type="password" name="password" id="password" placeholder="New password" required />
            </div>
        </div>
        <div class="btn-container sw-dir">
            <button type="submit" name="action" value="confirmForgotPassword" class="btn-submit">
                Confirm
            </button>
            <a href="auth?action=reset-pass" class="btn-submit back-btn">Back</a>
        </div>
    </form>
</main>
</body>
</html>
