package px.main.veterinaria.view;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import px.main.veterinaria.modelos.Cesta;

@Data
@NoArgsConstructor
public class TicketView {
	Integer mes, frequencia;
	BigDecimal ticketMedio, total;

	public TicketView(Integer mes) {
		this.mes = mes;
		this.setTotal(BigDecimal.ZERO);
		this.ticketMedio = BigDecimal.ZERO;
		this.frequencia = 0;
	}

	public void addFrequencia() {
		this.frequencia++;
	}

	public void setTicketMedio() {
		if (this.total != BigDecimal.ZERO)
			this.ticketMedio = this.total.divide(new BigDecimal(this.frequencia), 2, RoundingMode.HALF_UP);
	}

	public static List<TicketView> getLista(List<Cesta> lista) {
		List<TicketView> resultado = new ArrayList<TicketView>();
		for (int i = 0; i <12; i++) {
			resultado.add(new TicketView(i));
		}
		Calendar c = Calendar.getInstance();
		for (Cesta cesta : lista) {
			c.setTime(cesta.getData());
			if (cesta.getTotal() != null)
				resultado.get(c.get(Calendar.MONTH))
						.setTotal(resultado.get(c.get(Calendar.MONTH)).getTotal().add(cesta.getTotal()));
			resultado.get(c.get(Calendar.MONTH)).addFrequencia();
		}
		for (int z = 0; z < 12; z++) {
			resultado.get(z).setTicketMedio();
		}
		return resultado;
	}
}
