package org.example;


public class Main {

    public static void main(String[] args) {

        FpeService fpeService = new FpeServiceImpl("EF4359D8D580AA4F7F036D6F04FC6A94", "D8E7920AFA330A73");

        fpeService.pseudoRandomCodeToSequenceNumber(fpeService.sequenceNumberToPseudoRandomCode(1));
        fpeService.pseudoRandomCodeToSequenceNumber(fpeService.sequenceNumberToPseudoRandomCode(2));
        fpeService.pseudoRandomCodeToSequenceNumber(fpeService.sequenceNumberToPseudoRandomCode(31));
        fpeService.pseudoRandomCodeToSequenceNumber(fpeService.sequenceNumberToPseudoRandomCode(32));
        fpeService.pseudoRandomCodeToSequenceNumber(fpeService.sequenceNumberToPseudoRandomCode(1073741823));
    }
}