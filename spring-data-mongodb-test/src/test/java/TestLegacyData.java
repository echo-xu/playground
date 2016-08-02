import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

/**
 * Created by echo on 8/2/16.
 */
public class TestLegacyData {

    static ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    static MongoTemplate mongoTemplate = (MongoTemplate) ctx.getBean("mongoTemplate");
    static String collectionName = "employee";

    @BeforeClass
    public static void init() {
        String json = "{ \"_id\" : 1, \"_class\" : \"Employee\", \"name\" : \"echo\", \"department\" : { \"name\" : \"engineering\" } }";
        mongoTemplate.insert(json, collectionName);
        System.out.println("insert employee");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLegacyData() {
        Query query = new Query();
        Employee employee = mongoTemplate.findOne(query, Employee.class, collectionName);
        System.out.println("_id: " + employee.getId() + ", name: " + employee.getName());
    }

    @AfterClass
    public static void cleanup() {
        mongoTemplate.remove(new Query(), collectionName);
        System.out.println("remove employee");
    }
}
