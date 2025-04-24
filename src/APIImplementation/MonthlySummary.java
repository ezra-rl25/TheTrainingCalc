package APIImplementation;

import javastrava.service.Strava;

import java.time.LocalDateTime;

public class MonthlySummary {
    private double totalDistance = 0;
    private double totalMovingTime = 0;
    private double totalElevation = 0;
    private double acuteLoad = 0;
    private double chronicLoad=0;
    private double trimp=0;
    private double acr=0;
    private double consistency=0;
    private double tsb=0;

    public MonthlySummary (){
    }

    public void updateMonthlySummary (LocalDateTime endOfMonth, Strava api){
        ActivityAnalyzer analyzer=new ActivityAnalyzer(api);
        analyzer.updateMonthlyMetrics(endOfMonth);
        totalDistance=analyzer.getTotalDistance();
        totalMovingTime=analyzer.getTotalMovingTime();
        totalElevation=analyzer.getTotalElevation();
        chronicLoad=analyzer.getChronicLoad();
        trimp=analyzer.getMonthlyTrimp(endOfMonth);
        analyzer.updateWeeklyMetrics(endOfMonth);
        acuteLoad= analyzer.getAcuteLoad();
        acr=analyzer.getACR(endOfMonth);
        tsb=analyzer.getTSB(endOfMonth);
        consistency=analyzer.getMonthConsistency(endOfMonth);
    }

}
