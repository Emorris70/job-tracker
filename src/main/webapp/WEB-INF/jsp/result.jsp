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
            <%-- Title & Company Section --%>
            <div class="detail-header">
                <h1 class="detail-h1">${fn:escapeXml(job.jobTitle)}</h1>
                <p class="company-name">${fn:escapeXml(job.companyName)}</p>
                <span class="card-pill-status detail-pill-status status-${fn:toLowerCase(job.status)}">
                    ${job.status}
                </span>
                <div class="detail-btns">
                    <a href="${pageContext.request.contextPath}/edit?id=${job.id}" class="btn-deco" title="Edit">
                        <svg xmlns="http://www.w3.org/2000/svg" class="d-svg" viewBox="0 -960 960 960">
                            <path d="M200-200h57l391-391-57-57-391 391v57Zm-80 80v-170l528-527q12-11 26.5-17t30.5-6q16 0 31 6t26 18l55 56q12 11 17.5 26t5.5 30q0 16-5.5 30.5T817-647L290-120H120Zm640-584-56-56 56 56Zm-141 85-28-29 57 57-29-28Z"/>
                        </svg>
                    </a>
                    <form action="${pageContext.request.contextPath}/delete" method="POST" style="display:inline;">
                        <input type="hidden" name="id" value="${job.id}">
                        <button type="submit" title="Delete" class="btn-deco btn-danger"
                                onclick="return confirm('Delete this application?')">
                            <svg xmlns="http://www.w3.org/2000/svg" class="d-svg danger" viewBox="0 -960 960 960">
                                <path d="M280-120q-33 0-56.5-23.5T200-200v-520h-40v-80h200v-40h240v40h200v80h-40v520q0 33-23.5 56.5T680-120H280Zm400-600H280v520h400v-520ZM360-280h80v-360h-80v360Zm160 0h80v-360h-80v360ZM280-720v520-520Z"/>
                            </svg>
                        </button>
                    </form>
                </div>
            </div>

            <%-- Grid Info (Location, Salary, Date) --%>
            <div class="detail-grid">
                <div class="detail-item">
                    <label>
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 -960 960 960" fill="#787774" class="meta-icon">
                            <path d="M480-480q33 0 56.5-23.5T560-560q0-33-23.5-56.5T480-640q-33 0-56.5 23.5T400-560q0 33 23.5 56.5T480-480Zm0 294q122-112 181-203.5T720-560q0-117-73.5-188.5T480-820q-93 0-166.5 71.5T240-560q0 75 59 166.5T480-186Zm0 106Q319-217 239.5-334.5T160-560q0-150 96.5-245T480-900q127 0 223.5 95T800-560q0 112-79.5 229.5T480-80Zm0-480Z"/>
                        </svg>
                        Location
                    </label>
                    <p>${fn:escapeXml(job.location)}</p>
                </div>
                <div class="detail-item">
                    <label>
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 -960 960 960" fill="#787774" class="meta-icon">
                            <path d="M441-120v-86q-53-12-91.5-46T293-348l74-30q15 48 44.5 73t77.5 25q41 0 69.5-18.5T587-356q0-35-22-55.5T463-458q-86-27-118-64.5T313-614q0-65 42-101t86-41v-84h80v84q50 8 82.5 36.5T651-650l-74 32q-12-32-34-48t-60-16q-44 0-67 19.5T393-614q0 33 30 52t104 40q69 20 104.5 63.5T667-358q0 71-42 108t-104 46v84h-80Z"/>
                        </svg>
                        Salary Range
                    </label>
                    <p>${fn:escapeXml(job.salaryRange)}</p>
                </div>
                <div class="detail-item">
                    <label>
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 -960 960 960" fill="#787774" class="meta-icon">
                            <path d="M200-80q-33 0-56.5-23.5T120-160v-560q0-33 23.5-56.5T200-800h40v-80h80v80h320v-80h80v80h40q33 0 56.5 23.5T840-720v560q0 33-23.5 56.5T760-80H200Zm0-80h560v-400H200v400Zm0-480h560v-80H200v80Zm0 0v-80 80Zm280 240q-17 0-28.5-11.5T440-440q0-17 11.5-28.5T480-480q17 0 28.5 11.5T520-440q0 17-11.5 28.5T480-400Zm-160 0q-17 0-28.5-11.5T280-440q0-17 11.5-28.5T320-480q17 0 28.5 11.5T360-440q0 17-11.5 28.5T320-400Zm320 0q-17 0-28.5-11.5T600-440q0-17 11.5-28.5T640-480q17 0 28.5 11.5T680-440q0 17-11.5 28.5T640-400ZM480-240q-17 0-28.5-11.5T440-280q0-17 11.5-28.5T480-300q17 0 28.5 11.5T520-280q0 17-11.5 28.5T480-240Zm-160 0q-17 0-28.5-11.5T280-280q0-17 11.5-28.5T320-300q17 0 28.5 11.5T360-280q0 17-11.5 28.5T320-240Zm320 0q-17 0-28.5-11.5T600-280q0-17 11.5-28.5T640-300q17 0 28.5 11.5T680-280q0 17-11.5 28.5T640-240Z"/>
                        </svg>
                        Date Applied
                    </label>
                    <p>${formattedDate}</p>
                </div>
            </div>

            <%-- External Link Section --%>
            <c:if test="${not empty job.jobUrl}">
                <div class="detail-block detail-grid-b">
                    <h3 class="sub-head">Link</h3>
                    <a href="${fn:escapeXml(job.jobUrl)}" target="_blank" rel="noopener noreferrer" class="job-link">${fn:escapeXml(job.jobUrl)}</a>
                </div>
            </c:if>

            <%-- Description Section --%>
            <c:if test="${not empty job.description}">
                <div class="detail-block detail-grid-b">
                    <h3 class="sub-head">Job Description</h3>
                    <p class="description-text">${fn:escapeXml(job.description)}</p>
                </div>
            </c:if>
        </div>
    </div>
</main>
<jsp:include page="includes/footer.jsp"/>
</body>
</html>
