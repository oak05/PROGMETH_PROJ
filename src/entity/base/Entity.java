package entity.base;

public abstract class Entity {
	protected double gridX, gridY;

	// Constructor

	public Entity(double x, double y) {
		this.setGridX(x);
		this.setGridY(y);
	}

	// Getters & Setters

	public double getGridX() {
		return gridX;
	}

	public void setGridX(double gridX) {
		this.gridX = gridX < 0 ? 0 : gridX;
	}

	public double getGridY() {
		return gridY;
	}

	public void setGridY(double gridY) {
		this.gridY = gridY < 0 ? 0 : gridY;
	}

}