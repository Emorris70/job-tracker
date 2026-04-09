<%@ include file="taglib.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<jsp:include page="auth_head.jsp"/>
<body>
<main class="container">
    <h1 class="m-h">Forgot Password?</h1>
    <div class="errorMsg">
        <c:if test="${not empty sessionScope.error}">
            <p class="error-msg">${sessionScope.error}</p>
            <c:remove var="error" scope="session"/>
        </c:if>
    </div>
    <p class="p-deco">Enter your email to receive a rest link</p>
    <form method="POST" action="${pageContext.request.contextPath}/auth" id="auth-form">
        <div class="con-wrapper">
            <label for="email">email</label>
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
        <div class="btn-container">
            <button type="submit" class="btn-submit">
                Reset
            </button>
        </div>
        <p class="dir-deco center-i">
            Back to<a href="auth?action=login">Login</a>
        </p>
    </form>
</main>
</body>
</html>
