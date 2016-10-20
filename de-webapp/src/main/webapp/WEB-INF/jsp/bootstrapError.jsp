<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    min-height: 220px;
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

    <c:if test="${not empty status_code}">
        <h1 class="redHeader">${status_code} Error</h1>
    </c:if>
    <h1 class="blueHeader">Oops... That wasn't supposed to happen</h1>

    <p>
    The CyVerse team tracks these errors automatically, but feel free to <a href="mailto:support@cyverse.org">contact support</a> if the error persists.
        In the meantime, you can:
        <li>Check out our <a href="${status_url}">status</a> page </li>
        <li>Subscribe to our <a href="${newsletter_url}">newsletter</a></li>
        <li>Follow us on <a href="${twitter_url}">Twitter</a></li>
    </p>

    <hr />

    <div style="float: left"><a href="${login_url}">Go back to ${app_name}</a></div>
    <div style="float: right"><a href="http://www.cyverse.org">CyVerse Home Page</a></div>

    </div>

</body>
</html>
