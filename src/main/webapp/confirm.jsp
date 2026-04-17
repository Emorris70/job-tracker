<%@ include file="WEB-INF/jsp/includes/taglib.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<jsp:include page="auth_head.jsp"/>
<body>
<main class="container override-animation">
    <h1 class="m-h">Check your email</h1>
    <p class="p-deco s-p">Enter the code that we sent to the email address. The code expires in 15 minutes.</p>
    <div class="errorMsg">
        <c:if test="${not empty sessionScope.error}">
            <p class="error-msg">${sessionScope.error}</p>
            <c:remove var="error" scope="session"/>
        </c:if>
    </div>
    <form method="POST" action="${pageContext.request.contextPath}/auth" id="auth-exists">
        <div class="con-wrapper">
            <label for="v-code">Verification code</label>
            <div class="input-wrapper">
                <input type="text" name="v-code" id="v-code" placeholder="Enter code" />
            </div>
        </div>
        <div class="btn-container sw-dir">
            <button type="submit"
                    name="action"
                    value="confirm"
                    class="btn-submit">Continue</button>
            <a href="auth?action=sign-up" class="btn-submit back-btn">Back</a>
        </div>
    </form>
</main>
</body>
</html>
