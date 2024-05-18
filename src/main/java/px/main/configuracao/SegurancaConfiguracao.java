package px.main.configuracao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@EnableWebSecurity
public class SegurancaConfiguracao extends WebSecurityConfigurerAdapter {

	@Qualifier("userDetailsService")
	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	@Transactional
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
				.antMatchers("/usuario/alterarsenha").hasRole("ALTERARSENHA")
				.antMatchers("/usuario/salvarsenha").hasRole("ALTERARSENHA")
				.antMatchers("/usuario/**").hasRole("ADM")
				.antMatchers("/cliente/**").hasRole("CLIENTE")
				.antMatchers("/pet/**").hasRole("PET")
				.antMatchers("/agenda/**").hasRole("AGENDA")
				.antMatchers("/atendimento/**").hasRole("ATENDIMENTO")
				.antMatchers("/prontuario/**").hasRole("VETERINARIA")
				.antMatchers("/pet/vacina/**").hasRole("VACINA")
				.antMatchers("/caixa/**").hasRole("CAIXA")
				.antMatchers("/produto/**").hasRole("PRODUTO")
				.antMatchers("/estoque/**").hasRole("PRODUTO")
				.antMatchers("/cesta/**").hasRole("CESTA")
				.antMatchers("/nfe/**").hasRole("NFE")
				.antMatchers("/xml/**").hasRole("NFE")
				.antMatchers("/about").authenticated()
				.antMatchers("/produtos/**").permitAll()
				.antMatchers("/", "/home").permitAll()
				.antMatchers("/css/**", "/js/**", "/images/**", "/templates/**").permitAll()
				.anyRequest().authenticated()
				
				.and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login?logout").deleteCookies("JSESSIONID").invalidateHttpSession(true)
				
				.and().formLogin().loginPage("/login").defaultSuccessUrl("/atendimento/lista").permitAll()
				.and().rememberMe()
				
				.and().exceptionHandling().accessDeniedPage("/403")

				.and().logout().permitAll();
	}
}
