package fr.intact.sandboxproxy.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.intact.sandboxproxy.model.Site;
import fr.intact.sandboxproxy.repository.SiteRepository;

@RestController
@RequestMapping("/p")
public class ProxyController {
	
	@Autowired
	private SiteRepository siteRepository;
	
	@GetMapping("/{id}/**")
	public Site proxy(@PathVariable("id") Long id) {
		
		Optional<Site> site = siteRepository.findById(id);
		
		return site.orElse(new Site());
	}
	

}
