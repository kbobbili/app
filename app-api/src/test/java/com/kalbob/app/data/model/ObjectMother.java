package com.kalbob.app.data.model;

import com.github.javafaker.Faker;
import org.fluttercode.datafactory.impl.DataFactory;

import java.util.Locale;

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
