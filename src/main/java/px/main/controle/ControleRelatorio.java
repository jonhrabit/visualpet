package px.main.controle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import px.main.veterinaria.servicos.RelatorioService;

@Controller
@RequestMapping("/relatorio")
public class ControleRelatorio {
	@Autowired
	RelatorioService relatorioService;

	@RequestMapping(value = "/{ano}", method = RequestMethod.GET)
	public ModelAndView consulta(@PathVariable Integer ano) {
		ModelAndView model = new ModelAndView("relatorio/relatorio2");
		model.addObject("relatorio", relatorioService.get(ano));
		return model;
	}

}
