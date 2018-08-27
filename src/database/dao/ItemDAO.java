package database.dao;

import database.entities.Item;

public class ItemDAO extends BaseDAO<Item> {

	private static ItemDAO instance;

	private ItemDAO() {
		super(Item.class);
	}

	public static synchronized ItemDAO getInstance() {
		if (instance == null) {
			instance = new ItemDAO();
		}
		return instance;
	}

}