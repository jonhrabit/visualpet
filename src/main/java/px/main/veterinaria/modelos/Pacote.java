package px.main.veterinaria.modelos;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@ToString
@Table(name = "pacotes")
public class Pacote {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cliente_id")
	private Cliente cliente;

	@OneToMany(mappedBy = "pacote")
	@JsonIgnore
	List<Atendimento> atendimentos;

	private BigDecimal valor;

	private Boolean pago;

	@Column(columnDefinition = "text")
	private String descricao;

	public void put(Pacote pacoteNovo) {
		this.cliente = pacoteNovo.getCliente();
		this.descricao = pacoteNovo.getDescricao();
		this.valor = pacoteNovo.getValor();
		this.atendimentos = pacoteNovo.getAtendimentos();
		this.pago = pacoteNovo.getPago();
	}

}
