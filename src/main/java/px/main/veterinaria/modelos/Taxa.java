package px.main.veterinaria.modelos;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
@Table(name = "taxas")
public class Taxa {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer vezes, tipo, prazo;
	private BigDecimal valor;
	private String descricao;
	private boolean pgtoDiaUtil;
	
	public Taxa() {
	}
	
}
