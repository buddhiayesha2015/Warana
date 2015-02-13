<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>Welcome to Warana</title>

    <%--common css files--%>
    <link href='<c:url value="/css/bootstrap.css" />' rel="stylesheet" type="text/css"/>
    <link href='<c:url value="/css/bootstrap-theme.css" />' rel="stylesheet" type="text/css"/>
    <link href='<c:url value="/css/bootstrap-dialog.css" />' rel="stylesheet" type="text/css"/>
    <link href='<c:url value="/css/jquery.pnotify.default.css" />' rel="stylesheet" type="text/css"/>
    <link href='<c:url value="/css/warana/style.css" />' rel="stylesheet" type="text/css"/>
    <link href='<c:url value="/css/camera.css" />' rel="stylesheet" type="text/css"/>

    <%--common js files--%>
    <script src='<c:url value="/js/jquery-1.10.2.js" />' type="text/javascript"></script>
    <script src='<c:url value="/js/jquery-migrate-1.2.1.js" />' type="text/javascript"></script>
    <script src='<c:url value="/js/jquery.json.min.js" />' type="text/javascript"></script>
    <script src='<c:url value="/js/jquery.pnotify.js" />' type="text/javascript"></script>
    <script src='<c:url value="/js/bootstrap.js" />' type="text/javascript"></script>
    <script src='<c:url value="/js/bootstrap-dialog.js" />' type="text/javascript"></script>
    <script src='<c:url value="/js/underscore.js" />' type="text/javascript"></script>
    <script src='<c:url value="/js/moment.min.js" />' type="text/javascript"></script>
    <script src='<c:url value="/js/warana.common.js" />' type="text/javascript"></script>
    <script src='<c:url value="/js/warana.validation.js" />' type="text/javascript"></script>
    <script src='<c:url value="/js/warana.serializer.js" />' type="text/javascript"></script>
    <script src='<c:url value="/js/base.js" />' type="text/javascript"></script>
    <script src='<c:url value="/js/camera.js" />' type="text/javascript"></script>
    <script src='<c:url value="/js/jquery.easing.1.3.js" />' type="text/javascript"></script>

</head>
<body>

<header>
    <nav class="navbar-inverse navbar-fixed-top">
        <div class="container">
            <div class="logo-holder">
                <a href='<c:url value="/dashboard" />'><img src='<c:url value="/images/logo-icon.png" />'></a>
            </div>
            <sec:authorize ifNotGranted="ROLE_USER">
                <div class="collapse navbar-collapse header-small-icon">
                    <div class="hidden-xs">
                        <ul class="nav navbar-nav navbar-right">
                            <li>
                                <button id="loginBtn" data-toggle="modal" href="#login-popup" class="top-buttons">Sign
                                    In
                                </button>
                            </li>
                            <li>
                                <button id="signupBtn" class="top-buttons last-button">Sign Up</button>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="visible-xs">
                    <button type="button" data-toggle="dropdown"
                            class="top-buttons last-button dropdown-toggle float-right" aria-expanded="false"><span
                            class="glyphicon glyphicon-align-justify"></span>
                    </button>
                    <ul class="dropdown-menu pull-right home-links" role="menu">
                        <li><a data-toggle="modal" href="#login-popup"><span
                                class="glyphicon glyphicon-log-in"></span> Sign In</a></li>
                        <li class="divider"></li>
                        <li><a id="signupBtnSm" href="<c:url value="/#"/>"><span
                                class="glyphicon glyphicon-user"></span> Sign Up</a></li>
                    </ul>
                </div>
            </sec:authorize>
            <sec:authorize ifAnyGranted="ROLE_USER">
                <div class="collapse navbar-collapse header-small-icon">
                    <div class="hidden-xs">
                        <ul class="nav navbar-nav navbar-right">
                            <li>
                                <button data-toggle="dropdown"
                                        class="top-buttons dropdown-toggle">Dashboard
                                </button>
                                <ul class="dropdown-menu home-links" role="menu">
                                    <li><a href="<c:url value="/dashboard"/>"><span
                                            class="glyphicon glyphicon-dashboard"></span> Dashboard</a></li>
                                    <li class="divider"></li>
                                    <li><a href="<c:url value="/upload"/>"><span
                                            class="glyphicon glyphicon-cloud-upload"></span> Resume Upload</a></li>
                                    <li class="divider"></li>
                                    <li><a href="<c:url value="/process" />"><span
                                            class="glyphicon glyphicon-cog"></span> Process Resume'</a></li>
                                    <li class="divider"></li>
                                    <li><a href="<c:url value="/analyze" />"><span
                                            class="glyphicon glyphicon-th-list"></span> Analyze</a></li>
                                    <li class="divider"></li>
                                    <li><a href="<c:url value="/viewstat" />"><span
                                            class="glyphicon glyphicon-eye-open"></span> View Stat</a></li>
                                </ul>
                            </li>
                            <li class="dashboard-btn">
                                <a href="<c:url value="/myprofile"/>">
                                    <button class="top-buttons">${sessionScope.firstName} ${sessionScope.lastName}
                                    </button>
                                </a>
                            </li>
                            <li>
                                <button data-toggle="dropdown"
                                        class="top-buttons last-button dropdown-toggle"><span
                                        class="glyphicon glyphicon-cog"></span>
                                </button>
                                <ul class="dropdown-menu home-links" role="menu">
                                    <li><a href="<c:url value="/adminpanel/upload"/>"><span
                                            class="glyphicon glyphicon-file"></span> Company Documents</a></li>
                                    <li class="divider"></li>
                                    <li><a href="<c:url value="/help" />"><span
                                            class="glyphicon glyphicon-question-sign"></span> Help</a></li>
                                    <li class="divider"></li>
                                    <li><a href="<c:url value="/j_spring_security_logout" />"><span
                                            class="glyphicon glyphicon-log-out"></span> Logout</a></li>
                                </ul>
                            </li>

                        </ul>
                    </div>
                </div>
                <div class="visible-xs">
                    <button type="button" data-toggle="dropdown"
                            class="top-buttons last-button dropdown-toggle float-right" aria-expanded="false"><span
                            class="glyphicon glyphicon-align-justify"></span>
                    </button>
                    <ul class="dropdown-menu pull-right home-links" role="menu">
                        <li><a href="<c:url value="/dashboard"/>"><span
                                class="glyphicon glyphicon-dashboard"></span> Dashboard</a></li>
                        <li class="divider"></li>
                        <li><a href="<c:url value="/myprofile"/>"><span
                                class="glyphicon glyphicon-user"></span> My Profile</a></li>
                        <li class="divider"></li>
                        <li><a href="<c:url value="/help" />"><span
                                class="glyphicon glyphicon-question-sign"></span> Help</a></li>
                        <li class="divider"></li>
                        <li><a href="<c:url value="/j_spring_security_logout" />"><span
                                class="glyphicon glyphicon-log-out"></span> Logout</a></li>
                    </ul>
                </div>
            </sec:authorize>
        </div>
    </nav>
</header>
