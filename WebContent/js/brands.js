COLDIGO.marcas = new Object();

$(document).ready(function() {

    COLDIGO.marcas.cadastrar = function () {
        
        var marca = new Object();
        marca.nome = document.frmAddMarca.nome.value;

        if(marca.nome==""){
            COLDIGO.exibirAviso("Preencha todos os campos!")
        }else{

            $.ajax({
                type: "POST",
                url: COLDIGO.PATH + "marca/inserir",
                data: JSON.stringify(marca),
                success: function (msg) {
                    COLDIGO.exibirAviso(msg);
                    $("#addMarca").trigger("reset");
                },
                error: function (info) {
                    COLDIGO.exibirAviso("Erro ao cadastrar produto: "+info.status+" - " + info.statusText)
                }
            })


        }

    }

     //Busca no bd e exibe na página as marcas que atendem a busca
     COLDIGO.marcas.buscar = function(){

        var valorBusca = $("#campoBuscaMarca").val();         

        $.ajax({
            type: "GET",
            url: COLDIGO.PATH + "marca/buscar",
            data: "valorBusca="+valorBusca,
            success: function(dados){
                
                console.log(dados);
                
                $("#listaMarcas").html(COLDIGO.marcas.exibir(dados));           

            },
            error: function(info){
                COLDIGO.exibirAviso("Erro: "+ info.status + " - " + info.statusText);
            }
        })

    }

    COLDIGO.marcas.buscar();

    COLDIGO.marcas.exibir = function(listaDeMarcas) {
        
        var tabela = "<table>" +
        "<tr>"+
        "<th>Nome das marcas</th>" +
        "<th class='acoes'>Ações</th>" +
        "</tr>";

        if(listaDeMarcas != undefined && listaDeMarcas.length > 0){

            for (var i=0; i<listaDeMarcas.length; i++){
                tabela +=  "<tr>" +
                "<td>"+listaDeMarcas[i].nome+"</td>"+
                "<td>" +
                    "<a><img src='../../imgs/edit.png' alt='Editar registro'></a>" +
                    "<a><img src='../../imgs/delete.png' alt='Deletar registro'></a>" +
                "</td>" +
                "</tr>"
            }

        }else if(listaDeMarcas==""){
            tabela += "<tr><td colspan='2'>Nenhum registro encontrado</td></tr>"
        }
        tabela += "</table>";

        return tabela;
    }

    
})