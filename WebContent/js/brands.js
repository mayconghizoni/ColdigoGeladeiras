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
                
                console.log(dados); //Retornando tudo
                

            },
            error: function(info){
                COLDIGO.exibirAviso("Erro: "+ info.status + " - " + info.statusText);
            }
        })

    }

    
})