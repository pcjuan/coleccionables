package com.coleccionables.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coleccionables.models.dao.IProductoDao;
import com.coleccionables.models.entity.Producto;

/**
 * Usa patron facade
 * 
 * @author NB-JRIQUELME
 *
 */
@Service
public class ProductoService implements IProductoService {

	@Autowired
	private IProductoDao productoDao;

	@Override
	@Transactional(readOnly = true)
	public List<Producto> findProductosSalo() {
		return this.productoDao.findAllSalo();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Producto> findProductosByMarcaSP(String marca) {
		return this.productoDao.findProductosByMarca(marca);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Producto> findProductosByMarca(String marca) {
		return this.productoDao.findProductosByMarca(marca);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Producto> findProductosBandai() {
		return this.productoDao.findAllBandai();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Producto> findAll() {
		// TODO Auto-generated method stub
		return (List<Producto>) this.productoDao.findAll();
	}

	@Override
	@Transactional
	public void save(Producto producto) {
		// TODO Auto-generated method stub
		this.productoDao.save(producto);
	}

	@Override
	@Transactional(readOnly = true)
	public Producto findOne(int id) {
		// TODO Auto-generated method stub
		return this.productoDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(int id) {
		// TODO Auto-generated method stub
		this.productoDao.deleteById(id);
		;
	}

}
