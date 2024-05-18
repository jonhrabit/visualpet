console.log("Ticket js ON.")

function ticket() {
    let dd = new Date();
    dd = dd.getFullYear();
    var ano = prompt("Selecione o ANO:", dd);
    if (ano) {
	dialogo_loading();
	$
		.ajax({
		    url : "/cesta/ticket/" + ano,
		    success : function(result) {
			console.log(result);
			$("#dialogo").modal("hide");
			$("#resultado").html("");
			$('#resultado').append(
				"<h4>Ticket Médio - " + ano + "</h4>");

			var div = $("<div class='m-4'></div>");
			var table = $("<table id='tabela'></table>");
			div.append(table);
			$("#resultado").append(div);
			var tabela_teste = $("#tabela")
				.DataTable(
					{
					    data : result,
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
							data : "frequencia",
							title : "Vendas"
						    },
						    {
							data : "ticketMedio",
							title : "ticket",
							render : $.fn.dataTable.render
								.number(',',
									'.', 2,
									'R$ '),

						    },
						    {
							data : "total",
							title : "Total",
							render : $.fn.dataTable.render
								.number(',',
									'.', 2,
									'R$ '),

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