package agent;

import java.util.List;

public class LoggerModel implements PackagesChangedListener {

	private List<String> packages = null;
	
	public LoggerModel() {
		PackageChooserDialog initialPackagesDialog = new PackageChooserDialog(this);
		initialPackagesDialog.packAndShow();
	}
	
	@Override
	public void packagesChanged(List<String> newPackages) {
		this.packages = newPackages;
		newPackages.forEach(p -> System.out.println(p));
	}

}
