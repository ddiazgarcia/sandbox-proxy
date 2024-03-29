package fr.intact.sandboxproxy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import fr.intact.sandboxproxy.model.Site;

public interface SiteRepository extends JpaRepository<Site, Long> {

}
