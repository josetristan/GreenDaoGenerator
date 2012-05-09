package com.jtristan.greendaogenerator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class Pedido {

	public static void main(String[] args) throws Exception {
		Schema esquema = new Schema(4, "com.jtristan.greendao.dao");
		esquema.enableKeepSectionsByDefault();
		
		addCabeceraPedido(esquema);		
		new DaoGenerator().generateAll(esquema, "../GreenDao/src-gen");
		
	}
	
	/**
	 * @param esquema
	 */
	private static void addCabeceraPedido(Schema esquema) {
	        Entity cabeceraPedido = esquema.addEntity("Pedido");
	        //Activa la sección para nuestro propio código en el dao.
	        cabeceraPedido.setHasKeepSections(true);
	        cabeceraPedido.addIdProperty();
	        cabeceraPedido.addLongProperty("numeroPedido").unique();
	        cabeceraPedido.addStringProperty("cliente").notNull();
	        cabeceraPedido.addLongProperty("direccion").notNull();
	        cabeceraPedido.addLongProperty("idCondicionPago").notNull().getProperty();
	        cabeceraPedido.addBooleanProperty("finalizado");
	        cabeceraPedido.addDateProperty("fechaCreacion");	        	        
	        	        	        	        
	        Entity lineaPedido = esquema.addEntity("Linea");
			//Prueba de como crear un id. el addIdProperty todavía no está resuelto totalmente.			
	        Property idLineaPedido = lineaPedido.addLongProperty("_id").primaryKey().getProperty();
			Property idPedido = lineaPedido.addLongProperty("idPedido").notNull().getProperty();
			lineaPedido.addStringProperty("material");
			lineaPedido.addIntProperty("cantidad");
			lineaPedido.addDoubleProperty("precio");
			lineaPedido.addDateProperty("fechaCreacion");
			//Relaciones: Una línea va a pertenecer a un único pedido. 1:1.
			lineaPedido.addToOne(cabeceraPedido, idPedido);
			
			//Un pedido puede tener varias líneas. Para ello creamos un objeto ToMany.
			ToMany lineasDeUnPedido = cabeceraPedido.addToMany(lineaPedido, idPedido);
			lineasDeUnPedido.setName("Lineas");
			lineasDeUnPedido.orderAsc(idLineaPedido);
			
			Entity condicionPago = esquema.addEntity("CondicionPago");
			condicionPago.addIdProperty();
			condicionPago.addStringProperty("condicion");
			condicionPago.addDoubleProperty("porcentaje");
			condicionPago.addDoubleProperty("valor");			
				
	        // Tabla para n:m relaciones.
			// Le decimos de que tábla y campo van a venir  cada campo.
			Entity condicionesPagoDeUnPedido = esquema.addEntity("CondicionPagoDeUnPedido");
			Property idPedidoEnCondiciones = condicionesPagoDeUnPedido.addLongProperty("idPedido").notNull().getProperty();
			Property idCondicion = condicionesPagoDeUnPedido.addLongProperty("idCondicion").notNull().getProperty();
						
			condicionesPagoDeUnPedido.addToOne(condicionPago, idCondicion);
			
			ToMany condicionesDePagoDeUnPedido = cabeceraPedido.addToMany(condicionesPagoDeUnPedido, idPedidoEnCondiciones);
			condicionesDePagoDeUnPedido.setName("CondicionesPago");
			
						       			
	}

	
}
