package px.main.configuracao;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import org.springframework.stereotype.Service;

import com.fincatto.documentofiscal.DFUnidadeFederativa;
import com.fincatto.documentofiscal.nfe.NFeConfig;

@Service
public class NFeConfigIMP extends NFeConfig {

	private KeyStore keyStoreCertificado = null;
	private KeyStore keyStoreCadeia = null;
	
	public NFeConfigIMP() {
	}

	@Override
	public DFUnidadeFederativa getCUF() {
		return DFUnidadeFederativa.RS;
	}

	@Override
	public String getCertificadoSenha() {
		return "o5ii4466";
	}

	@Override
	public String getCadeiaCertificadosSenha() {
		return "senha";
	}

	@Override
	public KeyStore getCertificadoKeyStore() throws KeyStoreException {
		if (this.keyStoreCertificado == null) {
			this.keyStoreCertificado = KeyStore.getInstance("PKCS12");
			try (InputStream certificadoStream = new FileInputStream("/home/felipe/Documentos/certs/JoaoFelipe.pfx")) {
				this.keyStoreCertificado.load(certificadoStream, this.getCertificadoSenha().toCharArray());
			} catch (CertificateException | NoSuchAlgorithmException | IOException e) {
				this.keyStoreCadeia = null;
				throw new KeyStoreException("Nao foi possibel montar o KeyStore o certificado", e);
			}
		}
		return this.keyStoreCertificado;
	}

	@Override
	public KeyStore getCadeiaCertificadosKeyStore() throws KeyStoreException {
		if (this.keyStoreCadeia == null) {
			this.keyStoreCadeia = KeyStore.getInstance(KeyStore.getDefaultType());
			try (InputStream cadeia = new FileInputStream("/home/felipe/Documentos/certs/homologacao.cacerts")) {
				this.keyStoreCadeia.load(cadeia, this.getCadeiaCertificadosSenha().toCharArray());
			} catch (CertificateException | NoSuchAlgorithmException | IOException e) {
				this.keyStoreCadeia = null;
				throw new KeyStoreException("Nao foi possibel montar o KeyStore a cadeia de certificados", e);
			}
		}
		return this.keyStoreCadeia;
	}

}
