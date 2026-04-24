<%@ include file="/WEB-INF/jsp/includes/taglib.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<jsp:include page="includes/head.jsp"/>
<body>
<jsp:include page="includes/header.jsp"/>
<main>
    <div class="container">
        <div class="detail-head">
            <a href="${pageContext.request.contextPath}/home" class="back-link">← Back to Applications</a>
        </div>

        <div class="detail-card">

            <div class="settings-page-head">
                <h1 class="detail-h1">Account Settings</h1>
                <p class="s-p">Manage your account preferences</p>
            </div>

            <c:if test="${not empty settingsError}">
                <div class="errorMsg" style="margin-top:1rem;">
                    <p class="error-msg">${fn:escapeXml(settingsError)}</p>
                </div>
                <c:remove var="settingsError" scope="session"/>
            </c:if>

            <%-- Profile Info --%>
            <div class="detail-grid-b">
                <h3 class="sub-head">Profile</h3>
                <div class="settings-rows">
                    <div class="settings-row">
                        <span class="settings-row-label">Name</span>
                        <span class="settings-row-value">${fn:escapeXml(sessionScope.user.firstName)}</span>
                    </div>
                    <div class="settings-row">
                        <span class="settings-row-label">Email</span>
                        <span class="settings-row-value">${fn:escapeXml(sessionScope.user.email)}</span>
                    </div>
                </div>
            </div>

            <%-- Danger Zone --%>
            <div class="detail-grid-b">
                <h3 class="sub-head danger-label">Danger Zone</h3>
                <div class="danger-zone-card">
                    <div class="danger-zone-row">
                        <div>
                            <p class="danger-zone-title">Delete Account</p>
                            <p class="danger-zone-desc">Permanently delete your account and all job applications. This action cannot be undone.</p>
                        </div>
                        <button type="button" class="btn-delete-trigger" id="deleteToggle">Delete Account</button>
                    </div>

                    <div class="delete-confirm" id="deleteConfirm">
                        <p class="delete-confirm-warn">This will permanently delete your account and every job application you have saved. You will be signed out immediately and this cannot be reversed.</p>
                        <form action="${pageContext.request.contextPath}/settings" method="POST">
                            <input type="hidden" name="action" value="deleteAccount">
                            <div class="delete-confirm-actions">
                                <button type="button" class="btn-cancel-delete" id="cancelDelete">Cancel</button>
                                <button type="submit" class="btn-confirm-delete">Yes, Delete My Account</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

        </div>
    </div>
</main>
<jsp:include page="includes/footer.jsp"/>
<script src="${pageContext.request.contextPath}/js/settings.js" defer></script>
</body>
</html>
