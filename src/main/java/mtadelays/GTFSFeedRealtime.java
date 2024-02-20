package mtadelays;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import com.google.transit.realtime.GtfsRealtime;
//import com.google.transit.realtime.GtfsRealtime.FeedEntity;
import com.google.transit.realtime.GtfsRealtime.FeedMessage;
import com.google.transit.realtime.GtfsRealtime.TripUpdate.StopTimeUpdate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;




public class GTFSFeedRealtime {
	
	private final static String API_KEY = "ZQDTko7PBC4Hrs8vHqrJt9pibbSVvTCh9I4JftyO";
	
	private static class StopUpdateWithLine {
		StopTimeUpdate stopTimeUpdate;
		String routeID;
		public StopUpdateWithLine(StopTimeUpdate update, String route) {
			stopTimeUpdate = update;
			routeID = route;
		}
	}
	

	public static FeedMessage fetchRealtimeData(String urlString) throws Exception {
		URL url = new URL(urlString);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("x-api-key", API_KEY); // Replace with your API key
		try {
			return FeedMessage.parseFrom(connection.getInputStream());
		} finally {
			connection.disconnect();
		}
	}
	
    public static void printNextArrivalTime(String feedUrl, String stopId) throws Exception {
        // Fetch and parse the GTFS Realtime feed
        GtfsRealtime.FeedMessage feed = fetchRealtimeData(feedUrl);

        // Iterate through TripUpdate messages
        //print up to 3 next trains.
        List<StopUpdateWithLine> validUpdates = new LinkedList<StopUpdateWithLine>();
        
        for (GtfsRealtime.FeedEntity entity : feed.getEntityList()) {
            if (entity.hasTripUpdate()) {
            	
                List<GtfsRealtime.TripUpdate.StopTimeUpdate> stopTimeUpdates = entity.getTripUpdate().getStopTimeUpdateList();
                for (GtfsRealtime.TripUpdate.StopTimeUpdate stopTimeUpdate : stopTimeUpdates) {
                    // Check if this StopTimeUpdate is for the stop
                    if (stopTimeUpdate.hasStopId() && stopTimeUpdate.getStopId().equals(stopId)) {
                        // Check for arrival information 
                    	  
                    	if (stopTimeUpdate.hasArrival() && stopTimeUpdate.getArrival().hasTime()) {
                            long arrivalTime = stopTimeUpdate.getArrival().getTime();
                            long now = Instant.now().getEpochSecond();
                            if (arrivalTime >= now) {
                            	//System.out.println(stopTimeUpdate);
                            	StopUpdateWithLine stopDetail = new StopUpdateWithLine(stopTimeUpdate, entity.getTripUpdate().getTrip().getRouteId());
                            	validUpdates.add(stopDetail);
                            }
                        }
                    }
                }
            }
        }
        if (validUpdates.size() == 0) {
        	System.out.println("No upcoming trains found for stop " + stopId);
        	return;
        }
        validUpdates.sort(new Comparator<StopUpdateWithLine>() {

			@Override
			public int compare(StopUpdateWithLine o1, StopUpdateWithLine o2) {
				if (o1.stopTimeUpdate.hasArrival() && o2.stopTimeUpdate.hasArrival()) {
                    return Long.compare(o1.stopTimeUpdate.getArrival().getTime(), o2.stopTimeUpdate.getArrival().getTime());
                }
				return 0;
			}
        	
        });
        System.out.println("The next train(s) to arrive at " + stopId + ": ");
        for (int i = 0; i < 3; i++) { 
        	LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(validUpdates.get(i).stopTimeUpdate.getArrival().getTime()), ZoneId.systemDefault());
            System.out.println(dateTime.toString().substring(11, 16) + " (" + validUpdates.get(i).routeID + " line)");
        }
        
    }


	public static void main(String[] args) throws Exception {
		Scanner input = new Scanner(System.in);
		try {

			String line;
			
			System.out.println("I will provide information on upcoming trains at a given stop.");
			System.out.println("In order to determine what stop you want, I will first ask you what line, ");
			System.out.println("then what borough, and then provide you with possible stops ");
			System.out.println("for you to tell me which stop. I do not support Shuttle or SIR.");
			System.out.print("What is one of the lines your stop is on (one character): ");
			line = input.next().substring(0, 1);
			String feedUrl;
			switch(line) {
				case "A":
				case "C":
				case "E":
					feedUrl = "https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/nyct%2Fgtfs-ace";
					break;
				case "G":
					feedUrl = "https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/nyct%2Fgtfs-g";
					break;
				case "N":
				case "Q":
				case "R":
				case "W":
					feedUrl = "https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/nyct%2Fgtfs-nqrw";
					break;
				case "B":
				case "D":
				case "F":
				case "M":					
					feedUrl = "https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/nyct%2Fgtfs-bdfm";
					break;
				case "J":
				case "Z":
					feedUrl = "https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/nyct%2Fgtfs-jz";
					break;
				case "L":
					feedUrl = "https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/nyct%2Fgtfs-l";
					break;
				default:
					feedUrl = "https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/nyct%2Fgtfs";
			}
			printNextArrivalTime(feedUrl, "714N");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			input.close();
		}
	}
}
