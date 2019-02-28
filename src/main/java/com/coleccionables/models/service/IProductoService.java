package com.coleccionables.models.service;

import java.util.List;

import com.coleccionables.models.entity.Producto;

/**
 * Interfaz que sera implementada a través del facade
 * 
 * @author NB-JRIQUELME
 *
 */
public interface IProductoService {

	public List<Producto> findAll();

	public void save(Producto producto);

	public Producto findOne(int id);

	public void delete(int id);

	public List<Producto> findProductosBandai();

	public List<Producto> findProductosSalo();

	public List<Producto> findProductosByMarca(String marca);

	public List<Producto> findProductosByMarcaSP(String marca);

}
