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


    var receptes = $("#receptes");
    var servletURL = "Receptari?action=listReceptes";
    $.ajax({
        type: "GET",
        crossDomain: true,
        dataType: "json",
        async: true,
        url: servletURL,
        success: function (data) {
            var myHtml = renderListReceptes(data);
            receptes.html(myHtml);
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

  //p3//
    var servletURL = "user?action=formUser";
    $.ajax({
        type: "GET",
        crossDomain: true,
        dataType: "json",
        async: true,
        url: servletURL,
        success: function (data) {
            $("#username").html("<h5>Puntuacions: " + data.user +'</h5>');
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.info('in error');
            console.log(jqXHR, textStatus, errorThrown);
        }
    });


});

$(document).on('click', '[class*="puntuacio"]', function () {
    var receptaPuntuacio = $(this).attr("id").split("-");
    var recepta = receptaPuntuacio[0];
    var puntuacio = receptaPuntuacio[1];
    var afegir = $(this.parentElement);
    var servletURL = "Receptari?action=addReceptaPuntuacions&recepta=" + recepta + "&puntuacio=" + puntuacio;
    $.ajax({
        type: "GET",
        crossDomain: true,
        dataType: "json",
        async: true,
        url: servletURL,
        success: function (data) {
            
            afegir.hide();
            $("#check-" + data.receptaPuntuada).show();
            $("#puntuacio-" + data.receptaPuntuada).text(data.puntuacioRecepta);

            calculTotals(data);
            

        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.info('in error');
            console.log(jqXHR, textStatus, errorThrown);
            alert("You can not send Cross Domain AJAX requests: " + errorThrown);
        }
    });
});

function renderListReceptes(data) {
    var myHtml = "";
    $.each(data.jsonArray, function (index) {
        myHtml += '<div class="col s12 m3 l3"> <div class="card grey lighten-4 hoverable">';
        myHtml += renderRecepta(data.jsonArray[index]);
        myHtml += '</div></div>';
    });
    return myHtml;
}

function renderRecepta(dataRecepta) {
    var myHtmlP = "";
    var recepta = "";
    var puntuacio = 0.0;
    var afegit = false;
    $.each(dataRecepta, function (key, value) {
        if (key === 'name') {
            recepta = value;
        }
        if (key === 'puntuacio') {
            puntuacio = parseFloat(value);
        }
        if (key === 'afegit') {
            if (value === 'SI') {
                afegit = true;
            } else {
                afegit = false;
            }
        }
    });
    myHtmlP += '<div class="card-image"><img src="img/' + recepta + '.jpg"/><span class="card-title">' + recepta + '</span></div>';
    myHtmlP += '<div class="chip"><h6>Puntuació: <span id="puntuacio-' + recepta + '">' + puntuacio + '</h6></div>';
    
    if (afegit) {
        myHtmlP += '<div class="card-action right-align">';
        myHtmlP += '<img id ="check-' + recepta + '" style="width: 10px;" src="img/check.png"/></div></div>';

    } else {
        myHtmlP += '<div class="card-action right-align"><div>';
       for (var i = 1; i <= 5; i++){
        myHtmlP += '<a class ="puntuacio" href="#" id="' + recepta + '-' +i+'">'+i+'</a>';
       }
        myHtmlP += '</div><img id ="check-' + recepta + '" style="display: none;width: 10px;" src="img/check.png"/></div></div>';
    }

    return myHtmlP;
}


function calculTotalsLlista(data){
    var dades = [0,0.0];
    var total = 0.0;
     $.each(data.jsonArray, function (index) {
        
        var recepta = data.jsonArray[index];
        var m = recepta["afegit"];
        var puntuacio = recepta["puntuacio"];
        if (m === "SI"){
            dades[0] = dades[0] + 1;
            total = total + parseFloat(puntuacio);
        }
     });
    
    if(dades[0] !== 0) {
        dades[1] = total / dades[0];
    } else {
        dades[1] = 0.0;
    }
  
    return dades;
}

function calculTotals(data) {
    var mitjana = parseFloat(document.getElementById('mitjana-puntuacions').innerHTML);
    var puntuacions = parseInt(document.getElementById('total-puntuacions').innerHTML);
    $("#total-puntuacions").text(puntuacions + 1);
    $("#mitjana-puntuacions").text(((mitjana * puntuacions) + data.puntuacioRecepta ) / (puntuacions + 1)); 
}
