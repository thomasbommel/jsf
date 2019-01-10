package app;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import database.Database;
import database.DatabaseTable;
import database.StorageSiteTable;
import database.TransactionsTable;
import factory.shared.FactoryEvent;
import factory.shared.Position;
import factory.shared.ResourceBox;
import factory.shared.enums.EventKind;
import factory.shared.enums.Material;
import factory.shared.enums.SubsystemStatus;
import factory.shared.interfaces.Monitorable;
import factory.shared.interfaces.Placeable;
import factory.subsystems.agv.AgvTask;
import factory.subsystems.monitoring.MonitoringSystem;
import factory.subsystems.warehouse.WarehouseSystem;

/**
 * TODO Temporary test class, to be removed later
 */
public class Test implements Monitorable {
	
	private static final Database db = Database.INSTANCE;

	public static void main(String[] args) {
		System.out.println("------------------------------------- XML SETUP (no output) -------------------------------------");
		
		try {
			Document layoutDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File("resources/factory_layout.xml"));
			Element warehouseElem = (Element) layoutDoc.getElementsByTagName("warehouse").item(0);
			
			@SuppressWarnings("unused")
			WarehouseSystem whs = new WarehouseSystem(new MonitoringSystem(), warehouseElem);
		} catch (SAXException | IOException | ParserConfigurationException e1) {
			e1.printStackTrace();
		}
		
		System.out.println("------------------------------------- DATABASE TESTING -------------------------------------");
		
		db.initialize();
		
		testDatabase();
		
		System.out.println();
		System.out.println("------------------------------------- EVENT TESTING -------------------------------------");
		
		Test thiz = new Test();
		
		System.out.println(EventKind.AGV_CONTAINER_DELIVERED);
		System.out.println(EventKind.AGV_FORKLIFT_COLLISION);
		
		System.out.println("-- Valid Attachment --");
		new FactoryEvent(thiz, EventKind.AGV_CONTAINER_DELIVERED, new AgvTask(1, new ResourceBox(), new Position(0,0), new Position(0,0)));
		
		System.out.println("-- Zero (correct) Attachments --");
		new FactoryEvent(thiz, EventKind.CONVEYORS_LACK_OF_OIL);
		
		try {
			System.out.println("-- Invalid Attachment --");
			new FactoryEvent(thiz, EventKind.AGV_CONTAINER_DELIVERED, new Object());
		} catch (IllegalArgumentException e) {
			System.out.println("Exception caught.");
		}
		
		try {
			System.out.println("-- Too many Attachments --");
			new FactoryEvent(thiz, EventKind.AGV_FORKLIFT_COLLISION, new AgvTask(1, new ResourceBox(), new Position(0,0), new Position(0,0)));
		} catch (IllegalArgumentException e) {
			System.out.println("Exception caught.");
		}
		
		
	}
	
	public static void testDatabase() {
		//make sure database was freshly created before executing this test (otherwise there are duplicate entries)
		try {
			Random rng = new Random();
			Material[] materials = Material.values();
			
			for (DatabaseTable dbTable : db.getTables()) {
				switch(dbTable.getClass().getSimpleName()) {
					case "StorageSiteTable":
						StorageSiteTable storTable = (StorageSiteTable) dbTable;
						storTable.insertMaterial(materials[rng.nextInt(materials.length)], rng.nextInt(50));
						storTable.insertMaterial(materials[rng.nextInt(materials.length)], rng.nextInt(50));
						break;
					case "TransactionsTable":
						TransactionsTable transTable = (TransactionsTable) dbTable;
						transTable.insertTransaction("this_isAThirtyCharacterCompany", Material.SCREWS, 10, 50, "17.12.18", 1);
						transTable.insertTransaction("notSoLongName", Material.LUBRICANT, 20, 345, "23.12.18", 1);
						break;
					default:
						throw new RuntimeException("Invalid DatabaseTable for testing! (" + dbTable.getClass().getSimpleName() + ")");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		db.printToConsole();
	}

	public void notify(FactoryEvent event) {
		return;
	}

	public SubsystemStatus getStatus() {
		return null;
	}

	public boolean isReady() {
		return false;
	}

	@Override
	public List<Placeable> getPlaceables() {
		// TODO Auto-generated method stub
		return null;
	}
	
}