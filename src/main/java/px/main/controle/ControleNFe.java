package px.main.controle;

import java.math.BigDecimal;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fincatto.documentofiscal.DFModelo;
import com.fincatto.documentofiscal.DFUnidadeFederativa;
import com.fincatto.documentofiscal.nfe.classes.distribuicao.NFDistribuicaoIntRetorno;
import com.fincatto.documentofiscal.nfe400.classes.NFProtocolo;
import com.fincatto.documentofiscal.nfe400.classes.evento.NFEnviaEventoRetorno;
import com.fincatto.documentofiscal.nfe400.classes.lote.consulta.NFLoteConsultaRetorno;
import com.fincatto.documentofiscal.nfe400.classes.lote.envio.NFLoteEnvio;
import com.fincatto.documentofiscal.nfe400.classes.lote.envio.NFLoteEnvioRetornoDados;
import com.fincatto.documentofiscal.nfe400.classes.lote.envio.NFLoteIndicadorProcessamento;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNota;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaProcessada;
import com.fincatto.documentofiscal.nfe400.classes.statusservico.consulta.NFStatusServicoConsultaRetorno;
import com.fincatto.documentofiscal.nfe400.webservices.WSFacade;

import px.main.configuracao.NFeConfigIMP;
import px.main.nfe.NFNotaService;
import px.main.nfe.NfeFilesService;
import px.main.veterinaria.modelos.Cesta;
import px.main.veterinaria.servicos.CestaService;

@Controller
@RequestMapping("/nfe")
public class ControleNFe {
	@Autowired
	NFeConfigIMP config;
	@Autowired
	private CestaService cestaService;
	@Autowired
	private NfeFilesService nfeFilesService;

	@RequestMapping("")
	public ModelAndView index() throws KeyManagementException, UnrecoverableKeyException, KeyStoreException,
			NoSuchAlgorithmException, Exception {
		ModelAndView model = new ModelAndView("nfe/index");
		return model;
	}

	@RequestMapping("/status")
	public ModelAndView status() throws KeyManagementException, UnrecoverableKeyException, KeyStoreException,
			NoSuchAlgorithmException, Exception {
		NFStatusServicoConsultaRetorno retorno = new WSFacade(config).consultaStatus(DFUnidadeFederativa.RS,
				DFModelo.NFE);
		System.out.println(retorno.getStatus());
		System.out.println(retorno.getMotivo());
		ModelAndView model = new ModelAndView("nfe/index");
		model.addObject("retorno", retorno);
		return model;
	}

	@RequestMapping(value = "/enviarlote", method = RequestMethod.POST)
	public @ResponseBody NFLoteEnvioRetornoDados enviarLoteToCesta(@RequestBody List<Cesta> lista)
			throws KeyManagementException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException,
			Exception {
		NFLoteEnvio lote = new NFLoteEnvio();
		lote.setVersao("4.00");
		Integer idLote = 1;
		if (!nfeFilesService.cfg("ultimo_lote").equals("")) {
			idLote = Integer.parseInt(nfeFilesService.cfg("ultimo_lote")) + 1;
		}
		lote.setIdLote(idLote.toString());
		List<NFNota> notas = new ArrayList<NFNota>();
		NFNota nota = new NFNota();
		for (Cesta cesta : lista) {
			nota = NFNotaService.gerarNFNota(cesta);
			notas.add(nota);
		}
		lote.setNotas(notas);
		lote.setIndicadorProcessamento(NFLoteIndicadorProcessamento.PROCESSAMENTO_ASSINCRONO);

		NFLoteEnvioRetornoDados retorno = new WSFacade(config).enviaLote(lote);
		System.out.println(retorno.getRetorno().getInfoRecebimento().getRecibo());
		if (retorno.getRetorno().getStatus().equals("103")) {
			nfeFilesService.gravarRetorno(retorno);
			nfeFilesService.cfgSalvar("ultimo_lote", idLote.toString());
		}
		return retorno;
	}

	@RequestMapping(value = "/consultardata/{data}", method = RequestMethod.GET)
	public @ResponseBody List<Cesta> criarLote(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date data) {
		System.out.println(data);
		List<Cesta> lista = cestaService.getListDay(data);
		return lista;
	}

	@RequestMapping(value = "/processar/{lote}", method = RequestMethod.GET)
	public @ResponseBody NFLoteConsultaRetorno processarLote(@PathVariable String lote) throws KeyManagementException,
			UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, Exception {
		NFLoteEnvioRetornoDados retornoDados = nfeFilesService.lerRetorno(lote);
		NFLoteConsultaRetorno retorno = new WSFacade(config)
				.consultaLote(retornoDados.getRetorno().getInfoRecebimento().getRecibo(), DFModelo.NFE);
		if (retorno.getStatus().equals("104")) {
			nfeFilesService.deletarLote(lote);
			nfeFilesService.gravarRespostaLote(retorno);
			for (NFProtocolo protocolo : retorno.getProtocolos()) {
				for (NFNota nota : retornoDados.getLoteAssinado().getNotas()) {
					if (nota.getInfo().getIdentificador().equals("NFe" + protocolo.getProtocoloInfo().getChave())) {
						final NFNotaProcessada notaProcessada = new NFNotaProcessada();
						notaProcessada.setVersao(new BigDecimal("4.00"));
						notaProcessada.setProtocolo(protocolo);
						notaProcessada.setNota(nota);
						nfeFilesService.gravarNotaProcessada(notaProcessada);

						if (protocolo.getProtocoloInfo().getStatus().equals("100")) {
							cestaService.setNota(
									Integer.parseInt(
											notaProcessada.getNota().getInfo().getIdentificacao().getNumeroNota()),
									notaProcessada.getNota().getInfo().getChaveAcesso());
						}
						break;
					}

				}
			}
		} else {
			System.out.println("Lote ainda não processado.");
		}
		System.out.println(retorno);
		return retorno;
	}

	@RequestMapping(value = "/processamento", method = RequestMethod.GET)
	public @ResponseBody List<String> loteProcessamento() {
		String[] lista = nfeFilesService.processamentoLoteFiles();
		List<String> organizada = new ArrayList<String>();
		boolean encontrado = false;
		for (String dir : lista) {
			encontrado = false;
			for (String organiz : organizada) {
				if (organiz.equals(dir.split("-")[0])) {
					encontrado = true;
					break;
				}
			}
			if (!encontrado)
				organizada.add(dir.split("-")[0]);
		}
		return organizada;
	}

	@RequestMapping(value = "/notas", method = RequestMethod.GET)
	public @ResponseBody List<String> notasConsultar() {
		String[] lista = nfeFilesService.ConsultarNotas();
		List<String> organizada = new ArrayList<String>();
		boolean encontrado = false;
		for (String dir : lista) {
			encontrado = false;
			for (String organiz : organizada) {
				if (organiz.equals(dir.split("-")[0])) {
					encontrado = true;
					break;
				}
			}
			if (encontrado)
				break;
			organizada.add(dir.split("-")[0]);
		}
		return organizada;
	}

	@RequestMapping(value = "/nota/{chave}", method = RequestMethod.GET)
	public @ResponseBody NFNotaProcessada getNota(@PathVariable String chave) throws Exception {
		return nfeFilesService.lerNotaProcessada(chave);
	}

	@RequestMapping(value = "/cancelar/{chaveDeAcessoDaNota}/{protocoloDaNota}/{motivoCancelaamento}", method = RequestMethod.GET)
	public @ResponseBody NFEnviaEventoRetorno cancelarNota(@PathVariable String chaveDeAcessoDaNota,
			@PathVariable String protocoloDaNota, @PathVariable String motivoCancelaamento)
			throws KeyManagementException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException,
			Exception {
		return new WSFacade(config).cancelaNota(chaveDeAcessoDaNota, protocoloDaNota, motivoCancelaamento);
	}

	@RequestMapping(value = "/consultar/{cnpj}/{chaveAcesso}/{nsu}/{ultNsu}", method = RequestMethod.GET)
	public @ResponseBody NFDistribuicaoIntRetorno consultarNota(@PathVariable String cnpj,
			@PathVariable String chaveAcesso, @PathVariable String nsu, @PathVariable String ultNsu)
			throws KeyManagementException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException,
			Exception {
		return new WSFacade(config).consultarDistribuicaoDFe(cnpj, DFUnidadeFederativa.RS, chaveAcesso, nsu, ultNsu);
	}

	@RequestMapping(value = "/corrigir/{chaveDeAcessoDaNota}/{textoCorrecao}/{sequencialEventoDaNota}", method = RequestMethod.GET)
	public @ResponseBody NFEnviaEventoRetorno corrigeNota(@PathVariable String chaveDeAcessoDaNota,
			@PathVariable String textoCorrecao, @PathVariable int sequencialEventoDaNota) throws KeyManagementException,
			UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, Exception {
		return new WSFacade(config).corrigeNota(chaveDeAcessoDaNota, textoCorrecao, sequencialEventoDaNota);
	}

}
