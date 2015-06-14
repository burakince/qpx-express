package net.burakince.qpx_express_impl;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;

import com.google.api.services.qpxExpress.model.TripOption;

public interface FlightFinderService {

	List<TripOption> getTripResults(String origin, String destination, Date date, int adult, int child, int infant) throws GeneralSecurityException, IOException;

}
