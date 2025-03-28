package ee.taltech.inbankbackend.config;

/**
 * Holds all necessary constants for the decision engine.
 */
public class DecisionEngineConstants {
    // Loan Amount Constraints
    public static final Integer MINIMUM_LOAN_AMOUNT = 2000;  // €2000
    public static final Integer MAXIMUM_LOAN_AMOUNT = 10000; // €10000

    // Loan Period Constraints
    public static final Integer MINIMUM_LOAN_PERIOD = 12;  
    public static final Integer MAXIMUM_LOAN_PERIOD = 48;   

    
    public static final Integer DEBT_SEGMENT_BOUND = 2500; 
    public static final Integer SEGMENT_1_BOUND = 5000;     
    public static final Integer SEGMENT_2_BOUND = 7500;      
                                                            

    // Credit Modifiers
    public static final Integer SEGMENT_1_CREDIT_MODIFIER = 100;
    public static final Integer SEGMENT_2_CREDIT_MODIFIER = 300;
    public static final Integer SEGMENT_3_CREDIT_MODIFIER = 1000;

    public static final Integer MINIMUM_AGE_PERIOD = 18;
    public static final Integer BALTIC_LIFETIME_PERIOD = 76;
}