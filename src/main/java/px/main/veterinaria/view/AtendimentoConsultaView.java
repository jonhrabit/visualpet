package px.main.veterinaria.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import px.main.veterinaria.modelos.Atendimento;
import px.main.veterinaria.modelos.Servico;
import px.main.veterinaria.modelos.enumeracoes.SituacaoAtendimento;


@Data
public class AtendimentoConsultaView {
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.TIMESTAMP)
	private Date inicio;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fim;
	private Servico servico;
	private SituacaoAtendimento situacao;
	private List<Atendimento> resultado;
	
	public AtendimentoConsultaView(){
		Calendar primeiroDia=Calendar.getInstance();
		primeiroDia.set(Calendar.DAY_OF_MONTH,1);
		this.inicio=primeiroDia.getTime();
		this.fim=new Date();
		this.situacao=null;
		this.servico=null;
		this.resultado=new ArrayList<Atendimento>();
	}

}
