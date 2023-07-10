package it.polito.tdp.extflightdelays.model;

public class Rotta {

	private Airport origin;
	private Airport destination;
	private int n;
	
	public Rotta(Airport origin, Airport destination, int n) {
		this.origin = origin;
		this.destination = destination;
		this.n = n;
	}

	public Airport getOrigin() {
		return origin;
	}

	public void setOrigin(Airport origin) {
		this.origin = origin;
	}

	public Airport getDestination() {
		return destination;
	}

	public void setDestination(Airport destination) {
		this.destination = destination;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}
	
	
	
	
}
