package px.main.veterinaria.servicos;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import px.main.veterinaria.modelos.Atendimento;
import px.main.veterinaria.modelos.ProdutoAtendimento;
import px.main.veterinaria.modelos.enumeracoes.ServicoTipo;
import px.main.veterinaria.modelos.enumeracoes.SituacaoAtendimento;
import px.main.veterinaria.query.modelos.AtendimentosPorCliente;
import px.main.veterinaria.query.modelos.AtendimentosPorServico;
import px.main.veterinaria.query.modelos.RelatorioAtendimentosPorServico;
import px.main.veterinaria.repository.AtendimentoRepository;
import px.main.veterinaria.view.AtendimentoConsultaView;

@Service
public class AtendimentoService {
	@Autowired
	AtendimentoRepository atendimentoRepository;
	@Autowired
	PetService petService;
	@Autowired
	ClienteService clienteService;

	public List<Atendimento> All() {
		return atendimentoRepository.findAll();
	}

	public Atendimento get(Integer id) {
		Optional<Atendimento> atendimento = atendimentoRepository.findById(id);
		if (atendimento.isEmpty()) {
			return null;
		}
		return atendimento.get();
	}

	public List<ProdutoAtendimento> getProdutos(Integer id) {
		Optional<Atendimento> atendimento = atendimentoRepository.findById(id);
		if (atendimento.isEmpty()) {
			return null;
		}
		return atendimento.get().getProdutos();
	}

	public List<Atendimento> semana() {
		Calendar dataInicio = Calendar.getInstance();
		dataInicio.set(Calendar.HOUR_OF_DAY, 00);
		dataInicio.set(Calendar.MINUTE, 00);

		Calendar dataFim = Calendar.getInstance();
		dataFim.set(Calendar.HOUR_OF_DAY, 23);
		dataFim.set(Calendar.MINUTE, 59);
		dataFim.add(Calendar.DATE, +7);

		return atendimentoRepository.semanaAndNaoFinalizados(dataInicio.getTime(), dataFim.getTime());
	}

	public List<Atendimento> semana(ServicoTipo servicoTipo) {
		Calendar dataInicio = Calendar.getInstance();
		dataInicio.set(Calendar.HOUR_OF_DAY, 00);
		dataInicio.set(Calendar.MINUTE, 00);

		Calendar dataFim = Calendar.getInstance();
		dataFim.set(Calendar.HOUR_OF_DAY, 23);
		dataFim.set(Calendar.MINUTE, 59);
		dataFim.add(Calendar.DATE, +7);

		return atendimentoRepository.semanaToService(dataInicio.getTime(), dataFim.getTime(), servicoTipo);
	}

	public boolean iniciar(Integer idAtendimento) {
		Atendimento atendimento = this.get(idAtendimento);
		if (atendimento.getId() == 0)
			return false;
		atendimento.setDatainicio(new Date());
		atendimento.setSituacao(SituacaoAtendimento.ATENDIMENTO);
		this.salvar(atendimento);
		return true;
	}

	public Atendimento salvar(Atendimento atendimento) {
		if (atendimento.getAgenda() == null)
			atendimento.setAgenda(new Date());
		if (!atendimento.isNovo())
			atendimento.setProdutos(this.getProdutos(atendimento.getId()));
		return atendimentoRepository.save(atendimento);
	}

	public Atendimento salvarPdt(Atendimento atendimento) {
		if (atendimento.getAgenda() == null)
			atendimento.setAgenda(new Date());
		return atendimentoRepository.save(atendimento);
	}

	public Atendimento finalizar(Integer atendimentoId, BigDecimal valor, Integer forma) {
		Atendimento atendimento = this.get(atendimentoId);
		petService.anotar(atendimento.getPet().getId(), "Atendimento", atendimento.getServico().getTexto(), valor,
				atendimento.getId());
		clienteService.anotar(atendimento.getPet().getTutor().getId(), "Atendimento",
				atendimento.getServico().getTexto() + " de " + atendimento.getPet().getNome(), valor,
				atendimento.getId());

		atendimento.finalizar(valor, forma);

		return this.salvar(atendimento);
	}

	public boolean finalizado(Integer id) {
		Optional<Atendimento> atendimento = atendimentoRepository.findById(id);
		if (!atendimento.isEmpty())
			if (atendimento.get().getSituacao() == SituacaoAtendimento.FINALIZADO)
				return true;
		return false;
	}

	public Atendimento pendurar(Integer atendimentoId) {
		Atendimento atendimento = this.get(atendimentoId);
		atendimento.setSituacao(SituacaoAtendimento.PENDENTE);
		clienteService.anotar(atendimento.getPet().getTutor().getId(), "Atendimento",
				"Confiado " + atendimento.getServico().getTexto() + " de " + atendimento.getPet().getNome(),
				atendimento.getValor(), atendimento.getId());
		petService.anotar(atendimento.getPet().getId(), "Atendimento", "Banho Confiado.", atendimento.getValor(),
				atendimentoId);
		return this.salvar(atendimento);
	}

	public Atendimento cancelar(Integer atendimentoId) {
		Atendimento atendimento = this.get(atendimentoId);
		atendimento.setSituacao(SituacaoAtendimento.CANCELADO);
		clienteService.anotar(atendimento.getPet().getTutor().getId(), "Atendimento",
				"Cancelado " + atendimento.getServico().getTexto() + " de " + atendimento.getPet().getNome(),
				atendimento.getValor(), atendimento.getId());

		petService.anotar(atendimento.getPet().getId(), "Cancelado", "Banho", atendimentoId);
		return this.salvar(atendimento);
	}

	public List<Atendimento> aReceber() {
		return atendimentoRepository.findPendentes();
	}

	public boolean deletar(Integer id) {
		Optional<Atendimento> atendimentoOptional = atendimentoRepository.findById(id);
		if (atendimentoOptional.isEmpty())
			return false;
		atendimentoRepository.delete(atendimentoOptional.get());
		return true;
	}

	public List<Atendimento> getAtendimentosAno(Integer ano) {
		return atendimentoRepository.atendimentosAno(ano);
	}

	public AtendimentoConsultaView atualizar(AtendimentoConsultaView atendimentoConsultaView) {
		if ((atendimentoConsultaView.getSituacao() == null) && (atendimentoConsultaView.getServico() == null)) {
			atendimentoConsultaView.setResultado(atendimentoRepository.search(atendimentoConsultaView.getInicio(),
					atendimentoConsultaView.getFim()));
		} else if ((atendimentoConsultaView.getSituacao() == null) && (atendimentoConsultaView.getServico() != null)) {
			atendimentoConsultaView.setResultado(atendimentoRepository.search(atendimentoConsultaView.getInicio(),
					atendimentoConsultaView.getFim(), atendimentoConsultaView.getServico()));
		} else if ((atendimentoConsultaView.getSituacao() != null) && (atendimentoConsultaView.getServico() == null)) {
			atendimentoConsultaView.setResultado(atendimentoRepository.search(atendimentoConsultaView.getInicio(),
					atendimentoConsultaView.getFim(), atendimentoConsultaView.getSituacao()));
		} else {
			atendimentoConsultaView.setResultado(
					atendimentoRepository.search(atendimentoConsultaView.getInicio(), atendimentoConsultaView.getFim(),
							atendimentoConsultaView.getServico(), atendimentoConsultaView.getSituacao()));
		}
		return atendimentoConsultaView;
	}

	public List<AtendimentosPorCliente> getListaPorCliente(Integer mes, Integer ano) {
		mes -= 1;
		Calendar calendar = Calendar.getInstance();
		calendar.set(ano, mes, 1);
		Date inicialDate = calendar.getTime();
		calendar.set(Calendar.MONTH, mes + 1);
		Date finalDate = calendar.getTime();
		return atendimentoRepository.queryPorCliente(inicialDate, finalDate);
	}

	public List<AtendimentosPorServico> getListaPorServico(Integer mes, Integer ano) {
		mes -= 1;
		Calendar calendar = Calendar.getInstance();
		calendar.set(ano, mes, 1);
		Date inicialDate = calendar.getTime();
		calendar.set(Calendar.MONTH, mes + 1);
		Date finalDate = calendar.getTime();
		return atendimentoRepository.queryAtendimentosPorServico(inicialDate, finalDate);
	}

	public RelatorioAtendimentosPorServico getRelatorioPorServico(Integer mes, Integer ano) {
		RelatorioAtendimentosPorServico relatorioAtendimentosPorServico = new RelatorioAtendimentosPorServico();
		String[] meses = { "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro",
				"Outubro", "Novembro", "Dezembro" };
		Calendar calendar = Calendar.getInstance();
		Date inicialDate, finalDate;
		for (int i = 0; i < 3; i++) {
			mes -= 1;
			if (mes < 0) {
				mes = 11;
				ano -= 1;
			}
			calendar.set(ano, mes, 1, 0, 0);
			inicialDate = calendar.getTime();
			calendar.set(ano, mes + 1, 1, 0, 0);
			finalDate = calendar.getTime();
			relatorioAtendimentosPorServico.getTitulo().add(meses[mes] + "/" + ano);
			relatorioAtendimentosPorServico.getLista()
					.add(atendimentoRepository.queryAtendimentosPorServico(inicialDate, finalDate));
		}
		return relatorioAtendimentosPorServico;
	}

	public void deletarByPet(Integer id) {
		atendimentoRepository.deleteByPet(id);
	}

	public void excluirPacote(Integer pacote_id) {
		List<Atendimento> atendimentos = atendimentoRepository.findByPacoteId(pacote_id);
		atendimentos.forEach(a -> {
			atendimentoRepository.delete(a);
		});

	}
}
