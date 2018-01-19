<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<h2>Admin Menu</h2>
<section>
    <ul>
        <li><a href="dashboard">Dashboard</a></li>
        <li>
            <a href="product-management">Product Management</a>
             <ul>
                <li><a href="products-list">Products</a></li>
                <li><a href="groupings-list">Groupings</a></li>
                <li><a href="categories-list">Categories</a></li>
                <li><a href="suppliers-list">Suppliers</a></li>
            </ul>
        </li>
    </ul>
</section>
