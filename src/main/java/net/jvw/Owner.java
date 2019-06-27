package net.jvw;

public class Owner {

  private String id;
  private String firstName;
  private String lastName;
  private String car;

  public Owner() {
    //constructor for Jackson
  }

  public Owner(String id, String firstName, String lastName, String car) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.car = car;
  }

  public String getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getCar() {
    return car;
  }

}
