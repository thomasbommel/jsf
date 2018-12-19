package app;

import java.sql.SQLException;

import database.Database;
import database.StorageSiteTable;
import database.TransactionsTable;
import factory.shared.FactoryEvent;
import factory.shared.enums.EventKind;
import factory.shared.enums.Material;
import factory.shared.enums.SubsystemStatus;
import factory.shared.interfaces.Monitorable;
import factory.subsystems.agv.AgvTask;

/**
 * TODO Temporary test class, to be removed later
 */
public class Test implements Monitorable {
	
	private static final Database db = Database.INSTANCE;

	public static void main(String[] args) {
		System.out.println("------------------------------------- DATABASE TESTING -------------------------------------");
		
		testDatabase();
		
		System.out.println();
		System.out.println("------------------------------------- OTHER TESTING -------------------------------------");
		
		Test thiz = new Test();
		
		System.out.println(EventKind.AGV_CONTAINER_DELIVERED);
		System.out.println(EventKind.AGV_FORKLIFT_COLLISION);
		
		System.out.println("-- Valid Attachment --");
		new FactoryEvent(thiz, EventKind.AGV_CONTAINER_DELIVERED, new AgvTask());
		
		System.out.println("-- Zero (correct) Attachments --");
		new FactoryEvent(thiz, EventKind.AGV_FORKLIFT_COLLISION);
		
		try {
			System.out.println("-- Invalid Attachment --");
			new FactoryEvent(thiz, EventKind.AGV_CONTAINER_DELIVERED, new Object());
		} catch (IllegalArgumentException e) {
			System.out.println("Exception caught.");
		}
		
		try {
			System.out.println("-- Too many Attachments --");
			new FactoryEvent(thiz, EventKind.AGV_FORKLIFT_COLLISION, new AgvTask());
		} catch (IllegalArgumentException e) {
			System.out.println("Exception caught.");
		}
	}
	
	public static void testDatabase() {
		//make sure database was freshly created before executing this test (otherwise there are duplicate entries)
		
		StorageSiteTable storTable = (StorageSiteTable) db.getTables().get(0);
		TransactionsTable transTable = (TransactionsTable) db.getTables().get(1);
		
		try {
			storTable.insertMaterial(Material.CAR_BODIES, 3);
			storTable.insertMaterial(Material.COLOR_BLUE, 20);
			
			transTable.insertTransaction("this_isAThirtyCharacterCompany", Material.SCREWS, 10, 50, "17.12.18", 1);
			transTable.insertTransaction("notSoLongName", Material.LUBRICANT, 20, 345, "23.12.18", 1);
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
	
}