$(document).ready(function() {

    // automagically make any image alt a tooltip
    /*
    $(document).tooltip({ items: "img[alt]",
        content: function() { return $(this).attr("alt") } 
    });
    
    */
    
 
    $('ul.nav').dropit({ action: 'mouseenter' });


    

    
    toggleTheThings();
    
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
            $(item).toggleClass("collapse");
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





