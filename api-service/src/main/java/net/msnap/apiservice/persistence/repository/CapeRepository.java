package net.msnap.apiservice.persistence.repository;

import net.msnap.apiservice.persistence.entity.Cape;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CapeRepository extends JpaRepository<Cape, String> {
}

