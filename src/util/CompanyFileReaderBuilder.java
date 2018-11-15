package util;

import java.io.IOException;

public class CompanyFileReaderBuilder {
    private CompanyFileReader reader;

    public CompanyFileReaderBuilder(String filename) throws IOException {
        reader = new CompanyFileReader(filename);
    }

    public CompanyFileReader build() {
        return reader;
    }

    public CompanyFileReaderBuilder setIgnoreEmptyLines(boolean ignoreEmptyLines) {
        reader.setIgnoreEmptyLines(ignoreEmptyLines);
        return this;
    }

    public CompanyFileReaderBuilder setRemoveSpaces(boolean removeSpaces) {
        reader.setRemoveSpaces(removeSpaces);
        return this;
    }

    public CompanyFileReaderBuilder setRemoveComments(boolean removeComments) {
        reader.setRemoveComments(removeComments);
        return this;
    }

    public CompanyFileReaderBuilder setCommentSymbol(String commentSymbol) {
        reader.setCommentSymbol(commentSymbol);
        return this;
    }

}
