<jsp:include page="/taglib.jsp"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<jsp:include page="head.jsp"/>
<body>
<header>
    <h1 class="main-header">Job Tracker</h1>
    <div class="user-content">
        <div class="user-info">
            <img src="${pageContext.request.contextPath}/images/home-user.png" alt="user icon" class="img-d">
            <p class="user-email-d">${sessionScope.user.email}</p>
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
</header>

<main>

</main>

<footer>

</footer>
</body>
</html>
