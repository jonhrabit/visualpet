console.log("--------NFE ON------");
var lote = [];

function form_lote() {
    $("#resultado").html("");
    var div = $("<div class='col-8'></div>");
    let d = new Date();
    div.append("Dia: <input id='dia' type='date' class='form-control'/>");
    var div2 = $("<div class='row'></div>");
    div2
	    .append("<div class='m-3'><button onclick='data()' class='btn btn-primary'>Consultar</button></div>");
    div.append(div2);
    $("#resultado").append("<h4>Gerar Lote</h4>");
    $("#resultado").append(div);
}

function enviar_lote() {
    dialogo_loading();
    console.log(JSON.stringify(lote));
    $.ajax({
	type : 'POST',
	url : "/nfe/enviarlote?_csrf=" + $("#crsf").val(),
	dataType : "json",
	contentType : "application/json",
	processData : false,
	data : JSON.stringify(lote),
	success : function(result) {
	    console.log(result);

	    $("#dialogo").modal("hide");
	    $("#resultado").html("");

	    $("#resultado").append("<h4>Lote enviado</h4>");
	    $("#resultado").append(
		    "<div>Ambiente: " + result.retorno.ambiente + "</div>");
	    $("#resultado").append(
		    "<div>Recibo: " + result.retorno.infoRecebimento.recibo
			    + "</div>");
	    $("#resultado").append(
		    "<div>Mensagem: " + result.retorno.status + " - "
			    + result.retorno.motivo + "</div>");

	    lote = [];
	    atualizar_lote();

	},
	error : function() {
	    $("#dialogo").modal("hide");
	    alert("Ocorreu um erro ao enviar o lote.");
	}

    });
}
function data() {
    $
	    .ajax({
		url : "/nfe/consultardata/" + $("#dia").val(),
		success : function(result) {
		    console.log(result);
		    $("#tabela").remove();
		    let tab = $("<table class='table' id='tabela'></table>");
		    let row = $("<tr></tr>");
		    row.append("<td>Id</td>");
		    row.append("<td>Data</td>");
		    tab.append(row);
		    for (var i = 0; i < result.length; i++) {
			row = $("<tr></tr>");
			row.append("<td>" + result[i].id + "</td>");
			let d = new Date(result[i].data);
			let dataFormatada = d.getDate().toString().padStart(2,
				'0')
				+ '/'
				+ (d.getMonth() + 1).toString()
					.padStart(2, '0')
				+ '/'
				+ d.getFullYear()
				+ " "
				+ d.getHours().toString().padStart(2, '0')
				+ ":"
				+ d.getMinutes().toString().padStart(2, '0');
			row.append("<td>" + dataFormatada + "</td>");
			row.append("<td>" + result[i].cliente.nome + "</td>");
			row
				.append("<td><div class='btn btn-primary' onclick='add_lote(\""
					+ result[i].id + "\")'>+</div></td>");
			tab.append(row);
		    }
		    $("#resultado").append(tab);
		}
	    });
}
function add_lote(id) {
    $.ajax({
	url : "/cesta/rest/" + id,
	success : function(result) {
	    lote.push(result);
	    atualizar_lote();
	}
    });
}
function atualizar_lote() {
    $("#resultado2").html("");
    $("#resultado2").removeClass("alert alert-danger");
    $("#resultado2").removeClass("alert alert-primary");
    $("#resultado2")
	    .append(
		    "<div class='row'><h4 class='col-8'>Lote</h4><div class='btn btn-primary' onclick='enviar_lote()'>Enviar para a RF</div></div");
    for (var i = 0; i < lote.length; i++) {
	let div = $("<div class='row mt-4'></div>");
	div.append("<div class='col-1'><a href='/cesta/" + lote[i].id + "'>"
		+ lote[i].id + "</a></div>");
	let d = new Date(lote[i].data);
	let dataFormatada = d.getDate().toString().padStart(2, '0') + '/'
		+ (d.getMonth() + 1).toString().padStart(2, '0') + '/'
		+ d.getFullYear() + " "
		+ d.getHours().toString().padStart(2, '0') + ":"
		+ d.getMinutes().toString().padStart(2, '0');
	div.append("<div class='col-4'>" + dataFormatada + "</div>");
	div.append("<div class='col-5'><a href='/cliente/" + lote[i].cliente.id
		+ "'>" + lote[i].cliente.nome + "</a></div>");
	div.append("<div class='col-1 btn btn-danger' onclick='remover_lote(\""
		+ lote[i].id + "\")'>X</div>");
	$("#resultado2").append(div);
	div = $("<div></div>");
	let add = false;
	if (lote[i].cliente.cpf == null || lote[i].cliente.cpf == "") {
	    div.append("<div class='d-inline alert alert-danger'>CPF</div>");
	    add = true;
	}
	if (lote[i].cliente.email == null || lote[i].cliente.email == "") {
	    div.append("<div class='d-inline alert alert-danger'>Email</div>");
	    add = true;
	}
	if (lote[i].cliente.bairro == null || lote[i].cliente.bairro == "") {
	    div.append("<div class='d-inline alert alert-danger'>Bairro</div>");
	    add = true;
	}
	if (lote[i].cliente.endereco == null || lote[i].cliente.endereco == "") {
	    div
		    .append("<div class='d-inline alert alert-danger'>Endereço</div>");
	    add = true;
	}

	if (add == true) {
	    $("#resultado2").append(div);
	}
    }
}
function remover_lote(id) {
    for (let i = 0; i < lote.length; i++) {
	if (lote[i].id == id) {
	    lote.splice(i, 1);
	    break;
	}
    }
    atualizar_lote();
}

function processamento() {
    $
	    .ajax({
		url : "/nfe/processamento",
		success : function(result) {
		    $("#resultado").html("");
		    $("#resultado").append("<h4>Lotes em Processamento</h4>");
		    for (let i = 0; i < result.length; i++) {
			let div = $("<div class='row'></div>");
			div
				.append("<div class='col-6 m-2 btn btn-primary' onclick='processar(\""
					+ result[i].split(".")[0].split("-")[0]
					+ "\")'>Lote "
					+ result[i].split(".")[0] + "</div>");
			$("#resultado").append(div);
		    }
		}
	    });
}

function dialogo_loading() {
    console.log($("#dialogo").length);
    if ($("#dialogo").length) {
	$("#dialogo").remove();
    }
    let conteudo = '<div class="d-flex align-items-center"><strong class="text-warning">Aguarde...</strong><div class="spinner-border  text-warning ml-auto" role="status" aria-hidden="true"></div></div>';
    let texto = '<div class="modal" id="dialogo" data-backdrop="static"><div class="modal-dialog modal-sm"><div class="modal-content">';
    texto = texto + '<div class="modal-body">' + conteudo + '</div>';
    texto = texto + '</div></div></div>';
    $("body").append(texto);
    $("#dialogo").modal("show");
}
function processar(lote) {
    $
	    .ajax({
		url : "/nfe/processar/" + lote,
		success : function(result) {
		    $("#resultado").html("");

		    $("#resultado").append("<h4>Processamento do Lote</h4>");
		    $("#resultado").append(
			    "<div>Ambiente: " + result.ambiente + "</div>");
		    $("#resultado").append(
			    "<div>Recibo: " + result.numeroRecibo + "</div>");
		    $("#resultado").append(
			    "<div>Mensagem: " + result.motivo + "</div>");
		    for (let i = 0; i < result.protocolos.length; i++) {
			let div = $("<div></div>");
			if (result.protocolos[i].protocoloInfo.status == "100") {
			    div.addClass('p-2 m-2 border border-primary');
			} else {
			    div.addClass('p-2 m-2 border border-danger');
			}
			let d = result.protocolos[i].protocoloInfo.dataRecebimento[2]
				+ "/"
				+ result.protocolos[i].protocoloInfo.dataRecebimento[1]
				+ "/"
				+ result.protocolos[i].protocoloInfo.dataRecebimento[0]
				+ " "
				+ result.protocolos[i].protocoloInfo.dataRecebimento[3]
				+ ":"
				+ result.protocolos[i].protocoloInfo.dataRecebimento[4];

			div.append("<div>Data Recebimento: " + d + "</div>");

			div.append("<div>Chave: "
				+ result.protocolos[i].protocoloInfo.chave
				+ "</div>");
			div.append("<div>Mensagem: "
				+ result.protocolos[i].protocoloInfo.status
				+ " - "
				+ result.protocolos[i].protocoloInfo.motivo
				+ "</div>");
			$("#resultado").append(div);
		    }
		}
	    });
}
function consultar_notas() {
    $
	    .ajax({
		url : "/nfe/notas",
		success : function(result) {
		    $("#resultado").html("");
		    $("#resultado").append("<h4>Notas Arquivadas</h4>");
		    let tabela_notas = $("<table id='tabela'></table>");
		    $("#resultado").append(tabela_notas);
		    let dados = [];
		    let nota = {};
		    for (let i = 0; i < result.length; i++) {
			nota = {
			    chave : result[i].split(".")[0]
			}

			dados.push(nota);
		    }
		    $("#tabela")
			    .DataTable(
				    {
					data : dados,
					"language" : {
					    "url" : "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Portuguese-Brasil.json"
					},
					columns : [
						{
						    title : "Nota",
						    data : "chave"
						},
						{
						    title : "",
						    data : "chave",
						    render : function(data,
							    type, row) {
							return "<a class='btn btn-primary' href='\\xml\\notas\\"
								+ data
								+ ".xml' target='_blank'>Exibir</a>";
						    }
						},
						{
						    title : "",
						    data : "chave",
						    render : function(data,
							    type, row) {
							return "<div class='btn btn-secondary' onclick='detalhes(\""
								+ data
								+ "\")'>Detalhes</div>";
						    }
						} ]
				    });
		}
	    });
}

function detalhes(nfe) {
    $
	    .ajax({
		url : "/nfe/nota/" + nfe,
		success : function(result) {
		    $("#resultado2").html("");
		    $("#resultado2").append("<h4>Nota</h4>");
		    $("#resultado2")
			    .append(
				    "<div>" + result.nota.info.identificador
					    + "</div>");

		    let tabela = $("<table class='table>'></table>");
		    let linha = $("<tr></tr>");

		    linha.append("<td class='pt-4'>Cesta</td>");
		    linha.append("<td class='pt-4'><a href='/cesta/"
			    + result.nota.info.identificacao.numeroNota + "'>"
			    + result.nota.info.identificacao.numeroNota
			    + "</a></td>");
		    tabela.append(linha);

		    linha = $("<tr></tr>");
		    linha.append("<td>Data</td>");
		    linha
			    .append("<td>"
				    + DATA(result.nota.info.identificacao.dataHoraEmissao)
				    + "</td>");
		    tabela.append(linha);

		    linha = $("<tr></tr>");
		    linha.append("<td>Modelo</td>");
		    linha.append("<td>" + result.nota.info.identificacao.modelo
			    + "</td>");
		    tabela.append(linha);

		    linha = $("<tr></tr>");
		    linha.append("<td>Natureza</td>");
		    linha.append("<td>"
			    + result.nota.info.identificacao.naturezaOperacao
			    + "</td>");
		    tabela.append(linha);

		    linha = $("<tr></tr>");
		    linha.append("<td>tipo</td>");
		    linha.append("<td>" + result.nota.info.identificacao.tipo
			    + "</td>");
		    tabela.append(linha);

		    linha = $("<tr></tr>");
		    linha.append("<td class='pt-4'>Destinatário</td>");
		    linha.append("<td class='pt-4'>"
			    + result.nota.info.destinatario.razaoSocial
			    + "</td>");
		    tabela.append(linha);

		    linha = $("<tr></tr>");
		    linha.append("<td>CPF</td>");
		    linha.append("<td>" + result.nota.info.destinatario.cpfj
			    + "</td>");
		    tabela.append(linha);

		    linha = $("<tr></tr>");
		    linha.append("<td class='pt-4'>Total</td>");
		    linha.append("<td class='pt-4'>R$ "
			    + result.nota.info.total.icmsTotal.valorTotalNFe
			    + "</td>");
		    tabela.append(linha);

		    linha = $("<tr></tr>");
		    linha.append("<td class='pt-4'>Código</td>");
		    linha.append("<td class='pt-4'>"
			    + result.protocolo.protocoloInfo.status + "</td>");
		    tabela.append(linha);

		    linha = $("<tr></tr>");
		    linha.append("<td>Motivo</td>");
		    linha.append("<td>" + result.protocolo.protocoloInfo.motivo
			    + "</td>");
		    tabela.append(linha);

		    if (result.protocolo.protocoloInfo.status == '100') {
			$("#resultado2").addClass("alert alert-primary");
			$("#resultado2")
				.append(
					"<div class='m-2 btn btn-primary' onclick='alert(\"Em desenvolvimento.\")'>Cancelar</div>");
			$("#resultado2")
				.append(
					"<div class='m-2 btn btn-primary' onclick='alert(\"Em desenvolvimento.\")'>Corrigir</div>");
		    } else {
			$("#resultado2").addClass("alert alert-danger");
			$("#resultado2")
				.append(
					"<div class='m-2 btn btn-primary' onclick='alert(\"Em desenvolvimento.\")'>Corrigir</div>");
			$("#resultado2")
				.append(
					"<div class='m-2 btn btn-primary' onclick='alert(\"Em desenvolvimento.\")'>Cancelar</div>");
		    }

		    $("#resultado2").append(tabela);

		}
	    });

}
function DATA(data) {
    let d = new Date(data);
    let dataFormatada = d.getDate().toString().padStart(2, '0') + '/'
	    + (d.getMonth() + 1).toString().padStart(2, '0') + '/'
	    + d.getFullYear() + " " + d.getHours().toString().padStart(2, '0')
	    + ":" + d.getMinutes().toString().padStart(2, '0');
    return dataFormatada;
}