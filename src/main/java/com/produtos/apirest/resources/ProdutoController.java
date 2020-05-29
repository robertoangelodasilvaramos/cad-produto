package com.produtos.apirest.resources;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.produtos.apirest.dtos.ProdutoDto;
import com.produtos.apirest.models.Produto;
import com.produtos.apirest.repository.ProdutoRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api")
@Api(value = "Api REST Produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {
	
	private static final Logger log = LoggerFactory.getLogger(ProdutoController.class);
	
	@Autowired
	ProdutoRepository produtoRepository;
	
	@GetMapping("/produtos")
	@ApiOperation(value = "Retorna uma lista de produtos")
	public List<Produto> listaProdutos(){
		
		return produtoRepository.findAll();
	}
	
	
	@GetMapping("/produtos/{nome}")
	@ApiOperation(value = "Retorna um produto por Nome")
	public ResponseEntity<com.produtos.apirest.response.Response<Produto>> listaProdutoNome(@PathVariable(value = "nome") String nome){
		log.info("Buscando produtos por Nome: {}", nome);
		com.produtos.apirest.response.Response<Produto> response =  new com.produtos.apirest.response.Response<Produto>();
		Optional<Produto> produto = produtoRepository.findByNome(nome);
		
		if (!produto.isPresent()) {
			log.info("Produto não encontrada para o Nome: {}", nome);
			response.getErrors().add("produto não encontrada para o Nome " + nome);
			return ResponseEntity.badRequest().body(response);
		}

		response.setData(produto.get());
		return ResponseEntity.ok(response);
		
	}
	
	@GetMapping("/produto/{id}")
	@ApiOperation(value = "Retorna um produto por id")
	public ResponseEntity<com.produtos.apirest.response.Response<Produto>> listaProdutoId(@PathVariable(value = "id") long id){
		log.info("Buscando produtos por id: {}", id);
		com.produtos.apirest.response.Response<Produto> response =  new com.produtos.apirest.response.Response<Produto>();
		Optional<Produto> produto = produtoRepository.findById(id);
		
		if (!produto.isPresent()) {
			log.info("Produto não encontrada para o id: {}", id);
			response.getErrors().add("produto não encontrada para o id " + id);
			return ResponseEntity.badRequest().body(response);
		}

		response.setData(produto.get());
		return ResponseEntity.ok(response);
		
	}
	
	@PostMapping("/produto")
	@ApiOperation(value = "Cadastra um produto")
	public ResponseEntity<com.produtos.apirest.response.Response<ProdutoDto>> cadastrar(@Valid @RequestBody ProdutoDto produtoDto,
			BindingResult result) throws NoSuchAlgorithmException {
		log.info("Cadastrando Produto: {}", produtoDto.toString());
		com.produtos.apirest.response.Response<ProdutoDto> response = new com.produtos.apirest.response.Response<ProdutoDto>();

		validarDadosExistentes(produtoDto, result);
		Produto produto = this.passarProdutoParaDto(produtoDto);
		
		if (result.hasErrors()) {
			log.error("Erro validando dados de cadastro Produto: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		
		this.produtoRepository.save(produto);

		response.setData(produtoDto);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/produto/{id}")
	@ApiOperation(value = "deleta um produto por id")
	public ResponseEntity<com.produtos.apirest.response.Response<Produto>> deletaProduto(@PathVariable(value = "id") long id){
		log.info("Buscando produtos por id: {}", id);
		com.produtos.apirest.response.Response<Produto> response =  new com.produtos.apirest.response.Response<Produto>();
		Optional<Produto> produto = produtoRepository.findById(id);
		
		if (!produto.isPresent()) {
			log.info("Produto não encontrada para o id: {}", id);
			response.getErrors().add("produto não encontrada para o id " + id);
			return ResponseEntity.badRequest().body(response);
		}else {
			this.produtoRepository.deleteById(id);
		}

		
		response.setData(produto.get());
		return ResponseEntity.ok(response);
		
	}
	
	@PutMapping("/produto")
	@ApiOperation(value = "atualiza um produto")
	public Produto alterarProduto(@RequestBody Produto produto) {
		return produtoRepository.save(produto);
	}
	
	
	/**
	 * verifica se não tem um produto cadastra igual
	 * @param produtoPJDto
	 * @param result
	 */
	private void validarDadosExistentes(ProdutoDto produtoPJDto, BindingResult result) {
		this.produtoRepository.findByNome(produtoPJDto.getNome())
				.ifPresent(prod -> result.addError(new ObjectError("produto", "Produto já existente.")));
	}
	

	/**
	 * passa o produto para o produtoDto
	 * @param produtoDto
	 * @return
	 */
	private Produto passarProdutoParaDto(ProdutoDto produtoDto) {
		Produto produto = new Produto();
		produto.setNome(produtoDto.getNome());
		produto.setQuantidade(produtoDto.getQuantidade());
		produto.setValor(produtoDto.getValor());
		
		return produto;
	}

	

}
