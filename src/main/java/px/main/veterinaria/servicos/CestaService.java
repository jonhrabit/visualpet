package px.main.veterinaria.servicos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import px.main.veterinaria.modelos.Atendimento;
import px.main.veterinaria.modelos.Caixa;
import px.main.veterinaria.modelos.Cesta;
import px.main.veterinaria.modelos.ProdutoCesta;
import px.main.veterinaria.query.modelos.Vendas;
import px.main.veterinaria.repository.CestaRepository;
import px.main.veterinaria.repository.ProdutoCestaRepository;
import px.main.veterinaria.view.CestaConsultaView;

@Service
public class CestaService {
	@Autowired
	CestaRepository cestaRepository;
	@Autowired
	AtendimentoService atendimentoService;
	@Autowired
	ProdutoService produtoService;
	@Autowired
	PetService petService;
	@Autowired
	ProdutoCestaRepository produtoCestaRepository;
	@Autowired
	PacoteService pacoteService;

	public List<Cesta> all() {
		return cestaRepository.findAll();
	}

	public List<Cesta> buscarMes(Integer mes, Integer ano) {
		Calendar calendar = Calendar.getInstance();
		Date inicialDate, finalDate;
		mes -= 1;
		calendar.set(ano, mes, 1, 0, 0);
		inicialDate = calendar.getTime();
		calendar.set(ano, mes + 1, 1, 0, 0);
		finalDate = calendar.getTime();
		return cestaRepository.findByDataBetween(inicialDate, finalDate);
	}

	public List<Cesta> comprasAno(Integer ano) {
		Calendar calendar = Calendar.getInstance();
		Date inicialDate, finalDate;
		calendar.set(ano, 0, 1, 0, 0);
		inicialDate = calendar.getTime();
		calendar.set(ano, 12, 31, 23, 59);
		finalDate = calendar.getTime();
		List<Cesta> lista = new ArrayList<Cesta>();
		lista = cestaRepository.findByDataBetween(inicialDate, finalDate);
		return lista;
	}

	public List<Cesta> getCestasAno(Integer ano) {
		return cestaRepository.findCestasAno(ano);
	}

	public List<ProdutoCesta> listaProdutosMes(Integer mes, Integer ano) {
		List<Cesta> listaCesta = buscarMes(mes, ano);
		List<ProdutoCesta> listaProduto = new ArrayList<ProdutoCesta>();
		for (Cesta cesta : listaCesta) {
			for (ProdutoCesta produtoCesta : cesta.getProdutos()) {
				if (produtoCesta.getPrecoVenda().compareTo(BigDecimal.ZERO) != 0)
					listaProduto.add(produtoCesta);

			}
		}
		return listaProduto;
	}

	public Vendas relProdutosMes(Integer mes, Integer ano) {
		List<Cesta> listaCesta = buscarMes(mes, ano);
		List<ProdutoCesta> listaProduto = new ArrayList<ProdutoCesta>();
		List<Caixa> listaCaixa = new ArrayList<Caixa>();
		List<Atendimento> listaAtendimento = new ArrayList<Atendimento>();
		for (Cesta cesta : listaCesta) {
			for (Caixa caixa : cesta.getCaixas()) {
				listaCaixa.add(caixa);

			}
			for (ProdutoCesta produtoCesta : cesta.getProdutos()) {
				if (produtoCesta.getPrecoVenda().compareTo(BigDecimal.ZERO) != 0)
					listaProduto.add(produtoCesta);
				if (produtoCesta.getTipo().equals("servico"))
					listaAtendimento.add(atendimentoService.get(produtoCesta.getIdProdutoOriginal()));

			}
		}
		return new Vendas(listaProduto, listaCaixa, listaAtendimento);
	}

	public Cesta get(Integer id) {
		Optional<Cesta> produto = cestaRepository.findById(id);
		if (produto.isEmpty())
			return null;
		return produto.get();
	}

	public Cesta salvar(Cesta cesta) {
		return cestaRepository.save(cesta);
	}

	public List<Cesta> fechamento(Date data) {
		return cestaRepository.findByData(data);
	}

	public void baixa(Cesta cesta) {
		for (ProdutoCesta produtoCarrinho : cesta.getProdutos()) {
			switch (produtoCarrinho.getTipo()) {
			case "servico":
				atendimentoService.finalizar(produtoCarrinho.getIdProdutoOriginal(), produtoCarrinho.getPrecoVenda(),
						1);
				break;
			case "pacote":
				pacoteService.baixa(produtoCarrinho.getIdProdutoOriginal());
				break;
			default:
				produtoService.baixa(produtoCarrinho.getIdProdutoOriginal(), produtoCarrinho.getQuantidade());
				break;
			}
		}
	}

	public CestaConsultaView consultar(CestaConsultaView cestaConsultaView) {
		cestaConsultaView.setResultado(
				cestaRepository.searchBetween(cestaConsultaView.getDataInicial(), cestaConsultaView.getDataFinal()));
		return cestaConsultaView;
	}

	public List<Cesta> findJoinByProdutosCestaId(Integer id) {
		return cestaRepository.findJoinByProdutosCestaId(id);
	}

	public List<ProdutoCesta> relatorio(Integer id) {
		return produtoCestaRepository.findByIdProdutoOriginal(id);
	}

	public List<Cesta> getListDay(Date data) {
		Calendar inicio = Calendar.getInstance();
		inicio.setTime(data);
		inicio.set(Calendar.HOUR_OF_DAY, 0);
		inicio.set(Calendar.MINUTE, 0);
		inicio.set(Calendar.SECOND, 0);

		Calendar fim = Calendar.getInstance();
		fim.setTime(data);
		fim.set(Calendar.HOUR_OF_DAY, 23);
		fim.set(Calendar.MINUTE, 59);
		fim.set(Calendar.SECOND, 59);
		return cestaRepository.findByDataBetween(inicio.getTime(), fim.getTime()).stream()
				.filter(x -> x.getNf() == null).collect(Collectors.toList());
	}

	public void setNota(int i, String chaveAcesso) {
		Cesta cesta = cestaRepository.findById(i).get();
		cesta.setNf(chaveAcesso);
		this.salvar(cesta);
	}

}
