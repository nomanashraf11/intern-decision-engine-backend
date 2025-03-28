# Decision Engine

This project implements a decision engine for loan approval based on a user's personal code, loan amount, and period. It returns an approved loan amount and period by considering a credit modifier extracted from the last four digits of the personal code.

## Good Implementation

- **Input Validation**: Ensures the correctness of personal code, loan amount, and loan period.
- **Clear Structure**: The code is well-documented with meaningful comments and method descriptions for easy understanding.
- **Constants Management**: A dedicated `DecisionEngineConstants` class stores constants, avoiding hardcoded values and reducing the risk of errors.
- **Task Separation**: Logic is divided into smaller methods like `verifyInputs`, `getCreditModifier`, and `highestValidLoanAmount`, making the code modular and easier to test.
- **Custom Exceptions**: Error-specific custom exceptions provide clear feedback on what went wrong.
- **Unit Tests**: Unit tests are included to validate the functionality of the code.

## Areas for Improvement

### 1. Simplifying Decision Logic

The current decision logic is complex and could be optimized for better readability and performance. By directly applying the formula to determine the loan period instead of using a loop, the logic would become simpler and faster.

### 2. Simplifying Validation Logic

The validation checks for the loan amount can be simplified. Reducing the complexity of the conditions will make the code more readable and easier to maintain.

### 3. Confusing Loop Logic

The loop currently modifies the `loanPeriod` parameter, which results in losing the original requested period. Additionally, the loop does not check if the `loanPeriod` exceeds the maximum allowed value, which can cause performance issues, especially when the `creditModifier` is small.

### 4. Better Error Messages

Error messages should be more descriptive. Instead of a generic message like "No valid loan found!", providing specific feedback like "Loan rejected due to existing debt" or "Loan request cannot be processed due to insufficient credit" would help users understand exactly why their loan was rejected.

### 5. Apply SOLID Principles

Refactoring the `DecisionEngine` class to follow SOLID principles would enhance its modularity, maintainability, and testability. Separating concerns like validation logic and credit modifier calculations into their own classes will improve the overall design.

### 6. Reduce Loan Period Slider Step Size

The loan period slider on the frontend should allow for smaller step sizes, offering users greater flexibility and precision when selecting the loan period. This will improve the overall user experience by providing finer control over the loan period.

### 7. Enhance Code Reusability

Refactoring the loan approval logic into reusable services or modules will help make the code more maintainable and reduce duplication. This will also make it easier to reuse the code in different parts of the application.

### 8. Optimize Memory Usage

Optimizing memory usage is crucial, especially when the system handles large datasets or multiple requests. Reviewing and optimizing the data structures used in the decision engine will improve both memory efficiency and performance.

### 9. Improve Logging and Monitoring

More detailed logging and monitoring should be added to track the decision-making process, identify performance bottlenecks, and assist with debugging. Enhanced logs can provide valuable insights into system performance and errors, making it easier to resolve issues.

### 10. Handle Edge Cases

Edge cases, such as invalid input formats, empty values, or extreme loan amounts, should be properly handled to ensure the decision engine operates correctly in all scenarios. Addressing these cases will make the system more robust and reliable.

## Shortcomings and Issues

### 1. **API 404 Error**

- **Issue**: The API was returning a 404 error on the initial API hit. This was unclear because the route that was being hit did not exist.
- **Resolution**: Investigate the API routing configuration to ensure proper route handling and fix the incorrect route.

### 2. **Load Period**

- **Issue**: The load period was set incorrectly. According to the requirements, the load period should range from 12 to 48, but the upper bound was mistakenly set to 60. This mismatch was also present in the tests, which caused failures after the issue was identified.
- **Resolution**: Adjust the load period logic to match the correct range of 12 to 48, and update the test cases to reflect this change.

### 3. ** FLUTTER Mobile App Bugs**

- **Issue**: The mobile app had multiple bugs:
  - The load amount returned by the API was not correctly assigned to the UI, and it was displaying values based on the range slider instead.
  - The load period was displayed incorrectly as "6 to 60" instead of the correct range.
- **Resolution**: Fix the bug where the correct API response is assigned to the load amount on the mobile screen and ensure the load period is displayed properly.

### 4. **Unclear Decision Logic**

- **Issue**: The Decision Engine’s logic is unclear and does not align with the stated requirements. It finds the earliest period where any valid loan amount greater than `MINIMUM_LOAN_AMOUNT` is possible and then gives the maximum amount for that period. However, the decision should be based on different parameters.
- **Resolution**: Review and refactor the decision logic to ensure it aligns with the specified requirements. Consider revising the approach for selecting loan amounts and periods.

### 5. **Ignores Requested Loan Amount**

- **Issue**: After the requested loan amount is validated, it is not used in the final decision. Instead, the decision is based on the credit modifier, loan period, and minimum loan amount.
- **Resolution**: Adjust the decision-making process to incorporate the requested loan amount as a factor in the final outcome.

### 5. **Range Of last 4 digits of Personal Id code **

- **Issue**: The initial code has range calculator for last 4 digits of the personal id but in assessment instructions their was pointed out to use 4 fixed personal ids for now
- **Resolution**: I have adjusted the code for now for these hardcoded personal ids
