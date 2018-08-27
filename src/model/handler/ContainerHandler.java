package model.handler;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import database.dao.ContainerDAO;
import database.entities.Container;

public class ContainerHandler {

	private Container		newContainer;
	private List<Container>	containers;

	public List<Container> getContainers() {
		System.out.println("ContainerHandler.getContainers()");
		if (containers == null) {
			System.out.println("list was null");
			containers = ContainerDAO.getInstance().findAll();
		}

		return containers;
	}

	public void setContainers(List<Container> containers) {
		this.containers = containers;
	}

	public void deleteContainer(Container container) {
		System.out.println("ContainerHandler.deleteContainer");
		if (container != null) {
			System.out.println("deleteContainer: " + container);
			ContainerDAO.getInstance().beginTransaction();
			ContainerDAO.getInstance().delete(container);
			ContainerDAO.getInstance().commitTransaction();
		} else {
			System.out.println("container was null");
		}
	}

	public void createNew() {
		System.out.println("ContainerHandler.createNew");
		Container containerToCreate = getNewContainer();
		if (containers.contains(containerToCreate)) {
			FacesMessage msg = new FacesMessage("Dublicated", "This Container has already been added");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			ContainerDAO.getInstance().beginConversationWithTransaction();
			ContainerDAO.getInstance().persist(containerToCreate);
			containers.add(containerToCreate);
			ContainerDAO.getInstance().endConversationWithTransaction();
		}
	}

	public void reinit() {
		System.out.println("ContainerHandler.reinit");
		newContainer = new Container();
	}

	public Container getNewContainer() {
		if (newContainer == null) {
			reinit();
		}
		return newContainer;
	}

	public void setNewContainer(Container newContainer) {
		this.newContainer = newContainer;
	}

}
