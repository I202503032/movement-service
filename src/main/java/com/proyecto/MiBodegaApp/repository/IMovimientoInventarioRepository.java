package com.proyecto.MiBodegaApp.repository;

import com.proyecto.MiBodegaApp.model.MovimientoInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface IMovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Integer> {

    List<MovimientoInventario> findByProductoId(Integer productoId);

    @Modifying
    @Transactional
    @Query("DELETE FROM MovimientoInventario m WHERE m.producto.id = :productoId")
    void deleteByProductoId(Integer productoId);
}
