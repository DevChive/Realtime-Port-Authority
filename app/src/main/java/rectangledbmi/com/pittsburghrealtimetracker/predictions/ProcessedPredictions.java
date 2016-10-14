package rectangledbmi.com.pittsburghrealtimetracker.predictions;

import android.os.Bundle;

import com.google.android.gms.maps.model.Marker;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import rectangledbmi.com.pittsburghrealtimetracker.world.jsonpojo.Prd;
import rectangledbmi.com.pittsburghrealtimetracker.world.jsonpojo.Pt;
import rectangledbmi.com.pittsburghrealtimetracker.world.jsonpojo.Vehicle;

/**
 * <p>Holds prediction info.</p>
 * <p>Created by epicstar on 10/14/16.</p>
 * @author Jeremy Jao
 */

public class ProcessedPredictions {
    private final Marker marker;
    private final String predictions;

    /**
     * This is the date format to print
     *
     * @since 46
     */
    private final static String DATE_FORMAT_PRINT = "hh:mm a";

    /**
     * The default date format to parse... The timezone is set as EST in
     * {@link rectangledbmi.com.pittsburghrealtimetracker.SelectTransit#onCreate(Bundle)}
     * @since 46
     */
    private final static SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PRINT, Locale.US);

    static {
        dateFormat.setTimeZone(TimeZone.getTimeZone("EST"));
    }

    static ProcessedPredictions create(Marker marker, PredictionsType predictionsType, List<Prd> predictions) {
        return new ProcessedPredictions(marker, predictionsType, predictions);
    }

    private ProcessedPredictions(Marker marker, PredictionsType predictionsType, List<Prd> predictions) {
        // predictionstype is already in the marker but there's an IllegalStateException if not on main thread when running marker.getTag()...
        this.marker = marker;
        this.predictions = processPrds(predictionsType, predictions);
    }

    private static String processPrds(PredictionsType predictionsType, List<Prd> prds) {
        StringBuilder st = new StringBuilder();
        boolean isFirst = true;
        for (Prd prd : prds) {
            if (isFirst) {
                isFirst = false;
            } else {
                st.append("\n");
            }
            if (predictionsType instanceof Pt) {
                st.append(String.format("%s (%s): %s",
                    prd.getRt(), prd.getVid(), dateFormat.format(prd.getPrdtm()))
                );
            } else if (predictionsType instanceof Vehicle) {
                st.append(String.format("(%s) %s: %s",
                        prd.getStpid(), prd.getStpnm(), dateFormat.format(prd.getPrdtm()))
                );
            }
        }
        return st.toString().length() > 0 ? st.toString() : "No predictions available.";
    }

    public Marker getMarker() {
        return marker;
    }

    public String getPredictions() {
        return predictions;
    }
}