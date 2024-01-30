package org.example;

public interface FpeService {
    String sequenceNumberToPseudoRandomCode(int input);
    int pseudoRandomCodeToSequenceNumber(String input);
}
