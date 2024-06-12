package task.lab_project_oop;

public class Room {

    private String roomColumn;

    public Room() {
    }

    public Room(String roomColumn) {
        this.roomColumn = roomColumn;
    }

    public String getRoomColumn() {
        return roomColumn;
    }

    public void setRoomColumn(String roomColumn) {
        this.roomColumn = roomColumn;
    }
}