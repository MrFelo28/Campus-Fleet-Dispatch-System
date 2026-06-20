class CampusVehicle {
    protected String vehicleId;
    protected String driverName;
    protected double baseKmRate;
    protected double kilometresDriven;
    protected int jobsCompleted;
    private static int totalVehiclesCreated;
    private static double totalFleetRevenue;
    protected double revenueEarned=0.0;

    CampusVehicle(String vehicleId, String driverName, double baseKmRate){
         if (baseKmRate <= 0) {
            this.baseKmRate = 1.0;
        } else {
            this.baseKmRate = baseKmRate;
        }

        if (vehicleId == null || vehicleId.trim().isEmpty()) {
            this.vehicleId = "UNKNOWN";
        } else {
            this.vehicleId = vehicleId;
        }

        if (driverName == null || driverName.trim().isEmpty()) {
            this.driverName = "UNKNOWN";
        } else {
            this.driverName = driverName;
        }

        kilometresDriven=0.0;
        jobsCompleted=0;
        totalVehiclesCreated++;

    }
        protected double calculateFare(double kilometers, boolean peakHour){
            double fare=kilometers*baseKmRate;
            if(peakHour){
                fare+=fare*0.15;
            }
            
                return fare;
            
        }
        protected boolean canServe(String zone){
            if(zone!=null){
                return true;
            }
            else{
                return false;
            }
        }
        
        public String getVehicleId(){
            return vehicleId;
        }
        public String getDriverName(){
            return driverName;
        }
        public double getKilometresdriven(){
            return kilometresDriven;
        }
        public int getJobsCompleted(){
            return jobsCompleted;
        }
        public static int getTotalVehiclesCreated(){
            return totalVehiclesCreated;
        }

        public static double getTotalFleetRevenue(){
            return totalFleetRevenue;
        }
        public String getCategory(){
            return "CampusVehicle";
        }
        @Override
        public String toString(){
            return String.format("%-10s %-10s %10.2f %12.2f",
            getVehicleId(),
             getCategory(),
            getKilometresdriven(),
            revenueEarned);
        }
        @Override
        public boolean equals(Object obj) {
    if (this == obj){
        return true;
    }
    if (obj == null){
        return false;
    }
    if (!(obj instanceof CampusVehicle)){
        return false;
    }
    CampusVehicle other = (CampusVehicle) obj;

    return vehicleId.equalsIgnoreCase(other.vehicleId);
}
    
    public final boolean completeJob(String zone, double kilometres, boolean peakHour){
        if (kilometres<=0 || kilometres > 120){
            return false;
        } 
         if(!canServe(zone)){
            return false;
        }
        else {
       double fare= calculateFare(kilometres, peakHour);
       revenueEarned+=fare;
        kilometresDriven+=kilometres;
        jobsCompleted++;
        totalFleetRevenue+=fare;
        System.out.printf(
                "DISPATCH %s %s %.2f %.2f%n",
                vehicleId,
                zone,
                kilometres,
                fare);
        return true;
    }

}   
}
class ShuttleBus extends CampusVehicle{
    private int seatCount;
    ShuttleBus(String vehicleId, String driverName,int seatCount){
        super(vehicleId, driverName, 900.0);
        if(seatCount<10){
           this.seatCount=10;
        }
        else if(seatCount>60){
            this.seatCount=60;
        }
        else{
            this.seatCount=seatCount;
        }
    }

        @Override   
        public String getCategory(){
            return "ShuttleBus";
        }

        @Override
        public boolean canServe(String zone){
            if("MAIN".equals(zone) || "HOSTEL".equals(zone) || "LIBRARY".equals(zone)){
                return true;
            }
            else{
                return false;
            }
        } 
        @Override
        protected double calculateFare( double kilometres, boolean peakHour){
        return super.calculateFare(kilometres,peakHour) + (seatCount * 25.0);
        }
    }

class Motorbike extends CampusVehicle{
     private boolean insulatedBox;

    public Motorbike(String vehicleId,String driverName, boolean insulatedBox) {
        super(vehicleId, driverName, 650.0);
        this.insulatedBox = insulatedBox;
    }

    @Override
    public String getCategory() {
        return "Motorbike";
    }

    @Override
    protected boolean canServe(String zone) {
        if (!super.canServe(zone)) {
            return false;
        }

        return !("HEAVY".equals(zone) || "AIRPORT".equals(zone));
    }

    @Override
    protected double calculateFare(double kilometres,boolean peakHour) {

        double fare =super.calculateFare( kilometres,peakHour);
        fare *= 0.90;

        if (insulatedBox) {
            fare += 800.0;
        }

        return fare;
    }

}
class DeliveryVan extends CampusVehicle{
   private double loadLimitKg;

    public DeliveryVan(String vehicleId, String driverName,double loadLimitKg) {
        super(vehicleId, driverName, 1200.0);

        if (loadLimitKg < 100) {
            this.loadLimitKg = 100.0;
        } else if (loadLimitKg > 2000) {
            this.loadLimitKg = 2000.0;
        } else {
            this.loadLimitKg = loadLimitKg;
        }
    }

    @Override
    public String getCategory() {
        return "DeliveryVan";
    }

    @Override
    protected boolean canServe(String zone) {

        if (!super.canServe(zone)) {
            return false;
        }

        return !"MEDICAL".equals(zone);
    }

    @Override
    protected double calculateFare(double kilometres, boolean peakHour) {

        return super.calculateFare(kilometres, peakHour)+ loadLimitKg * 0.75;
    }

}
class EmergencyVan extends DeliveryVan{
        public EmergencyVan(String vehicleId, String driverName,double loadLimitKg) {
        super(vehicleId, driverName,loadLimitKg);
    }

    @Override
    public String getCategory() {
        return "EmergencyVan";
    }

    @Override
    protected boolean canServe(String zone) {
        return zone != null && !zone.trim().isEmpty();
    }

    @Override
    protected double calculateFare(double kilometres, boolean peakHour) {
        return super.calculateFare(kilometres,false)+ 5000.0;
    }

}
public class FleetDispatch{
        public static void main(String[] args) {

        ShuttleBus bus=new ShuttleBus("BUS-12", "Asha Mwinyi", 42);

        Motorbike bike=new Motorbike("BIKE-7", "Juma Omari", true);

        DeliveryVan van=new DeliveryVan("VAN-3", "Neema Kato", 750.0);

        EmergencyVan emergency =new EmergencyVan("MED-1", "Dr. Salim", 500.0);

        CampusVehicle[] fleet = {bus, bike, van, emergency};

        fleet[0].completeJob("MAIN", 12.0, true);
        fleet[0].completeJob("AIRPORT", 20.0, false);

        fleet[1].completeJob("HOSTEL", 4.0, true);
        fleet[1].completeJob("HEAVY", 8.0, false);

        fleet[2].completeJob("LIBRARY", 15.5, false);
        fleet[2].completeJob("MEDICAL", 5.0, false);

        fleet[3].completeJob("MEDICAL", 9.0, true);
        fleet[3].completeJob("AIRPORT", 130.0, false);

        System.out.println("\n===== FINAL REPORT =====");

        for (CampusVehicle vehicle : fleet) {
            System.out.println(vehicle);
        }

        System.out.printf(
                "\nTotal Vehicles Created : %d%n",
                CampusVehicle.getTotalVehiclesCreated());

        System.out.printf(
                "Total Fleet Revenue    : %.2f%n",
                CampusVehicle.getTotalFleetRevenue());
    }


}