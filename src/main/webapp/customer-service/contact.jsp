<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="net.tanesha.recaptcha.ReCaptcha" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaFactory" %>

<s:set name="errorMap" value="getFieldErrors()" />

<div class="grid_12">
	<header class="page">
	    <h1>Contact </h1>
	    <s:if test="#errorMap.size > 0">
			 <div class="error msg">
			     <div class="error_msg"><strong>Problem: </strong>Please check the information messages below.</div>
			 </div>
		</s:if>
		<s:if test="successMessage != null">
             <div class="error msg">
                 <div class="note_msg"><strong>Note: </strong><s:property value="successMessage"/></div>
             </div>
        </s:if>
	</header>
</div>

<div class="clear"></div>
<section class="single_page">
	
	<section class="grid_12">
	    <iframe width="932" height="300" class="contact_map frameborder" src="http://maps.google.com/maps?num=1&amp;vpsrc=0&amp;hl=en&amp;ie=UTF8&amp;t=m&amp;z=8&amp;ll=55.765289,-4.174119&amp;output=embed"></iframe>
	</section>
	    
	<section class="grid_7">
	<div id="contact">              
	        
	<div id="message"></div>
	
	<script type="text/javascript">
		var RecaptchaOptions = {
		    theme : 'white'
		};
	</script>
	
	<s:form id="contact_form" action="contact-send-message" method="post">
	<fieldset>
		<div class="clear"></div>
		   <div class="input_label user"><label for="name">Name</label></div>
		   <s:textfield id="name" name="name" size="30" />
		   <s:iterator value="#errorMap['name']">
	        <div class="info_msg"><strong>Info: </strong><s:property /></div>
		   </s:iterator>
		   <div class="clear"></div>
		                
	       <div class="input_label user_email"><label for="email">Email</label></div>
	       <s:textfield id="email" name="email" size="30" />
	       <s:iterator value="#errorMap['email']">
	            <div class="info_msg"><strong>Info: </strong><s:property /></div>
	          </s:iterator>
	       <div class="clear"></div>
	       
	       <div class="input_label user_subject"><label for="subject">Subject</label></div>
	       <s:textfield id="subject" name="subject" size="30" />
	       <div class="clear"></div>
	       
	       <s:textarea id="comments" name="comments" cols="40" rows="3"></s:textarea>
	       <s:iterator value="#errorMap['comments']">
	            <div class="info_msg"><strong>Info: </strong><s:property /></div>
	          </s:iterator>
	       <div class="clear"></div>
	       
	       <div class="grid_7 alpha">
	           <div class="div_verify">
	               <%
	                   ReCaptcha c = ReCaptchaFactory.newReCaptcha("6Le1sdUSAAAAAJE0dKgQC5Z96JS5OGJU31_-YXrE", "6Le1sdUSAAAAADHrvyT11KneSdfLx68Acsqas8ql", false);
	                   out.print(c.createRecaptchaHtml(null, null));
	               %>
	           <s:iterator value="#errorMap['captcha']">
	              <div class="info_msg"><strong>Info: </strong><s:property /></div>
	           </s:iterator>
	           </div>
	           
	           <input type="submit" class="button white contact_submit" id="submit" value="Send your message" />
	       </div>
	    <div class="clear"></div>
    </fieldset>
	</s:form>            
	</div>
	</section><!-- End .grid_6 -->
	    
	<section class="grid_5">
	    <h2 class="margin_top_5">Contact Information</h2>
	    <p>We are happy to help with any inquires, but to save time you might want to check our <a href="faqs">FAQ's (frequently asked questions)</a> page first. If you can't find the answer there then feel free to send a message to us and we will get back to you as soon as possible.</p>
	    <div class="clear"></div>
	    
	    <h4>Scottish Office</h4>
	    <ul class="list13"> <%--<li>3 Aillort Place, East Mains, East Kilbride, South Lanarkshire, Scotland, G744LL</li></ul> --%><li>Dummy Address Here</li></ul> 
	    <ul class="list14"><li>(044) 781 435 7824</li></ul> 
	    <ul class="list15">
	       <li>
				<script>
				    <!-- BEGIN Script
				    function encryptAddress(){
				        var address, altText;
				        address = 'sup'; address += 'port@' + 'groo' + 'vyfly' + '.com';
				        altText = 'email G'; altText += 'roovy F' + 'ly supp' + 'ort team';
				        document.write('<a hre' + 'f=mai' + 'lto:' + address + '>' + altText + '</a>');
				    }
				    encryptAddress();
				    //END -->
				</script>
				<noscript>email Groovy Fly support team (support-AT-groovyfly-DOT-com)</noscript>
	       </li></ul> 
	    <div class="clear"></div>
	</section><!-- End .grid_6 -->
</section><!-- End of .single_page -->