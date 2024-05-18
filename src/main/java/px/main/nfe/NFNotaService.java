package px.main.nfe;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.fincatto.documentofiscal.DFAmbiente;
import com.fincatto.documentofiscal.DFModelo;
import com.fincatto.documentofiscal.DFPais;
import com.fincatto.documentofiscal.DFUnidadeFederativa;
import com.fincatto.documentofiscal.nfe.NFTipoEmissao;
import com.fincatto.documentofiscal.nfe400.classes.NFEndereco;
import com.fincatto.documentofiscal.nfe400.classes.NFFinalidade;
import com.fincatto.documentofiscal.nfe400.classes.NFIndicadorFormaPagamento;
import com.fincatto.documentofiscal.nfe400.classes.NFModalidadeFrete;
import com.fincatto.documentofiscal.nfe400.classes.NFProcessoEmissor;
import com.fincatto.documentofiscal.nfe400.classes.NFRegimeTributario;
import com.fincatto.documentofiscal.nfe400.classes.NFTipo;
import com.fincatto.documentofiscal.nfe400.classes.NFTipoImpressao;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFIdentificadorLocalDestinoOperacao;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFIndicadorIEDestinatario;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFIndicadorPresencaComprador;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFMeioPagamento;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNota;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfo;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoDestinatario;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoEmitente;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoFormaPagamento;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoICMSTotal;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoIdentificacao;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoInformacoesAdicionais;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoPagamento;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoResponsavelTecnico;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoTotal;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoTransporte;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFOperacaoConsumidorFinal;

import org.springframework.stereotype.Service;

import px.main.veterinaria.modelos.Caixa;
import px.main.veterinaria.modelos.Cesta;
import px.main.veterinaria.modelos.Cliente;

@Service
public class NFNotaService {
	private final static String codNumerico = "99999998";
	private final static String cnpj = "00001246245027";

	public static NFNota gerarNFNota(Cesta cesta) {
		NFNota nota = new NFNota();
		nota.setInfo(gerarNFNotaInfo(cesta));
		return nota;
	}

	private static NFNotaInfo gerarNFNotaInfo(Cesta cesta) {
		NFNotaInfo notaInfo = new NFNotaInfo();
		notaInfo.setEmitente(getEmitente());
		notaInfo.setIdentificacao(getIdentificacao(cesta));
		notaInfo.setDestinatario(destinatario(cesta.getCliente()));
		notaInfo.setInformacaoResposavelTecnico(getResponsavel());
		notaInfo.setIdentificador(gerarChaveAcesso(notaInfo.getIdentificacao()));
		notaInfo.setVersao(new BigDecimal("4.00"));
		notaInfo.setTransporte(getTransporte());
		notaInfo.setItens(NFNotaItemService.gerarItens(cesta));
		notaInfo.setPagamento(getPagamento(cesta));
		notaInfo.setTotal(getNFNotaInfoTotal(cesta));
		notaInfo.setInformacoesAdicionais(getInformacoesAdicionais());
		int dv = Integer
				.parseInt(String.valueOf(notaInfo.getIdentificador().charAt(notaInfo.getIdentificador().length() - 1)));
		notaInfo.getIdentificacao().setDigitoVerificador(dv);

		return notaInfo;

	}

	private static NFNotaInfoEmitente getEmitente() {
		NFNotaInfoEmitente emitente = new NFNotaInfoEmitente();
		emitente.setCpf("01246245027");
		emitente.setInscricaoEstadual("0962219703");
		emitente.setClassificacaoNacionalAtividadesEconomicas("4623109");
		emitente.setInscricaoMunicipal("10987126");
		emitente.setRazaoSocial("Joao Felipe Pereira Xavier");
		emitente.setRegimeTributario(NFRegimeTributario.SIMPLES_NACIONAL);

		NFEndereco endereco = new NFEndereco();
		endereco.setCep("90830040");
		endereco.setTelefone("5130227926");
		endereco.setLogradouro("RUA FABIO DE BARROS");
		endereco.setUf(DFUnidadeFederativa.RS);
		endereco.setBairro("Nonoai");
		endereco.setNumero("319");
		endereco.setCodigoMunicipio("4314902");
		endereco.setDescricaoMunicipio("Porto Alegre");
		endereco.setCodigoPais(DFPais.BRASIL);

		emitente.setEndereco(endereco);

		return emitente;
	}

	private static NFNotaInfoIdentificacao getIdentificacao(Cesta cesta) {
		NFNotaInfoIdentificacao ide = new NFNotaInfoIdentificacao();
		ide.setAmbiente(DFAmbiente.HOMOLOGACAO);
		ide.setCodigoMunicipio("4314902");
		ide.setCodigoRandomico("99999998");
		ide.setDataHoraEmissao(ZonedDateTime.now());

		ide.setOperacaoConsumidorFinal(NFOperacaoConsumidorFinal.SIM);
		ide.setIndicadorPresencaComprador(NFIndicadorPresencaComprador.OPERACAO_PRESENCIAL);
		ide.setFinalidade(NFFinalidade.NORMAL);
		ide.setModelo(DFModelo.NFE);
		ide.setProgramaEmissor(NFProcessoEmissor.CONTRIBUINTE);
		ide.setNaturezaOperacao("VENDA DE MERCADORIA COMPRADA DE TERCEIRAS");
		ide.setNumeroNota(cesta.getId().toString());
		ide.setSerie("920");
		ide.setVersaoEmissor("2.0");
		ide.setTipo(NFTipo.SAIDA);
		ide.setTipoEmissao(NFTipoEmissao.EMISSAO_NORMAL);
		ide.setTipoImpressao(NFTipoImpressao.DANFE_NORMAL_RETRATO);
		ide.setIdentificadorLocalDestinoOperacao(NFIdentificadorLocalDestinoOperacao.OPERACAO_INTERNA);
		ide.setUf(DFUnidadeFederativa.RS);
		return ide;
	}

	private static NFNotaInfoDestinatario destinatario(Cliente cliente) {
		cliente.nullFields();
		NFNotaInfoDestinatario destinatario = new NFNotaInfoDestinatario();
		destinatario.setCnpj("99999999000191");
		destinatario.setRazaoSocial("NF-E EMITIDA EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL");
		// destinatario.setCpf(cliente.getCpf());
		// destinatario.setRazaoSocial(cliente.getNome());
		destinatario.setInscricaoEstadual("");
		if (cliente.getEmail() != null) {
			destinatario.setEmail(cliente.getEmail());
		} else {
			destinatario.setEmail("a@b.com");
		}
		destinatario.setIndicadorIEDestinatario(NFIndicadorIEDestinatario.NAO_CONTRIBUINTE);

		NFEndereco endereco = new NFEndereco();
		if (cliente.getCep() != null)
			endereco.setCep(cliente.getCep());
		if (cliente.getCelular() != null)
			endereco.setTelefone(NFNotaItemService.ASCII_esp(cliente.getCelular().trim()));
		else
			endereco.setTelefone("999999999");

		endereco.setUf(DFUnidadeFederativa.RS);

		if (cliente.getBairro() != null)
			endereco.setBairro(cliente.getBairro());
		else
			endereco.setBairro("NAO INFORMADO");

		if (cliente.getEndereco() != null) {
			String end[] = cliente.getEndereco().split(",");
			if (end.length > 0)
				if (!end[0].isBlank())
					endereco.setLogradouro(NFNotaItemService.ASCII(end[0].trim()));
			if (end.length > 1)
				if (!end[1].isBlank())
					endereco.setNumero(NFNotaItemService.ASCII(end[1].trim()));
			if (end.length > 2)
				if (!end[2].isBlank())
					endereco.setComplemento(NFNotaItemService.ASCII(end[2].trim()));
		}
		if (endereco.getLogradouro() == null)
			endereco.setLogradouro("NAO INFORMADO");
		if (endereco.getNumero() == null)
			endereco.setNumero("0");

		endereco.setCodigoMunicipio("4314902");
		if (cliente.getCidadeUF() != null)
			if (cliente.getCidadeUF().split("/")[0].equals("Porto Alegre"))
				endereco.setDescricaoMunicipio("Porto Alegre");
			else
				endereco.setDescricaoMunicipio(NFNotaItemService.ASCII(cliente.getCidadeUF().split("/")[0]));
		else
			endereco.setDescricaoMunicipio("NAO INFORMADO");

		endereco.setCodigoPais(DFPais.BRASIL);

		destinatario.setEndereco(endereco);

		return destinatario;
	}

	private static NFNotaInfoResponsavelTecnico getResponsavel() {
		NFNotaInfoResponsavelTecnico responsavel = new NFNotaInfoResponsavelTecnico();
		responsavel.setCnpj("72573546000142");
		responsavel.setContatoNome("JOAO FELIPE PEREIRA XAVIER");
		responsavel.setTelefone("992166439");
		responsavel.setEmail("joao.fpx@hotmail.com");
		return responsavel;
	}

	private static NFNotaInfoTransporte getTransporte() {
		NFNotaInfoTransporte transporte = new NFNotaInfoTransporte();
		transporte.setModalidadeFrete(NFModalidadeFrete.SEM_OCORRENCIA_TRANSPORTE);
		return transporte;
	}

	private static NFNotaInfoTotal getNFNotaInfoTotal(Cesta cesta) {
		final NFNotaInfoTotal total = new NFNotaInfoTotal();
		total.setIcmsTotal(getNFNotaInfoICMSTotal(cesta));
		// total.setIssqnTotal(getNFNotaInfoISSQNTotal());
		return total;
	}

	private static NFNotaInfoICMSTotal getNFNotaInfoICMSTotal(Cesta cesta) {
		final NFNotaInfoICMSTotal icmsTotal = new NFNotaInfoICMSTotal();
		icmsTotal.setBaseCalculoICMS(BigDecimal.ZERO);
		icmsTotal.setOutrasDespesasAcessorias(BigDecimal.ZERO);
		icmsTotal.setBaseCalculoICMSST(BigDecimal.ZERO);
		icmsTotal.setValorCOFINS(BigDecimal.ZERO);
		icmsTotal.setValorPIS(BigDecimal.ZERO);
		icmsTotal.setValorTotalDesconto(BigDecimal.ZERO);
		icmsTotal.setValorTotalDosProdutosServicos(BigDecimal.ZERO);
		icmsTotal.setValorTotalFrete(BigDecimal.ZERO);
		icmsTotal.setValorTotalICMS(BigDecimal.ZERO);
		icmsTotal.setValorTotalICMSST(BigDecimal.ZERO);
		icmsTotal.setValorTotalII(BigDecimal.ZERO);
		icmsTotal.setValorTotalIPI(BigDecimal.ZERO);
		icmsTotal.setValorTotalNFe(cesta.getTotal());
		icmsTotal.setValorTotalDosProdutosServicos(cesta.getTotal());
		icmsTotal.setValorTotalSeguro(BigDecimal.ZERO);
		icmsTotal.setValorICMSDesonerado(BigDecimal.ZERO);
		icmsTotal.setValorICMSFundoCombatePobreza(BigDecimal.ZERO);
		icmsTotal.setValorICMSPartilhaDestinatario(BigDecimal.ZERO);
		icmsTotal.setValorICMSPartilhaRementente(BigDecimal.ZERO);
		icmsTotal.setValorTotalFundoCombatePobreza(BigDecimal.ZERO);
		icmsTotal.setValorTotalFundoCombatePobrezaST(BigDecimal.ZERO);
		icmsTotal.setValorTotalFundoCombatePobrezaSTRetido(BigDecimal.ZERO);
		icmsTotal.setValorTotalIPIDevolvido(BigDecimal.ZERO);
		return icmsTotal;
	}

	/* private static NFNotaInfoISSQNTotal getNFNotaInfoISSQNTotal() {
		final NFNotaInfoISSQNTotal issqnTotal = new NFNotaInfoISSQNTotal();
		issqnTotal.setTributacao(NFNotaInfoRegimeEspecialTributacao.MICROEMPRESARIO_E_EMPRESA_PEQUENOPORTE);
		issqnTotal.setBaseCalculoISS(BigDecimal.ZERO);
		issqnTotal.setValorCOFINSsobreServicos(BigDecimal.ZERO);
		issqnTotal.setValorPISsobreServicos(BigDecimal.ZERO);
		issqnTotal.setValorTotalISS(BigDecimal.ZERO);
		issqnTotal.setValorTotalServicosSobNaoIncidenciaNaoTributadosICMS(BigDecimal.ZERO);
		issqnTotal.setDataPrestacaoServico(LocalDate.of(2014, 1, 1));
		issqnTotal.setValorDeducao(BigDecimal.ZERO);
		issqnTotal.setValorOutros(BigDecimal.ZERO);
		issqnTotal.setValorTotalDescontoIncondicionado(BigDecimal.ZERO);
		issqnTotal.setValorTotalDescontoCondicionado(BigDecimal.ZERO);
		issqnTotal.setValorTotalRetencaoISS(BigDecimal.ZERO);
		return issqnTotal;
	} */

	private static NFNotaInfoPagamento getPagamento(Cesta cesta) {
		NFNotaInfoPagamento pagamento = new NFNotaInfoPagamento();
		List<NFNotaInfoFormaPagamento> lista = new ArrayList<NFNotaInfoFormaPagamento>();
		NFNotaInfoFormaPagamento pgto1 = new NFNotaInfoFormaPagamento();
		for (Caixa caixa : cesta.getCaixas()) {
			switch (caixa.getForma()) {
			case "Dinheiro":
				pgto1.setMeioPagamento(NFMeioPagamento.DINHEIRO);
				break;
			case "Débito":
				pgto1.setMeioPagamento(NFMeioPagamento.CARTAO_DEBITO);
				break;
			case "Crédito":
				pgto1.setMeioPagamento(NFMeioPagamento.CARTAO_CREDITO);
				break;
			}
			pgto1.setValorPagamento(caixa.getValorBruto());
			pgto1.setIndicadorFormaPagamento(NFIndicadorFormaPagamento.A_VISTA);
			lista.add(pgto1);
		}
		pagamento.setDetalhamentoFormasPagamento(lista);
		return pagamento;
	}

	/* private static NFPessoaAutorizadaDownloadNFe getPessoaAutorizadaDownloadNFe(String cpfORcnpj) {
		final NFPessoaAutorizadaDownloadNFe pessoa = new NFPessoaAutorizadaDownloadNFe();
		pessoa.setCnpj(cpfORcnpj);
		return pessoa;
	} */

	private static NFNotaInfoInformacoesAdicionais getInformacoesAdicionais() {
		NFNotaInfoInformacoesAdicionais adicionais = new NFNotaInfoInformacoesAdicionais();
		/*
		 * List<NFNotaInfoObservacao> observacoesContribuinte = new
		 * ArrayList<NFNotaInfoObservacao>(); NFNotaInfoObservacao obs = new
		 * NFNotaInfoObservacao(); obs.
		 * setConteudoCampo("DOCUMENTO EMITIDO POR ME OU EPP OPTANTE PELO SIMPLES NACIONAL NAO GERA DIREITO A CREDITO FISCAL DE IPI"
		 * ); obs.setIdentificacaoCampo("campo1"); observacoesContribuinte.add(obs);
		 * adicionais.setObservacoesContribuinte(observacoesContribuinte);
		 */
		adicionais.setInformacoesComplementaresInteresseContribuinte(
				"DOCUMENTO EMITIDO POR ME OU EPP OPTANTE PELO SIMPLES NACIONAL NAO GERA DIREITO A CREDITO FISCAL DE IPI");

		return adicionais;
	}

	private static String gerarChaveAcesso(NFNotaInfoIdentificacao ide) {

		String chave = "";
		chave += ide.getUf().getCodigoIbge();
		chave += ide.getDataHoraEmissao().format(DateTimeFormatter.ofPattern("yy"));
		chave += ide.getDataHoraEmissao().format(DateTimeFormatter.ofPattern("MM"));
		chave += cnpj;
		chave += "55";
		chave += String.format("%03d", Integer.parseInt(ide.getSerie()));
		chave += String.format("%09d", Integer.parseInt(ide.getNumeroNota()));
		chave += "1";
		chave += codNumerico;

		int total = 0;
		int peso = 2;

		for (int i = 0; i < chave.length(); i++) {
			total += (chave.charAt((chave.length() - 1) - i) - '0') * peso;
			peso++;
			if (peso == 10)
				peso = 2;
		}
		int resto = total % 11;
		if (resto == 0 || resto == 1)
			chave += 0;
		else
			chave += 11 - resto;

		return chave;

	}
}
