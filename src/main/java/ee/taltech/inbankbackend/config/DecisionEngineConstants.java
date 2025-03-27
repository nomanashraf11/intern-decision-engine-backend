package ee.taltech.inbankbackend.config;

/**
 * Holds all necessary constants for the decision engine.
 */
public class DecisionEngineConstants {
    // Loan Amount Constraints
    public static final Integer MINIMUM_LOAN_AMOUNT = 2000;  // €2000
    public static final Integer MAXIMUM_LOAN_AMOUNT = 10000; // €10000

    // Loan Period Constraints
    public static final Integer MINIMUM_LOAN_PERIOD = 12;    // 12 months
    public static final Integer MAXIMUM_LOAN_PERIOD = 48;    // 48 months

    // Credit Segmentation Bounds (Last 4 digits of personal code)
    public static final Integer DEBT_SEGMENT_BOUND = 2500;   // 0000-2499 = Debt
    public static final Integer SEGMENT_1_BOUND = 5000;      // 2500-4999 = Segment 1
    public static final Integer SEGMENT_2_BOUND = 7500;      // 5000-7499 = Segment 2
                                                            // 7500-9999 = Segment 3

    // Credit Modifiers
    public static final Integer SEGMENT_1_CREDIT_MODIFIER = 100;
    public static final Integer SEGMENT_2_CREDIT_MODIFIER = 300;
    public static final Integer SEGMENT_3_CREDIT_MODIFIER = 1000;
}