package com.calculator.bin;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ProgressIndicator;
import javafx.util.Duration;

import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

public class PrimaryController {

    @FXML
    private TextField decInput;
    @FXML
    private TextField binInput;

    @FXML
    private ProgressIndicator progressDec;
    @FXML
    private ProgressIndicator progressBin;

    @FXML
    private ProgressBar resetBar;
    @FXML
    private void resetBarCheck(){ resetBar.setProgress(0);}

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    private void resetFields() throws IOException {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.01), event -> {
            double progress = resetBar.getProgress() + 0.01;
            if (progress > 1.0) {
                progress = 1.0;
            }
            resetBar.setProgress(progress);
        }));
        timeline.setCycleCount(100);
        timeline.setOnFinished(event -> {
            binInput.setText("");
            decInput.setText("");
        });
        timeline.play();

    }

    @FXML
    private void posOrNeg() throws IOException, InterruptedException {
        if (Integer.parseInt(decInput.getText()) > -1)
            decToBin();
        else
            negDecToBin();
    }

    private void decToBin() throws IOException, InterruptedException {
        binInput.setText(null);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.01), event -> {
            double progress = progressDec.getProgress() + 0.01;
            if (progress > 1.0) {
                progress = 1.0;
            }
            progressDec.setProgress(progress);
        }));
        timeline.setCycleCount(100);
        timeline.setOnFinished(event -> {
            String bin = "";
            for (int n = Integer.parseInt(decInput.getText()); n != 0; n = n / 2)
                bin += Integer.toString(n % 2);
            binInput.setText(new StringBuffer(bin).reverse().toString());
            progressDec.setProgress(0);
        });
        timeline.play();

    }

    public void binToDec() {
        decInput.setText(null);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.01), event -> {
            double progress = progressBin.getProgress() + 0.01;
            if (progress > 1.0) {
                progress = 1.0;
            }
            progressBin.setProgress(progress);
        }));
        timeline.setCycleCount(100);
        timeline.setOnFinished(event -> {
            String reverse = new StringBuffer(binInput.getText()).reverse().toString();
            long dezi = 0;
            for (int i = 0, multiPl = 1; i < reverse.length(); i++) {
                if ((reverse.charAt(i)) != '0')
                    dezi += Character.getNumericValue(reverse.charAt(i)) * multiPl;
                multiPl *= 2;
            }
            decInput.setText(Long.toString(dezi));
            progressBin.setProgress(0);
        });
        timeline.play();

    }

    public void negDecToBin() {
        binInput.setText(null);
        String bin = "";
        for (int n = Math.abs(Integer.parseInt(decInput.getText())); n != 0; n = n / 2)
            bin += Integer.toString(n % 2);

        char[] newArr = new StringBuffer(bin).reverse().toString().toCharArray();
        for (int i = 0; i < newArr.length; i++) {
            if (newArr[i] == '1')
                newArr[i] = '0';
            else
                newArr[i] = '1';
        }
        List<Character> list = addOneBit(newArr);
        list.add(0, '1');

        binInput.setText(list.toString().replaceAll("[\\[\\],\\s+]", ""));

    }

    public List<Character> addOneBit(char[] arr) {

        List<Character> list = new ArrayList<Character>();

        for (Character character : arr) {
            list.add(character);
        }

        int countBit = 1;
        for (int i = list.size() - 1; i >= 0 && list.get(i) == '1'; i--) {
            countBit++;
            list.set(i, '0');
        }
        int tIndex = arr.length - countBit;
        if (tIndex > 0)
            list.set(tIndex, '1');
        else
            list.add(0, '1');

        return list;
    }
}
