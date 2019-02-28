package com.coleccionables.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.coleccionables.models.entity.Producto;

public interface IProductoDao extends CrudRepository<Producto, Integer> {

	@Query(value = "{CALL obtener_productos_bandai}", nativeQuery = true)
	public List<Producto> findAllBandai();

	@Query(value = "SELECT * FROM productos WHERE productos.marca = 'salo'", nativeQuery = true)
	public List<Producto> findAllSalo();

	@Query(value = "SELECT * FROM productos WHERE productos.marca = ?1", nativeQuery = true)
	public List<Producto> findProductosByMarca(String marca);

	@Query(value = "call obtener_productos(?1)", nativeQuery = true)
	public List<Producto> findProductosByMarcaSP(String marca);

}
