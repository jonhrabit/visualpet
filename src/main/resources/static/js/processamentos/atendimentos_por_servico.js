var tabela = [];
function atendimentos_por_servico() {
	var mes = $("#mes").val();
	var ano = $("#ano").val();
	$.ajax({
		url : "/consultas/atendimentosporservico/" + mes + "/" + ano,
		success : function(result) {
			$("#resultado").html("");
			$("#resultado").append(
					"<h3>Atendimentos por Serviço de " + mes + "/" + ano
							+ "</h3>");
			var div = $("<div class='m-4'></div>");
			var table = $("<table id='tabela'></table>");
			div.append(table);
			$("#resultado").append(div);
			var tabela_teste = $("#tabela").DataTable({
				data : result,
				columns : [ {
					data : "servico",
					title : "Serviço"
				}, {
					data : "quantidade",
					title : "Quantidade"
				} ]
			});
		}
	});
}
function atendimentos_por_servico_trimestre() {
	$.ajax({
		url : "/consultas/atendimentosporservico/trimestre/" + $("#mes").val()
				+ "/" + $("#ano").val(),
		success : function(result) {
			tabela_relatorio_atendimentos_por_servico(result);
		}
	});
}
function form_mes_ano(div_name) {
	$(div_name).html("");
	var div = $("<div class='col-4'></div>");
	let d = new Date();
	div.append("Mês: <input id='mes' type='text' class='form-control' value='"
			+ (d.getMonth() + 1).toString().padStart(2, '0') + "'/>");
	div.append("Ano: <input id='ano' type='text' class='form-control'  value='"
			+ d.getFullYear() + "'/>");
	var div2 = $("<div class='row'></div>")

	div2
			.append("<div class='m-3'><button onclick='atendimentos_por_servico()' class='btn btn-primary'>Mês</button></div>");
	div2
			.append("<div class='m-3'><button onclick='atendimentos_por_servico_trimestre()' class='btn btn-primary'>Trimestre</button></div>");
	div.append(div2)
	$(div_name).append("<h4>Consulta Atendimentos por Serviço</h4>");
	$(div_name).append(div);
}
function tabela_relatorio_atendimentos_por_servico(dados) {
	for (var z = 0; z < dados.lista.length; z++) {
		var lista = dados.lista[z];
		for (var i = 0; i < lista.length; i++) {
			if (contem(lista[i].servico) == false) {
				tabela.push({
					"servico" : lista[i].servico,
					"m0" : 0,
					"m1" : 0,
					"m2" : 0
				});
			}
			switch (z) {
			case 0:
				tabela[linha(lista[i].servico)].m0 = lista[i].quantidade;
				break;
			case 1:
				tabela[linha(lista[i].servico)].m1 = lista[i].quantidade;
				break;
			case 2:
				tabela[linha(lista[i].servico)].m2 = lista[i].quantidade;
				break;
			}
		}
	}
	$("#resultado").html("");
	$("#resultado").append("<h3>Atendimentos por Serviço Trimestre</h3>");
	var div = $("<div class='m-4'></div>");
	var table = $("<table id='tabela'></table>");
	div.append(table);
	$("#resultado").append(div);
	var tabela_teste = $("#tabela").DataTable({
		data : tabela,
		columns : [ {
			data : "servico",
			title : "Serviço"
		}, {
			data : "m2",
			title : dados.titulo[2]
		}, {
			data : "m1",
			title : dados.titulo[1]
		}, {
			data : "m0",
			title : dados.titulo[0]
		} ]
	});

}

function contem(valor) {
	for (var i = 0; i < tabela.length; i++) {
		if (tabela[i].servico == valor) {
			return true;
		}
	}
	return false;
}
function linha(valor) {
	for (var i = 0; i < tabela.length; i++) {
		if (tabela[i].servico == valor) {
			return i;
		}
	}
	return NaN;
}
