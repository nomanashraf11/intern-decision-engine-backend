package ee.taltech.inbankbackend.service;

import ee.taltech.inbankbackend.config.DecisionEngineConstants;
import org.springframework.stereotype.Service;

@Service
public class CreditModifierService {
    public int getCreditModifier(String personalCode) {
        switch (personalCode) {
            case "49002010965":
                return 0;  // Debt, no loan approval
            case "49002010976":
                return DecisionEngineConstants.SEGMENT_1_CREDIT_MODIFIER;
            case "49002010987":
                return DecisionEngineConstants.SEGMENT_2_CREDIT_MODIFIER;
            case "49002010998":
                return DecisionEngineConstants.SEGMENT_3_CREDIT_MODIFIER;
            default:
                return 0;
        }
    }
}