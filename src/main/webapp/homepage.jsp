<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<div class="grid_12">
    <header class="page">
        <h1>Flies for Fishing</h1>
    </header>
</div>
<div class="clear"></div>

<section id="blog">

    <div class="grid_9">
      <p>We're <b>mad about fishing</b>! Are you?</p>
    
      <p>What a wonderful pastime we share. It's not just about catching fish, it's about being out there enjoying our free time in surroundings
      that take away the stress of everyday life. We hope we can, in a small way, help you make the most of your precious time by providing you with the
      <b>fly fishing fly</b> that will catch your next big fish.</p>
      
      <p>It's not just fishing gear that makes one successful. In our experience, knowledge is the greatest asset any angler can have. We hope to provide some of
      this in our <a href="<tiles:getAsString name="rel_path" ignore="true" />fishing-articles/fishing-articles-homepage">fishing articles</a>. If you're here for the first time, then welcome. We know this might seem like just another website
      to you, but to us it's an extension of our love for the sport and our unending desire to be the best <b>fly fishing shop</b> on the web.</p>
    </div>
    <div class="clear"></div>
    
	<div class="grid_9 placeslider">
	    <ul id="roundabt">
	        <li id="rab1"><img src="images/not-open-yet.png" alt="not-yet-open-note" /></li>
	        <li id="rab2"><img src="images/read-our-fishing-articles.png" alt="read-our-fishing-articles-note" /></li>
	        <li id="rab3"><img src="images/looking-for-fishing-fly-suppliers.png" alt="were-looking-for-fly-tiers-note" /></li>
	    </ul>
	    <div class="clear"></div>
	</div>
	<div class="clear"></div>
	
    <hr />

    <div class="grid_2 alpha">
        <img src="images/icons/2.png" alt="world map" />
    </div>
    <div class="grid_7 omega">
        <h3>Fly Fishing the World</h3>
        <p>No matter where you are in the world there is some commonality in the way we fish and what flies we use. We have tried 
        to design and stock a range that can be used in wide variety of places, situations and times of the year. Of course, 
        locality can demand that we be more selective. We have created a <b>collection of flies</b> which will hopefully 
        enable you to make a more informed decision on what to buy for your location.</p>
        <div class="clear"></div>
        <ul class="list7 grid_7 alpha"> 
            <s:property value="countryListItemsHtml" escapeHtml="false"/>     
        </ul> 
    </div>
    <div class="clear"></div>
</section> 

