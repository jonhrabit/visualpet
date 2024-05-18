package px.main.veterinaria.modelos;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ClienteInformacao extends Informacao {

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cliente_id")
	private Cliente cliente;

	public ClienteInformacao(String tipo, String texto, BigDecimal valor, Cliente cliente, Integer idLink) {
		super(tipo, texto, valor, idLink);
		this.cliente = cliente;
	}


	public ClienteInformacao() {
		super();
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

}
