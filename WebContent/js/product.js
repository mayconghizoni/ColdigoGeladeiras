COLDIGO.produto = new Object()

$(document).ready(function(){

    //Carrega as marcas registradas no BD no select do formulário de inserir
    COLDIGO.produto.carregarMarcas = function(){

        $.ajax({
            type: "GET",
            url: COLDIGO.PATH + "marca/buscar",
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
    
    //Carrega marcas ao carregar a página
    COLDIGO.produto.carregarMarcas()

    //Cadastra no BD o produto informado
    COLDIGO.produto.cadastrar = function(){


        //Cria e armazena as informações no objeto produto
        var produto = new Object();
        produto.categoria = document.frmAddProduto.categoria.value;
        produto.marcaId = document.frmAddProduto.marcaId.value;
        produto.modelo = document.frmAddProduto.modelo.value;
        produto.capacidade = document.frmAddProduto.capacidade.value;
        produto.valor = document.frmAddProduto.valor.value;


        //verifica se os campos não estão vazios
        if((produto.categoria=="")||(produto.marcaId=="")||(produto.modelo=="")||(produto.capacidade=="")||(produto.valor=="")){

            COLDIGO.exibirAviso("Preencha todos os campos!")

        } else {

            $.ajax({
                type: "POST", //define o metodo de envio como post
                url: COLDIGO.PATH + "produto/inserir",
                data: JSON.stringify(produto), //envia o objeto produto em formato JSON
                success: function(msg){
                    COLDIGO.produto.buscar(); //Atualiza lista de produdos
                    COLDIGO.exibirAviso(msg); //exibe a mensagem devolvida pelo servidor
                    $("#addProduto").trigger("reset");
                },
                error: function(info){
                    COLDIGO.exibirAviso("Erro ao cadastrar produto: "+info.status+" - " + info.statusText)
                }
            })

        }

    }

    //Busca no bd e exibe na página os produtos que atendem a busca
    COLDIGO.produto.buscar = function(){

        var valorBusca = $("#campoBuscaProduto").val();

        $.ajax({
            type: "GET",
            url: COLDIGO.PATH + "produto/buscar",
            data: "valorBusca="+valorBusca,
            success: function(dados){
                dados = JSON.parse(dados);

                $("#listaProdutos").html(COLDIGO.produto.exibir(dados));

            },
            error: function(info){
                COLDIGO.exibirAviso("Erro: "+ info.status + " - " + info.statusText);
            }
        })

    }

    //Transforma os dados dos produtos recebidos do servidor em uma tabela HTML
    COLDIGO.produto.exibir = function(listaDeProdutos){
        var tabela = "<table>" +
        "<tr>" +
        "<th>Categoria</th>" +
        "<th>Marca</th>" +
        "<th>Modelo</th>" +
        "<th>Cap.(l)</th>" +
        "<th>Valor</th>" +
        "<th class='acoes'>Ações</th>" +
        "</tr>";

        if(listaDeProdutos != undefined && listaDeProdutos.length > 0){

            for (var i=0; i<listaDeProdutos.length; i++){
                tabela += "<tr>" +
                "<td>"+listaDeProdutos[i].categoria+"</td>"+
                "<td>"+listaDeProdutos[i].marcaNome+"</td>"+
                "<td>"+listaDeProdutos[i].modelo+"</td>"+
                "<td>"+listaDeProdutos[i].capacidade+"</td>"+
                "<td>R$ "+COLDIGO.formatarDinheiro(listaDeProdutos[i].valor)+"</td>"+
                "<td>" +
                    "<a><img src='../../imgs/edit.png' alt='Editar registro'></a>" +
                    "<a onclick=\"COLDIGO.produto.excluir('"+listaDeProdutos[i].id+"')\"><img src='../../imgs/delete.png' alt='Deletar registro'></a>" +
                "</td>" +
                "</tr>"
            }

        }else if(listaDeProdutos==""){
            tabela += "<tr><td colspan='6'>Nenhum registro encontrado</td></tr>";
        }
        tabela += "</table>";

        return tabela;

    };

    //EXECUTA A FUNÇÃO DE BUSCAR AO CARREGAR A PÁGINA
    COLDIGO.produto.buscar();

    //Exclui o produto selecionado
    COLDIGO.produto.excluir = function(id){
        $.ajax({
            type: "DELETE", //Define metodo de envio
            url: COLDIGO.PATH + "produto/excluir/"+id, //Define url de envio e passa o valor id 
            success: function(msg){
                COLDIGO.exibirAviso(msg); // Exibe msg retornada do servidor em caso de sucesso
                COLDIGO.produto.buscar(); // Atualiza lista de produtos
            },
            error: function(info){
                COLDIGO.exibirAviso("Erro ao excluir produto: "+info.status+" - "+info.statusText); //Exibe mensagem de erro
            }
        })
    }
})