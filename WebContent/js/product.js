COLDIGO.produto = new Object();

$(document).ready(function(){

    //Carrega as marcas registradas no BD no select do formulário de inserir
    COLDIGO.produto.carregaMarcas = function(){

        $.ajax({
            type: "GET",
            url: "/ProjetoTrilhaWeb/rest/marca/buscar",
            success: function() {

            },
            error: function () {

            } 
        });


    }

});