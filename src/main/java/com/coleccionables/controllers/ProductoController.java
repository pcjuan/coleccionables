package com.coleccionables.controllers;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.coleccionables.models.entity.Producto;
import com.coleccionables.models.entity.User;
import com.coleccionables.models.service.IProductoService;
import com.coleccionables.utils.MailUtils;

@Controller
@SessionAttributes("producto")
public class ProductoController {

	private Logger logger = LoggerFactory.getLogger(ProductoController.class);

	@Value("${application.controller.titulo}")
	private String titulo;

	@Value("${application.controller.tituloEditar}")
	private String tituloEditar;

	@Value("${application.controller.tituloAgregar}")
	private String tituloAgregar;

	@Autowired
	private IProductoService productoService;

	/**
	 * Metodo que entrega todos los productos desde un web service rest. El listado
	 * de productos se almacenará en el cuerpo de la respuesta por medio de la
	 * anotación @ResponseBody
	 * 
	 * Por temas de pruebas y aprendizaje, este enviará un correo con un pdf, excel
	 * y csv.
	 * 
	 * @return List<Producto> listaProductos
	 */
	@RequestMapping(value = "/listarRest")
	public @ResponseBody List<Producto> listar() {

		List<Producto> productos = this.productoService.findAll();

		User user = new User();

		user.setFirstName("Juan Enrique");
		user.setLastName("Riquelme");
		user.setEmailAddress("juan.riquelmep@zentagroup.com");

		try {

			this.logger.info("A enviar correo");

			// generar pdf con productos
			// generar xls con produtos
			// generar csv con productos
			// adjuntar
			// enviar correo
			
			MailUtils mailUtils = new MailUtils();
			mailUtils.sendMail(user, productos);

		} catch (MailException ex) {
			this.logger.debug(ex.getMessage());
		}

		return productos;
	}

	/**
	 * Usa un procedimiento almacenado con parametros para obtener la data
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/listarParamSP", method = RequestMethod.GET)
	public String listarSP(Map<String, Object> model) {

		model.put("titulo", this.titulo);
		model.put("productos", this.productoService.findProductosByMarcaSP("Sh.Figuarts"));

		return "/listar";
	}

	/**
	 * Usa un custom query para buscar los productos por marca
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/listarCustom", method = RequestMethod.GET)
	public String listar(Map<String, Object> model) {

		model.put("titulo", this.titulo);
		model.put("productos", this.productoService.findProductosByMarca("evercrisp"));

		return "/listar";
	}

	/**
	 * Se obtienen todos los productos desde la base de datos, se asignan al objeto
	 * Model como atributo, de esta forma se pueden acceder desde la vista, asi
	 * mismo al final del este metodo se retorna la lista.
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/listar", method = RequestMethod.GET)
	public String listar(Model model) {

		model.addAttribute("titulo", this.titulo);
		model.addAttribute("productos", this.productoService.findAll());

		return "/listar";
	}

	/**
	 * Busca los productos asociados a Salo
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/listarSalo", method = RequestMethod.GET)
	public String listarSalo(Map<String, Object> model) {

		model.put("titulo", this.titulo);
		model.put("productos", this.productoService.findProductosSalo());

		return "/listar";
	}

	/**
	 * Busca los productos asociados a Bandai
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/listarBandai", method = RequestMethod.GET)
	public String listarBandai(Map<String, Object> model) {

		model.put("titulo", this.titulo);
		model.put("productos", this.productoService.findProductosBandai());

		return "/listar";
	}

	/**
	 * 
	 * Se muestra el formulario al usuario con los campos basicos para poder
	 * almacenar los campos que ingrese desde la vista.
	 * 
	 * Se crea el metodo que retornará la vista, no es necesario agregar en el
	 * request mapping el method = RequestMethod.GET ya que por defecto es get.
	 * 
	 * En vez de utilizar un objeto Model se utilizará una coleccion Map, además se
	 * agregará un objeto Producto para poder almacenar los atributos asociados al
	 * producto en el formulario. Este campo funciona de manera bidireccional.
	 * 
	 * Esta mapeado a la tabla de la base de datos y al formulario.
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/agregar")
	public String agregar(Map<String, Object> model) {

		Producto producto = new Producto();

		model.put("producto", producto);
		model.put("titulo", this.tituloAgregar);

		return "/agregar";
	}

	/**
	 * Se tomará lo que ingreso el usuario desde el formulario cuanod presione el
	 * boton submit y se asignará a un objeto que posteriormente sera actualizado en
	 * la entidad.
	 * 
	 * Para guardar se recibe un objeto desde el formulario.
	 * 
	 * Para realizar las validaciones, es necesario que el parametro tipo
	 * BindingResult este inmediatamente después que el parametro con la
	 * anotacion @valid, que corresponde al objeto mapeado que fue llenado en la
	 * vista y enviado via POST
	 * 
	 * @return
	 */
	@RequestMapping(value = "/agregar", method = RequestMethod.POST)
	public String guardar(@Valid Producto producto, BindingResult result, SessionStatus status,
			Map<String, Object> model) {

		if (result.hasErrors()) {
			model.put("titulo", this.tituloAgregar);
			return "agregar";
		}

		this.productoService.save(producto);
		status.setComplete();

		return "redirect:/listar";
	}

	/**
	 * Se elimina el registro en base al entregado como parametro en el argumento
	 * "@PathVariable(value="id")" de tipo Integer.
	 * 
	 * Luego se valida que el id sea mayor a cero y por último se llama al metodo
	 * del facade productoService.
	 * 
	 * Cabe destacar que en la anotación @RequestMapping viene el parametro que
	 * asocia este metodo a la url que lo esta invocando.
	 * 
	 * Para este caso como es necesario el id para poder eliminar el registro se
	 * agrega en el parametro de la url:
	 * 
	 * <b>@RequestMapping(value = "/eliminar/{id}")</b>
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Integer id) {

		if (id > 0) {
			this.productoService.delete(id);
		}

		return "redirect:/listar";
	}

	/**
	 * Se actualiza el registro en base al entregado como parametro en el argumento
	 * "@PathVariable(value="id")" de tipo Integer.
	 * 
	 * Luego se valida que el id sea mayor a cero y por último se llama al metodo
	 * del facade productoService.
	 * 
	 * Cabe destacar que en la anotación @RequestMapping viene el parametro que
	 * asocia este metodo a la url que lo esta invocando.
	 * 
	 * Para este caso como es necesario el id para poder eliminar el registro se
	 * agrega en el parametro de la url:
	 * 
	 * <b>@RequestMapping(value = "/eliminar/{id}")</b>
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/agregar/{id}")
	public String editar(@PathVariable(value = "id") Integer id, Map<String, Object> model) {

		Producto producto = null;

		if (id > 0) {

			producto = this.productoService.findOne(id);

			if (producto != null) {
				model.put("titulo", this.tituloEditar);
				model.put("producto", producto);
			}
		}

		return "/agregar";
	}
}
