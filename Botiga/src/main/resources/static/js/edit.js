jQuery(document).ready(function(){
	
	$(".editBtn").click(function(e){
		
		e.preventDefault();
		
		var nombre = $(this).closest("tr").children(":nth-child(1)").html();
		var precio = $(this).closest("tr").children(":nth-child(8)").html();
		var genero = $(this).closest("tr").children(":nth-child(2)").html();
		var distribuidora = $(this).closest("tr").children(":nth-child(3)").html();
		var plataforma = $(this).closest("tr").children(":nth-child(4)").html();
		var edad = $(this).closest("tr").children(":nth-child(5)").html();
		var cantidad = $(this).closest("tr").children(":nth-child(6)").html();
		var activar = $(this).closest("tr").children(":nth-child(7)").html();
		var id = $(this).closest("tr").data("id");
		
		$("#nom_juego").val(nombre);
		$("#pre_juego").val(precio);
		$("#gen_juego").val(genero);
		$("#dist_juego").val(distribuidora);
		$("#plat_juego").val(plataforma);
		$("#edad_rec").val(edad);
		$("#cant_juego").val(cantidad);
		$("#activar_juego").val(activar);
		$("#hidden_id").val(id);
		
	});
});