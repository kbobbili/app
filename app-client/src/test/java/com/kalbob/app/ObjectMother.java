package com.kalbob.app;

import com.github.javafaker.Faker;
import java.util.Locale;
import org.fluttercode.datafactory.impl.DataFactory;

public class ObjectMother {

  public static DataFactory dataFactory = new DataFactory();

  public static Faker faker = Faker.instance(Locale.US);

  public static DataFactory dataFactory() {
    return dataFactory;
  }

  public static Faker faker() {
    return faker;
  }

  public static Faker faker(Locale locale) {
    return Faker.instance(locale);
  }
}
