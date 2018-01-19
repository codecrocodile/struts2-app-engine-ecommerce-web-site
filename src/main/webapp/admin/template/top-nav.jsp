<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div class="container_12 clearfix">

    <div class="grid_12">
    
        <ul id="mainmenu">
            <li class="current"><a href="/admin/dashboard">Dashboard</a></li>
        </ul>
        
        <ul id="usermenu">
            <li><a href="" class="inbox">Inbox (0)</a></li>
            <li><a href='<s:property value="logoutUrl" />'>Logout</a></li>
        </ul>
        
    </div>
    
</div>