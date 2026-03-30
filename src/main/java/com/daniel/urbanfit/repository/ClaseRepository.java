package com.daniel.urbanfit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daniel.urbanfit.entity.Clase;
import java.util.List;


public interface ClaseRepository extends JpaRepository<Clase, Long> {
	
	List<Clase> findByMonitorId(Long monitorId);

}
