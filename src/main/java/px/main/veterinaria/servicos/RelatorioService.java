package px.main.veterinaria.servicos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import px.main.veterinaria.modelos.Atendimento;
import px.main.veterinaria.modelos.Caixa;
import px.main.veterinaria.modelos.Cesta;
import px.main.veterinaria.modelos.ProdutoCesta;
import px.main.veterinaria.modelos.enumeracoes.ServicoTipo;
import px.main.veterinaria.relatorio.Linha;
import px.main.veterinaria.relatorio.Relatorio;

@Service
public class RelatorioService {
	@Autowired
	CaixaService caixaService;
	@Autowired
	CestaService cestaService;
	@Autowired
	AtendimentoService atendimentoService;

	public Relatorio get(Integer ano) {
		List<Caixa> caixas = caixaService.getCaixaAno(ano);
		List<Atendimento> atendimentos = atendimentoService.getAtendimentosAno(ano);
		List<Cesta> cestas = cestaService.getCestasAno(ano);
		Relatorio relatorio = new Relatorio(ano.toString());
		relatorio.addLinha(getLucro(ano, caixas));
		relatorio.addLinha(getTicketMedio(ano, cestas));
		relatorio.addLinha(getAtendimentosPorSetor(ano, atendimentos));
		relatorio.addLinha(getAtendimentosPorTipo(ano, cestas));
		relatorio.addLinha(getPorFormaDePagamento(ano, caixas));

		return relatorio;
	}

	public List<Linha> getPorFormaDePagamento(Integer ano, List<Caixa> caixas) {
		List<String> formasPagamento = caixaService.getFormasDePagamentosDoAno(ano);
		return formasPagamento.stream().map(f -> {
			List<BigDecimal> totais = new ArrayList<BigDecimal>();
			for (int i = 0; i < 12; i++) {
				final int g = i;
				totais.add(caixas.stream().filter(c->c.getTipo().equals(1)).filter(c -> c.getMes().equals(g)).filter(cc -> cc.getForma().equals(f))
						.map(caixa -> caixa.getValor()).reduce(BigDecimal.ZERO, BigDecimal::add));
			}
			totais.add(totais.stream().reduce(BigDecimal.ZERO, BigDecimal::add));
			return new Linha(f, "Formas de Pagamento", totais);
		}).collect(Collectors.toList());
	}

	public List<Linha> getLucro(Integer ano, List<Caixa> caixas) {
		List<Linha> linhas = new ArrayList<Linha>();
		List<BigDecimal> totais = new ArrayList<BigDecimal>();
		for (int i = 0; i < 12; i++) {
			final int g = i;
			totais.add(caixas.stream().filter(c -> c.getMes().equals(g)).filter(cc -> cc.getTipo().equals(0))
					.map(caixa -> caixa.getValor()).reduce(BigDecimal.ZERO, BigDecimal::add));
		}
		totais.add(totais.stream().reduce(BigDecimal.ZERO, BigDecimal::add));
		linhas.add(new Linha("Débito", "Lucro", totais));
		totais = new ArrayList<BigDecimal>();
		for (int i = 0; i < 12; i++) {
			final int g = i;
			totais.add(caixas.stream().filter(c -> c.getMes().equals(g)).filter(cc -> cc.getTipo().equals(1))
					.map(caixa -> caixa.getValor()).reduce(BigDecimal.ZERO, BigDecimal::add));
		}
		totais.add(totais.stream().reduce(BigDecimal.ZERO, BigDecimal::add));
		linhas.add(new Linha("Crédito", "Lucro", totais));
		totais = new ArrayList<BigDecimal>();
		for (int i = 0; i < 12; i++) {
			totais.add(linhas.get(1).getValores().get(i).subtract(linhas.get(0).getValores().get(i)));
		}
		totais.add(totais.stream().reduce(BigDecimal.ZERO, BigDecimal::add));
		linhas.add(new Linha("Saldo", "Lucro", totais));
		return linhas;
	}

	public List<Linha> getTicketMedio(Integer ano, List<Cesta> cestas) {
		List<Linha> linhas = new ArrayList<Linha>();
		List<BigDecimal> totais = new ArrayList<BigDecimal>();
		List<Long> frequencia = new ArrayList<Long>();
		List<BigDecimal> ticket = new ArrayList<BigDecimal>();

		for (int i = 0; i < 12; i++) {
			final int g = i;
			totais.add(cestas.stream().filter(c -> c.getMes().equals(g)).map(cesta -> cesta.getTotal())
					.reduce(BigDecimal.ZERO, BigDecimal::add));
			frequencia.add(cestas.stream().filter(c -> c.getMes().equals(g)).collect(Collectors.counting()));
			if (totais.get(i).equals(BigDecimal.ZERO)) {
				ticket.add(BigDecimal.ZERO);
			} else {
				ticket.add(totais.get(i).divide(new BigDecimal(frequencia.get(i)), 2, RoundingMode.HALF_UP));
			}
		}
		linhas.add(new Linha("", "Ticket Médio", ticket));
		return linhas;
	}

	public List<Linha> getAtendimentosPorSetor(Integer ano, List<Atendimento> atendimentos) {
		ServicoTipo[] setores = ServicoTipo.values();
		List<Linha> linhas = new ArrayList<Linha>();
		List<BigDecimal> totais = new ArrayList<BigDecimal>();
		for (ServicoTipo setor : setores) {
			for (int i = 0; i < 12; i++) {
				final int g = i;
				totais.add(atendimentos.stream().filter(atendimento -> atendimento.getAgendaMes().equals(g))
						.filter(a -> a.getServico().getTipo().getId() == setor.getId()).map(a -> a.getValor())
						.reduce(BigDecimal.ZERO, BigDecimal::add));
			}
			totais.add(totais.stream().reduce(BigDecimal.ZERO, BigDecimal::add));
			linhas.add(new Linha(setor.getDescricao(), "Atendimentos por Setor", totais));
			totais = new ArrayList<BigDecimal>();
		}
		return linhas;
	}

	public List<Linha> getAtendimentosPorTipo(Integer ano, List<Cesta> cestas) {
		List<Linha> linhas = new ArrayList<Linha>();
		List<BigDecimal> totais = new ArrayList<BigDecimal>();

		for (int i = 0; i < 12; i++) {
			List<ProdutoCesta> produtos = new ArrayList<ProdutoCesta>();
			final int g = i;
			cestas.stream().filter(c -> c.getMes().equals(g)).forEach(c -> {
				c.getProdutos().stream().forEach(produto -> {
					produtos.add(produto);
				});
			});
			totais.add(produtos.stream().filter(p -> p.getTipo().equals("servico")).map(p -> p.getPrecoVenda())
					.reduce(BigDecimal.ZERO, BigDecimal::add));
		}
		totais.add(totais.stream().reduce(BigDecimal.ZERO, BigDecimal::add));
		linhas.add(new Linha("Serviços", "Vendas por Tipo", totais));
		totais = new ArrayList<BigDecimal>();
		for (int i = 0; i < 12; i++) {
			List<ProdutoCesta> produtos = new ArrayList<ProdutoCesta>();
			final int g = i;
			cestas.stream().filter(c -> c.getMes().equals(g)).forEach(c -> {
				c.getProdutos().stream().forEach(produto -> {
					produtos.add(produto);
				});
			});
			totais.add(produtos.stream().filter(p -> !p.getTipo().equals("servico")).map(p -> p.getPrecoVenda())
					.reduce(BigDecimal.ZERO, BigDecimal::add));
		}
		totais.add(totais.stream().reduce(BigDecimal.ZERO, BigDecimal::add));
		linhas.add(new Linha("Produtos", "Vendas por Tipo", totais));
		totais = new ArrayList<BigDecimal>();
		return linhas;
	}

}
