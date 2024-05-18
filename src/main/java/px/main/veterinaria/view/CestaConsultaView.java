package px.main.veterinaria.view;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import px.main.veterinaria.modelos.Cesta;
import px.main.veterinaria.modelos.Cliente;
import px.main.veterinaria.modelos.ProdutoCesta;

@Data
@AllArgsConstructor
public class CestaConsultaView {
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dataInicial;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dataFinal;

	private String tipo;
	private Integer forma;
	private Cliente tutor;
	private List<Cesta> resultado;

	public CestaConsultaView() {
		Calendar calendar= Calendar.getInstance();
		this.dataFinal=calendar.getTime();
		calendar.set(Calendar.DAY_OF_MONTH,1);
		this.dataInicial = calendar.getTime();
	}
	
	public BigDecimal getTotal() {
		BigDecimal total = new BigDecimal(0);
		for (Cesta cesta : this.resultado) {
			total = total.add(cesta.getTotal());
		}
		return total;
	}

	public List<Cesta> getResultadoFiltrado() {
		//Predicate<Cesta> byForma = cesta -> cesta.getFormaPagamento() == this.forma;
		Predicate<Cesta> byCliente = cesta -> cesta.getCliente().getId() == this.tutor.getId();

		if (this.tipo != "") {
			Iterator<Cesta> itr = resultado.iterator();
			while (itr.hasNext()) {
				Cesta cesta = itr.next();
				Iterator<ProdutoCesta> produtoItr = cesta.getProdutos().iterator();
				while (produtoItr.hasNext()) {
					ProdutoCesta produto = produtoItr.next();
					if (!produto.getTipo().equals(this.tipo)) {
						produtoItr.remove();
					}
				}
				if (cesta.getProdutos().size() <= 0) {
					itr.remove();
				}
			}
		}
		/*
		 * if (this.forma != 0) this.resultado =
		 * this.resultado.stream().filter(byForma).collect(Collectors.toList());
		 */
		if (this.tutor != null)
			this.resultado = this.resultado.stream().filter(byCliente).collect(Collectors.toList());

		return resultado;
	}

	public List<ProdutoCesta> getProdutos() {
		List<ProdutoCesta> lista = new ArrayList<ProdutoCesta>();
		for (Cesta cesta : this.resultado) {
			lista.addAll(cesta.getProdutos());
		}
		return lista;
	}

	public List<ProdutoCesta> getProdutosFiltrados() {
		List<ProdutoCesta> lista = this.getProdutos();
		Predicate<ProdutoCesta> byTipo = produto -> produto.getTipo() == this.tipo;

		if (this.tipo != "")
			lista = lista.stream().filter(byTipo).collect(Collectors.toList());

		return lista;
	}
}
