package com.formacionbdi.springboot.app.zuul.filters;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

@Component
public class PostTiempoTranscurridoFilter extends ZuulFilter {

	private static Logger log = LoggerFactory.getLogger(PostTiempoTranscurridoFilter.class);

	@Override
	public boolean shouldFilter() {
		return true; // si lo ponemos a true, ejecutamos siempre. aquí podemos evaluar datos de la
						// request.
	}

	@Override
	public Object run() throws ZuulException {
	
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		
		log.info(String.format("%s request enrutado a %s", request.getMethod(), request.getRequestURL().toString()));
		
		Long tiempoInicio = (Long) request.getAttribute("tiempoInicio");
		
		Long tiempoFin = System.currentTimeMillis();
		Long tiempoTranscurrido = tiempoFin - tiempoInicio;
		log.info(String.format("Tiempo transcurrido en segundos %s", tiempoTranscurrido/1000.00));
		log.info(String.format("Tiempo transcurrido en milisegundos %s", tiempoTranscurrido));
		return null;
	}

	@Override
	public String filterType() {
		return "post";
	}

	@Override
	public int filterOrder() {
		return 1;
	}

}
