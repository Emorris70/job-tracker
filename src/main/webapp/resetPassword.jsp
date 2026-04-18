<%@ include file="WEB-INF/jsp/includes/taglib.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<jsp:include page="auth_head.jsp"/>
<body>
<main class="container">
    <div class="c-col">
        <h1 class="m-h">Forgot Password?</h1>
        <p class="p-deco reset-p-1">Enter your email to begin</p>
    </div>
    <form method="POST" action="${pageContext.request.contextPath}/auth" id="auth-form" class="reset-form">
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
            <button type="submit" name="action" value="forgotPassword" class="btn-submit">
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
