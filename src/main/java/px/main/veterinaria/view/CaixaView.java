package px.main.veterinaria.view;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import px.main.veterinaria.modelos.Caixa;

public class CaixaView {
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dataInicial;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dataFinal;

	private String pagadores, forma;
	private BigDecimal totalCredito, totalDebito, total;
	private Integer size, tipo;
	private List<Caixa> resultado;

	public CaixaView(Date dataInicial, Date dataFinal, String forma, String pagadores, Integer tipo) {
		super();
		this.dataInicial = dataInicial;
		this.dataFinal = dataFinal;

		this.forma = forma;
		this.pagadores = pagadores;
		this.tipo = tipo;
	}

	public CaixaView() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 1);
		this.dataInicial = c.getTime();

		c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);

		this.dataFinal = c.getTime();

		this.forma = "";
		this.pagadores = "";
		this.tipo = 2;
		this.setResultado(null);
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public String getForma() {
		return forma;
	}

	public void setForma(String forma) {
		this.forma = forma;
	}

	public String getPagadores() {
		return pagadores;
	}

	public void setPagadores(String pagadores) {
		this.pagadores = pagadores;
	}

	public BigDecimal getTotalCredito() {
		return totalCredito;
	}

	public void setTotalCredito(BigDecimal totalCredito) {
		this.totalCredito = totalCredito;
	}

	public BigDecimal getTotalDebito() {
		return totalDebito;
	}

	public void setTotalDebito(BigDecimal totalDebito) {
		this.totalDebito = totalDebito;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public List<Caixa> getResultado() {
		return resultado;
	}

	public void setResultado(List<Caixa> resultado) {
		this.resultado = resultado;
		if (resultado != null) {
			this.size = resultado.size();

			this.totalCredito = new BigDecimal(0);
			this.totalDebito = new BigDecimal(0);
			this.total = new BigDecimal(0);

			for (Caixa caixa : resultado) {
				if (caixa.getTipo() == 1) {
					this.totalCredito = this.totalCredito.add(caixa.getValor());
				} else {
					this.totalDebito = this.totalDebito.add(caixa.getValor());
				}
			}
			this.total = this.totalCredito.subtract(this.totalDebito);
		}
	}

}
