package org.magnum.mcc.paths;

public class EdgeData {

	private double length_;
	private String imageId_;
	
	public EdgeData(double length){
		this.length_ = length;
		this.setImageId(null);
	}
	
	public EdgeData(double length, String imageID){
		this.length_=length;
		this.setImageId(imageID);
	}
	
	public double getLength() {
		return length_;
	}

	public void setLength(double length_) {
		this.length_ = length_;
	}

	public String getImageId() {
		return imageId_;
	}

	public void setImageId(String imageId_) {
		this.imageId_ = imageId_;
	}
	
}
