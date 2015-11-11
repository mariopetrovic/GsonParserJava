import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;

public class CsvFilesGenerator {

	// Delimiter used in CSV file
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";

	// CSV file header
	private static final String FILE_HEADER = "_id,name,type,latitude,longitude";

	private class CityInfo {
		String _id;
		String key;
		String name;
		String fullName;
		String iata_airport_code;
		String type;
		String country;

		Location geo_position;
		String location_id;
		String inEurope;
		String countryCode;
		String coreCountry;
		String distance;
	}

	private class Location {
		String latitude;
		String longitude;

	}

	public static void main(String[] args) {
		CsvFilesGenerator csv = new CsvFilesGenerator();
		String city = "CITY_NAME";
		String responseString = null;
		Gson gson = new Gson();
		try {
			city = args[0];
			responseString = csv.getHTML("http://api.goeuro.com/api/v2/position/suggest/en/" + city);
			CityInfo[] myCities = gson.fromJson(responseString, CityInfo[].class);
			writeCsvFile(city + "." + "csv", myCities);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("There is no information about required place");
			e.printStackTrace();
		}
	}

	private String getHTML(String urlToRead) {
		URL url;
		HttpURLConnection conn;
		BufferedReader rd;
		String line;
		String result = "";
		try {
			url = new URL(urlToRead);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			rd = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			while ((line = rd.readLine()) != null) {
				result += line;
			}

			rd.close();
			return result;
		} catch (IOException e) {
			System.out.println("IO exception");
			e.printStackTrace();

		} catch (Exception e) {
			System.out.println("Unpredictible error");
			e.printStackTrace();

		}
		return null;
	}

	public static void writeCsvFile(String fileName, CityInfo[] myCities) {

		FileWriter fileWriter = null;

		try {
			fileWriter = new FileWriter(fileName);

			// Write the CSV file header
			fileWriter.append(FILE_HEADER.toString());

			// Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);

			// Write a new student object list to the CSV file
			for (CityInfo city : myCities) {
				fileWriter.append(city._id);
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(city.name);
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(city.type);
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(city.geo_position.latitude);
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(city.geo_position.longitude);
				fileWriter.append(NEW_LINE_SEPARATOR);
			}

			System.out.println("CSV file was created successfully !!!");

		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} finally {

			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
				e.printStackTrace();
			}

		}
	}

}
