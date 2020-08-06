package assignment4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
	public static void main(String[] args) {
		//creating a date format
		DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
		builder.parseCaseInsensitive();
		builder.appendPattern("dd-MMM-yyyy HH:mm");
		DateTimeFormatter dateFormat = builder.toFormatter();
		
		List<String> linesList = new ArrayList<>();//will be used to hold each line of the csv file
		List<Measurement> measurements = new ArrayList<>();//will be used to store each Measurement object
		String[] holder;//will be used to store each different value of the line currently being handled

		try{
			//creating a stream from the csv file, and skipping the first 24 lines (no data in them)
			Stream<String> stream = Files.lines(Paths.get(".\\src\\assignment4\\hly3723.csv")).skip(24);
			linesList = stream.collect(Collectors.toList());//storing each line of the stream in an ArrayList
			for(String l: linesList) {//using enhanced for loop to iterate through the ArrayList and turn the strings into individual data variables
				holder = l.split(",");//splitting the line around ',' which is what separates values in the csv file line
				LocalDateTime timeDate = LocalDateTime.parse(holder[0], dateFormat);
				double temp = Double.parseDouble(holder[4]);
				double rain = Double.parseDouble(holder[2]);
				//since some of the wetb cells are empty in the csv file, have to handle this
				double wetb = holder[6] == " " ? Double.parseDouble(holder[6]) : 0.0;
				double dewpt = Double.parseDouble(holder[7]);
				double vappr = Double.parseDouble(holder[8]);
				double msl = Double.parseDouble(holder[10]);
				double sun = Double.parseDouble(holder[17]);
				int rhum = Integer.parseInt(holder[9]);
				int wdsp = Integer.parseInt(holder[12]);
				int wddir = Integer.parseInt(holder[14]);
				//creating the Measurement object by passing the parsed values
				Measurement m = new Measurement(timeDate, temp, rain, wetb, dewpt, vappr, msl, sun, rhum, wdsp, wddir);
				measurements.add(m);//adding the object to the ArrayList of objects
			}
			
			/*mapToDouble does the following, using Q1 as an example:
			 *	it returns a DoubleStream consisting of all temp(erature) values from the measurements  
			 */
			//using getAsDouble() ensures the OptionalDouble value is returned as a Double
			//max() returns the maximum temp(erature) value
			System.out.println("Q1 Maximum temperature all data points: "
			 + measurements.parallelStream().mapToDouble(m->m.temp).max().getAsDouble());
			//average() returns the average of all the values in the DoubleStream (which holds all sun values)
			System.out.println("Q2 Average sunshine hours all datapoints: "
			 + measurements.parallelStream().mapToDouble(m->m.sun).average().getAsDouble());
			//sum() returns the combined total of all rain values
			System.out.println("Q3 Total rainfall all datapoints: " + measurements.parallelStream().mapToDouble(m->m.rain).sum());
			//this produces a Boolean result type. This helps to filter results, namely to the year 2001 and the month of November  
			Predicate<Measurement> nov2001 = m->(m.timeDate.getYear() == 2001)  && (m.timeDate.getMonthValue() == 11);
			//here, we use the Predicate to filter the results. This only allows for timeDate values that match the requirements to be mapped
			System.out.println("Q4 Total sunshine hours in November 2001: "
			 + measurements.parallelStream().filter(nov2001).mapToDouble(m->m.sun).sum());
			//this Predicate ensures only rain values that are greater than 5 are included in the map below
			Predicate<Measurement> rainCheck = m->m.rain>5;
			//count() simply counts the number of results in the DoubleStream produced by mapToDobule(m->m.rain)
			System.out.println("Q5 The number of hours where rainfall was greater than 5mm: "
			 + measurements.parallelStream().filter(rainCheck).mapToDouble(m->m.rain).count());
			Predicate<Measurement> hPaCheck = m->m.msl>1047.2;//this ensures only hPa values over 1047.2 are included
			System.out.println("Q6 The list of times where Mean Sea level pressure exceeded 1047.2 hPa: ");
			measurements.parallelStream().filter(hPaCheck).map(m->m.timeDate.format(dateFormat)).forEach(result -> System.out.print(result + " "));
		} catch (IOException e) {
			e.printStackTrace();
		} catch(NumberFormatException e) {
			e.printStackTrace();
		}
	}
}