<jsp:include page="taglib.jsp"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<jsp:include page="auth_head.jsp"/>
<body>
<%--<jsp:include page="header.jsp"/>--%>
<main class="container">
    <h1 class="m-h">Welcome Back!</h1>

    <div class="errorMsg">
        <c:if test="${not empty sessionScope.error}">
            <p class="error-msg">${sessionScope.error}</p>
            <c:remove var="error" scope="session"/>
        </c:if>
    </div>

    <form method="POST" action="auth" id="auth-exists">
        <div class="con-wrapper">
            <label for="email">email</label>
            <div class="input-wrapper">
                <img src="images/mail.png" alt="mail icon" />
                <input type="email" name="email" id="email" placeholder="name@host.com" />
            </div>
        </div>
        <div class="con-wrapper">
            <label for="password">password</label>
            <div class="input-wrapper">
                <img src="images/lock-p.png" alt="lock icon" />
                <input
                        type="password"
                        name="password"
                        id="password"
                        placeholder="Password"
                />
            </div>
        </div>
        <p class="dir-deco">
            Don't have an account?<a href="auth?action=sign-up" id="sign-s">Sign up</a>
        </p>
        <div class="btn-container">
            <button
                    type="submit"
                    name="action"
                    value="login"
                    class="btn-submit">Login</button>
        </div>
        <p class="dir-deco center-i">
            Forgot Password?<a href="auth?action=reset-pass" id="reset-s">Reset Password</a>
        </p>
    </form>
</main>
</body>
</html>