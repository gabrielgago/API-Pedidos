package com.gabriel.pedidos.domain.converter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.gabriel.pedidos.domain.Categoria;
import com.gabriel.pedidos.domain.Produto;
import com.gabriel.pedidos.domain.converter.anotations.All;
import com.gabriel.pedidos.domain.dto.CategoriaDTO;

public interface Converter<VO, DTO> {

	final String GET = "get";
	final String SET = "set";

	default DTO from(final VO vo) {

		Class<?> dtoClass = this.getClass();

		Stream<Method> streamMetodosClasseVoList = carregarMetodosClasseVO(vo);

		Stream<Method> streamMetodosDTO = carregarMetodosClasseDTO(dtoClass);

		List<Method> listaDosMetodosSetDoDTO = streamMetodosDTO.filter(this::comecaComGET)
				.collect(Collectors.toList());

		Stream<Method> streamComApenasMetodosGetFiltradosDaClasseVO = streamMetodosClasseVoList
				.filter(this::comecaComGET);

		List<String> listaComApenasNomesDosMetodosDaClassDTOSemPrefixoSet = obterListaComApenasNomesDosMetodosDaClassDTOSemPrefixoSet(
				listaDosMetodosSetDoDTO);

		List<Method> streamComMetodosGetDoVO = streamComApenasMetodosGetFiltradosDaClasseVO
//				.forEach(this::ehUmaColecao)
				.filter(m -> ehUmMetodoGetValido(listaComApenasNomesDosMetodosDaClassDTOSemPrefixoSet, m.getName()))
				.collect(Collectors.toList());

		return createDTO(vo, dtoClass, listaDosMetodosSetDoDTO, streamComMetodosGetDoVO);

	}

	private List<String> obterListaComApenasNomesDosMetodosDaClassDTOSemPrefixoSet(
			List<Method> listaDosMetodosSetDoDTO) {
		return listaDosMetodosSetDoDTO.stream().map(m -> obterNomeSemPrefixo(m.getName(), TipoNome.SET))
				.collect(Collectors.toList());
	}
	
	private boolean comecaComGET(Method m) {
		return m.getName().startsWith(GET);
	}

	private boolean comecaComSET(Method m) {
		return m.getName().startsWith(SET);
	}

	private boolean ehUmMetodoGetValido(List<String> listaComApenasNomesDosMetodosDaClassDTOSemPrefixoSet,
			String nomeMetodo) {
		//categoriasNome
		//categorias
		//nome do metodo contem na lista de metodos separados do DTO
		return listaComApenasNomesDosMetodosDaClassDTOSemPrefixoSet
				.stream()
				.anyMatch( n -> n.contains(obterNomeSemPrefixo(nomeMetodo, TipoNome.GET)) );
	}

	private Stream<Method> carregarMetodosClasseVO(final VO vo) {
		Class<?> voClass = vo.getClass();
		Method[] metodosClasseVO = voClass.getMethods();
		Stream<Method> streamMetodosClasseVoList = List.of(metodosClasseVO).stream();
		return streamMetodosClasseVoList;
	}

	private Stream<Method> carregarMetodosClasseDTO(Class<?> c) {
		Method[] metodosCurrentClass = c.getMethods();
		return List.of(metodosCurrentClass).stream();
	}

	@SuppressWarnings("unchecked")
	private DTO createDTO(final VO vo, Class<?> dtoClass, List<Method> streamComApenasMetodosSetDaClasseDTO,
			List<Method> streamComMetodosGetDoVO) {
		DTO dto = null;

		try {
			Constructor<? extends Object> constructorDTO = dtoClass.getConstructor();
			final Object instanciaDTO = constructorDTO.newInstance();

			invocaMetodosSetDoDtoComOsRetornosDosMetodosVoSimilares(vo, streamComApenasMetodosSetDaClasseDTO,
					streamComMetodosGetDoVO, instanciaDTO);

			dto = (DTO) instanciaDTO;

		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return dto;
	}

	private void invocaMetodosSetDoDtoComOsRetornosDosMetodosVoSimilares(final VO vo,
			List<Method> streamComApenasMetodosSetDaClasseDTO, List<Method> streamComApenasMetodosGetDaClasseVO,
			final Object instanciaDTO) {

		Comparator<? super Method> comparator = (current, next) -> current.getName().compareTo(next.getName());
		streamComApenasMetodosGetDaClasseVO.sort(comparator);
		streamComApenasMetodosSetDaClasseDTO.sort(comparator);

		streamComApenasMetodosGetDaClasseVO.forEach(m -> {
			streamComApenasMetodosSetDaClasseDTO.forEach(m2 -> {

				String nomeMetodoVO = obterNomeSemPrefixo(m.getName(), TipoNome.GET);
				String nomeMetodoDTO = obterNomeSemPrefixo(m2.getName(), TipoNome.SET);

				if (nomeMetodoDTO.equals(nomeMetodoVO)) {
					try {
						m2.invoke(instanciaDTO, m.invoke(vo));
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						throw new RuntimeException(e);
					}
					//se contain eh uma lista e tem sufixo any, all, only
				}else if(nomeMetodoDTO.contains(nomeMetodoVO)) {
					String nomeSufixo = pegarNomeSufixo(nomeMetodoDTO);
					//primeira parte exclui pq e o nome do atributo relacionado
					//quebrar no uppercase
					//pegar do nome do atributo rel ate o sufixo
					//saber quantas posicoes tem entre um e outro
					//a quantidade de posicoes, sao a quantidade de niveis que teremos que entrar
					//ex produtosNomeAny, apenas 1 nivel, nome dentro de produtos
					//produtosCategoriasNomeAny, sao 2 niveis, nome dentro de categorias dentro de produtos 
					//ao varrer os metodos buscar por get? (nivel)
					try {
						Class<?> returnTypes = m.getReturnType();
						if(returnTypes.isInstance(List.class)) {
							List<VO> lista = (List<VO>) m.invoke(vo);
							Stream<VO> streamVO = lista.stream();
							switch (nomeSufixo) {
							case "Any":
								Optional<VO> qualquer = streamVO.findAny();
								m2.invoke(instanciaDTO, qualquer);
								break;
							case "Only":
								break;
							case "All":
								
								//lista de categorias
//								lista.forEach(model -> {
//									
//									Arrays.stream(model.getClass().getMethods())
//										.filter(this::comecaComGET)
//										//produto -> getNome
//									
//								});
								
								//
								
								All anotatioAll = m2.getDeclaredAnnotation(All.class); //@join
								String criterioDeJuncao = anotatioAll.criterioDeJuncao();
								if(criterioDeJuncao.isEmpty()) {
									m2.invoke(instanciaDTO, lista.toString());
								}
								break;
							}
							
							
							
						}
						
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						throw new RuntimeException(e);
					}
				}
			});
		});
	}

	private String pegarNomeSufixo(String nomeMetodoDTO) {
		String[] nomeSplitado = nomeMetodoDTO.split("(?=[A-Z])");
		return nomeSplitado[nomeSplitado.length-1];
		
	}

	private String obterNomeSemPrefixo(String nome, TipoNome tipoNome) {
		return tipoNome.remover(nome);
	}

	public static void main(String[] args) {

		Categoria cat1 = new Categoria(null, "Informática");
		Produto p1 = new Produto(null, "Computador", BigDecimal.valueOf(2000.00));
		Produto p2 = new Produto(null, "Impressora", BigDecimal.valueOf(800.00));
		Produto p3 = new Produto(null, "Mouse", BigDecimal.valueOf(80.00));

		cat1.addProdutos(p1, p2, p3);
		
		CategoriaDTO categoriaDTO = new CategoriaDTO().from(cat1);
		System.out.println(categoriaDTO);
		
		String[] split = "thisIsATrickyOne".split("(?=[A-Z])");
		System.out.println(List.of(split));
		
	}
}

class Telefone implements Comparable<Telefone> {

	public String ddd;
	public String numero;

	public Telefone(String ddd, String numero) {
		super();
		this.ddd = ddd;
		this.numero = numero;
	}

	public String getDdd() {
		return ddd;
	}

	public void setDdd(String ddd) {
		this.ddd = ddd;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@Override
	public int compareTo(Telefone o) {
		return this.numero.compareTo(o.numero);
	}

}

enum TipoNome {

	GET(new RemovedorPrefixoGet()), SET(new RemovedorPrefixoSet());

	private RemovedorPrefixo removedor;

	private TipoNome(RemovedorPrefixo removedor) {
		this.removedor = removedor;
	}

	public String remover(String nome) {
		return removedor.remover(nome);
	}

}

interface RemovedorPrefixo {

	final String SET = "set";
	final String GET = "get";

	String remover(String nome);

}

class RemovedorPrefixoGet implements RemovedorPrefixo {

	@Override
	public String remover(String nome) {
		if (!nome.startsWith(GET)) {
			throw new NamedNotContainsPrefix(GET);
		}
		return nome.split(GET)[1];
	}

}

class RemovedorPrefixoSet implements RemovedorPrefixo {

	@Override
	public String remover(String nome) {
		if (!nome.startsWith(SET)) {
			throw new NamedNotContainsPrefix(SET);
		}
		return nome.split(SET)[1];
	}

}

class NamedNotContainsPrefix extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7644851232243898926L;
	private static final String MENSAGEM = "Prefixo '{}' inválido.";

	public NamedNotContainsPrefix(String prefix) {
		super(MENSAGEM.replace("{}", prefix));
	}

	public NamedNotContainsPrefix(String prefix, Throwable ex) {
		super(MENSAGEM.replace("{}", prefix), ex);
	}
}
