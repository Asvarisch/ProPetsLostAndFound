package telran.ashkelon2020.lostfound.filters;

import static telran.ashkelon2020.lostfound.configuration.Constants.TOKEN_HEADER;
import static telran.ashkelon2020.lostfound.configuration.Constants.URI_VALIDATION;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;;



@Service
@Order(10)
public class AuthenticationFilter implements Filter {

	@Autowired
	RestTemplate restTemplate;

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		String token = request.getHeader(TOKEN_HEADER);
		System.out.println("request reached auth filter");
		if (!checkEndpoint(request.getServletPath())) {
			try {
				if (token != null) {
					HttpHeaders headers = new HttpHeaders();
					headers.add(TOKEN_HEADER, token);
					RequestEntity<String> requestEntity = new RequestEntity<>(headers, HttpMethod.GET,
							new URI(URI_VALIDATION));
					ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
					if (responseEntity.getStatusCodeValue() == 200) {
						System.out.println("successful validation in LF");
						response.setHeader(TOKEN_HEADER, responseEntity.getHeaders().getFirst(TOKEN_HEADER));
					}
				} else {
					response.sendError(403);
					return;
				}
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response.sendError(400);
				return;
			}
		}
		chain.doFilter(request, response);
	}

	private boolean checkEndpoint(String path) {
//		return path.startsWith("/lostfound/en/v1/");
		boolean res = path.startsWith("/lostfound/en/v1/tagscolors"); //found/\\w+@\\w+.\\w+/?
		res = res || path.startsWith("/lostfound/en/v1/userdata");
		return res;
	}

}