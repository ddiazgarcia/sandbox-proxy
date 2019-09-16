package fr.intact.sandboxproxy.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.intact.sandboxproxy.model.Site;
import fr.intact.sandboxproxy.repository.SiteRepository;

@RestController
@RequestMapping("")
public class ProxyController {
	
	@Autowired
	private SiteRepository siteRepository;
	
	@GetMapping("p/{id}/**")
	public ResponseEntity<String> proxyStart(@PathVariable("id") Long id, HttpServletRequest request) throws IOException {
		
		Optional<Site> site = siteRepository.findById(id);
		
		String siteUrl = site.get().getSiteUrl();
		
		Connection conn = Jsoup.connect(siteUrl);
		
		Response response = conn.execute();
		
		return jSoupResponseToSpringResponseEntity(response, siteUrl);
	}
	
	@GetMapping("/**")
	public ResponseEntity<byte[]> proxy(HttpServletRequest request) throws IOException {
		
		Long siteId = Arrays.stream(request.getCookies())
			.filter(cookie -> cookie.getName().equals("sx_id"))
			.map(cookie -> Long.parseLong(cookie.getValue()))
			.findFirst().orElse(2L);
		
		Optional<Site> site = siteRepository.findById(siteId);
		
		URL siteUrl = new URL(site.get().getSiteUrl());
		
		String localFile = request.getRequestURI();
		if(request.getQueryString() != null) {
			localFile = String.format("%s?%s", localFile, request.getQueryString());
		}
		
		//URL localUrl = new URL(request.getProtocol(), request.getServerName(), localFile);
		
		URL urlToGet = new URL(siteUrl.getProtocol(), siteUrl.getHost(), localFile);
		
		Connection conn = Jsoup.connect(urlToGet.toString());
		
		Response response = conn.execute();
		
		return jSoupResponseToSpringByteResponseEntity(response, siteId);
		
	}
	
	private ResponseEntity<String> jSoupResponseToSpringResponseEntity(Response jSoup, String siteUrl) {
		
		HttpStatus status = getHttpStatus(jSoup);
		
		HttpHeaders headers = getHeaders(jSoup);
		
		headers.add(HttpHeaders.SET_COOKIE, siteUrl);
		
		ResponseEntity<String> respEntity = new ResponseEntity<String>(jSoup.body(), headers, status);
		
		return respEntity;
		
	}
	
	private ResponseEntity<byte[]> jSoupResponseToSpringByteResponseEntity(Response jSoup, Long idSite) {
		
		HttpStatus status = getHttpStatus(jSoup);
		
		HttpHeaders headers = getHeaders(jSoup);
		
		headers.add(HttpHeaders.SET_COOKIE, String.format("%s=%s", "sx_id", idSite));
		
		ResponseEntity<byte[]> respEntity = new ResponseEntity<byte[]>(jSoup.bodyAsBytes(), headers, status);
		
		return respEntity;
		
	}
	
	private HttpStatus getHttpStatus (Response jSoup) {
		return HttpStatus.valueOf(jSoup.statusCode());
	}
	
	private HttpHeaders getHeaders(Response jSoup) {
		HttpHeaders headers = new HttpHeaders();
		jSoup.multiHeaders().forEach((name, values) -> {
			// Bypass Content-Encoding
			if(!name.toLowerCase().contains("content-encoding")) {
				headers.addAll(name, values);
			}
		});
		return headers;
	}
	
	private void changeRelativeRoutes(Element root, String elementName, String attribute, Long id){
		root.select(elementName).forEach(element -> {
			if (element.hasAttr(attribute)) {
				String relUrl = element.attr(attribute).trim();
			}
		});
	}
	

}
