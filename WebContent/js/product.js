COLDIGO.produto = new Object()

$(document).ready(function(){

    //Carrega as marcas registradas no BD no select do formulário de inserir
    COLDIGO.produto.carregarMarcas = function(){

        alert("tentando buscar marcas")
        
        $.ajax({
            type: "GET",
            url: "/ProjetoTrilhaWeb/rest/marca/buscar",
            success: function(marcas) {
                if (marcas!=""){


                    //Cria opção escolha com valor vazio como padrão caso haja algo no banco de dados
                    $("selMarca").html("")
                    var option = document.createElement("option")
                    option.setAttribute("value", "")
                    option.innerHTML = ("Escolha")
                    $("#selMarca").append(option)

                    //A cada valor encontrado no banco, cria mais uma option dentro do select
                    for (var i = 0; i < marcas.length; i++){

                        var option = document.createElement("option")
                        option.setAttribute("value", marcas[i].id)
                        option.innerHTML = (marcas[i].nome)
                        $("#selMarca").append(option)

                    }


                }else{
                    $("selMarca").html("")

                    //Caso o não tenha nenhum valor cadastrado no banco ele cria uma uma option com aviso!
                    var option = document.createElement("option")
                    option.setAttribute("value", "")
                    option.innerHTML = ("Cadastre uma marca primeiro!")
                    $("#selMarca").append(option)
                    $("#selMarca").addClass("aviso")

                }
            },
            error: function(info) {

                COLDIGO.exibirAviso("Erro ao buscar marcas: "+info.status+" - " + info.statusText)

                $("#selMarca").html("")
                var option = document.createElement("option")
                option.setAttribute("value", "")
                option.innerHTML = ("Erro ao carregar marcas!")
                $("#selMarca").append(option)
                $("#selMarca").addClass("aviso")

            } 
        })

    }
    
    COLDIGO.produto.carregarMarcas()

})