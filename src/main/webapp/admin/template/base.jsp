<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@ taglib prefix="sjc" uri="/struts-jquery-chart-tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<!DOCTYPE HTML>
<html lang="en">
<head>
<title><tiles:getAsString name="page-title"/></title>
<meta charset="utf-8">
<meta http-equiv="pragma" content="no-cache"> 
<meta http-equiv="expires" content="0">

<link rel="stylesheet" type="text/css" href="/admin/css/style.css">
<link rel="stylesheet" type="text/css" href="css/skins/orange.css" title="orange">
<link rel="stylesheet" type="text/css" href="css/uniform.default.css">
<link rel="stylesheet" type="text/css" href="css/demo_table_jui.css">

<tiles:insertAttribute name="customPageCss" />

<%-- this will add in the query files it needs from the plugin jar so no need add jquery from js directory --%>
<sj:head jqueryui="true" jquerytheme="le-frog"/>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/jquery.uniform.min.js"></script>
<script type="text/javascript" src="js/jquery.flot.min.js"></script>
<script type="text/javascript" src="js/custom.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>

<script type="text/javascript" src="../ckeditor/ckeditor.js"></script>
<script type="text/javascript" src="../ckeditor/adapters/jquery.js"></script>

</head>

<body class="fluid">

<header id="top">

    <tiles:insertAttribute name="header" />
    
</header>

<nav id="topmenu">

    <tiles:insertAttribute name="top-nav" />
    
</nav>

<section id="content">
    <section class="container_12 clearfix">
        
        <section id="main" class="grid_9 push_3">
            <article id="dashboard">
            
            
            
                 <tiles:insertAttribute name="main" />
                 
                 <div class="clear"></div>
            </article>
        </section>

        <aside id="sidebar" class="grid_3 pull_9">
            <div class="box menu">
            
                <tiles:insertAttribute name="side-nav" />
                
            </div>
        </aside>
        
    </section>
</section>

<footer id="bottom">
    <section class="container_12 clearfix">
        <div class="grid_12 alignright">
        
            <tiles:insertAttribute name="footer" />
            
        </div>
    </section>
</footer>

<sj:dialog id="dialog" autoOpen="false" modal="false" title="Groovy Fly"></sj:dialog>

<tiles:insertAttribute name="customPageJs" />

</body>
</html>