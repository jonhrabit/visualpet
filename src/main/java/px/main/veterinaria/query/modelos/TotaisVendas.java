package px.main.veterinaria.query.modelos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotaisVendas {
	String forma;
	BigDecimal Liquido, Bruto;
}
