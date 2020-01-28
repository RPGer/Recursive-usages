package myToolWindow;

public class MyNode {
    public String bookName;
    public String bookURL;

    public MyNode(String book, String filename) {
        bookName = book;
        bookURL = filename;
    }

    public String toString() {
        return bookName;
    }
}
