import com.datastax.driver.core.Session;

public class BookRepository {
    private static final String TABLE_NAME = "books";
    private String keySpaceName;
    private Session session;

    public BookRepository(Session session, String keySpaceName) {
        this.session = session;
        this.keySpaceName = keySpaceName;
    }

    public void createTable() {
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append(keySpaceName + "." + TABLE_NAME).append("(")
                .append("id uuid PRIMARY KEY, ")
                .append("title text,")
                .append("subject text);");

        String query = sb.toString();
        session.execute(query);
    }

    public void alterTableBooks(String columnName, String columnType) {
        StringBuilder sb = new StringBuilder("ALTER TABLE ")
                .append(keySpaceName + "." + TABLE_NAME).append(" ADD ")
                .append(columnName).append(" ")
                .append(columnType).append(";");

        String query = sb.toString();
        session.execute(query);
    }

    public void insertbookByTitle(Book book) {
        StringBuilder sb = new StringBuilder("INSERT INTO ")
                .append(keySpaceName + "." + TABLE_NAME).append("(id, title) ")
                .append("VALUES (").append(book.getId())
                .append(", '").append(book.getTitle()).append("');");

        String query = sb.toString();
        session.execute(query);
    }


}
