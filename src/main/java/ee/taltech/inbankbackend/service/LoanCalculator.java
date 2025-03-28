package ee.taltech.inbankbackend.service;
import org.springframework.stereotype.Service;

import ee.taltech.inbankbackend.config.DecisionEngineConstants;

@Service
public class LoanCalculator {
    public int findMaximumApprovedAmount(int creditModifier, int period) {
        int maxPossible = creditModifier * period;
        return Math.max(DecisionEngineConstants.MINIMUM_LOAN_AMOUNT,
                Math.min(maxPossible, DecisionEngineConstants.MAXIMUM_LOAN_AMOUNT));
    }
    
    public Integer findBestPossibleAmount(int creditModifier, int period) {
        int maxPossible = findMaximumApprovedAmount(creditModifier, period);
        
        for (int amount = maxPossible; amount >= DecisionEngineConstants.MINIMUM_LOAN_AMOUNT; amount -= 100) {
            if (calculateCreditScore(creditModifier, amount, period) >= 0.1) {
                return amount;
            }
        }
        return null;
    }
    
    public double calculateCreditScore(int creditModifier, int amount, int period) {
        return (creditModifier * period) / ((double) amount * 10.0);
    }
}