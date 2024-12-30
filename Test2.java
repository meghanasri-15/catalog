import org.json.JSONObject;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Test2 {
    public static void main(String[] args) throws Exception {
        // Step 1: Read JSON file
        String content = new String(Files.readAllBytes(Paths.get("testcase.json")));
        JSONObject json = new JSONObject(content);

        // Extract roots and base-encoded values
        List<Double> xValues = new ArrayList<>();
        List<BigInteger> yValues = new ArrayList<>();

        for (String key : json.keySet()) {
            double x = Double.parseDouble(key.substring(1)); // Extract x
            JSONObject valueObj = json.getJSONObject(key);
            int base = valueObj.getInt("base");
            String value = valueObj.getString("value");

            // Decode y
            BigInteger y = new BigInteger(value, base);
            xValues.add(x);
            yValues.add(y);
        }

        // Step 2: Determine degree of the polynomial (m = k - 1)
        int k = xValues.size();
        int m = k - 1;

        // Step 3: Solve for coefficients using Lagrange Interpolation
        BigInteger constantTerm = calculateConstantTerm(xValues, yValues);

        // Step 4: Print the constant term
        System.out.println("The constant term (c) is: " + constantTerm);
    }

    private static BigInteger calculateConstantTerm(List<Double> xValues, List<BigInteger> yValues) {
        BigInteger constant = BigInteger.ZERO;

        for (int i = 0; i < xValues.size(); i++) {
            double x_i = xValues.get(i);
            BigInteger y_i = yValues.get(i);

            BigInteger numerator = BigInteger.ONE;
            BigInteger denominator = BigInteger.ONE;

            for (int j = 0; j < xValues.size(); j++) {
                if (i != j) {
                    numerator = numerator.multiply(BigInteger.valueOf(Math.round(-xValues.get(j))));
                    denominator = denominator.multiply(BigInteger.valueOf(Math.round(x_i - xValues.get(j))));
                }
            }

            BigInteger term = y_i.multiply(numerator).divide(denominator);
            constant = constant.add(term);
        }

        return constant;
    }
}