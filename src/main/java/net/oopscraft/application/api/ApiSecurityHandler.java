
public class ApiSecurityHandler implements AuthenticationEntryPoint, AccessDeniedHandler {
	
	private static Logger LOGGER = LoggerFactory.getLogger(ApiSecurityFilter.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
    	LOGGER.warn(authException.getMessage());
    	response.setStatus(HttpStatus.SC_UNAUTHORIZED);
    	response.getWriter().write("Invalid Token Claims.");
    	response.getWriter().flush();
	}
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
		LOGGER.warn(accessDeniedException.getMessage());
		response.setStatus(HttpStatus.SC_FORBIDDEN);
    	response.getWriter().write("Unauthorized.");
    	response.getWriter().flush();
	}

}
