console.log("lucro js ON.")

class Mes{
    constructor(mes){
	this.mes=mes;
	this.entrada=0;
	this.saida=0;
    }
    get lucro(){
	return this.entrada-this.saida;
    }
}


function lucro() {
    let dd = new Date();
    dd = dd.getFullYear();
    var ano = prompt("Selecione o ANO:", dd);
    if (ano) {
	dialogo_loading();
	$
		.ajax({
		    url : "/caixa/consulta/" + ano,
		    success : function(result) {
			console.log(result);
			let periodo = [];
			for(let z = 0;z<12;z++){
			    periodo.push(new Mes(z));
			}
			console.log(periodo);
			for (let i = 0; i < result.length; i++) {
			    let d = new Date(result[i].data);
			    d= d.getMonth();
			    if((result[i].total!=0)||(result[i].total!=null)){
			    if(result[i].tipo==0){
				periodo[d].saida=periodo[d].saida+result[i].valor;
			    }else{
				periodo[d].entrada=periodo[d].entrada+result[i].valor;
			    }
			    }

			}
			$("#dialogo").modal("hide");
			$("#resultado").html("");
			$('#resultado').append(
				"<h4>Lucro " + ano + "</h4>");

			var div = $("<div class='m-4'></div>");
			var table = $("<table id='tabela'></table>");
			div.append(table);
			$("#resultado").append(div);
			var tabela_teste = $("#tabela")
				.DataTable(
					{
					    data : periodo,
					    pageLength : 12,
					    "bPaginate" : false,
					    "bLengthChange" : false,
					    "bFilter" : false,
					    "bInfo" : false,
					    "language" : {
						"url" : "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Portuguese-Brasil.json"
					    },
					    columns : [
						    {
							data : "mes",
							title : ""
						    },
						    {
							data : "mes",
							title : "Mês",
							render : function(data,
								type, row, meta) {
							    let meses = [
								    "Janeiro",
								    "Fevereiro",
								    "Março",
								    "Abril",
								    "Maio",
								    "Junho",
								    "Julho",
								    "Agosto",
								    "Setembro",
								    "Outubro",
								    "Novembro",
								    "Dezembro" ];
							    return meses[data];
							}

						    },
						    {
							data : "entrada",
							title : "Entradas",
							render : $.fn.dataTable.render
								.number(',',
									'.', 2,
									'R$ ')
						    },
						    {
							data : "saida",
							title : "Saídas",
							render : $.fn.dataTable.render
								.number(',',
									'.', 2,
									'R$ ')

						    },
						    {
							data : "lucro",
							title : "Lucro",
							render : $.fn.dataTable.render
								.number(',',
									'.', 2,
									'R$ ')
						    } ]
					});
		    }
		});
    }
}

function DATA(data) {
    let d = new Date(data);
    let dataFormatada = d.getDate().toString().padStart(2, '0') + '/'
	    + (d.getMonth() + 1).toString().padStart(2, '0') + '/'
	    + d.getFullYear() + " " + d.getHours().toString().padStart(2, '0')
	    + ":" + d.getMinutes().toString().padStart(2, '0');
    return dataFormatada;
}
function dialogo_loading() {
    console.log($("#dialogo").length);
    if ($("#dialogo").length) {
	$("#dialogo").modal("dispose");
    } else {
	let conteudo = '<div class="d-flex align-items-center"><strong class="text-warning">Aguarde...</strong><div class="spinner-border  text-warning ml-auto" role="status" aria-hidden="true"></div></div>';
	let texto = '<div class="modal" id="dialogo" data-backdrop="static"><div class="modal-dialog modal-sm"><div class="modal-content">';
	texto = texto + '<div class="modal-body">' + conteudo + '</div>';
	texto = texto + '</div></div></div>';
	$("body").append(texto);
	$("#dialogo").modal("show");
    }
}