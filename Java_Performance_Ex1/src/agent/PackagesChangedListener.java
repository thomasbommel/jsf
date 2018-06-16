package agent;

import java.util.EventListener;
import java.util.List;

public interface PackagesChangedListener extends EventListener {
	
	public void packagesChanged(List<String> newPackages);
}
