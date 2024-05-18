package px.main.veterinaria.servicos;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import px.main.veterinaria.modelos.Estoque;
import px.main.veterinaria.repository.EstoqueRepository;

@Service
public class EstoqueService {

	@Autowired
	EstoqueRepository estoqueRepository;

	public Optional<Estoque> get(Integer id) {
		return estoqueRepository.findById(id);
	}

	public Estoque salvar(Estoque estoque) {
		return estoqueRepository.save(estoque);
	}

	public void deletarAllByPordutooId(Integer id) {
		estoqueRepository.deleteByProdutoId(id);
	}
	
	public void deletar(Integer id) {
		estoqueRepository.deleteById(id);
	}

	public List<Estoque> all() {
		return estoqueRepository.findAll();
	}

}
