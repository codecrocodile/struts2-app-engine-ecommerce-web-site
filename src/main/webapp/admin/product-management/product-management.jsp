<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@ taglib prefix="sjc" uri="/struts-jquery-chart-tags"%>

<h1>Product Management</h1>
<div class="clear"></div>

<div style="width: 45%; float: left; margin-bottom: 30px;" class="statistics">
    <h3>Totals</h3>
    <table>
	    <s:iterator value="totals.entrySet()" >
	        <tr>
	            <td><s:property value="key" /></td>
	            <td><b><s:property value="value" /></b></td>
	        </tr>
	    </s:iterator>
    </table>
</div>

<div style="width: 45%; float: right; margin-bottom: 30px;" >
<h3>Stock Count Range to Product Count</h3>
<sjc:chart
        id="chartDate"
        cssStyle="width: 380px; height: 300px;"
        xaxisMin="0"
        xaxisMax="13"
        xaxisTick="[[1.5, '0'], [3.5, '< 6'] , [5.5, '7 - 12'], [7.5, '13 - 24'], [9.5, '25 - 48'], [11.5, '> 48']]"
    >
        <sjc:chartData
            label="Product Count"
            list = "stockCount2ProductCount"
            color="#990066"
            bars="{ show: true}"
        />
    </sjc:chart>
</div>

<div class="clear"></div>

<div style="width: 45%; float: left; margin-bottom: 30px;" class="statistics">
    <h3>Price Statistics</h3>
    <table>
        <s:iterator value="priceStatistics.entrySet()" >
            <tr>
                <td><s:property value="key" /></td>
                <td><b><s:property value="value" /></b></td>
            </tr>
        </s:iterator>
    </table>
</div>

<div style="width: 45%; float: right; margin-bottom: 30px;"  class="statistics">
    <h3>Grouping Shop Page Health</h3>
    <table>
        <s:iterator value="pageStatistics.entrySet()" >
            <tr>
                <td><s:property value="key" /></td>
                <td><b><s:property value="value" /></b></td>
            </tr>
        </s:iterator>
    </table>
</div>
     
     
<div class="clear"></div>

<div style="width: 45%; float: left; margin-bottom: 30px;" class="statistics">
    <h3>Products by Supplier</h3>
    <table>
        <s:iterator value="supplierStatistics.entrySet()" >
            <tr>
                <td><s:property value="key" /></td>
                <td><b><s:property value="value" /></b></td>
            </tr>
        </s:iterator>
    </table>
</div>

<div style="width: 45%; float: right; margin-bottom: 30px;"  class="statistics">
    <h3>Product Statuses</h3>
    <table>
        <s:iterator value="productStatusStatistics.entrySet()" >
            <tr>
                <td><s:property value="key" /></td>
                <td><b><s:property value="value" /></b></td>
            </tr>
        </s:iterator>
    </table>
</div>     