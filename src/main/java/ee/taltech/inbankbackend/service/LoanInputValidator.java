package ee.taltech.inbankbackend.service;
import org.springframework.stereotype.Service;

import ee.taltech.inbankbackend.config.DecisionEngineConstants;
import ee.taltech.inbankbackend.exceptions.InvalidLoanAmountException;
import ee.taltech.inbankbackend.exceptions.InvalidLoanPeriodException;

@Service
public class LoanInputValidator {
    public void validateLoanAmount(Long loanAmount) throws InvalidLoanAmountException {
        if (loanAmount < DecisionEngineConstants.MINIMUM_LOAN_AMOUNT || 
            loanAmount > DecisionEngineConstants.MAXIMUM_LOAN_AMOUNT) {
            throw new InvalidLoanAmountException("Invalid loan amount!");
        }
    }
    
    public void validateLoanPeriod(int loanPeriod) throws InvalidLoanPeriodException {
        if (loanPeriod < DecisionEngineConstants.MINIMUM_LOAN_PERIOD || 
            loanPeriod > DecisionEngineConstants.MAXIMUM_LOAN_PERIOD) {
            throw new InvalidLoanPeriodException("Invalid loan period!");
        }
    }
}