package ee.taltech.inbankbackend.service;

import org.springframework.stereotype.Service;

import ee.taltech.inbankbackend.config.DecisionEngineConstants;
import ee.taltech.inbankbackend.exceptions.InvalidAgeException;

@Service
public class AgeValidator {
    public void validate(int age) throws InvalidAgeException {
        if (age < DecisionEngineConstants.MINIMUM_AGE_PERIOD) {
            throw new InvalidAgeException("Loan rejected: You must be at least 18 years old to apply.");
        }
        if (age > (DecisionEngineConstants.BALTIC_LIFETIME_PERIOD - (DecisionEngineConstants.MINIMUM_LOAN_PERIOD / 12))) {
            throw new InvalidAgeException("Loan rejected: Your age exceeds the maximum eligible age.");
        }
    }
}