package main.service;

public class PredictionTimeExpiredException extends Exception {
    public PredictionTimeExpiredException(String message) {
        super(message);
    }
}
