package org.khush.assignmen2again;

public class CarRaceModel {
    private final int[] numbers = {1, 2, 3, 4};
    private final String[] types = {"Blue Car", "Red Car", "Orange Car", "Yellow Car"};
    private final String[] colors = {"Blue", "Red", "Orange", "Yellow"};
    private final double[] startX = {85, 75, 75, 75};
    private final double[] startY = {31, 111, 204, 280};
    private final double[] positions = {0, 0, 0, 0};
    private final double[] speeds = {1, 1, 1, 1};

    public int getCarCount() {
        return numbers.length;
    }

    public int getNumber(int index) {
        return numbers[index];
    }

    public String getType(int index) {
        return types[index];
    }

    public String getColor(int index) {
        return colors[index];
    }

    public double getStartX(int index) {
        return startX[index];
    }

    public double getStartY(int index) {
        return startY[index];
    }

    public double getPosition(int index) {
        return positions[index];
    }

    public double getSpeed(int index) {
        return speeds[index];
    }

    public void moveCar(int index, double distance) {
        positions[index] += distance;
    }

    public void setSpeed(int index, double speed) {
        speeds[index] = speed;
    }

    public void reset() {
        for (int i = 0; i < positions.length; i++) {
            positions[i] = 0;
            speeds[i] = 1;
        }
    }
}
