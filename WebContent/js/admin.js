COLDIGO = new Object()

$(document).ready(function() {

    $("header").load("/ProjetoTrilhaWeb/pages/admin/general/header.html");
    $("footer").load("/ProjetoTrilhaWeb/pages/admin/general/footer.html");
    
    COLDIGO.carregaPagina = function(pagename){
        $("section").empty();
        $("section").load(pagename+"/", 
        
        function(response, status, info){
            if (status == "error"){
                var msg = "Houve um erro ao encontrar a página: "+info.status+" - " + info.statusText;
                $("section").html(msg);
            }
        });
    }
});