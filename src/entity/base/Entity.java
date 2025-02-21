package entity.base;

public abstract class Entity {
	protected Double gridX, gridY;

	// Constructor

	public Entity(Double x, Double y) {
		this.setGridX(x);
		this.setGridY(y);
	}

	// Getters & Setters

	public Double getGridX() {
		return gridX;
	}

	public void setGridX(Double gridX) {
		this.gridX = gridX < 0 ? 0 : gridX;
	}

	public Double getGridY() {
		return gridY;
	}

	public void setGridY(Double gridY) {
		this.gridY = gridY < 0 ? 0 : gridY;
	}

}