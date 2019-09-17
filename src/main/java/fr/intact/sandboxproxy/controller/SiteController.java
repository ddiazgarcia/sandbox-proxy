package fr.intact.sandboxproxy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.intact.sandboxproxy.model.Site;
import fr.intact.sandboxproxy.repository.SiteRepository;

@RestController
@RequestMapping("/sites")
public class SiteController {
	
	@Autowired
	private SiteRepository siteRepository;
	
	@GetMapping("")
	public List<Site> getSites() {
		return siteRepository.findAll();
	}
	
	@GetMapping("{id}")
	public Site getSite(@PathVariable("id") Long id) {
		return siteRepository.findById(id).orElseGet(() -> new Site(0L, ""));
	}
}
