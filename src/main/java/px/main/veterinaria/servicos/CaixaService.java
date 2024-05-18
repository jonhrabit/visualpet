package px.main.veterinaria.servicos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import px.main.veterinaria.modelos.Caixa;
import px.main.veterinaria.modelos.Cesta;
import px.main.veterinaria.modelos.Extrato;
import px.main.veterinaria.modelos.Taxa;
import px.main.veterinaria.repository.CaixaRepository;
import px.main.veterinaria.repository.TaxasRepository;
import px.main.veterinaria.view.CaixaView;

@Service
@Log4j2
public class CaixaService {

	@Autowired
	CaixaRepository caixaRepository;
	@Autowired
	TaxasRepository taxasRepository;

	public List<String> getFormasDePagamentosDoAno(Integer ano) {
		return caixaRepository.formasDePagamentoDoAno(ano);
	}

	public List<Caixa> all() {
		return caixaRepository.findAll(Sort.by("data"));
	}

	public List<Caixa> findByValorBrutoIsNull() {
		return caixaRepository.findByTipoAndValorBrutoIsNull(1);
	}

	public List<Caixa> buscarMes(Integer mes, Integer ano) {
		Calendar calendar = Calendar.getInstance();
		Date inicialDate, finalDate;
		calendar.set(ano, mes, 1, 0, 0);
		inicialDate = calendar.getTime();
		calendar.set(ano, mes + 1, 1, 0, 0);
		finalDate = calendar.getTime();
		return caixaRepository.findByDataBetween(inicialDate, finalDate);
	}

	public List<Caixa> busca(Integer ano) {
		Calendar calendar = Calendar.getInstance();
		Date inicialDate, finalDate;
		calendar.set(ano, 1, 1, 0, 0);
		inicialDate = calendar.getTime();
		calendar.set(ano, 12, 31, 23, 59);
		finalDate = calendar.getTime();
		return caixaRepository.findByDataBetween(inicialDate, finalDate);
	}

	public Caixa get(Integer id) {
		Optional<Caixa> caixa = caixaRepository.findById(id);
		if (caixa.isEmpty())
			return new Caixa();
		return caixa.get();
	}

	public boolean existeVenda(String venda) {
		List<Caixa> caixa = caixaRepository.findByCodigoVenda(venda);
		if (caixa.size() > 0)
			return true;
		return false;
	}

	public Date dataPagamento(Taxa taxa) throws ParseException {
		Calendar dataPagamento = Calendar.getInstance();
		FeriadosService feriados = new FeriadosService();
		dataPagamento.add(Calendar.DATE, +taxa.getPrazo());
		if (taxa.isPgtoDiaUtil()) {
			boolean util = false;
			while (util == false) {
				if ((dataPagamento.get(Calendar.DAY_OF_WEEK) >= 6)
						|| ((feriados.verificaFeriado(dataPagamento.getTime())))) {
					dataPagamento.add(Calendar.DATE, +1);
				} else {
					util = true;
				}
			}
		}
		return dataPagamento.getTime();
	}

	public Caixa pagamento(BigDecimal valor, Integer vezes, Integer forma, Cesta cesta) {
		Caixa caixa = new Caixa();
		Taxa taxa = taxasRepository.findByTipoAndVezes(forma, vezes);

		String texto = "";
		if (vezes > 1) {
			texto = "Compra no " + taxa.getDescricao() + " em " + vezes + " vezes de " + cesta.getCliente().getNome()
					+ ".";
		} else {
			texto = "Compra no " + taxa.getDescricao() + " de " + cesta.getCliente().getNome() + ".";
		}
		if (forma == 0) {
			texto = "Compra em Dinheiro de " + cesta.getCliente().getNome() + ".";
		}

		caixa.setTexto(texto);
		caixa.setCesta(cesta);
		caixa.setForma(taxa.getDescricao());
		caixa.setVezes(vezes);
		caixa.setValor(valor.subtract(
				valor.multiply(taxa.getValor().divide(new BigDecimal(100))).setScale(2, RoundingMode.HALF_UP)));
		caixa.setTaxa(taxa.getValor());
		caixa.setValorBruto(valor);
		caixa.setRegistro(new Date());
		caixa.setTipo(1);
		try {
			caixa.setData(dataPagamento(taxa));
		} catch (ParseException e) {
			log.info(e.getMessage());
			e.printStackTrace();
		}
		return caixa;
	}

	public List<Caixa> fechamento(Date data) {
		Calendar inicio = Calendar.getInstance();
		inicio.setTime(data);
		inicio.set(Calendar.HOUR_OF_DAY, 0);
		inicio.set(Calendar.MINUTE, 0);
		inicio.set(Calendar.SECOND, 0);

		Calendar fim = Calendar.getInstance();
		fim.setTime(data);
		fim.set(Calendar.HOUR_OF_DAY, 0);
		fim.set(Calendar.MINUTE, 0);
		fim.set(Calendar.SECOND, 0);
		fim.add(Calendar.DAY_OF_MONTH, +1);
		return caixaRepository.findByRegistroBetween(inicio.getTime(), fim.getTime());
	}

	public List<Caixa> creditos(Date data) {
		Calendar inicio = Calendar.getInstance();
		inicio.setTime(data);
		inicio.set(Calendar.HOUR_OF_DAY, 0);
		inicio.set(Calendar.MINUTE, 0);
		inicio.set(Calendar.SECOND, 0);

		Calendar fim = Calendar.getInstance();
		fim.setTime(data);
		fim.set(Calendar.HOUR_OF_DAY, 0);
		fim.set(Calendar.MINUTE, 0);
		fim.set(Calendar.SECOND, 0);
		fim.add(Calendar.DAY_OF_MONTH, +1);
		return caixaRepository.findByDataBetween(inicio.getTime(), fim.getTime()).stream()
				.filter(c -> !c.compensacaoMesmoDia()).collect(Collectors.toList());
	}

	public Caixa salvar(Caixa caixa) {
		return caixaRepository.save(caixa);
	}

	public List<Taxa> listaTaxas() {
		return taxasRepository.findAll();
	}

	public List<String> listaTaxasForma() {
		return taxasRepository.findAllDescricao();
	}

	public CaixaView preencher(CaixaView caixaView) {
		Calendar c = Calendar.getInstance();
		c.setTime(caixaView.getDataInicial());
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 1);
		caixaView.setDataInicial(c.getTime());

		c.setTime(caixaView.getDataFinal());
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		caixaView.setDataFinal(c.getTime());

		if (caixaView.getTipo() == 2) {
			if (caixaView.getForma() == "") {
				caixaView.setResultado(caixaRepository.searchNotForma(caixaView.getDataInicial(),
						caixaView.getDataFinal(), caixaView.getPagadores()));
			} else {
				caixaView.setResultado(caixaRepository.search(caixaView.getDataInicial(), caixaView.getDataFinal(),
						caixaView.getForma(), caixaView.getPagadores()));
			}
		} else {
			if (caixaView.getForma() == "") {
				caixaView.setResultado(caixaRepository.searchByTipoNotForma(caixaView.getDataInicial(),
						caixaView.getDataFinal(), caixaView.getPagadores(), caixaView.getTipo()));
			} else {
				caixaView.setResultado(caixaRepository.searchByTipo(caixaView.getDataInicial(),
						caixaView.getDataFinal(), caixaView.getForma(), caixaView.getPagadores(), caixaView.getTipo()));
			}
		}
		caixaView.setSize(caixaView.getResultado().size());
		caixaView.setTotalCredito(BigDecimal.ZERO);
		caixaView.setTotalDebito(BigDecimal.ZERO);
		caixaView.setTotal(BigDecimal.ZERO);

		for (Caixa caixa : caixaView.getResultado()) {
			if (caixa.getTipo() == 1) {
				caixaView.setTotalCredito(caixaView.getTotalCredito().add(caixa.getValor()));
			} else {
				caixaView.setTotalDebito(caixaView.getTotalDebito().add(caixa.getValor()));
			}
		}
		caixaView.setTotal(caixaView.getTotalCredito().subtract(caixaView.getTotalDebito()));
		return caixaView;
	}

	public void deletar(Integer id) {
		caixaRepository.deleteById(id);
	}

	public List<Caixa> mensal(Integer mes, Integer ano) {
		Calendar inicio = Calendar.getInstance();
		inicio.set(ano, mes, 1, 0, 0, 1);

		Calendar fim = Calendar.getInstance();
		fim.set(ano, mes, inicio.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
		return caixaRepository.findByRegistroBetween(inicio.getTime(), fim.getTime());

	}

	public List<Extrato> compararListas(List<Extrato> extratos) {
		List<Caixa> caixas = caixaRepository.notCodigoVenda();
		System.out.println("Caixas: " + caixas.size());
		System.out.println("Extratos: " + extratos.size());
		System.out.println("Interações: " + (extratos.size() * caixas.size()));
		List<Extrato> lista = new ArrayList<Extrato>();
		Boolean contem = false;
		for (Extrato extrato : extratos) {
			if ((!extrato.getCodigo_Venda().equals("")) && (extrato.getStatus().equals("Aprovada"))) {
				for (Caixa caixa : caixas) {
					if (caixa.equal(extrato)) {
						contem = true;
						caixa.setCodigoVenda(extrato.getCodigo_Venda());
						this.salvar(caixa);
						break;
					}
				}
				if (!contem) {
					if (!this.existeVenda(extrato.getCodigo_Venda())) {
						lista.add(extrato);
					}
				}
				contem = false;
			}
		}
		return lista;
	}

	public List<Caixa> buscarNotCodigoVenda() {
		return caixaRepository.notCodigoVenda();
	}

	public Taxa getTaxa(Integer forma, Integer vezes) {
		return taxasRepository.findByTipoAndVezes(forma, vezes);
	}

	public List<Taxa> getTaxasPorTipo(Integer tipo) {
		return taxasRepository.findByTipo(tipo);
	}

	public List<Caixa> getCaixaAno(Integer ano) {
		return caixaRepository.findCaixaPorAno(ano);
	}
	public List<LocalDate> getFeriadosList(){
		FeriadosService feriados = new FeriadosService();
		return feriados.getDatas();
	}

}
