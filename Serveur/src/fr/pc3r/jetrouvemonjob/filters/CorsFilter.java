package fr.pc3r.jetrouvemonjob.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class CorsFilter implements javax.servlet.Filter{

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ((HttpServletResponse) response).addHeader("Access-Control-Allow-Origin", "*");
        ((HttpServletResponse) response).addHeader("Access-Control-Allow-Methods", "*");
        ((HttpServletResponse) response).addHeader("Access-Control-Allow-Headers", "Origin, Content-Type, X-Auth-Token");
        ((HttpServletResponse) response).addHeader("Access-Control-Allow-Headers", "Content-Type");
        ((HttpServletResponse) response).addHeader("Access-Control-Max-Age", "86400");
        // pass the request along the filter chain
        chain.doFilter(request, response);
    }

    public void init(FilterConfig fConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

}
