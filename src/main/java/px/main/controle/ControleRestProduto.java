package px.main.controle;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import px.main.veterinaria.modelos.Produto;
import px.main.veterinaria.repository.ProdutoRepository;

@RestController
public class ControleRestProduto {

	@Autowired
	ProdutoRepository produtoRepository;

	@GetMapping("/produtos")
	List<Produto> all() {
		return produtoRepository.findAll();
	}

	@PostMapping("/produtos")
	Produto newEmployee(@RequestBody Produto produto) {
		return produtoRepository.save(produto);
	}

	@GetMapping("/produtos/{id}")
	Produto one(@PathVariable int id) {
		Optional<Produto> produto = produtoRepository.findById(id);
		if (produto.isPresent()) {
			return produto.get();
		}
		return new Produto();
	}

	@PutMapping("/produtos/{id}")
	Produto replaceProduto(@RequestBody Produto newProduto, @PathVariable Integer id) {
		Optional<Produto> produtoOp=produtoRepository.findById(id);
		if(produtoOp.isPresent()) {
			Produto produto=produtoOp.get();
			produto.setCodigo(newProduto.getCodigo());
			produto.setDescricao(newProduto.getDescricao());
			produto.setLaboratorio(newProduto.getLaboratorio());
			produto.setNome(newProduto.getNome());
			produto.setPrecoVenda(newProduto.getPrecoVenda());
			produto.setQuantidadeMaxima(newProduto.getQuantidadeMaxima());
			produto.setQuantidadeMinima(newProduto.getQuantidadeMinima());
			produto.setTipo(newProduto.getTipo());
			produtoRepository.save(produto);
			return produto;
		}else {
			newProduto.setId(id);
			produtoRepository.save(newProduto);
			return newProduto;
		}
	}

	@PutMapping("/produtos/")
	@ResponseStatus(HttpStatus.OK)
	public List<Produto> replaceProdutos(@RequestBody List<Produto> listaProdutos) {
		for(Produto newProduto:listaProdutos) {
		Optional<Produto> produtoOp=produtoRepository.findById(newProduto.getId());
		if(produtoOp.isPresent()) {
			Produto produto=produtoOp.get();
			produto.setCodigo(newProduto.getCodigo());
			produto.setDescricao(newProduto.getDescricao());
			produto.setLaboratorio(newProduto.getLaboratorio());
			produto.setNome(newProduto.getNome());
			produto.setPrecoVenda(newProduto.getPrecoVenda());
			produto.setQuantidadeMaxima(newProduto.getQuantidadeMaxima());
			produto.setQuantidadeMinima(newProduto.getQuantidadeMinima());
			produto.setTipo(newProduto.getTipo());
			produtoRepository.save(produto);
		}else {
			produtoRepository.save(newProduto);
		}
		}
		return produtoRepository.findAll();
	}
	@DeleteMapping("/produtos/{id}")
	void deleteEmployee(@PathVariable Integer id) {
		produtoRepository.deleteById(id);
	}
}
