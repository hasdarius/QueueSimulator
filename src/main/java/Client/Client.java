package Client;

public class Client {
    private Integer ID;
    private Integer tArrival;
    private Integer tService;
    private Integer waitingTime;
    private Integer totalWaitingTime;

    public Client(Integer ID, Integer tArrival, Integer tService) {
        this.ID = ID;
        this.tArrival = tArrival;
        this.tService = tService;
    }

    public Integer getTotalWaitingTime() {
        return totalWaitingTime;
    }

    public void setTotalWaitingTime(Integer totalWaitingTime) {
        this.totalWaitingTime = totalWaitingTime;
    }

    public Integer getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(Integer waitingTime) {
        this.waitingTime = waitingTime;
    }

    public Integer gettArrival() {
        return tArrival;
    }

    public Integer gettService() {
        return tService;
    }

    public void settService(Integer tService) {
        this.tService = tService;
    }

    public String toPrint() {
        return "(" + this.ID.toString() + " , " + this.gettArrival().toString() + " , " + this.gettService().toString() + ")";
    }

}
