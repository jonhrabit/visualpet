'use strict';

if (sessionStorage.getItem("compras") == null) {
	sessionStorage.setItem("compras", 0);
}
compras(sessionStorage.getItem("compras"));

function compras(quant) {
	if (quant > 0) {
		$('#countProdutos').html(quant);
	} else {
		$('#countProdutos').html("");
	}
}

function add_servico(id) {
	$.ajax({
		url : "/cesta/addservico",
		data : {
			id : id,
			quantidade : 1
		},
		success : function(data) {
			sessionStorage.setItem("compras", Number(sessionStorage
					.getItem("compras"))
					+ data)
			compras(sessionStorage.getItem("compras"));
			$("#atendimento_status_"+id).html("<span class='text-success'>Adicionado</span>");
		}
	});
}
function nav_page(page,name_div,funcao){
	$("#ppaa").remove();
	if(page.totalPages!=1){
		var paginacao = '<nav aria-label="Page navigation" id="ppaa"><ul class="pagination justify-content-center">';
		if(page.number>0){
			paginacao=paginacao+'<li class="page-item"><a class="page-link" href="#" onclick="'+funcao+'('+0+')"><<</a></li>';
		}
		let numero;
		for(var x=-2; x<3 ; x++){
			numero=page.number+x;
			if(x!=0){
				if((numero>=0)&&(numero<page.totalPages)){
					paginacao=paginacao+'<li class="page-item"><a class="page-link" href="#" onclick="'+funcao+'('+numero+')">'+(numero+1)+'</a></li>';
				}
			}else{
				paginacao=paginacao+'<li class="page-item active"><span class="page-link">'+(page.number+1)+'</span></li>'
			}
		} 
		if(page.number!=page.totalPages-1)paginacao=paginacao+'<li class="page-item"><a class="page-link" href="#" onclick="'+funcao+'('+(page.totalPages-1)+')">>></a></li>';
		paginacao=paginacao+'</ul></nav>';
		
		$(name_div).append(paginacao);
	}
}