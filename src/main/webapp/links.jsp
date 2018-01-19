<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div class="grid_12">
    <header class="page">
        <h1>Fishing Websites</h1>
    </header>
</div>
<div class="clear"></div>

<section style="display:block; float:left">

<div class="grid_9">
<s:if test="addedMessage != null">
    <div class="succes_msg"><s:property value="addedMessage" /></div>
</s:if>

    <h3>Latest Sites Added</h3>
    
    <s:if test="lastAddedWebsites.size == 0">
        <p>No websites have been added yet</p>
    </s:if>
    <s:else>
        <s:iterator value="lastAddedWebsites" var="website">
		    <div class="listing" style="border: 1px solid #888; padding: 2px 15px; margin: 5px 0px; background-color: #eee" >
		        <div>
		          <h4 style="margin:0px; float:left; display:inline-block;" ><s:property value="title" /></h4>
		          <p style="margin:0px; float:right; display:inline-block; "><s:property value="dateAdded" /></p>
		          <div class="clear"></div>
		        </div>
		        <p style="margin:3px"><s:property value="description" /></p>
		        <s:if test="followLink == true">
		          <p style="margin:3px"><a href="<s:property value="websiteUrl" />" target="_blank"><s:property value="websiteUrl" /></a></p>
		        </s:if>
		        <s:else>
		          <p style="margin:3px"><a href="<s:property value="websiteUrl" />" target="_blank" rel="nofollow"><s:property value="websiteUrl" /></a></p>
		        </s:else>
		    </div>
        </s:iterator>
    </s:else>
</div>

<div class="clear"></div>

<div class="grid_9">
    <hr style="margin: 10px 0px"/>
</div>

<div class="clear"></div>

<div class="grid_9">
    <h3 style="margin: 0px 0px 10px 0px">Categories</h3>
</div>

<div class="clear"></div>

<s:iterator value="rootWebsiteCategories" status="itStatus">
    <s:if test="#itStatus.count == 1 || #itStatus.count > 1 && #itStatus.modulus(4) == 1" >
	    <div class="grid_3" style="margin: 5px"> 
	    <ul class="list6">
    </s:if>
    
            <li><a href="<s:property value="urlAlias"/>"><s:property value="name" /></a> (<s:property value="websiteCount" />)</li>
            
    <s:if test="#itStatus.count > 1 && #itStatus.modulus(4) == 0">
        </ul>
        </div>
    </s:if>
</s:iterator>

<div class="clear"></div>

<div class="grid_9">
    <hr style="margin: 10px 0px"/>
</div>

<div class="clear"></div>

<div class="grid_9">
    <h3 style="margin: 10px 0px">How to add you own site...</h3>

    <ul class="tabs">
         <li><a href="#tab1">Step 1: Read this first</a></li>
         <li><a href="#tab2">Step 2: Link back to us</a></li>
         <li><a href="#tab3">Step 3: Add your site</a></li>
    </ul>
    <div class="tab_container">
	<div id="tab1" class="tab_content">
	    <p>We are quite selective about the websites accepted for our directory. Only websites which are relevant i.e. websites <b>related to 
            fishing</b> or of general interest to anglers will be accepted. Newly added websites are reviewed by a member of our team on a weekly basis 
            and will be removed if deemed unsuitable. Please take time to <b>give a good description</b> of the website you are adding. Failure to do 
            so might result in your listing being removed.</p>
	</div>
	<div id="tab2" class="tab_content">
	    <p>We would love it if you would link back to http://www.groovyfly.com</p>
	   
	    <h6>Simple Link 1</h6>
	    Sample: <p><b><a href="http://www.groovyfly.com" target="_blank">www.groovyfly.com</a></b>, supplies some of the best quality fishing flies you will find on the web. They have fly collections for your locality and plenty of fly fishing and related articles of interest.</p>
		<code>
		    &lt;p&gt;<br />
		    &nbsp;&lt;b&gt;&lt;a href="http://www.groovyfly.com" target="_blank"&gt;www.groovyfly.com&lt;/a&gt;&lt;/b&gt;<br />
		    &nbsp;, supplies some of the best quality fishing flies you will find on the web. They have fly collections for your locality and plenty of fly fishing and related articles of interest.<br />
		    &lt;/p&gt;
		</code>
		
	    <h6>Simple Link 2</h6>
        Sample: <p>Visit www.groovyfly.com for great <b><a href="http://www.groovyfly.com" target="_blank">fly fishing flies</a></b></p> 
        <code>
            &lt;p&gt;<br />
            &nbsp;Visit www.groovyfly.com for great<br />
            &nbsp;&lt;b&gt;&lt;a href="http://www.groovyfly.com" target="_blank"&gt;fly fishing flies&lt;/a&gt;&lt;/b&gt;<br />
            &lt;/p&gt;
        </code>
	</div>
    <div id="tab3" class="tab_content">
    
        <s:set name="errorMap" value="getFieldErrors()" />

		<s:if test="#errorMap.size > 0">
		  <div class="info_msg">Please note all the errors below and correct them.</div>
          <script>
              var showLastTab = true; // made a wee adjustment to scripts.js, as are not able t change tab as scripts.js runs at the end of the page load
             // window.location.hash = '#tab3';
              $(document).ready(function() {
//             	window.location.hash = '#tab3';
	              jQuery('html, body').animate({
	                  scrollTop: jQuery("#tab3").offset().top - 1200 
	              }, 1500);
              });
          </script>
		</s:if>
		
        <s:form id="add_site_form" action="fishing-websites-add" cssClass="form_place" method="post">
			<div>
			    <label for="category">Category: <em class="required">(Required)</em></label>
			    <s:select id="category" name="categoryId"  cssClass="chosen-select-deselect" style="width:350px;" list="rootWebsiteCategories" listValue="name" listKey="websiteCategoryId" headerKey="-1" headerValue=""/>
			</div>
			<s:iterator value="#errorMap['categoryId']">
                <div class="error_msg" style="margin:0px"><s:property /></div>
            </s:iterator>
            <div class="clear"></div>
            
			<div>
			    <label for="websiteUrl">URL: <em class="required">(Required)</em></label>
			    <s:textfield id="websiteUrl" name="websiteUrl" cssClass="i-format"/>
			</div>
			<s:iterator value="#errorMap['websiteUrl']">
                <div class="error_msg" style="margin:5px 0px"><s:property /></div>
            </s:iterator>
			
			<div>
			    <label for="title">Title: <em class="required">(Required)</em></label>
			    <s:textfield id="title" name="title" cssClass="i-format"/>
			</div>
			<s:iterator value="#errorMap['title']">
                <div class="error_msg" style="margin:5px 0px"><s:property /></div>
            </s:iterator>
            
			<div>
			    <label for="description">Description: <em class="required">(Required)</em></label>
			    <s:textarea id="description" rows="5" cols="20" name="description" cssClass="default"/>
			</div>              
            <s:iterator value="#errorMap['description']">
                <div class="error_msg" style="margin:5px 0px"><s:property /></div>
            </s:iterator>
            			
			<div>
			    <label for="backlinkUrl">Backlink Url:</label>
			    <small style="display:block">Please include a backlink to http://www.groovyfly.com if you want the nofollow attribute removed.</small>
			    <s:textfield id="backlinkUrl" name="backlinkUrl" cssClass="i-format"/>
			</div>
			<s:iterator value="#errorMap['backlinkUrl']">
                <div class="error_msg" style="margin:5px 0px"><s:property /></div>
            </s:iterator>
            
            <div>
                <label for="contactEmail">Contact email: <em class="required">(Required)</em></label>
                <s:textfield id="contactEmail" name="contactEmail" cssClass="i-format"/>
            </div>
            <s:iterator value="#errorMap['contactEmail']">
                <div class="error_msg" style="margin:5px 0px"><s:property /></div>
            </s:iterator>
            
			<div>
			    <input class="button large blue" type="submit" value="Submit" />
			    <div class="clear"></div>
			</div> 
        </s:form>
    </div> <!-- end tab 3 -->
  </div> <!-- end tab container -->
</div> <!-- end container -->

<div class="clear"></div>

</section>




