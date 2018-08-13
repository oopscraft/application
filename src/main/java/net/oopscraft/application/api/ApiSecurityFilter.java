
public class ApiSecurityFilter implements Filter {
	
	private static Logger LOGGER = LoggerFactory.getLogger(ApiSecurityFilter.class);
	private static final String AUTHORIZATION_HEADER = "Authorization";
	
	@Value("${secretKey}")
	String secretKey;
 
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    	HttpServletRequest request = (HttpServletRequest) req;
    	HttpServletResponse response = (HttpServletResponse) res;
        String uri = request.getRequestURI();
        String method = request.getMethod();
        LOGGER.debug(String.format("[%s][%s]",  method, uri));
        
        // 토큰 디코딩
        Claims claims = null;
        try {
            String authorization = request.getHeader(AUTHORIZATION_HEADER);
            String token = parseToken(authorization);
        	claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        }catch(Exception e) {
        	AuthenticationException authenticationException = new BadCredentialsException("Invalid Token.");
        	LOGGER.error(authenticationException.getMessage());
        	throw authenticationException;
        }
        
        // De-serialize principal
        try {
        	String id = (String)claims.get("id");
        	String password = (String)claims.get("password");
        	String authorities = (String)claims.get("authorities");
        	User user = new User();
        	user.setId(id);
        	user.setPassword(password);
        	List<Authority> authorityList= JsonConverter.convertJsonToObjectList(authorities, Authority.class);
        	user.setAuthorityList(authorityList);
        	SecurityContext securityContext = SecurityContextHolder.getContext();
        	Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorityList());
        	securityContext.setAuthentication(authentication);
        }catch(Exception e) {
        	AuthenticationException authenticationException = new BadCredentialsException("Invalid Token Claims.");
        	LOGGER.error(authenticationException.getMessage());
        	throw authenticationException;
        }
        
        // forward
        chain.doFilter(req, res);
    }
 
    @Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Authorization 커스텀해더 값에서 JWT토큰 값 추출
	 * @param authorization
	 * @return
	 */
	private String parseToken(String authorization) {
        if(authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7, authorization.length());
            return token;
        }
        return null;
	}
	
	/**
	 * 토큰 Validation 체크
	 * @param token
	 * @return
	 */
	private boolean validateToken(String token) {
       try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            LOGGER.info("Invalid JWT signature: " + e.getMessage());
            LOGGER.debug("Exception " + e.getMessage());
            return false;
        }

	}

}
