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
		
		console.log(edad)
		
		$("#nom_juego").append(nombre);
		$("#pre_juego").append(precio);
		$("#generos").append(genero);
		$("#distribuidoras").append(distribuidora);
		$("#plataformas").append(plataforma);
		$("#edad_rec").append(edad);
		$("#cant_juego").append(cantidad);
		$("#activar_juego").append(activar);
		
	});
});