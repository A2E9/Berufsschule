package com.quantenquellcode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException, URISyntaxException {
        App.setRoot("secondary");
        readCaffeeDb();
    }

    @FXML
    private List<Caffee> readCaffeeDb() throws IOException, URISyntaxException {
        URL url = App.class.getResource("data/caffee_liste.db");
        URI uri = url.toURI();
        File file = new File(uri);
        Scanner sc = new Scanner(file);
        
        List<Caffee> caffeeList = new ArrayList<Caffee>();

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(";");

            String name = parts[0].trim();
            float small = (parts.length > 1 && !parts[1].isEmpty()) ? Float.parseFloat(parts[1]) : 0.0f;
        }
        sc.close();

        return caffeeList;
    }

}

class Caffee {

    String name;
    float small;
    float mid;
    float big;

    public Caffee(String name, float small, float mid, float big) {
        this.name = name;
        this.small = small;
        this.mid = mid;
        this.big = big;
    }
}