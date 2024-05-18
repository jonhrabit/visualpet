package px.main.veterinaria.servicos;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import px.main.veterinaria.modelos.Estoque;
import px.main.veterinaria.modelos.Produto;
import px.main.veterinaria.modelos.ProdutoAtendimento;
import px.main.veterinaria.modelos.ProdutoCesta;
import px.main.veterinaria.repository.ProdutoRepository;

@Service
public class ProdutoService {

	@Autowired
	ProdutoRepository produtoRepository;
	@Autowired
	EstoqueService estoqueService;

	public List<Produto> consultaByNome(String nome) {
		return produtoRepository.consultaByNome(nome);
	}

	public List<Produto> All() {
		return produtoRepository.findAll();
	}

	public Produto get(Integer id) {
		Optional<Produto> produto = produtoRepository.findById(id);
		if (produto.isEmpty())
			return new Produto();
		return produto.get();
	}

	public Produto get(String codigo) {
		Optional<Produto> produto = produtoRepository.findByCodigo(codigo);
		if (produto.isEmpty())
			return new Produto();
		return produto.get();
	}

	public Produto getByNCM(String ncm) {
		Optional<Produto> produto = produtoRepository.findByNcm(ncm);
		if (produto.isEmpty())
			return new Produto();
		return produto.get();
	}

	public ProdutoCesta getEstoque(Integer id) {
		Optional<Produto> produto = produtoRepository.findById(id);
		if ((produto.isEmpty()) || (produto.get().getQuantidade() <= 0))
			return new Produto().copyToProdutoCesta();
		return produto.get().copyToProdutoCesta();
	}

	public ProdutoAtendimento getEstoqueAtendimento(Integer id) {
		Optional<Produto> produto = produtoRepository.findById(id);
		if ((produto.isEmpty()) || (produto.get().getQuantidade() <= 0))
			return new Produto().copyToProdutoAtendimento();
		return produto.get().copyToProdutoAtendimento();
	}

	public ProdutoCesta getEstoque(String codigo) {
		Optional<Produto> produto = produtoRepository.findByCodigo(codigo);
		if ((produto.isEmpty()) || (produto.get().getQuantidade() <= 0))
			return new Produto().copyToProdutoCesta();
		return produto.get().copyToProdutoCesta();
	}

	public Produto salvar(Produto produto) {
		return produtoRepository.save(produto);
	}

	public boolean deletar(Integer id) {
		Optional<Produto> produto = produtoRepository.findById(id);
		if (produto.isEmpty())
			return false;
		estoqueService.deletarAllByPordutooId(id);
		produtoRepository.deleteById(id);
		return true;
	}

	public List<Produto> estoque() {
		return produtoRepository.findByQuantidadeGreaterThanEqualOrderByNomeAsc();
	}

	public Page<Produto> consultar(String nome, String filtro, Pageable pageable) {
		if (nome.equals("Todos"))
			nome = "";
		if (filtro.equals("Todos")) {
			return produtoRepository.consultar(nome, pageable);
		} else {
			return produtoRepository.consultar(nome, filtro, pageable);
		}
	}

	public List<Produto> aquisicao() {
		return produtoRepository.Aquisicao();
	}

	public void baixa(Integer idProduto, Integer quantidade) {
		Produto prod = produtoRepository.findById(idProduto).get();
		quantidade = Math.abs(quantidade);
		Iterator<Estoque> iterator = prod.getEstoque().iterator();
		while (iterator.hasNext()) {
			Estoque estoque = iterator.next();
			if (estoque.getQuantidade() > quantidade) {
				estoque.setQuantidade(estoque.getQuantidade() - quantidade);
				estoqueService.salvar(estoque);
				break;
			}
			if (estoque.getQuantidade() == quantidade) {
				estoqueService.deletar(estoque.getId());
				break;
			}
			if (estoque.getQuantidade() < quantidade) {
				quantidade = quantidade - estoque.getQuantidade();
				estoqueService.deletar(estoque.getId());
			}
		}
	}

	public List<String> listaTipos() {
		return produtoRepository.listaProdutos();
	}
}
