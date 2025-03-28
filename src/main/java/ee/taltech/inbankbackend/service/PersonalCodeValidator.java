package ee.taltech.inbankbackend.service;

import org.springframework.stereotype.Service;  // Add this import

import com.github.vladislavgoltjajev.personalcode.locale.estonia.EstonianPersonalCodeValidator;

import ee.taltech.inbankbackend.exceptions.InvalidPersonalCodeException;

import java.time.LocalDate;
import java.time.Period;

@Service
public class PersonalCodeValidator {
    private final EstonianPersonalCodeValidator validator = new EstonianPersonalCodeValidator();
    
    public void validate(String personalCode) throws InvalidPersonalCodeException {
        if (!validator.isValid(personalCode)) {
            throw new InvalidPersonalCodeException("Invalid personal ID code!");
        }
    }
    
    public int calculateAge(String personalCode) {
        int firstDigit = Character.getNumericValue(personalCode.charAt(0));
        int yearPrefix;
        if (firstDigit == 1 || firstDigit == 2) yearPrefix = 1800;
        else if (firstDigit == 3 || firstDigit == 4) yearPrefix = 1900;
        else if (firstDigit == 5 || firstDigit == 6) yearPrefix = 2000;
        else if (firstDigit == 7 || firstDigit == 8) yearPrefix = 2100;
        else throw new IllegalArgumentException("Invalid century in personal code");

        int year = yearPrefix + Integer.parseInt(personalCode.substring(1, 3));
        int month = Integer.parseInt(personalCode.substring(3, 5));
        int day = Integer.parseInt(personalCode.substring(5, 7));

        LocalDate birthDate = LocalDate.of(year, month, day);
        LocalDate today = LocalDate.now();

        return Period.between(birthDate, today).getYears();
    }
}