package util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class CompanyFileReader extends RandomAccessFile {

    private boolean ignoreEmptyLines;

    private boolean removeSpaces;

    private boolean removeComments;

    private String commentSymbol;

    public CompanyFileReader(String filename) throws FileNotFoundException {
        super(filename, "r");

        ignoreEmptyLines = false;
        removeSpaces = false;
        removeComments = false;
        commentSymbol = null;
    }

    public boolean isIgnoreEmptyLines() {
        return ignoreEmptyLines;
    }

    public void setIgnoreEmptyLines(boolean ignoreEmptyLines) {
        this.ignoreEmptyLines = ignoreEmptyLines;
    }

    public boolean isRemoveSpaces() {
        return removeSpaces;
    }

    public void setRemoveSpaces(boolean removeSpaces) {
        this.removeSpaces = removeSpaces;
    }

    public boolean isRemoveComments() {
        return removeComments;
    }

    public void setRemoveComments(boolean removeComments) {
        this.removeComments = removeComments;
    }

    public String getCommentSymbol() {
        return commentSymbol;
    }

    public void setCommentSymbol(String commentSymbol) {
        this.commentSymbol = commentSymbol;
    }

    public int countAllLines() throws IOException {
        long pointer = getFilePointer();
        seek(0);
        int count = 0;
        while (readLine() != null)
            count++;
        seek(pointer);
        return count;
    }

    public int countUsefulLines() throws IOException {
        long pointer = getFilePointer();
        seek(0);
        int count = 0;
        while (smartReadLine() != null)
            count++;
        seek(pointer);
        return count;
    }

    public void goToStartOfFile() throws IOException {
        seek(0);
    }

    public String smartReadLine() throws IOException {
        String line;
        CompanyParser parser = new CompanyParser();
        do {
            line = readLine();
            if (line == null)
                return line;
            if(removeComments)
                line = parser.removeComments(line, commentSymbol);
            if(removeSpaces)
                line = parser.removeSpaces(line);
        } while(ignoreEmptyLines && line.length() == 0);
        return line;
    }







}
