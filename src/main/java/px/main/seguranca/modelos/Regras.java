package px.main.seguranca.modelos;

public enum Regras {
	ROLE_ALTERARSENHA("Usuário pode alterar sua senha."),
	ROLE_ADM("Cadastrar e excluir outros usuários."),
	ROLE_ATENDIMENTO("Acesso ao módulo de atendimentos."),
	ROLE_AGENDA("Acesso ao módulo de agendamentos."),
	ROLE_CESTA("Acesso ao módulo de venda e cobrança."),
	ROLE_PRODUTO("Acesso ao módulo de controle de estoque para alteração e cadastro de produtos."),
	ROLE_CLIENTE("Acesso ao módulo de cadastro e alteração de clientes."),	
	ROLE_PET("Acesso ao módulo de cadastro e alteração de Pets diretamente."),
	ROLE_VETERINARIA("Acesso ao módulo de prontuários."),
	ROLE_VACINA("Acesso ao módulo de vacinas."),
	ROLE_CAIXA("Acesso ao módulo de relatório de caixa."),
	ROLE_CAIXAESPECIAL("Acesso a edição especial dos registros de caixa."),
	ROLE_NFE("Gerenciamento de NF-e."),
	ROLE_DEBUG("Permissão para visualizar informações de debug.");
	
	private String descricao;
	
	Regras(String descricao) {
		this.descricao=descricao;
	}
	public String getDescricao() {
		return this.descricao;
	}
}
