package hotel.common;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CustomWebConfig implements WebMvcConfigurer {

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		PageableHandlerMethodArgumentResolver argumentResolver = new PageableHandlerMethodArgumentResolver();
		argumentResolver.setPageParameterName("page-number");
		argumentResolver.setSizeParameterName("page-size");
		Pageable pageable = PageRequest.of(0, 3);
		argumentResolver.setFallbackPageable(pageable);
		argumentResolver.setOneIndexedParameters(true);
		resolvers.add(argumentResolver);
	}
}
