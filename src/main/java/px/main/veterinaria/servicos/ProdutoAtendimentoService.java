package px.main.veterinaria.servicos;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import px.main.veterinaria.modelos.ProdutoAtendimento;
import px.main.veterinaria.repository.ProdutoAtendimentoRepository;

@Service
public class ProdutoAtendimentoService {

	@Autowired
	ProdutoAtendimentoRepository produtoAtendimentoRepository;

	public List<ProdutoAtendimento> All() {
		return produtoAtendimentoRepository.findAll();
	}

	public ProdutoAtendimento get(Integer id) {
		Optional<ProdutoAtendimento> produto = produtoAtendimentoRepository.findById(id);
		if (produto.isEmpty())
			return new ProdutoAtendimento();
		return produto.get();
	}

	public ProdutoAtendimento salvar(ProdutoAtendimento produto) {
		return produtoAtendimentoRepository.save(produto);
	}

	public boolean deletar(Integer id) {
		Optional<ProdutoAtendimento> produto = produtoAtendimentoRepository.findById(id);
		if (produto.isEmpty())
			return false;
		produtoAtendimentoRepository.deleteById(id);
		return true;
	}

}
