<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="react_include.jsp" %>
<!DOCTYPE html>
<html>
<head>

<style>

    @font-face {
        font-family: TextaRegular;
        src: url(../../Texta_Font/Texta-Regular.otf);
    }

    @font-face {
        font-family: TextaBlack;
        src: url(../../Texta_Font/Texta-Black.otf);
    }

body {
    font-family: TextaRegular,Univers,Calibri,"Gill Sans","Gill Sans MT","Myriad Pro",Myriad,"DejaVu Sans Condensed","Liberation Sans","Nimbus Sans L",Tahoma,Geneva,"Helvetica Neue",Helvetica,Arial,sans-serif;
    font-size: 0.9em;
    background-color: #e2e2e2;
    background-image: linear-gradient( 180deg, #FFF,#e2e2e2);
    background-repeat: repeat-x;
}
h1 {
    font-size: 1.5em;
    line-height: .5em;
    font-family: TextaBlack, Univers, Calibri, "Gill Sans", "Gill Sans MT", "Myriad Pro",
    Myriad, "DejaVu Sans Condensed", "Liberation Sans", "Nimbus Sans L",
    Tahoma, Geneva, "Helvetica Neue", Helvetica, Arial,
    sans-serif !important;
}

.redHeader {
    color: darkred;
    float: right;
}

.blueHeader {
    color: #0971AB;
}

.darkBlueButton {
    color: #FFF;
    background-color: #185DA2;
    cursor: pointer;
    padding: 5px 5px;
    border-radius: 5px;
    border: 1px solid #424142;
    background-image: linear-gradient(bottom, #185DA2 0%, #2989E6 100%);
    background-image: -o-linear-gradient(bottom, #185DA2 0%, #2989E6 100%);
    background-image: -moz-linear-gradient(bottom, #185DA2 0%, #2989E6 100%);
    background-image: -webkit-linear-gradient(bottom, #185DA2 0%, #2989E6 100%);
    background-image: -ms-linear-gradient(bottom, #185DA2 0%, #2989E6 100%);
    font-family: Verdana;
    font-size: 11px;
    background-image: -webkit-gradient(
            linear,
            left bottom,
            left top,
            color-stop(0, #185DA2),
            color-stop(1, #2989E6)
    );
    text-decoration: none;
}
.darkBlueButton:hover {
    background-image: linear-gradient(bottom, #2989E6 0%, #185DA2 100%);
    background-image: -o-linear-gradient(bottom, #2989E6 0%, #185DA2 100%);
    background-image: -moz-linear-gradient(bottom, #2989E6 0%, #185DA2 100%);
    background-image: -webkit-linear-gradient(bottom, #2989E6 0%, #185DA2 100%);
    background-image: -ms-linear-gradient(bottom, #2989E6 0%, #185DA2 100%);
    background-image: -webkit-gradient(
            linear,
            left bottom,
            left top,
            color-stop(0, #2989E6),
            color-stop(1, #185DA2)
    );
}
.darkBlueButton:active {
    background-image: linear-gradient(bottom, #2989E6 0%, #246EBE 87%, #185DA2 100%);
    background-image: -o-linear-gradient(bottom, #2989E6 0%, #246EBE 87%, #185DA2 100%);
    background-image: -moz-linear-gradient(bottom, #2989E6 0%, #246EBE 87%, #185DA2 100%);
    background-image: -webkit-linear-gradient(bottom, #2989E6 0%, #246EBE 87%, #185DA2 100%);
    background-image: -ms-linear-gradient(bottom, #2989E6 0%, #246EBE 87%, #185DA2 100%);
    background-image: -webkit-gradient(
            linear,
            left bottom,
            left top,
            color-stop(0, #2989E6),
            color-stop(0.87, #246EBE),
            color-stop(1, #185DA2)
    );
}

a {
    color: #0971AB;
    text-decoration: none;
}
a:hover {
    text-decoration: none;
    border-bottom: 1px dotted #0971AB;
}
.container {
    position: relative;
    top: 100px;
    min-height: 320px;
    margin-left: auto;
    margin-right: auto;
    padding: 10px;
    max-width: 520px;
    border-radius: 5px;
    border: 1px solid #BBB;
    background-color: #FFF;
    background-image: linear-gradient(bottom, #E2E2E2 0%, #FFF 100%);
    background-image: -o-linear-gradient(bottom, #E2E2E2 0%, #FFF 100%);
    background-image: -moz-linear-gradient(bottom, #E2E2E2 0%, #FFF 100%);
    background-image: -webkit-linear-gradient(bottom, #E2E2E2 0%, #FFF 100%);
    background-image: -ms-linear-gradient(bottom, #E2E2E2 0%, #FFF 100%);
    background-image: -webkit-gradient(
        linear,
        left bottom,
        left top,
        color-stop(0, #E2E2E2),
        color-stop(1, #FFF)
    );;
}
</style>

</head>
<body>

<img src="../cyverse_logo.png" height="107px" width="500px"/>

<div class="container">

    <h1 class="blueHeader" style="text-align: center">Oops... That wasn't supposed to happen</h1>

    <p style="text-align: center">
        An unexpected error seems to have occurred. Try refreshing your page.
    </p>
    <p style="text-align: center">
        <button class="darkBlueButton" onclick="sessionStorage.removeItem('is_reloaded');window.location.replace('${login_url}')">Refresh</button>
    </p>

    <p>
        If the error persists and you want to learn more about what's going on
        and when it may be resolved, you can contact support with the below error details at <a href="mailto:support@cyverse.org">support@cyverse.org</a>
    </p>

    <div id="errorPanel"></div>

    <p>
        You can also learn more about goings-on at CyVerse, as well as
        <a href="${maintenance_cal_url}">scheduled maintenance</a> events, upcoming
        workshops, and more by checking our <a href="${facebook_url}">CyVerse Facebook</a> page,
        following us on <a href="${twitter_url}">CyVerse Twitter</a>, and making sure you're subscribed
        to our newsletter, <a href="${newsletter_url}">The Node</a>.
    </p>

    <hr/>

    <div style="float: right"><a href="http://www.cyverse.org">CyVerse Home Page</a></div>
    <div><a href="${login_url}">Go back to ${app_name}</a></div>
    <div><a href="${logout_url}">Log Out</a></div>

</div>

</body>
</html>

<script type="text/javascript">
    <%--If the user refreshes their browser, redirect to /de --%>
    if (sessionStorage.getItem("is_reloaded")) {
        window.location.replace('${login_url}');
        sessionStorage.removeItem("is_reloaded");
    } else {
        sessionStorage.setItem("is_reloaded", 1);
    }

    ReactDOM.render(
        React.createElement(
            CyVerseReactComponents.util.ErrorExpansionPanel,
            {
                errMsg: '${error_message}',
                username: '${username}',
                userAgent: '${user_agent}',
                date: '${date}',
                host: '${request_url}'
            }
        ),
        document.getElementById("errorPanel")
    );

</script>
