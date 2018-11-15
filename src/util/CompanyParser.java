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

}
