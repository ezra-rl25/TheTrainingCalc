package StravaDataAnalysis;

import javastrava.model.StravaActivity;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tester {

    public List<StravaActivity> getMock28DayRuns() {
        List<StravaActivity> activities = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 28; i++) {
            StravaActivity activity = new StravaActivity();
            activity.setName("Run Day " + (28 - i));

            // Random distance between 5km and 15km
            double distance = 5000 + random.nextInt(10000); // meters
            activity.setDistance((float) distance);

            // Pace between 4.5 and 6.5 min/km converted to seconds/km
            double pace = 270 + random.nextInt(120);
            int movingTime = (int) ((distance / 1000.0) * pace); // seconds
            activity.setMovingTime(movingTime);

            // Add 5–10% to elapsed time for realism
            activity.setElapsedTime((int) (movingTime * (1.05 + random.nextDouble() * 0.05)));

            // Elevation gain between 20m and 150m
            activity.setTotalElevationGain((float) (20 + random.nextInt(130)));

            activities.add(activity);
        }

        return activities;
    }

    public void process28Days(List<StravaActivity> list) {
        double totalElevation=0;
        double totalDistance=0;
        double totalMovingTime=0;
        double chronicLoad=0;
        double acuteLoad=0;

        for (StravaActivity act : list) {
            totalElevation += act.getTotalElevationGain();
            totalDistance += act.getDistance();
            totalMovingTime += act.getMovingTime();
            chronicLoad += (act.getDistance() * act.getMovingTime()/3600);
        }

        chronicLoad=chronicLoad/4;

        for (int i=20; i<list.size(); i++) {
            acuteLoad += (list.get(i).getDistance() * list.get(i).getMovingTime()/3600);
        }

        int activityNumber = 0;
        double totalLoad = 0;

// 1. Calculate load for each activity, and sum it
        List<Double> loads = new ArrayList<>();
        for (StravaActivity act : list) {
            double distanceKm = act.getDistance() / 1000.0;
            double timeHr = act.getMovingTime() / 3600.0;
            double load = distanceKm * timeHr;
            loads.add(load);
            totalLoad += load;
            activityNumber++;
        }

// 2. Calculate mean load
        double meanLoad = totalLoad / activityNumber;

// 3. Calculate variance
        double varianceSum = 0;
        for (double load : loads) {
            varianceSum += Math.pow(load - meanLoad, 2);
        }
        double standardDeviation = Math.sqrt(varianceSum / activityNumber);

// 4. Calculate consistency
        double consistency = 1 - (standardDeviation / meanLoad);
        consistency = Math.max(0, Math.min(1, consistency)); // clamp between 0 and 1

        System.out.printf("Total Elevation: %.2f meters%n", totalElevation);
        System.out.println();

        System.out.printf("Total Moving Time: %.2f hours%n", totalMovingTime/3600);
        System.out.println();

        System.out.printf("Total Distance: %.2f kilometers%n", totalDistance / 1000);
        System.out.println();

        System.out.printf("Acute Load: %.2f%n", acuteLoad);
        System.out.println();

        System.out.printf("Chronic Load: %.2f%n", chronicLoad);
        System.out.println();

        System.out.printf("ACR: %.2f%n", (acuteLoad / chronicLoad));
        System.out.println();

        System.out.printf("TSB: %.2f%n", (chronicLoad - acuteLoad));
        System.out.println();

        System.out.printf("Consistency: %.2f%n", Math.max(0, Math.min(1, consistency)));
        System.out.println();
    }

    public static void main(String[] args) {
        Tester test=new Tester();
        test.process28Days(test.getMock28DayRuns());
    }
}
