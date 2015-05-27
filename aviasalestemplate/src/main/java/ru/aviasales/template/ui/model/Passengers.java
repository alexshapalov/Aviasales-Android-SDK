package ru.aviasales.template.ui.model;

public class Passengers {
	private int adults = 1;
	private int children = 0;
	private int infants = 0;


	public Passengers(){}

	public Passengers(int adults, int children, int infants){
		this.adults = adults;
		this.children = children;
		this.infants = infants;
	}

	public int getAdults() {
		return adults;
	}

	public void setAdults(int adults) {
		this.adults = adults;
	}

	public int getChildren() {
		return children;
	}

	public void setChildren(int children) {
		this.children = children;
	}

	public int getInfants() {
		return infants;
	}

	public void setInfants(int infants) {
		this.infants = infants;
	}
}
