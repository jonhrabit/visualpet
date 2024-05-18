function form_mes_ano_clientes(div_name) {
	$(div_name).html("");
	var div = $("<div class='col-4'></div>");
	let d = new Date();
	div.append("Mês: <input id='mes' type='text' class='form-control' value='"
			+ (d.getMonth() + 1).toString().padStart(2, '0') + "'/>");
	div.append("Ano: <input id='ano' type='text' class='form-control'  value='"
			+ d.getFullYear() + "'/>");
	var div2 = $("<div class='row'></div>")

	div2
			.append("<div class='m-3'><button onclick='atendimentos_por_cliente()' class='btn btn-primary'>Consultar</button></div>");
	div2
			.append("<div class='m-3'><button onclick='atendimentos_por_cliente_grafico()' class='btn btn-primary'>Gráfico</button></div>");
	div.append(div2);
	$(div_name).append("<h4>Consulta Atendimentos por Clientes</h4>");
	$(div_name).append(div);
}
function atendimentos_por_cliente() {
	var mes = $("#mes").val();
	var ano = $("#ano").val();
	$
			.ajax({
				url : "/consultas/atendimentosporcliente/" + mes + "/" + ano,
				success : function(result) {
					$("#resultado").html("");
					$("#resultado").append(
							"<h3>Atendimentos por Cliente de " + mes + "/"
									+ ano + "</h3>");
					var div = $("<div class='m-4'></div>");
					var table = $("<table id='tabela'></table>");
					div.append(table);
					$("#resultado").append(div);
					var tabela_teste = $("#tabela")
							.DataTable(
									{
										data : result,
										"language" : {
											"url" : "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Portuguese-Brasil.json"
										},
										columns : [ {
											data : "cliente",
											title : "Cliente"
										}, {
											data : "pet",
											title : "Pet"
										}, {
											data : "atendimentos",
											title : "Atendimentos"
										} ]
									});
				}
			});
}
function atendimentos_por_cliente_grafico() {
	alert("Em desemvolvimento.");
	/*var mes = $("#mes").val();
	var ano = $("#ano").val();
	$
			.ajax({
				url : "/consultas/atendimentosporcliente/" + mes + "/" + ano,
				success : function(result) {
					$("#resultado").html("");
					console.log(result);
					$("#resultado")
							.append(
									$("<div id='chart' class='m-2 border border-dark'></div>"));

					google.charts.load('current', {
						'packages' : [ 'corechart' ]
					});
					google.charts.setOnLoadCallback(drawChart);

					function drawChart() {

						var data = google.visualization.arrayToDataTable(result);

						var options = {
							title : "Vendas de " + mes + "/" + ano
						};

						var chart = new google.visualization.BarChart(document
								.getElementById("chart"));

						chart.draw(data, options);
					}
				}
			});*/
}