package APIImplementation;

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

    public void updateMonthlySummary (){
        ActivityAnalyzer analyzer=new ActivityAnalyzer();
        analyzer.updateMonthlyMetrics();
        totalDistance=analyzer.getTotalDistance();
        totalMovingTime=analyzer.getTotalMovingTime();
        totalElevation=analyzer.getTotalElevation();
        chronicLoad=analyzer.getChronicLoad();
        trimp=analyzer.getMonthlyTrimp();
        analyzer.updateWeeklyMetrics();
        acuteLoad= analyzer.getAcuteLoad();
        acr=analyzer.getACR();
        tsb=analyzer.getTSB();
        consistency=analyzer.getMonthConsistency();
    }

}
