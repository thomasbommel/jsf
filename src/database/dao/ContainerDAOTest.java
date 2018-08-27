package database.dao;

import java.util.Random;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import database.entities.Container;

public class ContainerDAOTest {

	private ContainerDAO	containerDAO;
	private ItemDAO			itemDAO;

	@BeforeTest
	public void setUp() throws Exception {
		System.out.println("setUp");
		containerDAO = ContainerDAO.getInstance();
		itemDAO = ItemDAO.getInstance();
	}

	@AfterTest
	public void tearDown() throws Exception {
		System.out.println("tearDown");
	}

	@Test
	public void testCreateAndDelete() {
		final String containerName = "testContainer_" + new Random().nextInt(123456);
		containerDAO.beginConversation();

		int count = containerDAO.findAll().size();
		Container container = new Container(containerName);

		containerDAO.persist(container);
		containerDAO.endConversation();

	}

	/*
	 *
	 * @Test public void testParentChild() { final String childName =
	 * "testChildContainer_" + new Random().nextInt(123456); final String child2Name
	 * = "testChildContainer_" + new Random().nextInt(123456); final String
	 * parentName = "testParentContainer_" + new Random().nextInt(123456);
	 *
	 * containerDAO.openCurrentSessionwithTransaction(); Container child = new
	 * Container(childName); Container parent = new Container(parentName);
	 * persistContainer(child, parent);
	 *
	 * child.setParent(parent); persistContainer(child, parent);
	 * containerDAO.closeCurrentSessionwithTransaction();
	 *
	 * containerDAO.openCurrentSessionwithTransaction(); Container child2 = new
	 * Container(child2Name); parent = addChildToParent(parent, child2);
	 * containerDAO.closeCurrentSessionwithTransaction();
	 *
	 * containerDAO.openCurrentSessionwithTransaction(); parent =
	 * assertThatTwoChildrenWhereAddedToParent(parent);
	 * containerDAO.closeCurrentSessionwithTransaction();
	 *
	 * containerDAO.openCurrentSessionwithTransaction(); child =
	 * containerDAO.findById(child.getContainerId()); deleteContainer(child);
	 * containerDAO.closeCurrentSessionwithTransaction();
	 *
	 * containerDAO.openCurrentSessionwithTransaction(); parent =
	 * assertThatParentStillExistsWhenChildIsDeleted(parent);
	 * deleteContainer(parent); containerDAO.closeCurrentSessionwithTransaction();
	 *
	 * containerDAO.openCurrentSessionwithTransaction();
	 * assertThatSecondChildIsDeletedWhenParentIsDeleted(child2);
	 * containerDAO.closeCurrentSessionwithTransaction(); }
	 *
	 * @Test public void testAddItems() { final String containerName =
	 * "testContainer_" + new Random().nextInt(123456);
	 * containerDAO.openCurrentSessionwithTransaction(); Container container = new
	 * Container(containerName); persistContainer(container);
	 * containerDAO.closeCurrentSessionwithTransaction();
	 *
	 * Item item = new Item("toastbrot");
	 * itemDAO.openCurrentSessionwithTransaction(); itemDAO.persist(item);
	 * container.getItems().put(item, 7); containerDAO.persist(container);
	 *
	 * itemDAO.closeCurrentSessionwithTransaction(); }
	 *
	 * private Container assertThatTwoChildrenWhereAddedToParent(Container parent) {
	 * parent = containerDAO.findById(parent.getContainerId());
	 * Assertions.assertThat(parent.getChildrenUnmodifiable()).hasSize(2); return
	 * parent; }
	 *
	 * private Container addChildToParent(Container parent, Container child) {
	 * parent = containerDAO.findById(parent.getContainerId());
	 * persistContainer(child, parent); parent.addToChildren(child);
	 * persistContainer(child, parent); return parent; }
	 *
	 * private Container assertThatParentStillExistsWhenChildIsDeleted(Container
	 * parent) { parent =
	 * Objects.requireNonNull(containerDAO.findById(parent.getContainerId()),
	 * "parent should still exist");
	 * Assertions.assertThat(parent.getChildrenUnmodifiable()).hasSize(1); return
	 * parent; }
	 *
	 * private void assertThatSecondChildIsDeletedWhenParentIsDeleted(Container
	 * child2) { containerDAO.closeCurrentSessionwithTransaction();
	 * containerDAO.openCurrentSessionwithTransaction(); child2 =
	 * containerDAO.findById(child2.getContainerId());
	 * Assertions.assertThat(child2).isNull();
	 *
	 * }
	 *
	 * private void persistContainer(Container... containers) { for (Container cont
	 * : containers) { containerDAO.persist(cont); } }
	 *
	 * private void deleteContainer(Container... containers) { for (Container cont :
	 * containers) { containerDAO.delete(cont); } }
	 *
	 * private void assertThatContainerWasAdded(int countBeforeTest) {
	 * Assertions.assertThat(containerDAO.findAll().size()).isEqualTo(
	 * countBeforeTest + 1); }
	 *
	 * private void assertThatContainerWasDeleted(int countBeforeTest) {
	 * Assertions.assertThat(containerDAO.findAll().size()).isEqualTo(
	 * countBeforeTest); }
	 */

}
