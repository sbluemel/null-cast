package hibernate_reproducer.null_cast;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.UUID;
import org.hibernate.dialect.PostgreSQLDialect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class NullCastApplicationTests {

  @Container
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
  }

  @Autowired
  private EntityManager entityManager;

  /**
   * This test demonstrates that the cast of a null value to the wrong 'text' type - instead of 'varchar' - destroys the query optimization.
   * This cast was changed from 'varchar' to 'text' and caused a performance regression when updating from Spring Boot 2.7
   * to 3.0 and therefore from Hibernate 5.6 to 6.1.
   *
   * @see PostgreSQLDialect#castType(int)
   *
   * @see <a href="https://www.postgresql.org/message-id/20050131235516.22DF19A5812%40www.postgresql.com">Postgres BUG #1453: NULLs in UNION
   * query</a>
   * @see <a href="https://www.cybertec-postgresql.com/en/union-all-data-types-performance/">UNION ALL, data types and performance</a>
   */
  @Test
  void castsOfNullValuesDestroysUnionAllOptimization() {
    var uuid = UUID.fromString("a89a2540-5243-45b3-9fab-0aa0e0fa1a04");
    List<Sub> result =
        entityManager.createQuery("select x from Sub x where x.main.objectId='" + uuid + "'", Sub.class).getResultList();
    // This creates the following sql query:
    String query = "select s1_0.id,s1_0.clazz_,s1_0.main_id,s1_0.field_a,s1_0.field_b from (select id, main_id, field_a, cast(null as text) as field_b, 1 as clazz_ from sub_a union all select id, main_id, cast(null as text) as field_a, field_b, 2 as clazz_ from sub_b) s1_0 join (select id, object_id, 1 as clazz_ from main_a union all select id, object_id, 2 as clazz_ from main_b) m1_0 on m1_0.id=s1_0.main_id where m1_0.object_id='a89a2540-5243-45b3-9fab-0aa0e0fa1a04'";
    System.out.println("Not optimized Plan:");
    String badPlan = entityManager.createNativeQuery(
        "explain (format json) " + query,
        String.class).getSingleResult().toString();
    System.out.println(badPlan);
    System.out.println("====================================");
    System.out.println("Optimized Plan (with correct cast):");
    String goodPlan = entityManager.createNativeQuery(
        "explain (format json) " + query.replace("cast(null as text)", "cast(null as varchar)"),
        String.class).getSingleResult().toString();
    System.out.println(goodPlan);
    assertThat(badPlan).contains("Seq Scan");
    assertThat(goodPlan).doesNotContain("Seq Scan");
  }

}
