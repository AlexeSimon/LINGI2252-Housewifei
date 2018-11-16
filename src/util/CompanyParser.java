package util;

public class CompanyParser {

    public CompanyParser() {
    }

    public String removeComments(String input, String commentSymbol) {
        return input.split(commentSymbol)[0];
    }

    public String removeSpaces(String input) {
        return input.replaceAll("\\s", "");
    }

    public Class<?> recognizeStringClass(String input) {
        try {
            Integer.parseInt(input);
            return int.class;
        } catch (NumberFormatException e) {

        }

        try {
            Float.parseFloat(input);
            return float.class;
        } catch (NumberFormatException e) {

        }
        try {
            String.valueOf(input);
            return String.class;
        } catch (NumberFormatException e) {

        }

        return null;
    }
}
