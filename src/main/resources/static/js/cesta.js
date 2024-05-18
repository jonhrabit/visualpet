function formas_de_pagamento(objeto) {
    var lista = [];
    function checkLista(tipo) {
	for (var i = 0; i < lista.length; i++) {
	    if (lista[i].tipo == tipo)
		return true;
	}
	return false;
    }
    for (var j = 0; j < pgto.length; j++) {
	if (!checkLista(pgto[j].tipo)) {
	    lista.push({
		"descricao" : pgto[j].descricao,
		"tipo" : pgto[j].tipo,
		"valor" : pgto[j].valor
	    })
	}
    }
    for (i = 0; i < lista.length; i++) {
	if (lista[i].tipo != 0) {
	    $(objeto).append(
		    new Option(lista[i].descricao + " ( " + lista[i].valor
			    + "% )", lista[i].tipo));
	} else {
	    $(objeto).append(new Option(lista[i].descricao, lista[i].tipo));
	}

    }
}
function seila(tipo) {
    $.ajax({
	url : "/cesta/vezes/" + tipo,
	data : {},
	success : function(result) {
	    if (result.length > 1) {
		$("#divVezes").show();
		$("#vezes").html("")
		for (i = 0; i < result.length; i++) {
		    ;
		    $("#vezes")
			    .append(
				    new Option("x" + result[i].vezes + " ( "
					    + result[i].valor + "% )",
					    result[i].vezes));
		}
	    } else {
		$("#divVezes").hide();
		$("#vezes").val(1);
	    }
	}
    });
}

function add(id) {
    $.ajax({
	url : "/cesta/add",
	data : {
	    id : id,
	    quantidade : 1
	},
	success : function(data) {
	    if (data) {
		atualizar();
	    } else {
		alert("Produto não localizado.")
	    }
	}
    });
}
function down(id) {
    $.ajax({
	url : "/cesta/down",
	data : {
	    id : id
	},
	success : function(data) {
	    if (data) {
		atualizar();
	    } else {
		alert("Produto não localizado.")
	    }
	}
    });
}
function remover(id) {
    $.ajax({
	url : "/cesta/remover",
	data : {
	    id : id
	},
	success : function(data) {
	    if (data) {
		atualizar();
	    } else {
		alert("Produto não localizado.")
	    }
	}
    });
}
function pagar() {
    if (maior_que($("#valor").val(), $("#saldoI").val())) {
	$("#btn_pagar").prop('disabled', true);
	let v = 1;
	if ($("#vezes").val() != null) {
	    v = $("#vezes").val();
	}
	$.ajax({
	    url : "/cesta/pagar",
	    data : {
		forma : $("#forma").val(),
		clienteId : $("#clienteId").val(),
		vezes : v,
		valor : $("#valor").val()
	    },
	    success : function(data) {
		if (data != "") {
		    cesta = data;
		    $("#saldo").text("R$ " + Number(data.saldo).toFixed(2));
		    $("#valor").text("R$ " + Number(data.saldo).toFixed(2));
		    $("#saldoI").val(data.saldo);
		    if (data.quitado == true) {
			$("#modalExemplo").modal("hide");
			alert("Pagamento registrado com exito.");
			cesta = "";
			atualizar();
		    } else {
			alert("Pagamento cadastrado.");
		    }
		} else {
		    alert("Cesta de compras esta vazia.");
		}
	    }
	});
	$("#btn_pagar").prop('disabled', false);
    } else {
	alert("O valor excede o saldo da compra.");
    }
}

function atualizar() {
    $("#tabelaCesta").html("");
    $.ajax({
	url : "/cesta/atualizar",
	success : function(result) {
	    var total = 0;
	    var table = $('<table></table>').addClass("table");
	    var texto = "";
	    for (var i = 0; i < result.length; i++) {
		row = $('<tr></tr>');
		let tipo = "";
		if (result[i].tipo == "servico") {
		    tipo = "/atendimento/" + result[i].idProdutoOriginal;
		} else {
		    tipo = "/produto/" + result[i].idProdutoOriginal;
		}
		var link = $("<a />", {
		    href : tipo,
		    text : result[i].nome
		});

		var rowData = $('<td></td>').append(link);
		row.append(rowData);
		rowData = $('<td></td>').text(
			result[i].quantidade + "x R$ "
				+ Number(result[i].precoVenda).toFixed(2));
		row.append(rowData);
		rowData = $('<td></td>').text(
			"R$ "
				+ Number(
					result[i].precoVenda
						* result[i].quantidade)
					.toFixed(2));
		total = total + result[i].precoVenda * result[i].quantidade;
		row.append(rowData);
		rowData = $("<td></td>");
		if (result[i].quantidade > 1) {
		    var a = $("<a />", {
			onclick : "down(" + result[i].idProdutoOriginal + ")",
			text : "-"
		    }).addClass("btn btn-warning");
		    rowData.append(a);
		}
		var a = $("<a />", {
		    text : "+",
		    onclick : "add(" + result[i].idProdutoOriginal + ")"
		}).addClass("btn btn-warning");
		rowData.append(a);
		row.append(rowData);
		rowData = $("<td></td>");
		var a = $("<a />", {
		    onclick : "remover(" + result[i].idProdutoOriginal + ")",
		}).addClass("btn btn-danger").append($("<img/>", {
		    src : "/images/trash.png",
		    width : '16',
		    heigth : '16'
		}));
		rowData.append(a);
		row.append(rowData);
		table.append(row);
	    }
	    $("#tabelaCesta").append(table);

	    $(".total").text("R$ " + Number(total).toFixed(2));
	    $("#valor").val(Number(total).toFixed(2));
	    $("#saldoI").val(Number(total).toFixed(2));
	    if (cesta != "") {
		$("#saldo").text("R$ " + Number(caixa.saldo).toFixed(2));
		$("#valor").val(Number(caixa.saldo).toFixed(2));
		$("#saldoI").val(Number(caixa.saldo).toFixed(2));
	    }
	    sessionStorage.setItem("compras", result.length);
	    compras(sessionStorage.getItem("compras"));
	}
    });
}
function maior_que(valor1, valor2) {
    valor1 = valor1.split(".");
    valor2 = valor2.split(".");
    let num1 = Number(valor1[0]);
    let num2 = Number(valor2[0]);
    if (num1 <= num2)
	return true;
    let num3 = Number(valor1[1]);
    let num4 = Number(valor2[1]);
    if ((num1 == num2) && (num3 <= num4))
	return true;
    return false;
}