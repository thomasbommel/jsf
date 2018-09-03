package model.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import database.dao.ContainerDAO;
import database.entities.Container;

public class ContainerHandler {

	private Container		currentContainer;
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
			containers.remove(container);
			ContainerDAO.getInstance().beginConversationWithTransaction();
			container = ContainerDAO.getInstance().findById(container.getContainerId());
			ContainerDAO.getInstance().delete(container);
			ContainerDAO.getInstance().endConversationWithTransaction();
		} else {
			System.out.println("container was null");
		}
	}

	public void createNew() {
		System.out.println("ContainerHandler.createNew");
		Container containerToCreate = getCurrentContainer();

		if (containers.stream().anyMatch(cont -> cont.getName().equals(containerToCreate.getName()))) {
			System.out.println("duplicate");
			FacesMessage msg = new FacesMessage("Duplicated", "This Container has already been added");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			ContainerDAO.getInstance().beginConversationWithTransaction();
			ContainerDAO.getInstance().persist(containerToCreate);
			containers.add(containerToCreate);
			ContainerDAO.getInstance().endConversationWithTransaction();
			currentContainer = null;
		}
	}

	public Container getCurrentContainer() {
		if (currentContainer == null) {
			currentContainer = new Container();
		}
		return currentContainer;
	}

	public TreeNode getTreeTable() {
		TreeNode root = new DefaultTreeNode(new Container("RootContainer"), null);
		ContainerDAO.getInstance().beginConversationWithTransaction();
		List<Container> containerList = ContainerDAO.getInstance().findAll();
		ContainerDAO.getInstance().endConversationWithTransaction();

		Map<Container, TreeNode> containerNodes = new HashMap<>();
		containerList.forEach(c -> {
			containerNodes.put(c, new DefaultTreeNode(c));
		});

		containerNodes.entrySet().forEach(e -> {
			Container parent = e.getKey().getParent();

			if (parent == null) {
				root.getChildren().add(e.getValue());
			} else {
				containerNodes.get(parent).getChildren().add(e.getValue());
			}
		});

		return root;
	}

}
