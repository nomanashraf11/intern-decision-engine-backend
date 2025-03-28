package ee.taltech.inbankbackend.service;

import com.github.vladislavgoltjajev.personalcode.locale.estonia.EstonianPersonalCodeValidator;
import ee.taltech.inbankbackend.config.DecisionEngineConstants;
import ee.taltech.inbankbackend.exceptions.InvalidAgeException;
import ee.taltech.inbankbackend.exceptions.InvalidLoanAmountException;
import ee.taltech.inbankbackend.exceptions.InvalidLoanPeriodException;
import ee.taltech.inbankbackend.exceptions.InvalidPersonalCodeException;
import ee.taltech.inbankbackend.exceptions.NoValidLoanException;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.stereotype.Service;

/**
 * A service class that provides a method for calculating an approved loan amount and period for a customer.
 * The loan amount is calculated based on the customer's credit modifier,
 * which is determined by the last four digits of their ID code.
 */
@Service
public class DecisionEngine {
    private final PersonalCodeValidator personalCodeValidator;
    private final AgeValidator ageValidator;
    private final LoanInputValidator loanInputValidator;
    private final CreditModifierService creditModifierService;
    private final LoanCalculator loanCalculator;

    public DecisionEngine(PersonalCodeValidator personalCodeValidator,
                        AgeValidator ageValidator,
                        LoanInputValidator loanInputValidator,
                        CreditModifierService creditModifierService,
                        LoanCalculator loanCalculator) {
        this.personalCodeValidator = personalCodeValidator;
        this.ageValidator = ageValidator;
        this.loanInputValidator = loanInputValidator;
        this.creditModifierService = creditModifierService;
        this.loanCalculator = loanCalculator;
    }

    public Decision calculateApprovedLoan(String personalCode, Long requestedAmount, int requestedPeriod)
            throws InvalidPersonalCodeException, InvalidLoanAmountException, InvalidLoanPeriodException,
            NoValidLoanException, InvalidAgeException {
        
        try {
            validateInputs(personalCode, requestedAmount, requestedPeriod);
        } catch (Exception e) {
            return new Decision(null, null, e.getMessage());
        }
        
        int creditModifier = creditModifierService.getCreditModifier(personalCode);
        if (creditModifier == 0) {
            throw new NoValidLoanException("No valid loan due to debt!");
        }
        
        double requestedScore = loanCalculator.calculateCreditScore(creditModifier, requestedAmount.intValue(), requestedPeriod);
        if (requestedScore >= 0.1) {
            int maxApprovedAmount = loanCalculator.findMaximumApprovedAmount(creditModifier, requestedPeriod);
            return new Decision(maxApprovedAmount, requestedPeriod, null);
        }
        
        Integer bestPossibleAmount = loanCalculator.findBestPossibleAmount(creditModifier, requestedPeriod);
        if (bestPossibleAmount != null && bestPossibleAmount >= DecisionEngineConstants.MINIMUM_LOAN_AMOUNT) {
            return new Decision(bestPossibleAmount, requestedPeriod, null);
        }
        
        for (int extendedPeriod = requestedPeriod + 1; extendedPeriod <= DecisionEngineConstants.MAXIMUM_LOAN_PERIOD; extendedPeriod++) {
            bestPossibleAmount = loanCalculator.findBestPossibleAmount(creditModifier, extendedPeriod);
            if (bestPossibleAmount != null) {
                return new Decision(bestPossibleAmount, extendedPeriod, null);
            }
        }
        
        throw new NoValidLoanException("No valid loan found!");
    }

    private void validateInputs(String personalCode, Long loanAmount, int loanPeriod) 
            throws InvalidPersonalCodeException, InvalidLoanAmountException, 
                   InvalidLoanPeriodException, InvalidAgeException {
        personalCodeValidator.validate(personalCode);
        int age = personalCodeValidator.calculateAge(personalCode);
        ageValidator.validate(age);
        loanInputValidator.validateLoanAmount(loanAmount);
        loanInputValidator.validateLoanPeriod(loanPeriod);
    }
}