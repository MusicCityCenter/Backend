/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.magnum.mcc.building;

public class PathData {

	private final String from;
	private final String to;
	private final double length;
	private final double angle;

	public PathData(String from, String to, double length, double angle) {
		super();
		this.from = from;
		this.to = to;
		this.length = length;
		this.angle = angle;
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	public double getLength() {
		return length;
	}

	public double getAngle() {
		return angle;
	}

}
