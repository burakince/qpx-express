package net.burakince.qpx_express_impl;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.api.services.qpxExpress.model.FlightInfo;
import com.google.api.services.qpxExpress.model.LegInfo;
import com.google.api.services.qpxExpress.model.PricingInfo;
import com.google.api.services.qpxExpress.model.SegmentInfo;
import com.google.api.services.qpxExpress.model.SliceInfo;
import com.google.api.services.qpxExpress.model.TripOption;

public class Main {

	public static void main(String[] args) {
		FlightFinderService service = new GoogleQpxExpress();
		Date today = Calendar.getInstance().getTime();
		List<TripOption> results = null;

		try {
			results = service.getTripResults("NYC", "LGA", today, 1, 0, 0);
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (results == null)
			return;

		for (TripOption trip : results) {
			String price = trip.getSaleTotal();

			List<SliceInfo> slices = trip.getSlice();
			for (SliceInfo slice : slices) {
				int duration = slice.getDuration();

				System.out.println(String.format("Price: %s Duration: %d minutes", price, duration));
				
				List<SegmentInfo> segmentInfos = slice.getSegment();
				for (SegmentInfo segmentInfo : segmentInfos) {
					FlightInfo flightInfo = segmentInfo.getFlight();
					String carrier = flightInfo.getCarrier();
					String number = flightInfo.getNumber();

					System.out.println(String.format("\tFlightNumber: %s %s", carrier, number));

					List<LegInfo> legInfos = segmentInfo.getLeg();
					for (LegInfo legInfo : legInfos) {
						String aircraft = legInfo.getAircraft();

						String origin = legInfo.getOrigin();
						String originTerminal = legInfo.getOriginTerminal();
						String departureTime = legInfo.getDepartureTime();

						String destination = legInfo.getDestination();
						String destinationTerminal = legInfo.getDestinationTerminal();
						String arrivalTime = legInfo.getArrivalTime();

						System.out.println(String.format("\t\tAircraft: %s | Origin: %s Terminal: %s Time: %s -> Destination: %s Terminal: %s Time: %s",
								aircraft,
								origin,
								originTerminal,
								departureTime,
								destination,
								destinationTerminal,
								arrivalTime));
					}
				}
			}

			List<PricingInfo> pricingInfos = trip.getPricing();
			for (PricingInfo pricing : pricingInfos) {
				String latestTicketTime = pricing.getLatestTicketingTime();
				System.out.println(String.format("Latest Ticket Time: %s", latestTicketTime));
			}

			System.out.println("\n------------------------------------------\n");
		}
	}

}
