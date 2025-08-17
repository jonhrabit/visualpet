function criar_tabela_tutor(dados, name_div) {
	$(name_div).html("");
	var table = $('<table></table>').addClass("table");
	var row;
	for (var i = 0; i < dados.length; i++) {
		row = $('<tr></tr>');

		rowData = $('<td></td>').text(dados[i].nome);
		row.append(rowData);

		rowData = $('<td></td>').text(dados[i].celular);
		row.append(rowData);

		var link = $(
				"<a />",
				{
					text : "Selecionar",
					href : "#",
					onclick : "$('#modal_tabela').modal('hide');"
							+ "selecionar_tutor(" + dados[i].id + ",\""
							+ dados[i].nome + "\",\"" + dados[i].celular
							+ "\")"
				}).addClass("btn btn-primary");

		var rowData = $('<td></td>').append(link);
		row.append(rowData);

		table.append(row);
	}
	$(name_div).append(table);
	let btn_novo = $(
			"<a />",
			{
				text : "Novo Tutor",
				href : "#",
				onclick : "$('#modal_tabela').modal('hide');"
						+ "$('#celular').focus();" + "$('#idTutor').val('0')"
			}).addClass("btn btn-primary");
	$(name_div).append(btn_novo);
	$("#modal_tabela").modal("show");
}
function criar_tabela_pet(dados, name_div) {
	$(name_div).html("");
	var table = $('<table></table>').addClass("table");
	var row;
	for (var i = 0; i < dados.length; i++) {
		row = $('<tr></tr>');

		rowData = $('<td></td>').text(dados[i].nome);
		row.append(rowData);

		rowData = $('<td></td>').text(dados[i].tutor.nome);
		row.append(rowData);

		rowData = $('<td></td>').text(dados[i].especie);
		row.append(rowData);

		rowData = $('<td></td>').text(dados[i].raca);
		row.append(rowData);

		var link = $(
				"<a />",
				{
					text : "Selecionar",
					href : "#",
					onclick : "$('#modal_tabela').modal('hide');"
							+ "selecionar_pet(\"" + dados[i].nome + "\"," + dados[i].tutor.id + ",\""
							+ dados[i].tutor.nome + "\",\"" + dados[i].tutor.celular + "\")"
				}).addClass("btn btn-primary");

		var rowData = $('<td></td>').append(link);
		row.append(rowData);
		table.append(row);
	}
	$(name_div).append(table);
	let btn_novo = $(
			"<a />",
			{
				text : "Novo Pet",
				href : "#",
				onclick : "$('#modal_tabela').modal('hide');"
						+ "$('#celular').focus();" + "$('#idTutor').val('0')"
			}).addClass("btn btn-primary");
	$(name_div).append(btn_novo);
	$("#modal_tabela").modal("show");
}

function selecionar_pet(nome,idTutor,nomeTutor,celularTutor) {
	$("#pet").val(nome);
	$("#idTutor").val(idTutor);
	$("#tutor").val(nomeTutor);
	$("#celular").val(celularTutor);
	
	if ($("#tutor").val() != "") {
		$("#valor").focus();
	} else {
		consultar_tutor_pelo_nome_pet();
	}
}
function selecionar_tutor(id, nome, celular) {
	$("#idTutor").val(id);
	$("#tutor").val(nome);
	$("#celular").val(celular);

	if ($("#pet").val() == "") {

		$.ajax({
			url : "/pet/consulta_id",
			data : {
				idTutor : id
			},
			success : function(result, textStatus) {
				if (textStatus == "success") {
					criar_tabela_pet(result, ".modal-body");
				} else {
					$("#pet").val("Nenhum");
					$("#pet").focus();
					$("#modal_tabela").modal("hide");
				}
			}
		});
	} else {
		$("#valor").focus();
	}
}
function consultar_pet_pelo_nome() {
	if ($("#pet").val() != "") {
		$.ajax({
			url : "/pet/consulta",
			data : {
				nome : $('#pet').val()
			},
			success : function(result) {
				criar_tabela_pet(result, ".modal-body");
			}
		});
	}
}
function consultar_tutor_pelo_nome_pet() {
	$.ajax({
		url : "/cliente/consulta_nome_pet",
		data : {
			nome : $('#pet').val()
		},
		success : function(result) {
			criar_tabela_tutor(result, ".modal-body");
		}
	});
}
function consultar_tutor() {
	if ($("#tutor").val() != "") {
		$.ajax({
			url : "/cliente/consulta",
			data : {
				nome : $('#tutor').val()
			},
			success : function(result) {
				criar_tabela_tutor(result, ".modal-body");
			}
		});
	}
}
function consultar_pet() {
	$.ajax({
		url : "/pet/consulta_id",
		data : {
			nome : $('#idTutor').val()
		},
		success : function(result) {
			criar_tabela_pet(result, ".modal-body");
		}
	});
}

function filtro() {
	$("#tutorFiltro").modal("show");
}
function modificado(tipo) {
	$('#tutorFiltro').modal('hide');
	atualizar(tipo);
}

function dialogo_deletar(id) {
	var texto = "Tem certeza da exclusão do atendimento?";
	texto = texto + "<p><p>";
	texto = texto + '<div class="row justify-content-between">';
	texto = texto + '<div class="col-5">';
	texto = texto
			+ "<a class='btn btn-primary' href='#' onclick='$(\"#modal_tabela\").modal(\"hide\")'>Não</a>";
	texto = texto + '</div><div class="col-2">';
	texto = texto + "<a class='btn btn-danger' href='/atendimento/deletar/"
			+ id + "'>Sim</a></div>";

	$(".modal-body").html(texto);
	$("#modal_tabela").modal("show");
}
function dialogo_confiar(id) {
	var texto = "Tem certeza em confiar o pagamento?";
	texto = texto + "<p><p>";
	texto = texto + '<div class="row justify-content-between">';
	texto = texto + '<div class="col-5">';
	texto = texto
			+ "<a class='btn btn-primary' href='#' onclick='$(\"#modal_tabela\").modal(\"hide\")'>Não</a>";
	texto = texto + '</div><div class="col-2">';
	texto = texto + "<a class='btn btn-danger' href='#' onclick='$(\"#modal_tabela\").modal(\"hide\");confiar("+id+")'>Sim</a></div>";

	$(".modal-body").html(texto);
	$("#modal_tabela").modal("show");
}
function dialogo_cancelar(id) {
	var texto = "Tem certeza em cancelar o atendimento?";
	texto = texto + "<p><p>";
	texto = texto + '<div class="row justify-content-between">';
	texto = texto + '<div class="col-5">';
	texto = texto
			+ "<a class='btn btn-primary' href='#' onclick='$(\"#modal_tabela\").modal(\"hide\")'>Não</a>";
	texto = texto + '</div><div class="col-2">';
	texto = texto + "<a class='btn btn-danger' href='#' onclick='$(\"#modal_tabela\").modal(\"hide\");cancelar("+id+")'>Sim</a></div>";

	$(".modal-body").html(texto);
	$("#modal_tabela").modal("show");
}
function tabela(dados, name_div){
	var semana = ["Domingo", "Segunda-Feira", "Terça-Feira", "Quarta-Feira", "Quinta-Feira", "Sexta-Feira", "Sábado"];
	$(name_div).html("");
	var table = $('<table></table>').addClass("table");
	let row, d, dataFormatada, ultimaData, link;
	
	for (var i = 0; i < dados.length; i++) {
		d = new Date(dados[i].agenda);
		dataFormatada = d.getDate().toString().padStart(2, '0') + '/'
				+ (d.getMonth() + 1).toString().padStart(2, '0') + '/'
				+ d.getFullYear()+" - "+semana[d.getDay()] ;
		
		if((ultimaData==0)||(ultimaData!=dataFormatada)){
			row = $('<tr class="bg-dark text-white" COLSPAN="6"><td class="font-weight-bold" COLSPAN="11" align="center">'+dataFormatada+'</td></tr>');
			ultimaData=dataFormatada;
			table.append(row);
		}
		row = $('<tr id="atendimento'+dados[i].id+'"></tr>');
		
		d = new Date(dados[i].agenda);
		dataFormatada = d.getDate().toString().padStart(2, '0') + '/'
				+ (d.getMonth() + 1).toString().padStart(2, '0') + '/'
				+ d.getFullYear() + " "
				+ d.getHours().toString().padStart(2, '0') + ":"
				+ d.getMinutes().toString().padStart(2, '0');
		link = $("<a />", {
			href : "/atendimento/" + dados[i].id,
			text : dataFormatada
		});
		var rowData = $('<td></td>').append(link);
		row.append(rowData);

		rowData = $('<td></td>').text(dados[i].servico.texto);
		row.append(rowData);

		link = $("<a />", {
			href : "/pet/" + dados[i].pet.id,
			text :dados[i].pet.nome
		});
		rowData = $('<td></td>').append(link);
		
		rowData.append('<p class="d-sm-block d-md-none"><a class="text-muted"'+
			'href="/cliente/'+dados[i].pet.tutor.id+'">'+dados[i].pet.tutor.nome+'</a></p>');
		row.append(rowData);
		
		link = $("<a />", {
			href : "/cliente/" + dados[i].pet.tutor.id,
			text :dados[i].pet.tutor.nome
		});
		rowData = $('<td></td>').append(link).addClass("d-none d-md-table-cell");
		row.append(rowData);
		
		
		rowData = $('<td></td>').text(dados[i].pet.tutor.celular).addClass("d-none d-md-table-cell");
		row.append(rowData);
		
		rowData = $('<td></td>').text("R$ "+ dados[i].valor).addClass("d-none d-md-table-cell");
		if(dados[i].pacote!=null){
				rowData.append('<a class="badge" href="/pacote/'+dados[i].pacote.id+'"> <img '+
				'src="/images/atendimentos/medalha.png" height="20" width="20"></a>');
		}
		if(dados[i].produtos.length>0){
			if(dados[i].venda==true){
				rowData.append('<a class="badge badge-warning" href="/atendimento/produtos/'+dados[i].id+'"> <img '+
						'src="/images/itens.png" height="20" width="20"></a>');
			}else{
				rowData.append('<a class="badge" href="/atendimento/produtos/'+dados[i].id+'"> <img '+
				'src="/images/itens.png" height="20" width="20"></a>');
			}
		}
		row.append(rowData);
		
		let menu ='<div class="dropdown dropleft" id="atendimento_menu_'+dados[i].id+'">'+
				'<a class="btn dropdown-toggle" href="#" role="button" id="dropdownMenuLink" data-toggle="dropdown"'+
				'aria-haspopup="true" aria-expanded="false"><img width="12"	height="12" src="/images/menu.png"></a>'+
				'<div class="dropdown-menu" aria-labelledby="dropdownMenuLink">'+
				'<a href="#" class="dropdown-item" id="iniciar_atendimento_'+dados[i].id+'" onclick="iniciar('+dados[i].id+')"><img width="16"	height="16" src="/images/iniciar.png">Iniciar </a>'+
				'<a href="#" class="dropdown-item" onclick="add_servico('+dados[i].id+')"> <img width="16"	height="16" src="/images/shopping_car.png">Finalizar </a>'+
				'<a href="#" class="dropdown-item" onclick="dialogo_confiar('+dados[i].id+')"> <img width="16"	height="16" src="/images/pendurar.png">Confiar </a>'+
				'<a href="#" class="dropdown-item" onclick="dialogo_cancelar('+dados[i].id+')"> <img width="16"	height="16" src="/images/cancelar.png">Cancelar </a>'+
				'<a class="dropdown-item" href="/atendimento/produtos/'+dados[i].id+'"> <img width="16"	height="16" src="/images/itens.png">Itens </a>'+
				'<a class="dropdown-item" href="/atendimento/'+dados[i].id+'"> <img width="16"	height="16" src="/images/editar.png">Editar</a>'+
				'<a href="#" class="dropdown-item" onclick="dialogo_deletar('+dados[i].id+')"> <img width="16"	height="16" src="/images/trash.png">Excluir </a>'+
				'</div></div>';
		
		if(dados[i].situacao=='FINALIZADO'){
			row.addClass('table-warning');
			rowData = $('<td id="atendimento_status_'+dados[i].id+'"></td>').html("<span class='text-warning'>Finalizado</span>");
			row.append(rowData);
		}else if(dados[i].situacao=='CANCELADO'){
			rowData = $('<td id="atendimento_status_'+dados[i].id+'"></td>').html("<span class='text-danger'>Cancelado</span>");
			row.append(rowData);
		}else if(dados[i].situacao=='ATENDIMENTO'){
			row.addClass('table-primary');
			rowData = $('<td id="atendimento_status_'+dados[i].id+'"></td>').append(menu);
			row.append(rowData);
		}else if(dados[i].situacao=='PENDENTE'){
			row.addClass('table-warning');
			rowData = $('<td id="atendimento_status_'+dados[i].id+'"></td>').html("<span class='text-danger'>Confiado</span>");
			row.append(rowData);
		}else{
			rowData = $('<td id="atendimento_status_'+dados[i].id+'"></td>').append(menu);
			row.append(rowData);
		}
		table.append(row);
	}
	$(name_div).append(table);
}