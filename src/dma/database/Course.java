package dma.database;

public class Course {
    private int id = 0;
    private String name = "";
    private int teacherId = 0;
    private int maxAmountSeats = 0;
    private int seatsUsed = 0;

    public Course(String name, int maxAmountSeats, int teacherId) {
        this.name = name;
        this.maxAmountSeats = maxAmountSeats;
        this.teacherId = teacherId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getMaxAmountSeats() {
        return maxAmountSeats;
    }

    public void setMaxAmountSeats(int maxAmountSeats) {
        this.maxAmountSeats = maxAmountSeats;
    }

    public int getSeatsUsed() {
        return seatsUsed;
    }

    public void setSeatsUsed(int seatsUsed) {
        this.seatsUsed = seatsUsed;
    }

    public void reserveSeat(){
        this.seatsUsed++;
    }
}
