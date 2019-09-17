package fr.intact.sandboxproxy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Route {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private ProtocolType protocolType;
	
	@Column(name="includeWWW")
	private Boolean includeWWW;
	
	private String routeUrl;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ProtocolType getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(ProtocolType protocolType) {
		this.protocolType = protocolType;
	}

	public Boolean getIncludeWWW() {
		return includeWWW;
	}

	public void setIncludeWWW(Boolean includeWWW) {
		this.includeWWW = includeWWW;
	}

	public String getRouteUrl() {
		return routeUrl;
	}

	public void setRouteUrl(String routeUrl) {
		this.routeUrl = routeUrl;
	}
	
	
	
}
