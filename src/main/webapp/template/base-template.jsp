<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" dir="ltr" lang="en-US"> 
<head>
    <title><s:property value="page.title"/></title>
    
    <link rel="shortcut icon" href="/favicon.ico" />
    <meta http-equiv="content-type" content="text/html;charset=UTF-8" />
    <meta charset="UTF-8" />
    <meta name="description" content="<s:property value="page.metaDescription"/>" />
    <meta name="keywords" content="<s:property value="page.metaKeywords"/>" />
            
    <!--========= STYLES =========-->
    <link rel="stylesheet" href="/css/reset.css" />
    <link rel="stylesheet" href="/css/grid_12.css" />
    <link rel="stylesheet" href="/css/colorbox.css" />
    <link rel="stylesheet" href="/css/jquery.rating.css" />
    
    <link rel="stylesheet" href="/css/uniform.default.css">
    <link rel="stylesheet" href="/css/chosen.css">
    
    <link rel="stylesheet" href="/css/cookiecuttr.css" />
    <link rel="stylesheet" href="/css/style.css" />
    <link rel="stylesheet" href="/css/config.css" />
    

    
    <!--[if IE 7]><link rel="stylesheet" href="/css/ie7.css" /><![endif]-->
    <!--[if IE 8]><link rel="stylesheet" href="/css/ie8.css" /><![endif]-->
    <style>
        <tiles:insertAttribute name="customPageCss" ignore="true" />
    </style>
    
    <!--============ JQUERY =============-->
    <%-- this will add in the query files it needs from the plugin jar so no need add jquery from js directory --%>
    <sj:head jqueryui="true" jquerytheme="pepper-grinder" loadFromGoogle="true" /> <%-- it seems that we need to load from google or loadatonce to get full code and get autocomplete to work --%>
    <script src="/js/jquery.rating.pack.js" type="text/javascript"></script>
    <script src="/js/jquery.animate-colors-min.js" type="text/javascript"></script>
    <script src="/js/jquery.colorbox.js" type="text/javascript"></script>
    <script src="/js/jquery.easing.1.3.js" type="text/javascript"></script>
    <script src="/js/jquery.roundabout.js" type="text/javascript"></script>
    <script src="/js/cufon-yui.js" type="text/javascript"></script>
    <script src="/js/qlassik.cufonfonts.js" type="text/javascript"></script>
    <script src="/js/scripts_call_head.js" type="text/javascript"></script>
    <script src="/js/jquery.keyfilter-1.7.min.js" type="text/javascript"></script>
    <script src="/js/jquery.blockui.2.65.js" type="text/javascript"></script>
    <script src="/js/jquery.cookie.js" type="text/javascript"></script>
    <script src="/js/jquery.cookiecuttr.js" type="text/javascript"></script>
    <script src="/js/chosen.jquery.min.js" type="text/javascript"></script>
    
    <script type="text/javascript">
    
        $(document).ready(function () {
            // activate cookie cutter
            $.cookieCuttr( {
                cookieDeclineButton: true,
                cookieAnalytics: false,
                cookiePolicyLink: '/privacy-policy' ,
                cookieMessage: 'We use cookies on this website to enhance your experience, you can read about them in our <a href="{{cookiePolicyLink}}" title="read about our cookies">Privacy Policy</a>. '
            });
            
            if (jQuery.cookie('cc_cookie_decline') == "cc_cookie_decline") {
               
            } else {
                
            }
        }); 
    
        $('#roundabt li').focus(function() {
                var useText = (typeof descs[$(this).attr('id')] != 'undefined') ? descs[$(this).attr('id')] : '';
                $('#description').html(useText).fadeIn(200);
            }).blur(function() {
                $('#description').fadeOut(100);
        });
            
        $(document).ready(function() {
            $('ul#roundabt').roundabout({
                easing: 'easeInOutBack',
                duration: 1000
            });
        });
    </script>
                    
    <!--=== ENABLE HTML5 TAGS FOR IE ===-->
    <!--[if IE]>
    <script src="/js/html5.js"></script>
    <![endif]-->
    
</head>

<body>
    <header class="main_header">
        <section class="container">
            <div class="top">
                <a class="logo" href="/homepage"></a>
            </div>
        </section>
        <div class="border1"></div>
    </header>
    
    <section id="wrap">
    
        <div class="top_bg"></div>
        
        <nav id="horizontal">
            <ul id="nav">
                <li><a href="/homepage">Home</a></li>
                <li><a href="/customer-services/customer-service-homepage">Customer Services</a>
                    <ul>
                        <li><a href="/customer-services/terms-of-sale">Terms of Sale</a></li>
                        <li><a href="/customer-services/delivery-rates-and-policy">Delivery Rates &amp; Policy</a></li>
                        <li><a href="/customer-services/returns-policy">Returns Policy</a></li>
                        <li><a href="/customer-services/faqs">FAQ's</a></li>
                        <li><a href="/customer-services/contact">Contact</a></li>                    
                    </ul>
                </li>
                
                <li><a href="/fishing-articles/fishing-articles-homepage">Fishing Articles<span></span></a>
                    <ul>
                        <li><a href="/fishing-articles/groovy-fly-fishing-pages">Fly Fishing for Beginners</a></li>
                        <li><a href="/fishing-articles/groovy-fly-fishing-pages">Anglers Enotmology</a></li>
                        <li><a href="/fishing-articles/groovy-fly-fishing-pages">The Angling Chef</a></li>
                    </ul>
                </li>
                
                <li><a id="shopping-cart-subtotal" href="/shop/shopping-cart-page"><img src="/images/fugue/shopping-basket--arrow-green.png" alt="shopping basket"/> Checkout: <s:property value="shoppingCart.getSubTotalString()" />  |  Items: <s:property value="shoppingCart.itemCount" /></a></li>
            </ul>
        </nav><!-- End of menu -->
        
<!--         <nav id="horizontal-basket"> -->
<!--             <ul id="nav"> -->

<!--             </ul> -->
<!--         </nav>End of menu -->

        <section class="container layout">
            
            <tiles:insertAttribute name="main" />
            
            <tiles:insertAttribute name="sidebar" ignore="true" />
            
        </section> <!-- End section .container layout -->
    
    </section> <!-- End section #wrap -->
    
    <footer class="main_footer">
        <section class="container">
            <div class="grid_3">
                <h3 class="ft_title">Latest Fly Fishing Articles</h3>
                <ul class="vnav">
		            <li><a href="#">Enotmology and Taxonomy</a></li>
		            <li><a href="#">Nymph Fishing</a></li>
		            <li><a href="#">When Nothing Bites</a></li>
		            <li><a href="#">How to Start Fly Fishing</a></li>
                </ul>
            </div>
            
            <div class="grid_3">
		        <h3 class="ft_title">The Angling Chef</h3>
                <ul class="vnav">
		            <li><a href="#">How to fillet a trout or salmon</a></li>
		            <li><a href="#">Old fashioned Potted Salmon</a></li>
		            <li><a href="#">Smoked Trout Pat&eacute;</a></li>
		            <li><a href="#">Trout with Almonds</a></li>
                </ul>
            </div>
            
            <div class="grid_6">
                <h3 class="ft_title">Keep Up-to-date</h3>
                <div class="social">
                    <a href="/rss" class="feed" target="_blank"></a>
                </div>
                <p>We're not big on social networking, but we would like you to be able to keep up-to-date on what's happening with the Groovy Fly site, so why not subscribe to our feed or our bi-annual newsletter.</p>
                <div class="newsletter">
                    <div class="subscribe_text">Subscribe to our bi-annual newsletter</div>
                    <s:form id="subscr_form" namespace="/" action="subscriber-submit">
                        <s:textfield name="email" cssClass="subscr_email" placeholder="Enter your email here"/>
                        <sj:submit id="subscr_submit" 
                         value="Subscribe" 
                         cssClass="button green newsletter_submit" 
                         resetForm="true" 
                         dataType="json"
                         onSuccessTopics="complete"
                         onErrorTopics="error"
                         targets="XXX"
                         indicator="indicator"/>
                        <img id="indicator" src="/images/indicator-brown-bg.gif" alt="Loading..." style="display: none;"/> 
                    </s:form>
                  </div>
              </div>
        </section>
        
    <section class="mini_footer">
        <div class="container">
            <a class="logo_on_footer" href="/homepage"></a>
            <nav id="ft_links">
                <ul id="nav_footer">
                    <li><a href="/homepage">Home</a></li>
                    <li><a href="/about">About</a></li>
                    <li><a href="/terms">Terms</a></li>
                    <li><a href="/privacy-policy">Privacy Policy</a></li>
                    <li><a href="/fishing-websites-list">Links</a></li>
                    <li><a href="/fly-fishing-suppliers/fly-fishing-suppliers-homepage">Suppliers</a></li>
                    <li><a href="/rss" target="_blank">RSS</a></li>
                    <!-- <li><a href="/sitemap">Sitemap</a></li>  -->
                </ul>
            </nav>
        </div>
    </section>
    
    <div class="border2"></div>

    </footer> <!-- End .main_footer -->
    
    <script type="text/javascript"> Cufon.now(); </script><!-- this stops the flicker -->
    <sj:dialog id="dialog" autoOpen="false" modal="false" showEffect="clip" hideEffect="clip" title="Groovy Fly"></sj:dialog>
    
    <!-- JAVASCRIPTS -->
    <script src="/js/scripts.js" type="text/javascript"></script>
    <script type="text/javascript">
        $.subscribe('complete', function(event, data) {
            var response = JSON.parse(event.originalEvent.request.responseText);
            $('#dialog').html(response.message);
            $('#dialog').dialog('open');
        });
        
        $.subscribe('error', function(event, data) {
            $('#dialog').html("There seems to have been an problem subscribing you to the newsletter. Please comeback and try again later.");
            $('#dialog').dialog('open');
        });
        
    </script>
       
    <tiles:insertAttribute name="customPageJs" />
</body>
</html>