package com.esprit.tn.pidev.services;

import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;


public class SmsService {
    // Twilio credentials
    private static final String ACCOUNT_SID = "ACdca9a7abfbad8043980be8392f2d6c7e";
    private static final String AUTH_TOKEN = "defc37fab5db3063a44f1db7606619bd";
    private static final String TWILIO_PHONE_NUMBER = "+14582248723"; // Removed trailing space

    public SmsService() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public void sendSms(String toPhoneNumber, String messageBody) {
        // Input validation
        if (toPhoneNumber == null || toPhoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Le numéro de téléphone du destinataire est requis.");
        }
        if (!toPhoneNumber.matches("^\\+[1-9]\\d{1,14}$")) {
            throw new IllegalArgumentException("Numéro invalide : doit être au format E.164 (ex. +1234567890), reçu : '" + toPhoneNumber + "'");
        }
        if (messageBody == null || messageBody.trim().isEmpty()) {
            throw new IllegalArgumentException("Le corps du message ne peut pas être vide.");
        }

        try {
            Message message = Message.creator(
                            new PhoneNumber(toPhoneNumber),
                            new PhoneNumber(TWILIO_PHONE_NUMBER),
                            messageBody)
                    .create();
            System.out.println("SMS envoyé à " + toPhoneNumber + ": " + message.getSid());
        } catch (ApiException e) {
            System.err.println("Erreur lors de l'envoi du SMS : " + e.getMessage());
            throw e; // Let the caller handle the exception
        }
    }
}