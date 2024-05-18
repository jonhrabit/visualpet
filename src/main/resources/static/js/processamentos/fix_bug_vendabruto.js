function fix(){
	$.ajax({
		url : "/caixa/fix",
		success : function(result) {
			alert(result+ " Registros de caixa sem valor bruto reparados.")
		}
	});
}

