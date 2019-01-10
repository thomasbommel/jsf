package factory.shared;

/**
 * this class is used as general Position of an Object in the factory
 */
public class Position {

	Double xPos, yPos;
	
	public Position(Double xPos, Double yPos)
	{
		this.xPos = xPos;
		this.yPos = yPos;
	}
	
	public void setPositionX(Double xPos) {
		this.xPos = xPos;
	}

	public void setPositionY(Double yPos) {
		this.yPos = yPos;
	}

	public Double getPositionX() {
		return xPos;
	}

	public Double getPositionY() {
		return yPos;
	}
	
	public Position add(Position pos2)
	{
		return new Position(xPos+pos2.getPositionX(), yPos+pos2.getPositionY());
	}
	
	public Position subtract(Position pos2)
	{
		return new Position(xPos-pos2.getPositionX(), yPos-pos2.getPositionY());
	}
	
	public Position multiply(Double mult)
	{
		return new Position(xPos*mult, yPos*mult);
	}
	
	public Position divide(Double div)
	{
		return new Position(xPos/div, yPos/div);
	}
	
	public double length()
	{
		return Math.sqrt(xPos*xPos+yPos*yPos);
	}
	
	public boolean equals(Position pos2)
	{
		return xPos == pos2.getPositionX() && yPos == pos2.getPositionY();
	}
	
	public String toString()
	{
		return xPos + ", " + yPos;
	}
}
