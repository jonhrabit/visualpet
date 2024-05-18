package px.main.veterinaria.query.modelos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import px.main.veterinaria.modelos.Atendimento;
import px.main.veterinaria.modelos.Caixa;
import px.main.veterinaria.modelos.ProdutoCesta;
import px.main.veterinaria.modelos.enumeracoes.ServicoTipo;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vendas {

	List<ProdutoCesta> produtos;
	List<Caixa> caixas;
	List<Atendimento> atendimentos;

	public List<TotaisVendas> getTotaisVendas() {
		Map<String, Long> formasPagamento = caixas.stream()
				.collect(Collectors.groupingBy(Caixa::getForma, Collectors.counting()));
		List<String> forma = formasPagamento.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());
		return forma.stream().map(c -> {
			TotaisVendas t = new TotaisVendas();
			t.setForma(c);
			t.setLiquido(getTotalLiquido(c));
			t.setBruto(getTotalBruto(c));
			return t;
		}).collect(Collectors.toList());
	}

	public BigDecimal getTotalLiquido(String forma) {
		return caixas.stream().filter(p -> p.getForma().equals(forma)).map(p -> p.getValor()).reduce(BigDecimal.ZERO,
				BigDecimal::add);
	}

	public BigDecimal getTotalBruto(String forma) {
		return caixas.stream().filter(p -> p.getForma().equals(forma)).map(p -> p.getValorBruto())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	/*
	 * public BigDecimal getTotalProdutos() { return this
	 * .getTotalBrutoSemDinheiro().multiply(new BigDecimal(this.getCountProduto())
	 * .divide(new BigDecimal(this.getCount()), 4, RoundingMode.HALF_UP))
	 * .setScale(2, RoundingMode.HALF_UP); }
	 * 
	 * public BigDecimal getTotalServicos() { return this
	 * .getTotalBrutoSemDinheiro().multiply(new BigDecimal(this.getCountServico())
	 * .divide(new BigDecimal(this.getCount()), 4, RoundingMode.HALF_UP))
	 * .setScale(2, RoundingMode.HALF_UP); }
	 */

	public Long getCountServico() {
		return produtos.stream().filter(p -> p.isServico()).count();
	}

	public Long getCountProduto() {
		return produtos.stream().filter(p -> !p.isServico()).count();
	}

	public Long getCount() {
		return produtos.stream().count();
	}

	public BigDecimal getSumServico() {
		return produtos.stream().filter(p -> p.getTipo().equals("servico")).map(p -> p.getPrecoVenda())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	public BigDecimal getSumProduto() {
		return produtos.stream().filter(p -> !p.getTipo().equals("servico")).map(p -> p.getPrecoVenda())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	public BigDecimal getSum() {
		return produtos.stream().map(p -> p.getPrecoVenda()).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	public BigDecimal getTotalPetShop() {
		List<ProdutoCesta> lista = new ArrayList<ProdutoCesta>();
		for (Atendimento atendimento : atendimentos) {
			if (atendimento.getServico().getTipo() == ServicoTipo.ESTETICA)
				if (this.contem(atendimento.getId()) != null)
					lista.add(this.contem(atendimento.getId()));
		}
		return lista.stream().map(p -> p.getPrecoVenda()).reduce(BigDecimal.ZERO, BigDecimal::add);

	}

	public BigDecimal getTotalClinica() {
		List<ProdutoCesta> lista = new ArrayList<ProdutoCesta>();
		for (Atendimento atendimento : atendimentos) {
			if (atendimento.getServico().getTipo() == ServicoTipo.CLINICA)
				if (this.contem(atendimento.getId()) != null)
					lista.add(this.contem(atendimento.getId()));
		}
		BigDecimal totalAtendimentos = lista.stream().map(p -> p.getPrecoVenda()).reduce(BigDecimal.ZERO,
				BigDecimal::add);
		BigDecimal vacinas = produtos.stream().filter(p -> p.getTipo().equals("vacina")).map(p -> p.getPrecoVenda())
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		return totalAtendimentos.add(vacinas);

	}

	public ProdutoCesta contem(Integer idAtendimento) {
		for (ProdutoCesta produtoCesta : produtos) {
			if (produtoCesta.getIdProdutoOriginal().equals(idAtendimento))
				return produtoCesta;
		}
		return null;
	}
}
