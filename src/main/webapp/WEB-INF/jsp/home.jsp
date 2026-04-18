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
                    <input type="text" name="companyName" placeholder="e.g. Google">
                </div>

                <div class="form-group">
                    <label>Position <span class="required-star">*</span></label>
                    <input type="text" name="jobTitle" placeholder="e.g. Software Engineer">
                </div>

                <div class="form-group">
                    <label>Location<span class="required-star">*</span></label>
                    <input type="text" name="location" placeholder="e.g. Madison, WI">
                </div>

                <div class="form-group">
                    <label>Salary Range<span class="required-star">*</span></label>
                    <input type="text" name="salaryRange" placeholder="e.g. $70k - $90k">
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
                    <input type="date" name="dateApplied">
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
        <div class="card-page-d">
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
                                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 -960 960 960" fill="#787774" class="meta-icon">
                                        <path d="M480-480q33 0 56.5-23.5T560-560q0-33-23.5-56.5T480-640q-33 0-56.5 23.5T400-560q0 33 23.5 56.5T480-480Zm0 294q122-112 181-203.5T720-560q0-117-73.5-188.5T480-820q-93 0-166.5 71.5T240-560q0 75 59 166.5T480-186Zm0 106Q319-217 239.5-334.5T160-560q0-150 96.5-245T480-900q127 0 223.5 95T800-560q0 112-79.5 229.5T480-80Zm0-480Z"/>
                                    </svg>
                                    <span>${job.location}</span>
                                </div>
                                <div class="meta-item">
                                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 -960 960 960" fill="#787774" class="meta-icon">
                                        <path d="M441-120v-86q-53-12-91.5-46T293-348l74-30q15 48 44.5 73t77.5 25q41 0 69.5-18.5T587-356q0-35-22-55.5T463-458q-86-27-118-64.5T313-614q0-65 42-101t86-41v-84h80v84q50 8 82.5 36.5T651-650l-74 32q-12-32-34-48t-60-16q-44 0-67 19.5T393-614q0 33 30 52t104 40q69 20 104.5 63.5T667-358q0 71-42 108t-104 46v84h-80Z"/>
                                    </svg>
                                    <span>${job.salaryRange}</span>
                                </div>
                                <div class="meta-item">
                                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 -960 960 960" fill="#787774" class="meta-icon">
                                        <path d="M200-80q-33 0-56.5-23.5T120-160v-560q0-33 23.5-56.5T200-800h40v-80h80v80h320v-80h80v80h40q33 0 56.5 23.5T840-720v560q0 33-23.5 56.5T760-80H200Zm0-80h560v-400H200v400Zm0-480h560v-80H200v80Zm0 0v-80 80Zm280 240q-17 0-28.5-11.5T440-440q0-17 11.5-28.5T480-480q17 0 28.5 11.5T520-440q0 17-11.5 28.5T480-400Zm-160 0q-17 0-28.5-11.5T280-440q0-17 11.5-28.5T320-480q17 0 28.5 11.5T360-440q0 17-11.5 28.5T320-400Zm320 0q-17 0-28.5-11.5T600-440q0-17 11.5-28.5T640-480q17 0 28.5 11.5T680-440q0 17-11.5 28.5T640-400ZM480-240q-17 0-28.5-11.5T440-280q0-17 11.5-28.5T480-300q17 0 28.5 11.5T520-280q0 17-11.5 28.5T480-240Zm-160 0q-17 0-28.5-11.5T280-280q0-17 11.5-28.5T320-300q17 0 28.5 11.5T360-280q0 17-11.5 28.5T320-240Zm320 0q-17 0-28.5-11.5T600-280q0-17 11.5-28.5T640-300q17 0 28.5 11.5T680-280q0 17-11.5 28.5T640-240Z"/>
                                    </svg>
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
    </div>
</main>

<footer>

</footer>
</body>
</html>
