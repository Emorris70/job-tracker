<jsp:include page="taglib.jsp"/>
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
            <label for="first_name">first Name</label>
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
        <div class="con-wrapper">
            <label for="password">password</label>
            <div class="input-wrapper">
                <img src="images/lock-p.png" alt="lock icon" />
                <input
                        type="password"
                        name="password"
                        id="password"
                        placeholder="Password"
                <%-- add title and pattern--%>
                        required
                />
            </div>
        </div>
        <p class="dir-deco">
            Already have an account?<a href="auth?action=login">Login</a>
        </p>
        <div class="btn-container">
            <button type="submit"
                    name="action"
                    value="signUp"
                    class="btn-submit" >
                Sign Up
            </button>
        </div>
    </form>
</main>
</body>
</html>
