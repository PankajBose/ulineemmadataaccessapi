package uline.emma.metadataaccess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@RestController
public class MetadataAccess {
    private static Connection connection;
    //private static final Logger LOGGER = LoggerFactory.getLogger(MetadataAccess.class);
    //private static final String welcomeContent = IOUtils.toString(MetadataAccess.class.getClassLoader().getResourceAsStream("welcome-message.html"));

    public static void main(String[] args) {
        init();

        SpringApplication.run(MetadataAccess.class, args);
    }

    @RequestMapping(value = "/", produces = "text/html")
    String welcome() {
        return "Welcome to EMMA metadata access application. Build 2023-03-19 17:03";
    }

    @GetMapping(value = "/search", produces = "application/json")
    public static ResponseData search() throws SQLException {
        ResultSet resultSet = connection.prepareStatement("select \"Subject\", \"ToEmail\", \"FromEmail\", \"Assigned\", \"AssignedDate\", \"Queue\", \"InProcessDate\", \"Handled_Category_Text\", \"Handled_by\", \"TxtHandledDate\" from public.emailitemsarchived\n" +
                "where emailitemsarchived.\"TxtHandledDate\" > '01/05/2023'\n" +
                "order by public.emailitemsarchived.\"TxtHandledDate\"\n" +
                "Limit 1000").executeQuery();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        List<Map<String, Object>> data = new ArrayList<>();
        while (resultSet.next()) {
            Map<String, Object> row = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                Object object = resultSet.getObject(i);
                String columnName = metaData.getColumnName(i);
                row.put(columnName, object);
            }
            data.add(row);
        }

        ResponseData responseData = new ResponseData();
        responseData.setRow(data);
        return responseData;
    }

    private static void init() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://emmapostgresqlserver.postgres.database.azure.com:5432/db_mentoradmin", "emmapgadmin@emmapostgresqlserver", "Uline#2023");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}