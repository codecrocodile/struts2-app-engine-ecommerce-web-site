<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>

<!DOCTYPE HTML>
<html lang="en">
<head>
<title>Groovy Fly Admin Login</title>
<meta charset="utf-8">
<link rel="stylesheet" type="text/css" href="css/style.css">

<sj:head jqueryui="true" jquerytheme="le-frog"/>

</head>
<body>

<header id="top">
    <div class="container_12 clearfix">
        <div id="logo" class="grid_12">
            <h1>Groovy Fly Admin</h1>
        </div>
    </div>
</header>

<div id="login" class="box">
    <h2>Login</h2>
    <section>
                
        <s:iterator value="getFieldErrors()">
            <div class="error msg">
                <s:property value="getValue()[0]"/>
            </div>
        </s:iterator>
                
        <s:form id="login_form" action="login"><!-- action is just the plain name without.action - this will be filled in for you -->
            <dl>
                <dt><label for="username">Username</label></dt>
                <dd>
                    <s:textfield name="username" id="username" />
                </dd>
            
                <dt><label for="password">Password</label></dt>
                <dd>
                    <s:password name="password" id="password" />
                </dd>
            </dl>
            
            <p>
                <s:submit value="Login" id="loginbtn" cssClass="button gray" />
                <a id="forgot" href="#">Forgot Password?</a>
            </p>
        </s:form>
    </section>
</div>

</body>
</html>