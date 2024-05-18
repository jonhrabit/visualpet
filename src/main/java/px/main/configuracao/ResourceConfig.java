package px.main.configuracao;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
@EnableWebMvc
public class ResourceConfig implements WebMvcConfigurer {

	@Value("${fotos.path}")
	private String pathFotos;
	@Value("${nfe.path}")
	private String pathNFE;

	private static final String[] CLASSPATH_RESOURCE_LOCATIONS = { "classpath:/META-INF/resources/",
			"classpath:/resources/", "classpath:/static/", "classpath:/public/" };

	public void addResourceHandlers(final ResourceHandlerRegistry registry) {

		registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
		registry.addResourceHandler("/webjars/**").addResourceLocations("/webjars/").resourceChain(false);
		registry.addResourceHandler("/fotos/**").addResourceLocations("file:///" + pathFotos + File.separator)
				.setCachePeriod(3600).resourceChain(true).addResolver(new PathResourceResolver());
		registry.addResourceHandler("/xml/**").addResourceLocations("file:///" + pathNFE + File.separator)
				.setCachePeriod(3600).resourceChain(true).addResolver(new PathResourceResolver());
	}
}