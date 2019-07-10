import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        CassandraConnector cassandraConnector = new CassandraConnector();
        cassandraConnector.connect("127.0.0.1", 9042);
        Session session = cassandraConnector.getSession();
        KeyspaceRepository keyspaceRepository = new KeyspaceRepository(session);

        String keyspaceName = "library";
        keyspaceRepository.createKeyspace(keyspaceName, "SimpleStrategy", 1);

        ResultSet result = session.execute("SELECT * FROM system_schema.keyspaces;");

        List<String> matchedKeyspaces = result.all()
                .stream()
                .filter(r -> r.getString(0).equals(keyspaceName.toLowerCase()))
                .map(r -> r.getString(0))
                .collect(Collectors.toList());

        String s = matchedKeyspaces.get(0);
        System.out.println(s);

        BookRepository bookRepository = new BookRepository(session, keyspaceName);
        bookRepository.createTable();

        ResultSet result2 = session.execute("SELECT * FROM " + keyspaceName + ".books;");
        List<String> columnNames =
                result2.getColumnDefinitions().asList().stream()
                        .map(cl -> cl.getName())
                        .collect(Collectors.toList());

        for (String str: columnNames) {
            System.out.println(str);
        }

//        bookRepository.alterTableBooks("descr", "text");
        ResultSet result3 = session.execute("SELECT * FROM " + keyspaceName + ".books;");
        List<String> columnNames2 =
                result3.getColumnDefinitions().asList().stream()
                        .map(cl -> cl.getName())
                        .collect(Collectors.toList());

        for (String str: columnNames2) {
            System.out.println(str);
        }

        Book book = new Book();
        book.setId(UUID.fromString("e325c590-90e5-4f21-8633-30f7bf16227b"));
        book.setTitle("thirdTitle");

        bookRepository.insertbookByTitle(book);

        ResultSet result4 = session.execute("SELECT * FROM " + keyspaceName + ".books;");
        String title = result4.one().getString("title");

        System.out.println(result4.all());

    }
}