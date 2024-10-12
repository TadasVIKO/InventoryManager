package lt.bropro.inventorymanager.server.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
    private String title;
    private String address;
    private LocalDate date;
    private String meetupTime;
    private String arrivalTime;
    private String readyTime;
    private String soundCheckTime;
    private String guestTime;
    private String endTime;
    @ManyToOne(targetEntity = EventType.class, cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private EventType eventType;
    @OneToMany(targetEntity = StoredItems.class, cascade = CascadeType.MERGE, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<StoredItems> storedItemsList;
    @OneToMany(targetEntity = Employee.class, cascade = CascadeType.MERGE, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Employee> employeeList;
    @OneToMany(targetEntity = Bill.class, cascade = CascadeType.MERGE, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Bill> billList;

    public Event(String title, String address, LocalDate date, String meetupTime, String arrivalTime, String readyTime, String soundCheckTime, String guestTime, String endTime, EventType eventType) {
        this.title = title;
        this.address = address;
        this.date = date;
        this.meetupTime = meetupTime;
        this.arrivalTime = arrivalTime;
        this.readyTime = readyTime;
        this.soundCheckTime = soundCheckTime;
        this.guestTime = guestTime;
        this.endTime = endTime;
        this.eventType = eventType;
    }

    public Event() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public List<StoredItems> getStoredItemsList() {
        return storedItemsList;
    }

    public void setStoredItemsList(List<StoredItems> storedItemsList) {
        this.storedItemsList = storedItemsList;
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    public List<Bill> getBillList() {
        return billList;
    }

    public void setBillList(List<Bill> billList) {
        this.billList = billList;
    }

    public String getMeetupTime() {
        return meetupTime;
    }

    public void setMeetupTime(String meetupTime) {
        this.meetupTime = meetupTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getReadyTime() {
        return readyTime;
    }

    public void setReadyTime(String readyTime) {
        this.readyTime = readyTime;
    }

    public String getSoundCheckTime() {
        return soundCheckTime;
    }

    public void setSoundCheckTime(String soundCheckTime) {
        this.soundCheckTime = soundCheckTime;
    }

    public String getGuestTime() {
        return guestTime;
    }

    public void setGuestTime(String guestTime) {
        this.guestTime = guestTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
