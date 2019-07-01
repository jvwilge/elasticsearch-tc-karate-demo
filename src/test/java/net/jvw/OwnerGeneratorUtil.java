package net.jvw;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Generate test data for elasticsearch bulk insert
 */
public class OwnerGeneratorUtil {

  private static final String[] FIRST_NAMES = new String[]{"John", "Jane", "Harry", "Harriet", "Luke", "Lucy",
      "Karl", "Chloe", "Fritz", "Frida", "Jeffrey", "Walter", "Maude", "Mike", "Anna", "Bobby", "Sid", "Jennifer",
      "James", "Shirley", "Denise", "Hans", "Holly"};
  private static final String[] LAST_NAMES = new String[]{"Doe", "Nak", "Wilson", "Fischer", "Becker", "Dubois",
      "Kowalski", "Popov", "Vega", "Winfield", "Virtanen", "Olsen", "Nielsen", "Gruber"};
  private static final String[] CARS = new String[]{"Toyota", "Lexus", "BMW", "Peugeot", "Mini", "Citroen", "Seat", "Ferrari"};

  private static Set<String> names = new HashSet<>(); //to prevent duplicates

  private static ObjectMapper mapper = new ObjectMapper();

  public static void main(String[] args) throws IOException {
    int i = 0;
    while (i < 180) {
      String id = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
      String firstName = FIRST_NAMES[ThreadLocalRandom.current().nextInt(0, FIRST_NAMES.length)];
      String lastName = LAST_NAMES[ThreadLocalRandom.current().nextInt(0, LAST_NAMES.length)];
      String car = CARS[ThreadLocalRandom.current().nextInt(0, CARS.length)];

      if (names.add(firstName + lastName)) {
        i++;

        Owner owner = new Owner(id, firstName, lastName, car);

        Map<String, Map<String, String>> map = new HashMap<>();
        Map<String, String> index = new HashMap<>();
        map.put("index", index);
        index.put("_index", "owner");
        index.put("_id", owner.getId());

        System.out.println(mapper.writeValueAsString(map));
        System.out.println(mapper.writeValueAsString(owner));
      }
    }
  }

}
