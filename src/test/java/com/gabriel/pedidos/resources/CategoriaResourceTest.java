package com.gabriel.pedidos.resources;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;

import io.restassured.http.ContentType;

@WebMvcTest
public class CategoriaResourceTest {

	@Autowired
	private CategoriaResource categoriaResource;
	
	@BeforeEach
	public void setup() {
		standaloneSetup(this.categoriaResource);
	}
	
	@Test
	public void deveRetornarSucesso_QuandoCarregarAhCategoriaPorId() {
		
//		when(this.categoriaService.buscar(1)).thenReturn(new Categoria(1, "Escritorio"));
		
		given()
		.accept(ContentType.JSON)
		.when().get("/categorias/{id}", 1L)
		.then()
		.statusCode(HttpStatus.OK.value());
	}
	
}
