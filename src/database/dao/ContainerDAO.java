package database.dao;

import database.entities.Container;

public class ContainerDAO extends BaseDAO2<Container> {

	private static ContainerDAO instance;

	private ContainerDAO() {
		super(Container.class);
	}

	public static synchronized ContainerDAO getInstance() {
		if (instance == null) {
			instance = new ContainerDAO();
		}
		return instance;
	}

}