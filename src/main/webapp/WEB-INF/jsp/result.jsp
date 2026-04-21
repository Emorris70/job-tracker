<%@ include file="/WEB-INF/jsp/includes/taglib.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<jsp:include page="includes/head.jsp"/>
<body>
<jsp:include page="includes/header.jsp"/>
<main class="container detail-view">
    <div class="container">
        <div class="detail-head">
            <a href="${pageContext.request.contextPath}/home" class="back-link">← Back to Applications</a>
        </div>

        <div class="detail-card">
            <%-- Title & Company Section --%>
            <div class="detail-header">
                <h1 class="detail-h1">${job.jobTitle}</h1>
                <p class="detail-company">${job.companyName}</p>
                <span class="card-pill-status status-${fn:toLowerCase(job.status)}">
                    ${job.status}
                </span>
                <div class="detail-btns">
                    <button class="edit" title="edit btn-deco"></button>
                    <form action="${pageContext.request.contextPath}/delete" method="POST" style="display:inline;">
                        <input type="hidden" name="id" value="${job.id}">
                        <button type="submit" class="btn-danger" onclick="return confirm('Delete this application?')">Delete</button>
                    </form>
                </div>
            </div>
            <%-- Grid Info (Location, Salary, Date) --%>
            <div class="detail-grid">
                <div class="detail-item">
                    <label>Location</label>
                    <p>${job.location}</p>
                </div>
                <div class="detail-item">
                    <label>Salary Range</label>
                    <p>${job.salaryRange}</p>
                </div>
                <div class="detail-item">
                    <label>Date Applied</label>
                    <p>${job.dateApplied}</p>
                </div>
            </div>

            <%-- External Link Section --%>
            <c:if test="${not empty job.jobUrl}">
                <div class="detail-block">
                    <h3 class="detail-h3">Link</h3>
                    <a href="${job.jobUrl}" target="_blank" class="job-link">${job.jobUrl}</a>
                </div>
            </c:if>

            <%-- Description Section --%>
            <div class="detail-block">
                <h3 class="detail-h3">Job Description</h3>
                <div class="description-text">
                    ${job.description}
                </div>
            </div>
        </div>
    </div>
</main>
</body>
</html>
