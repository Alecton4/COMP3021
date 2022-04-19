package midterm01;

public abstract class Coin {
	private String representation;

	/**
	 * @return the representation
	 */
	public String getRepresentation() {
		return representation;
	}

	/**
	 * @param representation the representation to set
	 */
	public void setRepresentation(String representation) {
		this.representation = representation;
	}

	public abstract double getAsUSD();

	public Coin(String representation) {
		this.representation = representation;
	}

	public String toString() {
		return representation + ":" + getAsUSD() + " USD";
	}
}
