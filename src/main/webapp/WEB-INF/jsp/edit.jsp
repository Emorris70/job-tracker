<%@ include file="/WEB-INF/jsp/includes/taglib.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<jsp:include page="includes/head.jsp"/>
<body>
<jsp:include page="includes/header.jsp"/>
<main>
    <div class="container">
        <div class="detail-head">
            <a href="${pageContext.request.contextPath}/view?id=${job.id}" class="back-link">← Back to Application</a>
        </div>

        <div class="form-card">
            <c:if test="${not empty editError}">
                <div class="errorMsg">
                    <p class="error-msg">${editError}</p>
                </div>
                <c:remove var="editError" scope="session"/>
            </c:if>

            <form action="${pageContext.request.contextPath}/edit?id=${job.id}" method="POST" class="app-grid-form">

                <div class="form-group">
                    <label>Company <span class="required-star">*</span></label>
                    <input type="text" name="companyName" value="${fn:escapeXml(job.companyName)}" required>
                </div>

                <div class="form-group">
                    <label>Position <span class="required-star">*</span></label>
                    <input type="text" name="jobTitle" value="${fn:escapeXml(job.jobTitle)}" required>
                </div>

                <div class="form-group">
                    <label>Location <span class="required-star">*</span></label>
                    <input type="text" name="location" value="${fn:escapeXml(job.location)}" required>
                </div>

                <div class="form-group">
                    <label>Salary Range <span class="required-star">*</span></label>
                    <input type="text" name="salaryRange" value="${fn:escapeXml(job.salaryRange)}" required>
                </div>

                <div class="form-group">
                    <label>Status <span class="required-star">*</span></label>
                    <select name="status" required>
                        <option value="Applied"      ${job.status == 'Applied'      ? 'selected' : ''}>Applied</option>
                        <option value="Interviewing" ${job.status == 'Interviewing' ? 'selected' : ''}>Interviewing</option>
                        <option value="Offer"        ${job.status == 'Offer'        ? 'selected' : ''}>Offer</option>
                        <option value="Rejected"     ${job.status == 'Rejected'     ? 'selected' : ''}>Rejected</option>
                    </select>
                </div>

                <div class="form-group">
                    <label>Date Applied <span class="required-star">*</span></label>
                    <input type="date" name="dateApplied" value="${job.dateApplied}" required>
                </div>

                <div class="form-group full-width">
                    <label>Link</label>
                    <input type="url" name="jobUrl" value="${fn:escapeXml(job.jobUrl)}" placeholder="https://...">
                </div>

                <div class="form-group full-width">
                    <label>Description</label>
                    <textarea name="description" rows="4">${fn:escapeXml(job.description)}</textarea>
                </div>

                <div class="form-footer">
                    <button type="submit" class="btn-primary">Save Changes</button>
                </div>
            </form>
        </div>
    </div>
</main>
<jsp:include page="includes/footer.jsp"/>
</body>
</html>
