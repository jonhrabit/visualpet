console.log("relatorio js ON.")

var d = new Date();
$("#ano").val(d.getFullYear());
$("#mes").val(d.getMonth() + 1);

function atualizar() {
	let mes = $("#mes").val();
	let ano = $("#ano").val();
	$.ajax({
		url : "/consultas/vendasbrutas/" + mes + "/" + ano,
		data : {},
		success : function(result) {
			criar_tabela(result, "#resultado");
		}
	});
}

function totais(dados, formaPgto, tipo) {
	let total = 0;
	for (var i = 0; i < dados.length; i++) {
		if (dados[i].formaPgto == formaPgto) {
			if (dados[i].tipo == tipo) {
				total = total
						+ Number(dados[i].precoVenda * dados[i].quantidade);
			}
		}
	}
	return Number(total).toFixed(2);
}
function soma_totais(dados, comando) {
	let total = 0;
	switch (comando) {
	case "servico_sd":
		total += parseFloat(totais(dados, 2, "servico"));
		total += parseFloat(totais(dados, 3, "servico"));
		total += parseFloat(totais(dados, 4, "servico"));
		return Number(total).toFixed(2);
	case "servico":
		total += parseFloat(totais(dados, 1, "servico"));
		total += parseFloat(totais(dados, 2, "servico"));
		total += parseFloat(totais(dados, 3, "servico"));
		total += parseFloat(totais(dados, 4, "servico"));
		return Number(total).toFixed(2);
	case "produto_sd":
		total += parseFloat(totais(dados, 2, "produto"));
		total += parseFloat(totais(dados, 3, "produto"));
		total += parseFloat(totais(dados, 4, "produto"));
		return Number(total).toFixed(2);
	case "produto":
		total += parseFloat(totais(dados, 1, "produto"));
		total += parseFloat(totais(dados, 2, "produto"));
		total += parseFloat(totais(dados, 3, "produto"));
		total += parseFloat(totais(dados, 4, "produto"));
		return Number(total).toFixed(2);
	case "td":
		total += parseFloat(totais(dados, 1, "produto"));
		total += parseFloat(totais(dados, 1, "servico"));
		return Number(total).toFixed(2);
	case "tde":
		total += parseFloat(totais(dados, 2, "produto"));
		total += parseFloat(totais(dados, 2, "servico"));
		return Number(total).toFixed(2);
	case "tc":
		total += parseFloat(totais(dados, 3, "produto"));
		total += parseFloat(totais(dados, 3, "servico"));
		return Number(total).toFixed(2);
	case "tcc":
		total += parseFloat(totais(dados, 4, "produto"));
		total += parseFloat(totais(dados, 4, "servico"));
		return Number(total).toFixed(2);
	case "total_sem_dinheiro":
		total += parseFloat(totais(dados, 2, "servico"));
		total += parseFloat(totais(dados, 3, "servico"));
		total += parseFloat(totais(dados, 4, "servico"));
		total += parseFloat(totais(dados, 2, "produto"));
		total += parseFloat(totais(dados, 3, "produto"));
		total += parseFloat(totais(dados, 4, "produto"));
		return Number(total).toFixed(2);
	case "total_geral":
		total += parseFloat(totais(dados, 1, "servico"));
		total += parseFloat(totais(dados, 2, "servico"));
		total += parseFloat(totais(dados, 3, "servico"));
		total += parseFloat(totais(dados, 4, "servico"));
		total += parseFloat(totais(dados, 1, "produto"));
		total += parseFloat(totais(dados, 2, "produto"));
		total += parseFloat(totais(dados, 3, "produto"));
		total += parseFloat(totais(dados, 4, "produto"));
		return Number(total).toFixed(2);
	}
	return Number(0).toFixed(2);
}

function criar_tabela(dados, div_name) {
	$(div_name).html("");
	$("#sd").text("R$ " + totais(dados, 1, "servico"));
	$("#sde").text("R$ " + totais(dados, 2, "servico"));
	$("#sc").text("R$ " + totais(dados, 3, "servico"));
	$("#scc").text("R$ " + totais(dados, 4, "servico"));
	$("#total_servico").text("R$ " + soma_totais(dados, "servico"));
	$("#total_servico_sd").text("R$ " + soma_totais(dados, "servico_sd"));

	$("#pd").text("R$ " + totais(dados, 1, "produto"));
	$("#pde").text("R$ " + totais(dados, 2, "produto"));
	$("#pc").text("R$ " + totais(dados, 3, "produto"));
	$("#pcc").text("R$ " + totais(dados, 4, "produto"));
	$("#total_produto").text("R$ " + soma_totais(dados, "produto"));
	$("#total_produto_sd").text("R$ " + soma_totais(dados, "produto_sd"));

	$("#td").text("R$ " + soma_totais(dados, "td"));
	$("#tde").text("R$ " + soma_totais(dados, "tde"));
	$("#tc").text("R$ " + soma_totais(dados, "tc"));
	$("#tcc").text("R$ " + soma_totais(dados, "tcc"));
	$("#total_sem_dinheiro").text(
			"R$ " + soma_totais(dados, "total_sem_dinheiro"));
	$("#total_geral").text("R$ " + soma_totais(dados, "total_geral"));

	var total = 0;
	var table = $('<table></table>').addClass("table");
	var row;
	for (var i = 0; i < dados.length; i++) {
		row = $('<tr></tr>');

		var link = $("<a />", {
			href : "/cesta/" + dados[i].cesta,
			text : i+1
		});
		var rowData = $('<td></td>').append(link);
		row.append(rowData);

		let d = new Date(dados[i].data);
		let dataFormatada = d.getDate().toString().padStart(2, '0') + '/'
				+ (d.getMonth() + 1).toString().padStart(2, '0') + '/'
				+ d.getFullYear() + " "
				+ d.getHours().toString().padStart(2, '0') + ":"
				+ d.getMinutes().toString().padStart(2, '0');

		rowData = $('<td></td>').text(dataFormatada);
		row.append(rowData);

		rowData = $('<td></td>').text(dados[i].nome);
		row.append(rowData);

		rowData = $('<td></td>').text(dados[i].tipo);
		row.append(rowData);

		let textoPgto = "";
		if (dados[i].formaPgto == 1) {
			textoPgto = "Dinheiro";
		}
		if (dados[i].formaPgto == 2) {
			textoPgto = "Débito";
		}
		if (dados[i].formaPgto == 3) {
			textoPgto = "Crédito";
		}
		if (dados[i].formaPgto == 4) {
			textoPgto = "Crédito/Comp.";
		}

		rowData = $('<td></td>').text(textoPgto);
		row.append(rowData);
		if(dados[i].tipo=='produto'){
		rowData = $('<td></td>').text(
				dados[i].quantidade + "x R$ "
						+ Number(dados[i].precoVenda).toFixed(2));
		row.append(rowData);
		}else{
			rowData = $('<td></td>');
			row.append(rowData);
			
		}
		rowData = $('<td></td>').text(
				"R$ "
						+ Number(dados[i].precoVenda * dados[i].quantidade)
								.toFixed(2));
		total = total + dados[i].preco * dados[i].quantidade;
		row.append(rowData);
		table.append(row);
	}
	$(div_name).append(table);
	$("#total").text("R$ " + Number(total).toFixed(2));
}