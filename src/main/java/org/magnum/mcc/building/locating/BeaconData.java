package org.magnum.mcc.building.locating;

public class BeaconData {
	private String uuid;
	private int major;
	private int minor;
	private int rssi;
	private String distance;
	private double accuracy;
	
	BeaconData(String uid, int maj, int min, int rsi, String dist, double acc){
		uuid=uid;
		major=maj;
		minor=min;
		rssi=rsi;
		distance=dist;
		accuracy=acc;
	}
	
	public String getAggregateId(){
		return uuid+"-"+major+"-"+minor;
	}

}
