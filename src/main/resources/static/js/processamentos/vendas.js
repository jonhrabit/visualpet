function form_vendas() {
    $("#resultado").html("");
    var div = $("<div class='col-4'></div>");
    let d = new Date();
    div.append("Mês: <input id='mes' type='text' class='form-control' value='"
	    + (d.getMonth() + 1).toString().padStart(2, '0') + "'/>");
    div.append("Ano: <input id='ano' type='text' class='form-control'  value='"
	    + d.getFullYear() + "'/>");
    var div2 = $("<div class='row'></div>");
    div2
	    .append("<div class='m-3'><button onclick='vendas_mes()' class='btn btn-primary'>Consultar</button></div>");
    div2
	    .append("<div class='m-3'><button onclick='vendas_mes_grafico()' class='btn btn-primary'>Gráficos</button></div>");
    div.append(div2);
    $("#resultado").append("<h4>Vendas</h4>");
    $("#resultado").append(div);
}

function vendas_mes() {
    var mes = $("#mes").val();
    var ano = $("#ano").val();
    $
	    .ajax({
		url : "/consultas/vendas/" + mes + "/" + ano,
		success : function(result) {
		    $("#resultado").html("");
		    $("#resultado").append(
			    "<h3>Vendas de " + mes + "/" + ano + "</h3>");
		    console.log(result);
		    var div = $("<div class='m-4'></div>");
		    let table = $("<table id='tabela'></table>");
		    div.append(table);
		    $("#resultado").append(div);
		    let tabela_teste = $("#tabela")
			    .DataTable(
				    {
					data : result.produtos,
					"language" : {
					    "url" : "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Portuguese-Brasil.json"
					},
					"scrollY" : "380px",
					"scrollCollapse" : true,
					"paging" : false,
					dom : '<"top"if>rCt<"footer">',
					columns : [
						{
						    data : "nome",
						    title : "Nome"
						},
						{
						    data : "tipo",
						    title : "Tipo"
						},
						{
						    data : "precoVenda",
						    title : "Preço",
						    render : $.fn.dataTable.render
							    .number(',', '.',
								    2, 'R$ '),

						} ],
					footerCallback : function(row, data,
						start, end, display) {
					    var api = this.api(), data;
					    var intVal = function(i) {
						return typeof i === 'string' ? i
							.replace(/[\$,]/g, '') * 1
							: typeof i === 'number' ? i
								: 0;
					    };
					    let total = api
						    .column(2)
						    .data()
						    .reduce(
							    function(a, b) {
								return intVal(a)
									+ intVal(b);
							    }, 0);
					    let pageTotal = api.column(2, {
						page : 'current'
					    }).data().reduce(function(a, b) {
						return intVal(a) + intVal(b);
					    }, 0);
					    $('.footer').html(
						    '<div>Filtrado R$ '
							    + Number(pageTotal)
								    .toFixed(2)
							    + '     Total R$ '
							    + total.toFixed(2)
							    + '</div>');
					}
				    });
		}
	    });
}
function vendas_mes_grafico() {
    dialogo_loading();
    var mes = $("#mes").val();
    var ano = $("#ano").val();
    $
	    .ajax({
		url : "/consultas/vendas/" + mes + "/" + ano,
		success : function(result) {
		    console.log("---------CONSULTAS/VENDAS/-----------");
		    console.log(result);
		    $("#resultado").html("");
		    $("#resultado")
			    .append(
				    $("<h3>Vendas do mês " + mes + "/" + ano
					    + "</h3>"));
		    var div = $("<div class='row'></div>");
		    div.append($("<div id='chart1' class='m-2'></div>"));
		    div.append($("<div id='chart2' class='m-2'></div>"));
		    div.append($("<div id='chart3' class='m-2'></div>"));
		    div.append($("<div id='chart4' class='m-2'></div>"));

		    $("#resultado").append(div);
		    $("#resultado")
			    .append(
				    $("<div id='chart4' class='m-2 border border-dark'></div>"));

		    google.charts.load('current', {
			'packages' : [ 'corechart' ]
		    });
		    google.charts.setOnLoadCallback(drawChart);

		    function drawChart() {

			var data = google.visualization.arrayToDataTable([
				[ 'Tipo', 'Valor' ],
				[ 'Produtos  - R$ ' + result.totalProdutos,
					result.sumProduto ],
				[ 'Serviços  - R$ ' + result.totalServicos,
					result.sumServico ] ]);

			var options = {
			    title : "Produtos/Serviços",
			    width : 400,
			    height : 400
			};

			var chart = new google.visualization.PieChart(document
				.getElementById("chart1"));

			chart.draw(data, options);

			data = google.visualization.arrayToDataTable([
				[ 'Setor', 'Valor' ],
				[ 'Estética  - R$ ' + result.totalPetShop,
					result.totalPetShop ],
				[ 'Clinica  - R$ ' + result.totalClinica,
					result.totalClinica ] ]);

			options = {
			    title : "Setor"
			};

			chart = new google.visualization.PieChart(document
				.getElementById("chart2"));

			chart.draw(data, options);

			var data = new google.visualization.DataTable();

			data.addColumn('string', 'Forma de Pagamento');
			data.addColumn('number', 'Bruto');

			for (var i = 0; i < result.totaisVendas.length; i++) {
			    data.addRow([ result.totaisVendas[i].forma,
				    result.totaisVendas[i].bruto]);
			}

			options = {
			    title : "Bruto",
			    legend: 'none',
			    isStacked : true
			};

			chart = new google.visualization.BarChart(document
				.getElementById("chart3"));

			chart.draw(data, options);

			var data = new google.visualization.DataTable();

			data.addColumn('string', 'Forma de Pagamento');
			data.addColumn('number', 'Liquido');

			for (var i = 0; i < result.totaisVendas.length; i++) {
			    data.addRow([ result.totaisVendas[i].forma,
				    result.totaisVendas[i].liquido ]);
			}

			options = {
			    title : "Líquido",
			    legend: 'none',
			    isStacked : true
			};

			chart = new google.visualization.BarChart(document
				.getElementById("chart4"));

			chart.draw(data, options);

		    }
		}
	    });
    $("#dialogo").modal("hide");
}