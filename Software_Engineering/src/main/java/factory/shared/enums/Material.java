package factory.shared.enums;

public enum Material {
	CAR_BODIES			(200),
	NAILS				(500),
	COLOR_RED			(300),
	COLOR_GREEN			(300),
	COLOR_BLUE			(300);
	
	/** Amount of the material that fits into a single container. */
	public final int containerAmount;
	
	private Material(int containerAmount) {
		this.containerAmount = containerAmount;
	}
}
