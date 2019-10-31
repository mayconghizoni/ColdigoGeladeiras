COLDIGO.produto = new Object()

$(document).ready(function(){

    //Carrega as marcas registradas no BD no select do formulário de inserir
    COLDIGO.produto.carregarMarcas = function(id){

        if(id!=undefined){
            select = "#selMarcaEdicao";
        }else{
            select = "#selMarca";
        }

        $.ajax({
            type: "GET",
            url: COLDIGO.PATH + "marca/buscar",
            data: "valorBusca=",
            success: function(marcas) {
                if (marcas!=""){


                    //Cria opção escolha com valor vazio como padrão caso haja algo no banco de dados
                    $(select).html("")
                    var option = document.createElement("option")
                    option.setAttribute("value", "")
                    option.innerHTML = ("Escolha")
                    $(select).append(option)

                    //A cada valor encontrado no banco, cria mais uma option dentro do select
                    for (var i = 0; i < marcas.length; i++){

                        var option = document.createElement("option")
                        option.setAttribute("value", marcas[i].id)

                        if((id!=undefined)&&(id==marcas[i].id)){
                            option.setAttribute("selected", "selected");
                        }
                        

                        option.innerHTML = (marcas[i].nome)
                        $(select).append(option)

                    }


                }else{
                    $(select).html("")

                    //Caso o não tenha nenhum valor cadastrado no banco ele cria uma uma option com aviso!
                    var option = document.createElement("option")
                    option.setAttribute("value", "")
                    option.innerHTML = ("Cadastre uma marca primeiro!")
                    $(select).append(option)
                    $(select).addClass("aviso")

                }
            },
            error: function(info) {

                COLDIGO.exibirAviso("Erro ao buscar marcas: "+info.status+" - " + info.statusText)

                $(select).html("")
                var option = document.createElement("option")
                option.setAttribute("value", "")
                option.innerHTML = ("Erro ao carregar marcas!")
                $(select).append(option)
                $(select).addClass("aviso")

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
                    "<a onclick=\"COLDIGO.produto.exibirEdicao('"+listaDeProdutos[i].id+"')\"><img src='../../imgs/edit.png' alt='Editar registro'></a>" +
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

        var modalExcluiProduto = {
            title: "Excluir produto",
            height: 200,
            width: 550,
            modal: true,
            buttons:{
                "Sim": function(){
                    $.ajax({
                        type: "DELETE", //Define metodo de envio
                        url: COLDIGO.PATH + "produto/excluir/"+id, //Define urexcluirexcluirl de envio e passa o valor id 
                        success: function(msg){
                            COLDIGO.exibirAviso(msg); // Exibe msg retornada do servidor em caso de sucesso
                            COLDIGO.produto.buscar(); // Atualiza lista de produtos
                            $("#modalExcluiProduto").dialog("close"); //Fecha modal de edição
                        },
                        error: function(info){
                            COLDIGO.exibirAviso("Erro ao excluir produto: "+info.status+" - "+info.statusText); //Exibe mensagem de erro
                        }
                    })
                },
                "Cancelar": function(){
                    $(this).dialog("close");
                }
            },
            close: function(){
                //caso o usuário simplesmente feche a caixa de edição não acontece nada.
            }
        }

        $("#modalExcluiProduto").dialog(modalExcluiProduto);

    }

    // Esta função exibe uma modal de edição de produto.
    COLDIGO.produto.exibirEdicao = function(id){
        $.ajax({
            type: "GET", //Define metodo de envio como GET
            url: COLDIGO.PATH + "produto/buscarPorId", //define a url de envio
            data: "id="+id, //dados transmitidos
            success: function(produto){

                document.frmEditaProduto.idProduto.value = produto.id;
                document.frmEditaProduto.modelo.value = produto.modelo;
                document.frmEditaProduto.capacidade.value = produto.capacidade;
                document.frmEditaProduto.valor.value = produto.valor;

                var selCategoria = document.getElementById('selCategoriaEdicao');
                for(var i=0; i < selCategoria.length; i++){
                    if(selCategoria.options[i].value == produto.categoria){
                        selCategoria.options[i].setAttribute("selected", "selected");
                    }else{
                        selCategoria.options[i].removeAttribute("selected");
                    }
                }

                COLDIGO.produto.carregarMarcas(produto.marcaId);

                var modalEditaProduto = {
                    title: "Editar Produto",
                    height: 400,
                    width: 550,
                    modal: true,
                    buttons:{
                        "Salvar": function(){
                            COLDIGO.produto.editar();
                        },
                        "Cancelar": function(){
                            $(this).dialog("close");
                        }
                    },
                    close: function(){
                        //caso o usuário simplesmente feche a caixa de edição não acontece nada.
                    }
                }

                $("#modalEditaProduto").dialog(modalEditaProduto);

            },

            error: function(info){
                COLDIGO.exibirAviso("Erro ao buscar produto para edição: "+info.status+" - "+ info.statusText);
            }
        })
    }

    COLDIGO.produto.editar = function(){

        //Cria novo obejeto e armazena dados do formulário dentro deste objeto
        var produto = new Object();
        produto.id = document.frmEditaProduto.idProduto.value;
        produto.categoria = document.frmEditaProduto.categoria.value;
        produto.capacidade = document.frmEditaProduto.capacidade.value;
        produto.marcaId = document.frmEditaProduto.marcaId.value;
        produto.modelo = document.frmEditaProduto.modelo.value;
        produto.valor = document.frmEditaProduto.valor.value;

        $.ajax({
            type:"PUT", //Define método de envio
            url: COLDIGO.PATH + "produto/alterar", //Define url de envio
            data: JSON.stringify(produto), //Monta dados de envio do tipo JSON
            success: function(msg){

                COLDIGO.exibirAviso(msg);   //Exibe mensagem de aviso com sucesso em caso de update completo.
                COLDIGO.produto.buscar();   //Faz uma nova busca de produtos pra tabela
                $("#modalEditaProduto").dialog("close"); //Fecha modal de edição
            
            },
            error: function(info){
                COLDIGO.exibirAviso("Erro ao editar produto: "+ info.status +" - "+ info.statusText); //Mostra mensagem de erro em caso de falha.
            }
        });

    }

})