package APIImplementation;

public class WeeklySummary {
    private double totalDistance = 0;
    private double totalMovingTime = 0;
    private double totalElevation = 0;
    private double acuteLoad = 0;
    private double trimp=0;
    private double consistency=0;
    private double tsb=0;

    public WeeklySummary (){
    }

    public void updateWeeklySummary (){
        ActivityAnalyzer analyzer=new ActivityAnalyzer();
        analyzer.updateWeeklyMetrics();
        totalDistance=analyzer.getTotalDistance();
        totalMovingTime=analyzer.getTotalMovingTime();
        totalElevation=analyzer.getTotalElevation();
        acuteLoad= analyzer.getAcuteLoad();
        trimp=analyzer.getWeeklyTrimp();
        tsb=analyzer.getTSB();
        consistency=analyzer.getMonthConsistency();
    }

}
