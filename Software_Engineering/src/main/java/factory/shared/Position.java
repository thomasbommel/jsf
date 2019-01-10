package factory.shared;

/**
 * this class is used as general Position of an Object in the factory
 */
public class Position {

	private int positionX;
	private int positionY;   

	public Position(int positionX, int positionY) {
		super();
		this.positionX = positionX;
		this.positionY = positionY;
	}

	public int getPositionX() {
		return positionX;
	}

	public void setPositionX(int positionX) {
		this.positionX = positionX;
	}

	public int getPositionY() {
		return positionY;
	}

	public void setPositionY(int positionY) {
		this.positionY = positionY;
	}

	@Override
	public String toString() {
		return "Position [positionX=" + positionX + ", positionY=" + positionY + "]";
	}

	
}
