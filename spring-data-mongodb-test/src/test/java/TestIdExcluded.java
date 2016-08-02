import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * Created by echo on 8/1/16.
 */
public class TestIdExcluded {

    static ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    static MongoTemplate mongoTemplate = (MongoTemplate) ctx.getBean("mongoTemplate");
    static String collectionName = "employee";

    @BeforeClass
    public static void init() {
        Employee employee = new Employee();
        employee.setId(101);
        employee.setName("echo");
        Department department = new Department();
        department.setId(400);
        department.setName("engineering");

        employee.setDepartment(department);

        mongoTemplate.insert(employee, collectionName);

        System.out.println("insert employee");

    }

    @Test(expected = IllegalArgumentException.class)
    public void testIdExcluded() {
        Query query = new Query();
        query.fields().exclude("_id");
        Employee employee = mongoTemplate.findOne(query, Employee.class, collectionName);

        System.out.println("_id: " + employee.getId() + ", name: " + employee.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInnerIdNotIncluded() {

        Query query = new Query();
        query.fields().include("department.name");
        Employee employee = mongoTemplate.findOne(query, Employee.class, collectionName);

        System.out.println("_id: " + employee.getId() + ", name: " + employee.getName());

    }

    @AfterClass
    public static void cleanup() {
        mongoTemplate.remove(new Query(), collectionName);
        System.out.println("remove employee");
    }

}


class Employee {
    private long id;
    private String name;
    private Department department;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}

class Department {

    private long id;
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
