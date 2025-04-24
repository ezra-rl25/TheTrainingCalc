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
    private double trimp=0;

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
        totalElevation=0;
        totalDistance=0;
        totalMovingTime=0;
        acuteLoad=0;
        trimp=0;
        for (StravaActivity act : activities) {
            totalElevation += act.getTotalElevationGain();
            totalDistance += act.getDistance();
            totalMovingTime += act.getMovingTime();
            acuteLoad += (act.getDistance() * act.getMovingTime());
            trimp += (act.getAverageHeartrate()*act.getElapsedTime());
        }
    }

    public void updateMonthlyMetrics(LocalDateTime endOfMonth) {
        List<StravaActivity> activities = getActivitiesInMonth(endOfMonth);
        totalElevation=0;
        totalDistance=0;
        totalMovingTime=0;
        chronicLoad=0;
        trimp=0;
        for (StravaActivity act : activities) {
            totalElevation += act.getTotalElevationGain();
            totalDistance += act.getDistance();
            totalMovingTime += act.getMovingTime();
            chronicLoad += (act.getDistance() * act.getMovingTime());
            trimp += (act.getAverageHeartrate()*act.getElapsedTime());
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

    public double getMonthlyTrimp(LocalDateTime endOfMonth) {
        updateMonthlyMetrics(endOfMonth);
        return trimp;
    }
    public double getWeeklyTrimp(LocalDateTime endOfWeek) {
        updateWeeklyMetrics(endOfWeek);
        return trimp;
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

    public double getMonthConsistency(LocalDateTime endOfMonth) {
        updateMonthlyMetrics(endOfMonth);
        int activityNumber=0;
        double meanLoad=0;
        double totalDistance=0;
        double totalMovingTime=0;

        List<StravaActivity> activities = getActivitiesInMonth(endOfMonth);
        for (StravaActivity act : activities) {
            totalMovingTime+=act.getMovingTime();
            totalDistance=+act.getDistance();
            activityNumber++;
        }

        meanLoad=totalMovingTime*totalDistance/activityNumber;

        double varianceSum = 0;

        for (StravaActivity act : activities) {
            varianceSum += Math.pow((act.getDistance()*act.getMovingTime()) - meanLoad, 2);
        }

        double standardDeviation = Math.sqrt(varianceSum / activityNumber);

        // Consistency score
        double consistency = 1 - (standardDeviation / meanLoad);

        // Clamp result between 0 and 1
        return Math.max(0, Math.min(1, consistency));
    }
}
