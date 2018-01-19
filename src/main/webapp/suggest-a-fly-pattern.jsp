<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div class="grid_12">
    <header class="page">
        <h1>Suggest a Fly Pattern</h1>
    </header>
</div>
<div class="clear"></div>

<div class="grid_9">

<p>Here at Groovy Fly we always take suggestions, praise and criticisms from you, our customer, very seriously. As part of our business 
growth plan we are always looking for new products and developing services based on your suggestions.</p>
<p>We would love to hear your suggestions about what flies we should sell. Unlike many other online shops, we
don't believe in stocking hundreds, if not thousands, of fly patterns that catch anglers and not the fish. We would love 
it if you would take the time to suggest patterns that you love to use. You might even see them on our product listings very soon.</p>


<s:set name="errorMap" value="getFieldErrors()" />

<s:if test="#errorMap.size > 0">
  <div class="info_msg">Please note all the errors below and correct them.</div>
</s:if>
        
<s:form id="add_site_form" action="suggest-a-fly-pattern-submitted" cssClass="form_place" method="post">  

    <s:token />
    <s:hidden id="XCoord" name="XCoord" />
    <s:hidden id="YCoord" name="YCoord" />

    <p><b style="font-size: 18px">Step One</b> - choose your fishing location (<b>right mouse button click to place marker</b>).<p>
    <div>
        <div id="map-canvas" style="width: 100%; height: 400px;"></div> 
    </div>
    <s:iterator value="#errorMap['XCoord']">
        <div class="error_msg" style="margin:5px 0px"><s:property /></div>
    </s:iterator>
    <s:iterator value="#errorMap['YCoord']">
        <div class="error_msg" style="margin:5px 0px"><s:property /></div>
    </s:iterator>
    
    <p style="padding-top: 20px"><b style="font-size: 18px">Step Two</b> - tell us the fly pattern(s) you love to use.<p>
    <div>
        <label for="patternNames[0]">Fly Pattern(s):</label>
        <s:if test="patternNames == null || patternNames.length == 0">
            <s:textfield id="patternNames[0]" name="patternNames[0]" cssClass="i-format autoComp" maxlength="50"/>
        </s:if>
        <s:else>
            <s:iterator status="stat" value="patternNames"> 
                <s:set value="patternNames" var="name" />
                <s:textfield id="patternNames[%{#stat.index}]" name="patternNames[%{#stat.index}]" cssClass="i-format autoComp" maxlength="50" value="%{toString()}" />
            </s:iterator>
        </s:else>
        
        <input id="addBtn" style="float: right; margin: 10px 0px 0px 5px; " class="button small green" type="button" value="Add Another" />
        <input id="removeBtn" style="float: right; margin: 10px 0px 0px 5px; display: none;" class="button small green" type="button" value="Remove Last" />
        <div class="clear"></div>
    </div>
    <s:iterator value="#errorMap['patternNames']">
        <div class="error_msg" style="margin:5px 0px"><s:property /></div>
    </s:iterator>
    
    <p style="padding-top: 20px"><b style="font-size: 18px">Step Three</b> (optional) - tell us your email address and we will contact you if we start stocking the pattern.<p>
    <div>
        <label for="name">Name: <em class="required">(Optional)</em></label>
        <s:textfield id="name" name="name" cssClass="i-format" maxlength="50"/>
    </div>
    <s:iterator value="#errorMap['name']">
        <div class="error_msg" style="margin:5px 0px"><s:property /></div>
    </s:iterator>
    
    <div>
        <label for="email">Email: <em class="required">(Optional)</em></label>
        <s:textfield id="email" name="email" cssClass="i-format" maxlength="150"/>
    </div>
    <s:iterator value="#errorMap['email']">
        <div class="error_msg" style="margin:5px 0px"><s:property /></div>
    </s:iterator>
    
    <div>
        <input class="button large blue" type="submit" value="Submit" />
        <div class="clear"></div>
    </div> 
</s:form>


</div>
<!-- replaced with the google loader, need to check if I need to use my key with the loader api???? 
<script type="text/javascript"
	src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCSLPIHvdc85_JczO57hSqBXOJ63BsK3JQ&sensor=false"> 
</script>
 -->
<script type="text/javascript" src="http://www.google.com/jsapi"></script>
<script type="text/javascript">
      google.load("maps", "3",  {callback: initialize, other_params:"sensor=false"});
      
      function initialize() {
 	    var zoom = 2;
 	    var latlng = new google.maps.LatLng(37.4419, -100.1419);
    	  
  	    if (google.loader.ClientLocation) {
  	        zoom = 5;
  	        latlng = new google.maps.LatLng(google.loader.ClientLocation.latitude, google.loader.ClientLocation.longitude);
  	    }
  	    
        var mapOptions = {
          center: latlng,
          zoom: zoom,
          mapTypeId: google.maps.MapTypeId.ROADMAP,
          panControl: false
        };
        var map = new google.maps.Map(document.getElementById("map-canvas"), mapOptions);
        
        
        
        google.maps.event.addListener(map, 'rightclick', function(event) {
            placeMarker(event.latLng);
            
            $("#XCoord").val(event.latLng.lat());
            $("#YCoord").val(event.latLng.lng());
        });
        
        var marker = null;
        function placeMarker(location) {
        	if (marker !== null) {
        		marker.setMap(null);	
        	}
            marker = new google.maps.Marker({
       	      position: location,
       	      map: map
       	    });
        }

        var XCoord = ${XCoord};
        var YCoord = ${YCoord};
        
        if (XCoord !== -900 && YCoord !== -900) {
        	placeMarker(new google.maps.LatLng(XCoord, YCoord));       
        }

      }
      //google.maps.event.addDomListener(window, 'load', initialize);
      
      function getFormattedLocation() {
    	    if (google.loader.ClientLocation.address.country_code == "US" &&
    	      google.loader.ClientLocation.address.region) {
    	      return google.loader.ClientLocation.address.city + ", " 
    	          + google.loader.ClientLocation.address.region.toUpperCase();
    	    } else {
    	      return  google.loader.ClientLocation.address.city + ", "
    	          + google.loader.ClientLocation.address.country_code;
    	    }
    	  }
      
      var noBtns = 1;
      
      $("#addBtn").click(function() {
    	   var str = '<s:textfield name="patternNames[%s]" cssClass="i-format autoComp" maxlength="50"/>'.replace('%s', noBtns);
    	   $(str).insertBefore('#addBtn');
    	   noBtns++;
    	   $("#removeBtn").css('display', 'block');
    	   
           $( ".autoComp" ).autocomplete({
               source: availableTags,
               minLength: 3
           });
    	   
    	   return false;
      });
      $("#removeBtn").click(function() {
          $('#addBtn').prev().remove();
          noBtns--;
          if (noBtns < 2) {
        	  $("#removeBtn").css('display', 'none');  
          }
          return false;
     });
      
      var availableTags = [<s:property value="flyPatternList" />];
      
      $( ".autoComp" ).autocomplete({
          source: availableTags,
          minLength: 3
      });
      

      
</script>

