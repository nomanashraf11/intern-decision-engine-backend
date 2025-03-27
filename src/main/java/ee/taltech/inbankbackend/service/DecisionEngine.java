package ee.taltech.inbankbackend.service;

import com.github.vladislavgoltjajev.personalcode.locale.estonia.EstonianPersonalCodeValidator;
import ee.taltech.inbankbackend.config.DecisionEngineConstants;
import ee.taltech.inbankbackend.exceptions.InvalidLoanAmountException;
import ee.taltech.inbankbackend.exceptions.InvalidLoanPeriodException;
import ee.taltech.inbankbackend.exceptions.InvalidPersonalCodeException;
import ee.taltech.inbankbackend.exceptions.NoValidLoanException;
import org.springframework.stereotype.Service;

/**
 * A service class that provides a method for calculating an approved loan amount and period for a customer.
 * The loan amount is calculated based on the customer's credit modifier,
 * which is determined by the last four digits of their ID code.
 */
@Service
public class DecisionEngine {

    // Used to check for the validity of the presented ID code.
    private final EstonianPersonalCodeValidator validator = new EstonianPersonalCodeValidator();
   private int creditModifier = 0;


    /**
     * Calculates the maximum loan amount and period for the customer based on their ID code,
     * the requested loan amount and the loan period.
     * The loan period must be between 12 and 48 months (inclusive).
     * The loan amount must be between 2000 and 10000€ months (inclusive).
     *
     * @param personalCode ID code of the customer that made the request.
     * @param loanAmount Requested loan amount
     * @param loanPeriod Requested loan period
     * @return A Decision object containing the approved loan amount and period, and an error message (if any)
     * @throws InvalidPersonalCodeException If the provided personal ID code is invalid
     * @throws InvalidLoanAmountException If the requested loan amount is invalid
     * @throws InvalidLoanPeriodException If the requested loan period is invalid
     * @throws NoValidLoanException If there is no valid loan found for the given ID code, loan amount and loan period
     */
public Decision calculateApprovedLoan(String personalCode, Long requestedAmount, int requestedPeriod)
        throws InvalidPersonalCodeException, InvalidLoanAmountException, InvalidLoanPeriodException,
        NoValidLoanException {
    
  try {
            verifyInputs(personalCode, requestedAmount, requestedPeriod);
        } catch (Exception e) {
            return new Decision(null, null, e.getMessage());
        }
    
    this.creditModifier = getCreditModifier(personalCode);
    if (this.creditModifier == 0) {
        throw new NoValidLoanException("No valid loan due to debt!");
    }
    
    // Check if requested amount is acceptable
    double requestedScore = calculateCreditScore(this.creditModifier, requestedAmount.intValue(), requestedPeriod);
    if (requestedScore >= 0.1) {
        int maxApprovedAmount = findMaximumApprovedAmount(this.creditModifier, requestedPeriod);
        return new Decision(maxApprovedAmount, requestedPeriod, null);
    }
    
    // Find best possible loan amount
    Integer bestPossibleAmount = findBestPossibleAmount(this.creditModifier, requestedPeriod);
    if (bestPossibleAmount != null && bestPossibleAmount >= DecisionEngineConstants.MINIMUM_LOAN_AMOUNT) {
        return new Decision(bestPossibleAmount, requestedPeriod, null);
    }
    
    // Try with extended periods
    for (int extendedPeriod = requestedPeriod + 1; extendedPeriod <= DecisionEngineConstants.MAXIMUM_LOAN_PERIOD; extendedPeriod++) {
        bestPossibleAmount = findBestPossibleAmount(this.creditModifier, extendedPeriod);
        if (bestPossibleAmount != null) {
            return new Decision(bestPossibleAmount, extendedPeriod, null);
        }
    }
    
    throw new NoValidLoanException("No valid loan found!");
}
private int findMaximumApprovedAmount(int creditModifier, int period) {
int maxPossible = creditModifier * period;
  return Math.max(DecisionEngineConstants.MINIMUM_LOAN_AMOUNT,
                Math.min(maxPossible, DecisionEngineConstants.MAXIMUM_LOAN_AMOUNT));
}

private Integer findBestPossibleAmount(int creditModifier, int period) {
    int maxPossible = findMaximumApprovedAmount(creditModifier, period);
    
    // Check from maxPossible down to minimum amount
    for (int amount = maxPossible; amount >= DecisionEngineConstants.MINIMUM_LOAN_AMOUNT; amount -= 100) {
        if (calculateCreditScore(creditModifier, amount, period) >= 0.1) {
            return amount;
        }
    }
    return null;
}

private double calculateCreditScore(int creditModifier, int amount, int period) {
return (creditModifier * period) / ((double) amount * 10.0);

}
    /**
     * Calculates the largest valid loan for the current credit modifier and loan period.
     *
     * @return Largest valid loan amount
     */


    /**
     * Calculates the credit modifier of the customer to according to the last four digits of their ID code.
     * Debt - 0000...2499
     * Segment 1 - 2500...4999
     * Segment 2 - 5000...7499
     * Segment 3 - 7500...9999
     *
     * @param personalCode ID code of the customer that made the request.
     * @return Segment to which the customer belongs.
     */
  private int getCreditModifier(String personalCode) {
    // Safely extract last 4 digits

String lastFourStr = personalCode.substring(personalCode.length() - 4);

    int lastFourDigits = Integer.parseInt(lastFourStr);
    
    System.out.println("Last 4 digits: " + lastFourDigits + ", Segment: " + 
    (lastFourDigits < 2500 ? "Debt" : 
     lastFourDigits < 5000 ? "Segment 1" :
     lastFourDigits < 7500 ? "Segment 2" : "Segment 3"));

    if (lastFourDigits < DecisionEngineConstants.DEBT_SEGMENT_BOUND) {
        return 0;  // Debt
    } else if (lastFourDigits < DecisionEngineConstants.SEGMENT_1_BOUND) {
        return DecisionEngineConstants.SEGMENT_1_CREDIT_MODIFIER;
    } else if (lastFourDigits < DecisionEngineConstants.SEGMENT_2_BOUND) {
        return DecisionEngineConstants.SEGMENT_2_CREDIT_MODIFIER;
    } else {
        return DecisionEngineConstants.SEGMENT_3_CREDIT_MODIFIER;
    }
}
    /**
     * Verify that all inputs are valid according to business rules.
     * If inputs are invalid, then throws corresponding exceptions.
     *
     * @param personalCode Provided personal ID code
     * @param loanAmount Requested loan amount
     * @param loanPeriod Requested loan period
     * @throws InvalidPersonalCodeException If the provided personal ID code is invalid
     * @throws InvalidLoanAmountException If the requested loan amount is invalid
     * @throws InvalidLoanPeriodException If the requested loan period is invalid
     */
    private void verifyInputs(String personalCode, Long loanAmount, int loanPeriod)
        throws InvalidPersonalCodeException, InvalidLoanAmountException, InvalidLoanPeriodException {


   if (!(DecisionEngineConstants.MINIMUM_LOAN_PERIOD <= loanPeriod)
                || !(loanPeriod <= DecisionEngineConstants.MAXIMUM_LOAN_PERIOD)) {
            throw new InvalidLoanPeriodException("Invalid loan period!");
        }
    if (!validator.isValid(personalCode)) {
        throw new InvalidPersonalCodeException("Invalid personal ID code!");
    }

    if (loanAmount < DecisionEngineConstants.MINIMUM_LOAN_AMOUNT || loanAmount > DecisionEngineConstants.MAXIMUM_LOAN_AMOUNT) {
        throw new InvalidLoanAmountException("Invalid loan amount!");
    }

   
  
}
    
}
