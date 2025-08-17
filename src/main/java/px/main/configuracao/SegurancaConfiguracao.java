package px.main.configuracao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SegurancaConfiguracao {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceImp();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(
				auth -> auth
						.requestMatchers("/atendimento/**").hasAuthority("ROLE_ATENDIMENTO")
						.requestMatchers("/css/**", "/js/**", "/images/**", "/templates/**").permitAll()
						.requestMatchers("/usuario/alterarsenha").hasAuthority("ROLE_ALTERARSENHA")
						.requestMatchers("/usuario/salvarsenha").hasAuthority("ROLE_ALTERARSENHA")
						.requestMatchers("/usuario/**").hasAuthority("ROLE_ADM")
						.requestMatchers("/cliente/**").hasAuthority("ROLE_CLIENTE")
						.requestMatchers("/pet/**").hasAuthority("ROLE_PET")
						.requestMatchers("/agenda/**").hasAuthority("ROLE_AGENDA")
						.requestMatchers("/prontuario/**").hasAuthority("ROLE_VETERINARIA")
						.requestMatchers("/pet/vacina/**").hasAuthority("ROLE_VACINA")
						.requestMatchers("/caixa/**").hasAuthority("ROLE_CAIXA")
						.requestMatchers("/produto/**").hasAuthority("ROLE_PRODUTO")
						.requestMatchers("/estoque/**").hasAuthority("ROLE_PRODUTO")
						.requestMatchers("/cesta/**").hasAuthority("ROLE_CESTA")
						.requestMatchers("/about").authenticated()
						.requestMatchers("/produtos/**").permitAll()
						.requestMatchers("/", "/home").permitAll()
						.anyRequest().authenticated())
				.formLogin(login -> login
						.loginPage("/login")
						.defaultSuccessUrl("/atendimento/lista")
						.permitAll())
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/login?logout")
						.deleteCookies("JSESSIONID")
						.invalidateHttpSession(true)
						.clearAuthentication(true)
						.permitAll())
				.exceptionHandling(exceptionHandling -> exceptionHandling
						.accessDeniedPage("/403"));
		return http.build();
	}
}
