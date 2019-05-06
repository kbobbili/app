package com.kalbob.app.data.model;

import com.github.javafaker.Faker;
import java.util.Locale;
import org.fluttercode.datafactory.impl.DataFactory;

public class ObjectMother {

  static DataFactory dataFactory = new DataFactory();

  static Faker faker = Faker.instance(Locale.US);

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
