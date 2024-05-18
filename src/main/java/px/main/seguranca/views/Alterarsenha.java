package px.main.seguranca.views;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class Alterarsenha {
	private String senha,nova,repetida;

	public Alterarsenha() {
	}
	
	public boolean CompararSenhas(){
		if(nova.equals(repetida)){
		return true;	
		}else{
		return false;
		}
	}
}
