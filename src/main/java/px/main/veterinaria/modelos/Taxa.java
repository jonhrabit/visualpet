package px.main.veterinaria.modelos;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
@Table(name = "taxas")
public class Taxa {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Integer id;
	private Integer vezes, tipo, prazo;
	private BigDecimal valor;
	private String descricao;
	private boolean pgtoDiaUtil;
	
	public Taxa() {
	}
	
}
