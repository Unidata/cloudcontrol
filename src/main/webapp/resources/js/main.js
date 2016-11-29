$(document).ready(function() {

    /* tool tips */
    $('.tooltip').tooltipster({
        delay: 0,
        theme: 'tooltipster-light',
        trigger: 'hover',
        position: 'top-left',
        contentAsHTML: 'true'
    });
       
    /* horizontal nav drop down menu */
    $('ul.nav').dropit({ action: 'click', triggerEl: 'b' });

    /**/
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
    
    /* AJAX requests */
    $("button").on( "click", function() {
        imageAjaxRequest($(this));
    });
  
    $(document).ajaxSend(function( event, jqxhr, settings ){
        if (!settings.url.includes("status")) {
            $("#wait").css("display", "block");
        }
    });
    $(document).ajaxComplete(function( event, jqxhr, settings ){
        $("#wait").css("display", "none");
    });
    
    /* image status refresh */
    setInterval(function () {
        refreshData();
    }, 10000); // refresh every 30 seconds

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



function imageAjaxRequest(button) {
    var imageId = $(button).attr("id");
    var action = $(button).attr("class");
	var url = "/dashboard/docker/image/" + imageId + "/" + action;
    $.ajax({
        url: url
    })
    .done(function(data){
        if (action === "inspect") {
            $("#dialog").dialog({
                modal: true,
                open: function () {
                    $(this).load(url);
                },   
                title: "Inspection Details for Image " + imageId,
                height: 300,
                width: 800,
                buttons: { 
                    Close: function() {
                        $(this).dialog("close");
                    }
                }
            });
        } else {
            if (!data.includes("Error")){
                if (action === "start"){
                    $(button).html("Stop Image");
                    $(button).attr("class", "stop");
                    $(button).prev().children('span').attr('class', 'active');
                } 
				if (action === "stop"){
                    $(button).html("Start Image");
                    $(button).attr("class", "start");
                    $(button).prev().children('span').attr('class', 'inactive');
                }
				if (action === "show"){
                    $(button).html("Hide from Users");
                    $(button).attr("class", "hide");
                    $(button).prev().children('span').attr('class', 'active');
                }
				if (action === "hide"){
                    $(button).html("Show to Users");
                    $(button).attr("class", "show");
                    $(button).prev().children('span').attr('class', 'inactive');
                }
                $(button).prev().children('span').html(data);
				var selector;
				if (action === "start" || action === "stop") {
                    selector = "tr#" + imageId.replace(":", "") + "Toggle td:nth-child(3)"
			    }
				if (action === "show" || action === "hide") {
                    selector = "tr#" + imageId.replace(":", "") + "Toggle td:nth-child(4)"
			    }
				$(selector).html(data);
            } else { // generic error message 
                $("#dialog").dialog({
                    modal: true,
                    open: function () {
                        $(this).text(data);
                    },   
                    title: "Error",
                    height: 200,
                    width: 200,
                    buttons: { 
                        Close: function() {
                            $(this).dialog("close");
                        }
                    }
                });
            }            
        }
    })
    .fail(function(data){
        $("#dialog").dialog({
            modal: true,
            open: function () {
                $(this).text("Unable to " + action + " image.  Please contact the site administrator.");
            },   
            title: "Error",
            height: 200,
            width: 200,
            buttons: { 
                Close: function() {
                    $(this).dialog("close");
                }
            }
        });
    });
}

function refreshData() {
    $.ajax({
        url: "/dashboard/docker/image/list/status"
    })
    .done(function(data){
        for (var x in data){
            var id = x;
            var status = data[x];
            var statusColumn = $("tr#" + id.replace(":", "") + "Toggle td:nth-child(3)");
            $(statusColumn).html(status);
            var imageconfigurationRow = $("#" + id.replace(":", "") + "ToggleSection td div p:contains('Status') span");
            imageconfigurationRow.html(status);
        }        
    });

}

if (!String.prototype.splice) {
    /**
     * {JSDoc}
     *
     * The splice() method changes the content of a string by removing a range of
     * characters and/or adding new characters.
     *
     * @this {String}
     * @param {number} start Index at which to start changing the string.
     * @param {number} delCount An integer indicating the number of old chars to remove.
     * @param {string} newSubStr The String that is spliced in.
     * @return {string} A new string with the spliced substring.
     */
    String.prototype.splice = function(start, delCount, newSubStr) {
        return this.slice(0, start) + newSubStr + this.slice(start + Math.abs(delCount));
    };
}



