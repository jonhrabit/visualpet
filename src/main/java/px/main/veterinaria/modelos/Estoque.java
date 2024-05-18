package px.main.veterinaria.modelos;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.util.Date;

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
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "estoque")
public class Estoque {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Integer id;

	private Integer quantidade;
	private BigDecimal precoCompra;
	private String texto, usuario;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date validade;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date registro;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "produto_id")
	@JsonIgnore
	private Produto produto;

	public void put(Estoque newEstoque) {
		this.quantidade = newEstoque.getQuantidade();
		this.precoCompra = newEstoque.getPrecoCompra();
		this.texto = newEstoque.getTexto();
		this.validade = newEstoque.getValidade();
		this.usuario = newEstoque.getUsuario();
		this.registro = newEstoque.getRegistro();
	}

}
