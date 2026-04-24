<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error - Job Tracker</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css"/>
    <style>
        .error-page {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
            gap: 12px;
            text-align: center;
            padding: 2rem;
        }
        .error-code {
            font-size: 4rem;
            font-weight: 700;
            color: #37352F;
            line-height: 1;
        }
        .error-msg {
            font-size: 1.1rem;
            color: #787774;
            max-width: 400px;
        }
        .error-link {
            margin-top: 8px;
            padding: 0.5rem 1.25rem;
            border: 1px solid #E0E0E0;
            border-radius: 6px;
            color: #37352F;
            font-size: 0.9rem;
            transition: background 0.2s;
        }
        .error-link:hover { background: #F7F7F5; }
    </style>
</head>
<body>
<div class="error-page">
    <p class="error-code"><%= response.getStatus() %></p>
    <p class="error-msg">Something went wrong. The page you're looking for may not exist or you don't have access to it.</p>
    <a href="${pageContext.request.contextPath}/home" class="error-link">← Back to Home</a>
</div>
</body>
</html>
