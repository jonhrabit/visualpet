package px.main.veterinaria.modelos;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Extrato {
	private String transacao_ID, debito_Credito, tipo_Transacao, status, tipo_Pagamento, codigo_Venda;

	private BigDecimal valor_Bruto, valor_Desconto, valor_Taxa, valor_Liquido;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	private Date data_Transacao;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	private Date data_Compensacao;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	private Date valor_Recebido;

	private Integer parcelas;
	private Boolean recebimentos, recebidos;
	private BigDecimal valor_Taxa_Intermediacao, valor_Taxa_Parcelamento;

	public void setRecebidos(String valor) {
		if (valor.equals("1")) {
			this.recebidos = true;
		} else {
			this.recebidos = false;
		}
	}

	public void setRecebimentos(String valor) {
		if (valor.equals("1")) {
			this.recebimentos = true;
		} else {
			this.recebimentos = false;
		}
	}
	public BigDecimal tratarValor(String campo) {
		campo = campo.replace(".", "");
		campo = campo.replace(",", ".");
		return new BigDecimal(campo);
	}
	
	public Extrato(String linha, String separador) {
		String[] campo = linha.split(separador);
		this.setTransacao_ID(campo[0]);
		this.setDebito_Credito(campo[3]);
		this.setTipo_Transacao(campo[4]);
		this.setStatus(campo[5]);
		this.setTipo_Pagamento(campo[6]);
		this.setValor_Bruto(tratarValor(campo[7]));
		this.setValor_Desconto(tratarValor(campo[8]));
		this.setValor_Taxa(tratarValor(campo[9]));
		this.setValor_Liquido(tratarValor(campo[10]));

		this.setValor_Taxa_Intermediacao(new BigDecimal(campo[23].replace(",", ".")));
		this.setValor_Taxa_Parcelamento(new BigDecimal(campo[24].replace(",", ".")));

		try {
			this.setData_Transacao(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(campo[13]));
			this.setData_Compensacao(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(campo[14]));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.setParcelas(Integer.parseInt(campo[16]));
		this.setCodigo_Venda(campo[18]);
		this.setRecebimentos(campo[20]);
		this.setRecebidos(campo[21]);
	}

}
