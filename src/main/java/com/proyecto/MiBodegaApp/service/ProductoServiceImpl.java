package com.proyecto.MiBodegaApp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.MiBodegaApp.model.Producto;
import com.proyecto.MiBodegaApp.repository.IProductoRepository;
import com.proyecto.MiBodegaApp.repository.IMovimientoInventarioRepository;

@Service
public class ProductoServiceImpl implements ProductoService {

	@Autowired
	private IProductoRepository productoRepository;

	@Autowired
	private IMovimientoInventarioRepository movimientoRepository; // Inyecta el repositorio de movimientos

	@Override
	public Producto save(Producto producto) {
		return productoRepository.save(producto);
	}

	@Override
	public Optional<Producto> get(Integer id) {
		return productoRepository.findById(id);
	}

	@Override
	public void update(Producto producto) {
		productoRepository.save(producto);
	}

	@Override
	@Transactional
	public void delate(Integer id) {
		// 1. Borramos primero los movimientos que pertenecen a este producto
		movimientoRepository.deleteByProductoId(id);

		// 2. Luego eliminamos el producto normalmente sin errores de restricción
		productoRepository.deleteById(id);
	}

	@Override
	public List<Producto> findAll() {
		return productoRepository.findAll();
	}
}


