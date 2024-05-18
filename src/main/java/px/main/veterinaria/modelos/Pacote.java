package px.main.veterinaria.modelos;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	@GeneratedValue(strategy = IDENTITY)
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
