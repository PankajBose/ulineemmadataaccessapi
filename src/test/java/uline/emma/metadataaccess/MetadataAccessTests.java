package uline.emma.metadataaccess;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

@SpringBootTest
class MetadataAccessTests {
    @Test
    void contextLoads() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection("jdbc:postgresql://emmapostgresqlserver.postgres.database.azure.com:5432/db_mentoradmin", "emmapgadmin@emmapostgresqlserver", "Uline#2023");
        ResultSet resultSet = connection.prepareStatement("select \"Subject\", \"ToEmail\", \"FromEmail\", \"Assigned\", \"AssignedDate\", \"Queue\", \"InProcessDate\", \"Handled_Category_Text\", \"Handled_by\", \"TxtHandledDate\" from public.emailitemsarchived\n" +
                "where emailitemsarchived.\"TxtHandledDate\" > '01/05/2023'\n" +
                "order by public.emailitemsarchived.\"TxtHandledDate\"\n" +
                "Limit 1000").executeQuery();
        int columnCount = 10;
        while (resultSet.next()) {
            for (int i = 1; i <= columnCount; i++) {
                Object object = resultSet.getObject(i);
                System.out.println("object = " + object);
            }
        }
    }

    @Test
    void add() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://emmadataaccessdemo.azurewebsites.net/add");
        httpPost.addHeader("content-type", "application/json");
        HttpResponse httpResponse = httpClient.execute(httpPost);
        Scanner sc = new Scanner(httpResponse.getEntity().getContent());

        //Printing the status line
        System.out.println(httpResponse.getStatusLine());
        while (sc.hasNext()) {
            System.out.println(sc.nextLine());
        }
    }
}