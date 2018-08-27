package database.entities;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "container")
public class Container {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "container_id")
	private Integer				containerId;

	@Column(name = "container_name")
	private String				name;

	@ManyToOne
	@JoinColumn(name = "container_parent")
	private Container			parent;

	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
	private Set<Container>		children;

	@ElementCollection
	@CollectionTable(name = "item_container", joinColumns = @JoinColumn(name = "container_id"))
	@Column(name = "count")
	@MapKeyJoinColumn(name = "item_id", insertable = true, updatable = true)
	private Map<Item, Integer>	items;

	public Container() {
		super();
	}

	public Container(String name) {
		this();
		this.name = name;
	}

	public Container(String name, Container parent) {
		this(name);
		this.parent = parent;
	}

	public void addToChildren(Container child) {
		child.setParent(this);
		children.add(child);
	}

	public Integer getContainerId() {
		return containerId;
	}

	public void setContainerId(Integer containerId) {
		this.containerId = containerId;
	}

	public Container getParent() {
		return parent;
	}

	public void setParent(Container parent) {
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Container> getChildrenUnmodifiable() {
		if (children == null) {
			children = new HashSet<>();
		}
		return java.util.Collections.unmodifiableSet(children);
	}

	public Map<Item, Integer> getItems() {
		if (items == null) {
			items = new HashMap<>();
		}
		return items;
	}

	public void setItems(Map<Item, Integer> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "Container [containerId=" + containerId + ", name=" + name + ", parent=" + parent + ", children="
				+ children + ", items=" + items + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((children == null) ? 0 : children.hashCode());
		result = prime * result + ((containerId == null) ? 0 : containerId.hashCode());
		result = prime * result + ((items == null) ? 0 : items.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Container other = (Container) obj;
		if (children == null) {
			if (other.children != null) {
				return false;
			}
		} else if (!children.equals(other.children)) {
			return false;
		}
		if (containerId == null) {
			if (other.containerId != null) {
				return false;
			}
		} else if (!containerId.equals(other.containerId)) {
			return false;
		}
		if (items == null) {
			if (other.items != null) {
				return false;
			}
		} else if (!items.equals(other.items)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (parent == null) {
			if (other.parent != null) {
				return false;
			}
		} else if (!parent.equals(other.parent)) {
			return false;
		}
		return true;
	}

}
