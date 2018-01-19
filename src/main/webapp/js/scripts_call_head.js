Cufon.replace('h1', { fontFamily: 'Qlassik Bold', hover: true }); 
Cufon.replace('h2, h3, h4, h5, h6', { fontFamily: 'Qlassik Medium', hover: true }); 

$(document).ready(function(){
	//Examples of how to assign the ColorBox event to elements
	$("a[class='crbox']").colorbox({photo : true});
	$(".video-crbox").colorbox({iframe:true, innerWidth:640, innerHeight:510});
	$(".iframe-crbox").colorbox({width:"80%", height:"80%", iframe:true});
	
});