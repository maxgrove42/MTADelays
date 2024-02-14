package mtadelays;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

import com.google.transit.realtime.GtfsRealtime;



//https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/camsys%2Fsubway-alerts
public class SubwayAlerts {

	public static void main(String[] args) {
		String feedUrl = "https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/camsys%2Fsubway-alerts";
        try {
			GtfsRealtime.FeedMessage feed = GTFSFeedRealtime.fetchRealtimeData(feedUrl);
			
			
			
			
	        
	        for (GtfsRealtime.FeedEntity entity : feed.getEntityList()) {
	            if (entity.hasAlert() && entity.getAlert().getInformedEntity(0).hasRouteId()) {
	            	//if (entity.getAlert().getInformedEntity().getRouteId() == "7") {
	            		
	            		System.out.println(entity.getAlert().getHeaderText().getTranslation(0).getText());
	            	//}
	            }
	        }
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
