/* ====== SEARCH FORM ===== */	
$("#searchform #s").focus(function(){
	$(this).animate({width:'270px'}, 250); /* on focus, increasing the input width of search to left side*/
});
	 
$("#searchform #s").focusout(function(){
	$(this).animate({width:'187px'}, 250);  /* on focus, decreasing the input width of search to left side*/
});


/* ====== TABS ====== */
$(document).ready(function() {

	//When page loads...
	$(".tab_content").hide(); //Hide all content
	
	if (typeof showLastTab != "undefined" && showLastTab === true) {
		$("ul.tabs li:last").addClass("active").show(); //Activate first tab
		$(".tab_content:last").show(); //Show first tab content
	} else {
		$("ul.tabs li:first").addClass("active").show(); //Activate first tab
		$(".tab_content:first").show(); //Show first tab content		
	}


	//On Click Event
	$("ul.tabs li").click(function() {

		$("ul.tabs li").removeClass("active"); //Remove any "active" class
		$(this).addClass("active"); //Add "active" class to selected tab
		$(".tab_content").hide(); //Hide all tab content

		var activeTab = $(this).find("a").attr("href"); //Find the href attribute value to identify the active tab + content
		$(activeTab).fadeIn(); //Fade in the active ID content
		return false; // Prevent browser from visiting `#`
	});

});


/* ====== ACCORDION ====== */
$(document).ready(function() {
	 
	$('.accordion_button').click(function() {
	
		$('.accordion_button').removeClass('acdn_on');
	 	$('.accordion_container').slideUp('normal');
		
		if($(this).next().is(':hidden') == true) {
			$(this).addClass('acdn_on');
			$(this).next().slideDown('normal');
		 } 
		  
	 });
	$('.accordion_button').mouseover(function() {
		$(this).addClass('acdn_over');
		
	}).mouseout(function() {
		$(this).removeClass('acdn_over');										
	});
		
	$('.accordion_container').hide();
	
	/* message boxes
	------------------------------------------------------------------------- */
	$('.info_msg,.note_msg,.tip_msg,.error_msg,.succes_msg').click(function() {
	    $(this).fadeTo('slow', 0);
	    $(this).slideUp(500);
	});

});


/* ====== TOGGLE  ====== */
$(".toggle_content").hide();

$(".toggle_block h4").click(function(){
	$(this).toggleClass("tgg_close").next().slideToggle("medium");
});


/* ====== MENU  ====== */
var site = function() {
	this.navLi = $('#nav li').children('ul').hide().end();
	this.init();
};

site.prototype = {
 	
 	init : function() {
 		this.setMenu();
 	},
 	
 	setMenu : function() {
 	
 	$.each(this.navLi, function() {
 		if ( $(this).children('li ul li ul')[0] ) {
 			$(this)
 				.append('<span />')
 				.children('span')
 					.addClass('menuChildren');
 		}
 	});
 	
 		this.navLi.hover(function() {
 			// mouseover
			$(this).find('> ul').stop(true, true).slideDown('medium', 'easeOutCubic');
			$(this).children('a:first').addClass("hov");
 		}, function() {
 			// mouseout
 			$(this).find('> ul').stop(true, true).hide(); 
			$(this).children('a:first').removeClass("hov");			
		});
 		
 	}
 
};
new site();

$("#nav ul a").hover(function() {
	$(this).animate({ backgroundColor: "#eee" }, 300);
},function() {
	$(this).animate({ backgroundColor: "#ddd" }, 300);
});


/*=========== SIDEBAR MENU =========*/
$(".vertical-menu li a").hover(function() {
	$(this).animate({ backgroundColor: "#eee" }, 200);
},function() {
	$(this).animate({ backgroundColor: "#ddd" }, 200);
});


/* =========== FOOTER MENU ============ */
var fadeDuration = 150; //time in milliseconds
      
      $('.vnav li a').hover(function() {
        $(this).animate({ paddingLeft: '10px' }, fadeDuration);
      }, function() {
        $(this).animate({ paddingLeft: '0' }, fadeDuration); 	
      });


/* ========= FILTERABLE PORTFOLIO ============= */
$(document).ready(function() {
	$('ul#filter a').click(function() {
		$(this).css('outline','none');
		$('ul#filter .current').removeClass('current');
		$(this).parent().addClass('current');
		
		var filterVal = $(this).text().toLowerCase().replace(' ','-');
				
		if(filterVal == 'all') {
			$('ul#portfolio li.hidden').fadeIn('slow').removeClass('hidden');
		} else {
			
			$('ul#portfolio li').each(function() {
				if(!$(this).hasClass(filterVal)) {
					$(this).fadeOut('normal').addClass('hidden');
				} else {
					$(this).fadeIn('slow').removeClass('hidden');
				}
			});
		}
		
		return false;
	});
});


 /* ========== PORTFOLIO IMAGE HOVER ======== */
$(function() {
	$('.img_pf_hover').hover(function(){
		$('img',this).animate({"opacity": "0.6"},{queue:true,duration:200});
		
	}, function() {
		$('img',this).animate({"opacity": "1"},{queue:true,duration:300});
	});
});	

 /* ========== FLICKR IMAGE HOVER ======== */
$(function() {
	$('.flickr_photos img').hover(function(){
		$(this).animate({"opacity": "0.6"},{queue:true,duration:200});
		
	}, function() {
		$(this).animate({"opacity": "1"},{queue:true,duration:300});
	});
});

/* =========== PRICE BOX HOVER ANIMATION =========== */
$(function() {
	$('.price_box .collumn').hover(function(){
		$(this).animate({marginTop:'3px'},{queue:false,duration:300});
	}, function(){
		$(this).animate({marginTop:'10px'},{queue:false,duration:300});
	});
});

/* ======== FOOTER SOCIAL ICONS ======*/
$(function() {
	$('.social a').hover(function(){
		$(this).animate({"opacity": "0.6"},{queue:true,duration:200});
		
	}, function() {
		$(this).animate({"opacity": "1"},{queue:true,duration:300});
	});
});

/* ======== FORMS ======*/

$(".chosen-select").chosen(); 
$(".chosen-select-deselect").chosen(
		{'allow_single_deselect':true}
); 
                        

