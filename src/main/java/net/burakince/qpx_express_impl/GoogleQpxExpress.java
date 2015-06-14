package net.burakince.qpx_express_impl;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.qpxExpress.QPXExpress;
import com.google.api.services.qpxExpress.QPXExpressRequestInitializer;
import com.google.api.services.qpxExpress.model.PassengerCounts;
import com.google.api.services.qpxExpress.model.SliceInput;
import com.google.api.services.qpxExpress.model.TripOption;
import com.google.api.services.qpxExpress.model.TripOptionsRequest;
import com.google.api.services.qpxExpress.model.TripsSearchRequest;
import com.google.api.services.qpxExpress.model.TripsSearchResponse;

public class GoogleQpxExpress implements FlightFinderService {

	private static final String APPLICATION_NAME = "Hackathon";
	private static final String API_KEY = "AIzaSyCGmaxh9SMK7pJG2mmxiWREkYlJE1ZSe90";

	/** Global instance of the HTTP transport. */
	private static HttpTransport httpTransport;

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	public GoogleQpxExpress() {
	}

	@Override
	public List<TripOption> getTripResults(String origin, String destination, Date date, int adult, int child, int infant) throws GeneralSecurityException, IOException {
		httpTransport = GoogleNetHttpTransport.newTrustedTransport();

		PassengerCounts passengers = new PassengerCounts();
		if (adult > 0) {
			passengers.setAdultCount(adult);
		}
		if (child > 0) {
			passengers.setChildCount(child);
		}
		if (infant > 0) {
			passengers.setInfantInSeatCount(infant);
		}

		List<SliceInput> slices = new ArrayList<SliceInput>();

		SliceInput slice = new SliceInput();
		// NYC
		slice.setOrigin(origin);
		// LGA
		slice.setDestination(destination);
		// yyyy-MM-dd
		slice.setDate(df.format(date));
		slices.add(slice);

		TripOptionsRequest request = new TripOptionsRequest();
		request.setSolutions(10);
		request.setPassengers(passengers);
		request.setSlice(slices);

		TripsSearchRequest parameters = new TripsSearchRequest();
		parameters.setRequest(request);

		QPXExpress qpXExpress = new QPXExpress
				.Builder(httpTransport, JSON_FACTORY, null)
				.setApplicationName(APPLICATION_NAME)
				.setGoogleClientRequestInitializer(new QPXExpressRequestInitializer(API_KEY))
				.build();

		TripsSearchResponse list = qpXExpress
				.trips()
				.search(parameters)
				.execute();

		return list.getTrips().getTripOption();
	}

}
