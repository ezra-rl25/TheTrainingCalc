package APIImplementation;
import javastrava.model.StravaActivity;
import javastrava.service.Strava;

import java.time.LocalDateTime;
import java.util.List;

public class ActivityAnalyzer {
    private final Strava api;
    private double totalDistance = 0;
    private double totalMovingTime = 0;
    private double totalElevation = 0;
    private double acuteLoad = 0;
    private double chronicLoad=0;

    public ActivityAnalyzer(Strava api) {
        this.api = api;
    }

    public List<StravaActivity> getActivitiesInWeek(LocalDateTime endOfWeek) {
        LocalDateTime startOfWeek = endOfWeek.minusDays(7);

        return api.listAuthenticatedAthleteActivities(startOfWeek, endOfWeek);
    }
    public List<StravaActivity> getActivitiesInMonth(LocalDateTime endOfMonth) {
        LocalDateTime startOfWeek = endOfMonth.minusDays(28);

        return api.listAuthenticatedAthleteActivities(startOfWeek, endOfMonth);
    }

    public void updateWeeklyMetrics(LocalDateTime endOfWeek) {
        List<StravaActivity> activities = getActivitiesInWeek(endOfWeek);
        for (StravaActivity act : activities) {
            totalElevation += act.getTotalElevationGain();
            totalDistance += act.getDistance();
            totalMovingTime += act.getMovingTime();
            acuteLoad += (act.getDistance() * act.getMovingTime());
        }
    }

    public void updateMonthlyMetrics(LocalDateTime endOfMonth) {
        List<StravaActivity> activities = getActivitiesInMonth(endOfMonth);
        for (StravaActivity act : activities) {
            totalElevation += act.getTotalElevationGain();
            totalDistance += act.getDistance();
            totalMovingTime += act.getMovingTime();
            chronicLoad += (act.getDistance() * act.getMovingTime());
        }
    }

    public double getTotalDistance () {
        return totalDistance;
    }
    public double getTotalElevation () {
        return totalElevation;
    }
    public double getAcuteLoad () {
        return acuteLoad;
    }
    public double getTotalMovingTime () {
        return totalMovingTime;
    }
    public double getChronicLoad () {
        return chronicLoad;
    }
    public double getACR(LocalDateTime endOfMonth) {
        updateWeeklyMetrics(endOfMonth);
        double acuteLoad=getAcuteLoad();
        updateMonthlyMetrics(endOfMonth);
        double chronicLoad=getChronicLoad();
        return (acuteLoad/chronicLoad);
    }
    public double getTSB(LocalDateTime endOfMonth) {
        updateWeeklyMetrics(endOfMonth);
        double acuteLoad=getAcuteLoad();
        updateMonthlyMetrics(endOfMonth);
        double chronicLoad=getChronicLoad();
        return (chronicLoad-acuteLoad);
    }
}
