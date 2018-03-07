package tk.ljyuan71.file.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import tk.ljyuan71.file.utils.FileSysConfig;
import tk.ljyuan71.file.utils.FileSysUtils;

/**
 * Servlet Filter implementation class FileSysFilter
 */
public class FileSysFilter implements Filter {
	private static final String INVALID_ACCESS_MESSAGE = "不合法的访问";
    /**
     * Default constructor. 
     */
    public FileSysFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		String filesys = request.getParameter("filesys");
		String filePrivateKey = request.getParameter("filePrivateKey");
		String privateKey = FileSysConfig.getSystemConfig("filesys_"+filesys);
		boolean isPass = true;
		if (StringUtils.isBlank(filesys) || StringUtils.isBlank(filePrivateKey)) {
			isPass = false;
		} else if (StringUtils.isBlank(privateKey) || 
				!filePrivateKey.equals(FileSysUtils.getMD5(privateKey,null))) {
			isPass = false;
		}
		if (isPass) {
			chain.doFilter(request, response);
		} else {
			HttpServletResponse res = (HttpServletResponse)response;
			res.setStatus(401);
			render(res, "text/plain;charset=UTF-8", INVALID_ACCESS_MESSAGE);
			return;
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}
	
	/**
	 * 发送内容。使用UTF-8编码。
	 * 
	 * @param response
	 * @param contentType
	 * @param text
	 */
	public static void render(HttpServletResponse response, String contentType,
			String text) {
		response.setContentType(contentType);
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		try {
			response.getWriter().write(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
