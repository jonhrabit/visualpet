package px.main.veterinaria.modelos;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
@Table(name = "caixa")
public class Caixa {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Integer id;

	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;

	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	@Temporal(TemporalType.TIMESTAMP)
	private Date registro;

	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	@JoinColumn(name = "cesta_id")
	@JsonIgnore
	private Cesta cesta;

	private String texto, codigoVenda, forma;
	private Integer tipo, vezes;
	private BigDecimal valor, valorBruto, taxa;

	public Caixa() {
		this.data = new Date();
		this.registro = new Date();
		this.tipo = 0;
	}

	public boolean equal(Extrato extrato) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(this.registro);
		Calendar calendarExtrato = Calendar.getInstance();
		calendarExtrato.setTime(extrato.getData_Transacao());
		if ((calendar.get(Calendar.DAY_OF_MONTH) == calendarExtrato.get(Calendar.DAY_OF_MONTH))
				&& (calendar.get(Calendar.MONTH) == calendarExtrato.get(Calendar.MONTH))
				&& (calendar.get(Calendar.YEAR) == calendarExtrato.get(Calendar.YEAR))
				&& (this.getValor().compareTo(extrato.getValor_Liquido()) == 0)) {
			return true;
		}
		return false;
	}
	public boolean compensacaoMesmoDia() {
		Calendar data = Calendar.getInstance();
		Calendar registro = Calendar.getInstance();
		data.setTime(this.data);
		registro.setTime(this.registro);
		boolean igual = true;
		if(data.get(0)!=registro.get(0))
			igual=false;
		if(data.get(1)!=registro.get(1))
			igual=false;
		if(data.get(2)!=registro.get(2))
			igual=false;
		return igual;
	}
	public Integer getMes() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(this.data);
		return calendar.get(Calendar.MONTH);
	}
}
