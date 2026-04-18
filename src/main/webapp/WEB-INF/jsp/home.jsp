<%@ include file="/WEB-INF/jsp/includes/taglib.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<jsp:include page="includes/head.jsp"/>
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
    <div class="container">

        <div class="m-c-head">
            <div class="c-head">
                <h2 class="h-2">Job Applications</h2>
                <p class="s-p">Track your job search progress</p>
            </div>

            <div class="btn-container main-btn">
                <svg xmlns="http://www.w3.org/2000/svg"
                     class="img-d svg-color-c"
                     viewBox="0 -960 960 960"
                     fill="#e3e3e3">
                    <path d="M440-440H200v-80h240v-240h80v240h240v80H520v240h-80v-240Z"/></svg>
                <span class="add-app-c">
                    New Application
                </span>
                <svg xmlns="http://www.w3.org/2000/svg"
                     class="img-d svg-color-c"
                     viewBox="0 -960 960 960"
                     fill="#e3e3e">
                    <path d="m256-200-56-56 224-224-224-224 56-56 224 224 224-224 56 56-224 224 224 224-56 56-224-224-224 224Z"/></svg>
                <span class="cancel-c">
                    Cancel
                </span>
            </div>
        </div>
        <div class="form-card" id="add-app-form">
            <c:if test="${not empty error}">
                <div class="errorMsg">
                    <p class="error-msg">${error}</p>
                </div>
                <c:remove var="error" scope="session"/>
            </c:if>
            <form action="${pageContext.request.contextPath}/add" method="POST" class="app-grid-form">
                <div class="form-group">
                    <label>Company <span class="required-star">*</span></label>
                    <input type="text" name="companyName" placeholder="e.g. Google" required>
                </div>

                <div class="form-group">
                    <label>Position <span class="required-star">*</span></label>
                    <input type="text" name="jobTitle" placeholder="e.g. Software Engineer" required>
                </div>

                <div class="form-group">
                    <label>Location<span class="required-star">*</span></label>
                    <input type="text" name="location" placeholder="e.g. Madison, WI" required>
                </div>

                <div class="form-group">
                    <label>Salary Range<span class="required-star">*</span></label>
                    <input type="text" name="salaryRange" placeholder="e.g. $70k - $90k" required>
                </div>

                <div class="form-group">
                    <label>Status<span class="required-star">*</span></label>
                    <select name="status" required>
                        <option value="Applied">Applied</option>
                        <option value="Interviewing">Interviewing</option>
                        <option value="Offer">Offer</option>
                        <option value="Rejected">Rejected</option>
                    </select>
                </div>

                <div class="form-group">
                    <label>Date Applied <span class="required-star">*</span></label>
                    <input type="date" name="dateApplied" required>
                </div>

                <div class="form-group full-width">
                    <label>Link</label>
                    <input type="url" name="jobUrl" placeholder="https://...">
                </div>

                <div class="form-group full-width">
                    <label>Description</label>
                    <textarea name="description" rows="3" placeholder="Job requirements, tech stack..."></textarea>
                </div>

                <div class="form-footer">
                    <button type="submit" class="btn-primary">Add Application</button>
                </div>
            </form>
        </div>
        <c:choose>
            <c:when test="${not empty jobs}">
                <c:forEach var="job" items="${jobs}">
                    <div class="card-parent" data-id="${job.id}">
                        <div class="card-head-parent">
                            <div class="card-head-content">
                                <h2 class="card-h2">${job.jobTitle}</h2>
                                <p class="company-name">${job.companyName}</p>
                            </div>
                            <span class="card-pill-status status-${fn:toLowerCase(job.status)}">
                                    ${job.status}
                            </span>
                        </div>

                        <div class="card-meta-info">
                            <div class="meta-item">
                                <img src="${pageContext.request.contextPath}/images/location.png" alt="location" class="meta-icon">
                                <span>${job.location}</span>
                            </div>
                            <div class="meta-item">
                                <img src="${pageContext.request.contextPath}/images/dollar-sign.png" alt="salary" class="meta-icon">
                                <span>${job.salaryRange}</span>
                            </div>
                            <div class="meta-item">
                                <img src="${pageContext.request.contextPath}/images/calendar.png" alt="date" class="meta-icon">
                                <span>${job.dateApplied}</span>
                            </div>
                        </div>

                        <div class="card-content-parent">
                            <div class="card-description">
                                    ${fn:substring(job.description, 0, 100)}${fn:length(job.description) > 100 ? '...' : ''}
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <p>No Applications Yet – Click above to get started!!</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</main>

<footer>

</footer>
</body>
</html>
