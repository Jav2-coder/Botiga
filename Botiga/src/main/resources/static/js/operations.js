jQuery(document).ready(function(){
	
	$(".añadir_carrito").click(function(e){
		
		e.preventDefault();
		
		var pathname = window.location.pathname;
		
		var id = pathname.substr(pathname.lastIndexOf('/') + 1);
		var nombre = $("#prod_nombre").text();
		var precio = $("#prod_precio").text();
		var genero = $("#prod_genero").text();
		var distribuidora = $("#prod_distr").text();
		var plataforma = $("#prod_plat").text();
		var edad = $("#prod_edad").text();
		var cantidad = $("#prod_cuant").val();
		
		var total = parseFloat(precio) * cantidad;
		var price = parseFloat(precio);
		
		$("#nom_carro").text(nombre);
		$("#pre_carro").text(price.toFixed(2));
		$("#tot_carro").text(cantidad);
		
		$("#total").text(total.toFixed(2));
		
		console.log(nombre + " | " + precio + " | " + total);
	});
});