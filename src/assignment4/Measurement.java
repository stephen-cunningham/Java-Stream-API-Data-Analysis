package assignment4;

import java.time.LocalDateTime;

public class Measurement {
	//declaring the variables for the Measurement class
	//note: variables made public and thus getters and setters (encapsulation) not used, as per the assignment instructions
	public LocalDateTime timeDate;
	public Double temp, rain, wetb, dewpt, vappr, msl, sun;
	public Integer rhum, wdsp, wddir;
	
	//the constructor, which creates an instance of the Measurement class when called upon and passed the appropriate values as arguments
	public Measurement(LocalDateTime timeDate, 
			double temp, double rain, double wetb, double dewpt, double vappr, double msl, double sun, 
			int rhum, int wdsp, int wddir) {
		this.timeDate = timeDate;
		this.temp = temp;
		this.rain = rain;
		this.wetb = wetb;
		this.dewpt = dewpt;
		this.vappr = vappr;
		this.msl = msl;
		this.sun = sun;
		this.rhum = rhum;
		this.wdsp = wdsp;
		this.wddir = wddir;
	}
}