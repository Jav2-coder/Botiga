jQuery(document).ready(function(){
	
	$(".editBtn").click(function(e){
		
		e.preventDefault();
		
		var nombre = $(this).closest("tr").children(":nth-child(1)").text();
		var precio = null;
		var genero = null;
		var distribuidora = null;
		var plataforma = null;
		var edad = null;
		var cantidad = null;
		var imagen = null;
		var activar = null;
		
		console.log(nombre);
		
	});
});