package com.monopatines.viajes.repository;

import com.monopatines.viajes.model.PausaModel;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PausaRepository extends JpaRepository<PausaModel, Long> {

}