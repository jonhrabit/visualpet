package px.main.veterinaria.modelos;


import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
