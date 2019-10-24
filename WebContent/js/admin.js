COLDIGO = new Object()

$(document).ready(function() {

    //Cria uma constante com o valor da URI raiz do REST
    COLDIGO.PATH = "/ProjetoTrilhaWeb/rest/"

    $("header").load("/ProjetoTrilhaWeb/pages/admin/general/header.html");
    $("footer").load("/ProjetoTrilhaWeb/pages/admin/general/footer.html");
    
    COLDIGO.carregaPagina = function(pagename){
        //Remove conteudo da pagina criada pela modal do JqueryUI
        if($(".ui.dialog"))
        $("ui.dialog").remove();
        //Limpa dados da tag section
        $("section").empty();
        $("section").load(pagename+"/", 
        
        function(response, status, info){
            if (status == "error"){
                var msg = "Houve um erro ao encontrar a página: "+info.status+" - " + info.statusText;
                $("section").html(msg);
            }
        });
    }

    //Define as configurações base de uma modal de aviso
    COLDIGO.exibirAviso = function(aviso){
        var modal ={
            title: "Mensagem",
            height: 250,
            width: 400,
            modal: true,
            buttons:{
                "OK": function(){
                    $(this).dialog("close");
                }
            }
        }

        $("#modalAviso").html(aviso)
        $("#modalAviso").dialog(modal)

    }



});

//Exibe os valores financeiros no formato da moeda real
COLDIGO.formatarDinheiro = function(valor){
    return valor.toFixed(2).replace('.',',').replace(/(\d)(?=(\d{3})+\,)/g, "$1.");
}