$(document).ready(function () {
    /*
     * MATERIAZLIZE
     */
    $('ul.tabs').tabs('select_tab', 'test4');
    $('.chips').material_chip();
    $('.chips-initial').material_chip({
        data: [{tag: 'Apple'}, {tag: 'Microsoft'}, {tag: 'Google'}]
    });
    $('.chips-placeholder').material_chip({
        placeholder: 'Enter a tag',
        secondaryPlaceholder: '+Tag'
    });
    $('.chips').on('chip.add', function (e, chip) {
        alert("on add");
        alert(chip.tag);
        $('.chips-initial').material_chip('data').forEach(function (untag) {
            alert(untag.tag);
        });
    });

    $('.chips').on('chip.delete', function (e, chip) {
        alert("on delete");
    });

    $('.chips').on('chip.select', function (e, chip) {
        alert("on select");
    });
    $(".button-collapse").sideNav();
    $('.modal-trigger').leanModal();
    /*
     * END MATERIALIZE
     */


    var receptesPuntuades = $("#receptesPuntuades");
    var servletURL = "puntuacions?action=receptesPuntuadesList";
    $.ajax({
        type: "GET",
        crossDomain: true,
        dataType: "json",
        async: true,
        url: servletURL,
        success: function (data) {
            var myHtml = renderReceptesPuntuades(data);
            receptesPuntuades.html(myHtml);
            var dades = calculTotalsLlista(data);
            $("#total-puntuacions").text(dades[0]);
            $("#mitjana-puntuacions").text(dades[1]);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.info('in error');
            console.log(jqXHR, textStatus, errorThrown);
            alert("You can not send Cross Domain AJAX requests: " + errorThrown);
        }
    });
});

$(document).on('click', '[class*="deleteRecepta"]', function () {
    var recepta = $(this).attr("id");

    var servletURL = "puntuacions?action=deleteRecepta&recepta=" + recepta;
    $.ajax({
        type: "GET",
        crossDomain: true,
        dataType: "json",
        async: true,
        url: servletURL,
        success: function (data) {
            window.location.href = "puntuacions.html";

        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.info('in error');
            console.log(jqXHR, textStatus, errorThrown);
            alert("You can not send Cross Domain AJAX requests: " + errorThrown);
        }
    });
});



function renderReceptesPuntuades(data) {
    var myHtml = "";
    $.each(data.jsonArray, function (index) {
        myHtml += '<div class="col s12 m12 l12"> <div class="card grey lighten-4 hoverable">';
        myHtml += renderRecepta(data.jsonArray[index]);
        myHtml += '</div></div>';
    });
    return myHtml;
}

function renderRecepta(dataRecepta) {
    //TODO si stock és 0
    var myHtmlP = "";
    var recepta = "";
    var puntuacio = 0.0;
    $.each(dataRecepta, function (key, value) {
        if (key === 'name') {
            recepta = value;
        }
        if (key === 'puntuacio') {
            puntuacio = parseFloat(value);
        }
    });
 
    myHtmlP +='<div class="card-panel grey lighten-5 z-depth-1 hoverable"><div class="row valign-wrapper">';
    myHtmlP += '<div class="col s2"><img  class="circle responsive-img" src="img/' + recepta + '.jpg"/></div>';
    myHtmlP += '<div class="col s10">\n\
                <div class="chip"><h6>Recepta: <span id="name-' + recepta + '">' + recepta + '</h6></div>';
    myHtmlP += '<div class="chip"><h6>Puntuació: <span id="puntuacio-' + recepta + '">' + puntuacio + '</span></h6></div></div>';
    myHtmlP += '<div class="card-action right-align"><a class ="deleteRecepta" href="#" id="' + recepta + '">Eliminar</a>';
    myHtmlP += '</div></div></div>';
    return myHtmlP;
}

function calculTotalsLlista(data){
    var dades = [0,0.0];
    var total = 0.0;
     $.each(data.jsonArray, function (index) {
        
        var recepta = data.jsonArray[index];
        var puntuacio = recepta["puntuacio"];
        dades[0] = dades[0] + 1;
        total = total + parseFloat(puntuacio);
     });
    if(dades[0] !== 0) {
        dades[1] = total / dades[0];
    } else {
        dades[1] = 0.0;
    }
  
    return dades;
}