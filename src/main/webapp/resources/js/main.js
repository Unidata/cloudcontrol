$(document).ready(function() {

    $('.tooltip').tooltipster({
        delay: 0,
        theme: 'tooltipster-light',
        trigger: 'hover',
		position: 'top-left',
		contentAsHTML: 'true'
    });
       
    $('ul.nav').dropit({ action: 'click', triggerEl: 'b' });

    $('.accordion h3').click(function() {
       $(this).toggleClass('expand');
	   $(this).next().toggle();
       return false;
    }).next().hide();	   

    var $selected = $('.accordion a[href=\'' + window.location.pathname + '\']');
    $selected.removeAttr('href');
    $selected.parent('li').toggleClass('selected').parent('ul').toggle();
	$selected.parents('ul').prev().toggleClass('expand');

    

    /* toggle */
    toggleTheThings();
    
	/* zebra stripes */
    $("table tbody tr:nth-child(odd)").addClass("odd");
    $("table.list").tablesorter();
    $("table.list").bind("sortEnd",function() { 
        $("table tbody tr").removeClass("odd");
        $("table tbody tr:nth-child(odd)").addClass("odd");
    }); 
	

});

function toggleTheThings () {
	
    $(".hideandshow").hide();

    function runEffect(item){ 
            var corresponding = $(item).attr('id') + "Section";            
            $("#"+ corresponding).toggle();
            $(item).toggleClass("expand");
            var contents = $(item).html();
            if (contents.match('show')) {
                $(item).html(contents.replace('show','hide'));
            } else {
                $(item).html(contents.replace('hide','show'));
            }
        };    

    $(".toggle").click(function () {  
        runEffect($(this));
        return false;
    });   
} 





