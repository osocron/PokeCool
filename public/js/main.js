$(document).ready(function () {

    $('select').material_select();

    $.contextMenu({
        selector: '.dashboard-context',
        trigger: 'left',
        autoHide: true,
        callback: function (key, options) {
            var pokeID = $(this).attr('id');
            if (key === "delete") {
                var conf = confirm("¿Está seguro de borrar el pokemon?\n\n");
                if (conf == true) {
                    var xhttp = new XMLHttpRequest();
                    xhttp.onreadystatechange = function () {
                        if (xhttp.readyState == 4 && xhttp.status == 200) {
                            document.getElementById(pokeID).remove();
                        }
                    };
                    xhttp.open("POST", "/del/" + pokeID, true);
                    xhttp.send();
                }
            }
            else if (key == "edit") {

                $("#pokeForm").attr("action", "/update/" + pokeID);

                var tr = $(this);
                var elemArr = [];
                $(this).children('td').each(function () {
                    elemArr.push($(this));
                });
                var src = elemArr[0].children("img").attr("src");
                var img = $('#pokeImg');
                $("#imgURL").val(src);
                img.attr('src', src);
                img.show();
                $("#nombreInput").val(elemArr[1].text());
                var select = $("#tipoSelect");
                select.val(elemArr[2].attr("id"));
                select.material_select();
                $("#pesoInput").val(elemArr[3].text());
                $("#alturaInput").val(elemArr[4].text());
                $("#vidaInput").val(elemArr[5].text());
                $("#puntosCombateInput").val(elemArr[6].text());
                $("#apodoInput").val(elemArr[7].text());
                Materialize.updateTextFields();
                $('#editModal').openModal();
            }
        },
        items: {
            "edit": {
                name: "Editar",
                icon: "paste"
            },
            "delete": {
                name: "Borrar",
                icon: "delete"
            }
        }
    });

    function readURL(input) {
        var img = $('#pokeImg');
        img.attr('src', input.value);
        img.show();
    }

    $("#imgURL").change(function () {
        readURL(this);
    });

});


