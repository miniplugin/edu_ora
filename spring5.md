# 스프링 5 프로그래밍 입문 학습 요약

---
- [학습목차](https://github.com/miniplugin/human)
- 초보 웹 개발자를 위한 스프링 5 프로그래밍 입문, 최범균 저
---

## ch 02 스프링 시작하기

org.springframework.spring-context

#### mvn
```bash
$ mvn compile
```

#### Container & Bean
Role|Spring
---|---
의존 역전 컨테이너|BeanFactory, ApplicationContext
객체 조립 공식 제공|Bean

**스프링의 Bean 제공 방법**
- `AnnotationConfigApplicationContext`
- `GenericXmlApplicationContext`
- `GenericGroovyApplicationContext`

#### 예제

```java
@Configuration
public class AppContext {
    @Bean
    public Greeter greeter() {
        Greeter g = new Greeter();
        g.setFormat("%s, 안녕하세요?");
        return g;
    }
}
```

```
+--------------------+
| AppContext         |
+--------------------+
| greeter(): Greeter |
+--------------------+
           ↑ 객체 조립 공식 요청
+--------------------+
| AnnotationConfig.. |
+--------------------+
    ↑ 객체 요청
+------+   call   +---------+
| Main | -------> | Greeter |
+------+          +---------+
```

---

## ch 03 스프링 DI
DI를 하는 이유는 변경의 유연함때문이다.

#### 조립기를 이용한 DI
- 조립기를 new up하면 하위 객체들도 모두 생성된다. (조립기 = 여러 객체를 담고 있는 일종의 컨테이너)
- 조립기를 사용해도 의존의 의존을 쉽게 교체할 수 있다.

```java
public class Assembler {
    public Assembler() {
        memberDao = new MemberDao();
        regSvc = new MemberRegisterService(memberDao);
        pwdSvc = new ChangePasswordService();
        pwdSvc.setMemberDao(memberDao);
    }
}
```

#### 스프링 설정을 이용한 DI

스프링 컨테이너(`ApplicationContext`)도 결국은 객체를 담고 있는 컨테이너이며, 애플리케이션 부트 시점에 `@Configuration`, `@Bean` 등의 설정이 붙은 클래스를 객체를 자동 생성한다는 점만 조립기와 다름 

- 설정용 클래스를 만들다 (e.g. `AppConf.java`).
- 클래스 선언에 `@Configuration` annotation을 붙여 설정임을 명시
- `@Configuration` 선언한 클래스의 각 메서드에 `@Bean` annotation을 붙여 Bean임을 명시
- 스프링 컨테이너를 new up하고 `getBean()` 함수를 호출하여 Bean으로 조립 공식을 명시했던 객체를 구한다.

```java
ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConf.class);
Foo foo = ctx.getBean("foo", Foo.class);
// getBean(name: String, type: String)
```

- 생성자 주입 vs 세터(setter) 주입
- 스프링 컨테이너는 기본적으로 싱글톤으로 객체를 생성한다. `getBean()` 함수를 호출해 객체를 여러번 구하더라도 반환되는 객체는 호출할 때마다 다른 불변 객체가 아니라, 최초 한번 생성된 객체가 계속 반환된다.
- `@Autowired` annotation을 의존 주입 대상에 붙이면 스프링 설정 클래스(e.g. `AppConf.java`)의 `@Bean` 메서드에서 의존 주입을 하지 않아도 자동 주입됨
- 항상 스프링 DI, 즉 `@Bean`을 이용해서 의존 주입을 해야 하나? NO.

--- 

## ch04 의존 자동 주입

- 의존 자동 주입이란? `@Autowired`
- `@Autowired` 적용 가능 위치는? Field, Setter method
- 의존 자동 주입을 했는데, 일치하는 Bean이 선언되어 있지 않은 경우? `NoSuchBeanDefinitionException`
- Bean이 중복 선언되었고, 스프링이 하나를 선택할 수 없을 때? `NoUniqueBeanDefinitionException`
- 스프링에게 힌트 제공하기 `@Qualifier`
```java
@Configuration
public class AppCtx {
    @Bean
    @Qualifier("foo")
    public Foo foo() {}    
}

public class SomeService {
    @Autowired
    @Qualifier("foo")
    public void funcRequiresFoo() {}    
}
```
- `@Qualifier` 적용 가능 위치는? Field, Setter method
- 상속 그래프에 포함된 자식 객체일 때, 정확한 객체를 주입 받으려면?
    - `@Qualifier`를 이용해서 Bean 이름을 명시하거나
    - `@Autowired`선언된 필드 또는 함수의 매개 변수 타입을 하위 타입으로 명시
- 의존 자동 주입이 되지 않아도 작동하는 로직일 때는? 
    - `@Autowired(required = false)` 하거나
    - 필드 또는 매개 변수의 타입을 `Optional<Foo> foo`로 선언하거나
    - 필드 또는 매개 변수의 타입을 `@Nullable`로 선언
- `@Autowired(required = false)`는 Setter 함수가 호출되지 않는 반면, Optional, Nullable은 호출됨 유의
- 생성자에서 `@Autowired` 선언된 필드를 초기화했다면, 스프링이 의존 자동 주입을 다시 시도하므로 초기화된 필드값은 덮어써짐 유의

> 자동 주입을 하는 코드와 수동으로 주입하는 코드가 섞여 있으면 주입을 제대로 하지 않아서 `NullPointerException`이 발생했을 때 원인을 찾는데 오랜 시간이 걸릴 수 있다. 의존 자동 주입을 사용한다면 일관되게 사용해야 이런 문제가 줄어든다. 의존 자동 주입을 사용하고 있다면 일부 자동 주입을 적용하기 어려운 코드를 제외한 나머지 코드는 의존 자동 주입을 사용하자. 

---

## ch 05 컴포넌트 스캔

- 스프링이 직접 클래스를 검색해서 Bean으로 등록해 주는 기능
    - 자동 Bean 등록할 클래스 선언 바로 위에 `@Component` annotation 추가
    - 설정 클래스 선언 바로 위에 `@ComponentScan(basePackage = {"target package 1","target package 2"})` 추가
- 별칭을 부여하려면? `@Component("foo")`
- 별칭을 부여하지 않으면? 클래스 이름을 camelCase로 바꿔 Bean 이름으로 사용함. e.g. `class MemberDao` -> `memberDao`
- 스캔 대상에서 제외하려면?
```java
@Configuration
@ComponentScan(basePackage = {"foo"}, excludeFilters = @Filter(type = FilterType.REGEX, pattern = "foo\\..*Dao"))
public class AppCtx { }
```
- 다른 필터?
    - `excludeFilters = @Filter(type = FilterType.ASPECTJ, pattern = "foo.*Dao")`
    - `excludeFilters = @Filter(type = FilterType.ANNOTATION, classes = {NoProduct.class, ManualBean.class})`
    - `excludeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = MemberDao.class)` 자신 및 하위 타입 제외
- 다른 스캔 대상? `@Controller`, `@Service`, `@Repository`, `@Aspect`, `@Controller`
- Bean 이름 충돌?
    - 자동 스캔 과정에 충돌이 발생하면, 둘 중에 하나에 이름을 명시
    - 수동 등록한 Bean과 자동 스캔한 Bean 이름이 충돌하면, 수동 등록한 Bean 우선 사용

---

## ch 06 빈 라이프사이클과 범위
```
컨테이너 초기화                               컨테이너 종료
<------------------------------------>    <--------> 
+--------+    +--------+    +--------+    +--------+ 
| 객체생성 | -> | 의존설정 | -> |  초기화  | -> |  소멸   |  
+--------+    +--------+    +--------+    +--------+
```
```java
// 컨테이너 초기화
ApplicationContext ctx = new AnnotationConfigApplicationContext(AppCtx.class);
foo = ctx.getBean("foo", Foo.class);
// 컨테이너 종료
ctx.close();
```
- 컨테이너가 초기화되면 컨테이너를 사용할 수 있다. 컨테이너를 사용한다는 것은 `getBean()`가 같은 메서드를 이용해서 컨테이너에 보관된 Bean 객체를 구한다는 것을 뜻한다.
- Bean 객체의 생성과 소멸 시점에 추가적인 로직을 실행하고 싶다면, `InitializingBean`, `DisposableBean` 인터페이스를 구현한다. e.g. 데이터베이스 연결을 끊는다. 채팅을 위한 Tcp 커넥션을 끊는다.
```java
public class AClient implements InitializingBean, DisposableBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        // Hook here...
    }
    
    @Override
    public void destroy() throws Exception {
        // Hook here...
    }
}
```
- 인터페이스를 구현하지 않고 커스텀 초기화 및 종료를 위한 메서드를 직접 정의하려면? `@Bean(initMethod = "..", destroyMethod = "..")`
```java
@Configuration
public class AppCtx {
    @Bean(initMethod = "connect", destroyMethod = "close")
    public ExClient client() {
        return new ExClient();
    }    
}
```
- Bean 객체의 라이프를 관리하려면? `@Scope("prototype")`
```java
@Configuration
public class AppCtx {
    @Bean
    @Scope("prototype")
    public AClient client() {
        return new AClient();
    }    
}
```
> `주의` 프로토타입 범위의 Bean은 컨테이너의 완전한 라이프사이클을 따르지 않으므로, 직접 소멸 처리해줘야 함.

---

## ch07 AOP 프로그래밍
- `pom.xml`에 `aspectjweaver` 의존 모듈 추가 필요
- AOP, Aspect Oriented Programming? 여러 객체에 공통으로 적용할 수 있는 기능을 분리해서 재사용성을 높여주는 프로그래밍 기법. 핵심 기능(비즈니스 로직)에 공통 기능(로깅, 보안, 캐싱 등)을 삽입하는 것이 요체.
- 기본 설정된 스프링에서는 "런타임에 프록시 객체를 생성해 공통 기능을 삽입하는 방법"만 제공
- Aspect? 공통 기능

용어|의미
---|---
Advice|"언제" 적용할 것인가? e.g. 메서드 호출전에
JoinPoint|Advice를 적용 가능한 지점. e.g. 메서드 호출
Pointcut|Advice가 적용되는 JoinPoint. 정규표현식 또는 AspectJ 문법 이용
Weaving|Advice를 적용하는 행위
Aspect|여러 객체에 적용되는 공통 기능. e.g. 트랜잭션 처리 등

- Advice의 종류?

종류|설명
---|---
Before|메서드 호출 전
After Returning|예외 없이 메서드 실행된 후
After Throwing|예외 발생했을 때
After|메서드 실행 후(like finally)
Around|메서드 실행 전, 후를 데코레이트

- 적용법
    - Aspect로 사용할 클래스에 `@Aspect` 선언
    - 공통 기능을 적용할 `@Pointcut` 선언
    - 공통 기능 구현 메서드에 `@Around` 선언
    - 스프링 설정 클래스에 `@EnableAspectJAutoProxy` 선언하고, Aspect를 Bean으로 등록
```java
@Aspect
public class ExeTimeAspect {
    @Pointcut("execution(public * package..factorial(..))") // 패턴에 맞는 메서드를 호출하면 데코레이트
    private void measureTarget() { }
    
    @Around("measureTarget()")
    public Object measure(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Object result = joinPoint.proceed();
        } finally {
            Signature sig = joinPoint.getSignature();
            // sig.getName(); // e.g. factorial
            // joinPoint.getTarget().getClass().getSimpleName(); // e.g. RecCalculator  
            // Arrays.toString(joinPoint.getArgs()); // e.g. [5]
        }
    }
}

@Configuration
@EnableAspectJAutoProxy
public class AppCtx {
    @Bean
    public ExeTimeAspect exeTimeAspect() {
        return new ExeTimeAspect();
    }
}
```
- Main 함수에서 `@Pointcut` 패턴과 일치하는 객체의 함수(e.g. `RecCalculator#factorial()`)를 실행하면, Aspect가 작동함. 이때 객체의 타입은 `com.sun.proxy.$Proxy17`과 같은 임시 생성 타입임. 즉, 런타임에 `Calculator`를 구현한 `$Proxy17` 객체를 생성하고 `RecCalculator`를 랩핑한 것임.
```
+---------------+       +---------------+
| <<interface>> | <|--- | RecCalculator |
| Calculator    |       +---------------+
+---------------+       +---------------+
| + factorial() | <|--- | $Proxy17      |
+---------------+       +---------------+
```
- 인터페이스를 사용하지 않으려면? `@EnableAspectAutoProxy(proxyTargetClass = true)`
```java
@Configuration
@EnableAspectAutoProxy(proxyTargetClass = true)
public class AppCtx { }
```
- `@Pointcut("execution(???)")` 용법
    - `*` 와일드 카드 e.g. `set*`, `get*(*, *)`
    - `..` 0개 이상 e.g. `root..*` root 패키지 및 하위의 모든 패키지
```
execution({visibility} {return type} {class name pattern} {method name pattern}(param pattern))
```
- Advice 적용 순서? `@Order(Integer)` 선언
```java
@Aspect
@Order(1)
public class FooAspect {}

@Aspect
@Order(2)
public class BarAspect {}
```
- `@Around`에 execution 표현식을 직접 선언할 수도 있음. e.g. `@Around("execution(public * root..*(..))")`
- `@Pointcut` 재사용
```java
public class CommonPointcut {
    @Pointcut("execution(public * root..*(..))")
    public void commonTarget() { }
}

@Aspect
public class FooAspect {
    @Around("CommonPointcut.commonTarget()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable { }
}
```

---

## ch 08 DB 연동
- `pom.xml`에 `spring-jdbc`, `tomcat-jdbc`, `mysql-connector-java` 의존 모듈 추가 필요
- 회원 테이블 생성
```sql
CREATE USER 'spring5ex'@'localhost' IDENTIFIED BY 'secret';
CREATE DATABASE spring5ex CHARACTER SET=utf8;
GRANT PRIVILEGES ON spring5ex.* TO 'spring5ex'@'localhost';

CREATE TABLE spring5ex.members (
    id AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255),
    password VARCHAR(100),
    name VARCHAR(100),
    regdate datetime,
    UNIQUE KEY (email)
) ENGINE=InnoDB CHARACTER SET=utf8;

INSERT INTO member (email, password, name, regdate) VALUES ('foo@bar.com', '1234', 'Foo', NOW());
```
- 스프링이 제공하는 DB 연동 기능은 DataSource를 사용해서 DB Connection을 구한다. DB 연동에 사용할 DataSource를 스프링 Bean으로 등록하고 DB 연동 기능을 구현한 Bean 객체는 DataSource를 주입 받아서 사용한다
```java
@Configuration
public class AppCtx {
    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        DataSource ds = new DataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl("jdbs:mysql://localhost/spring5ex?characterEncoding=utf8");
        ds.setUsername("spring5ex");
        ds.setPassword("secret");
        ds.setInitialSize(2); // 커넥션 풀의 커넥션 개수
        ds.setMaxActive(10); // 커넥션 풀의 최대 커넥션 개수
        return ds;
    }

    @Bean
    public MemberDao memberDao() {
        return new MemberDao(dataSource());
    }
}
```
- 커넥션 풀에 커넥션을 요청하면 해당 커넥션은 활성(active) 상태가 되고, 커넥션을 다시 커넥션 풀에 반환하면 유휴(idle) 상태가 된다.
- `maxActive`를 10으로 지정하면 이는 커넥션 풀이 수용할 수 있는 동시 DB 커넥션이 10개라는 뜻이다. 현재 활성 커넥션이 10개인데 다시 커넥션을 요청하면 다른 커넥션이 반환될 때까지 대기한다. 이 대기 시간이 `maxWait`다. 대기 시간 내에 풀에 반환된 커넥션이 있으면 해당 커넥션을 구하게 되고, 대기 시간 내에 반환된 커넥션이 없으면 예외가 발생한다.

#### 목록 조회 쿼리
- `List<T> query(String sql, RowMapper<T> rowMapper)`
- `List<T> query(String sql, Object[] args, RowMapper<T> rowMapper)`
- `List<T> query(String sql, RowMapper<T> rowMapper, Object ...args)`

```java
public class MemberDao {
    private static final String QUERY_SELECT_BY_EMAIL = "SELECT * FROM members WHERE email = ?";
    private JdbcTemplate jdbcTemplate;

    public MemberDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Member selectByEmail(String email) {
        List<Member> results = jdbcTemplate.query(QUERY_SELECT_BY_EMAIL, new RowMapper<Member>() {
            @Override
            public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
                Member member = new Member(
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getTimestamp("regdate").toLocalDateTime()
                );
                member.setId(rs.getLong("id"));
                return member;
            }
        }, email);

        return results.isEmpty() ? null : results.get(0);
    }
}
```

#### 단일 행 조회 쿼리
- `T queryForObject(String sql, Class<T> requiredType)`
- `T queryForObject(String sql, Class<T> requiredType, Object ...args)`
- `T queryForObject(String sql, RowMapper<T> rowMapper)`
- `T queryForObject(String sql, RowMapper<T> rowMapper, Object ...args)`
```java
private static final String QUERY_COUNT = "SELECT COUNT(*) FROM members";

public int count() {
    Integer count = jdbcTemplate.queryForObject(QUERY_COUNT, Integer.class);
    return count;
}
```
- `queryForObject()` 메서드를 사용하려면 쿼리 실행 결과는 반드시 한 행이어야 한다. 만약 쿼리 실행 결과 행이 없거나 두 개 이상이면 `IncorrectRequltSizeDataAccessException`이 발생한다. 행의 개수가 0이면 하위 클래스인 `EmptyResultDataAccessException`이 발생한다. 따라서 결과 행이 정확하게 한 개가 아니면 `queryForMethod()` 메서드 대신 `query()` 메서드를 사용해야 한다.

#### 변경 쿼리
- `int update(String sql)`
- `int update(String sql, Object ...args)`
```java
private static final String QUERY_UPDATE = "UPDATE members SET name = ?, password = ? WHERE email = ?";

public void update(Member member) {
    jdbcTemplate.update(QUERY_UPDATE, member.getName(), member.getPassword(), member.getEmail());
}
```

#### PreparedStatement 사용
- `List<T> query(PreparedStatementCreator psc, RowMapper<T> rowMapper)`
- `int update(PreparedStatementCreator psc)`
- `PreparedStatementCreator`를 구현한 클래스는 `createPreparedStatement()` 메서드의 파라미터로 전달받는 `Connection`을 이용해서 `PreparedStatement` 객체를 생성하고 인덱스 파라미터를 알맞게 설정한 뒤에 리턴하면 된다.
```java
private static final String QUERY_INSERT = "INSERT INTO members (email, password, name, regdate) VALUES (?, ?, ?, ?)";

public void insert(final Member member) {
    jdbcTemplate.update(new PreparedStetementCreator() {
        @Override
        public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
            PreparedStatement pstmt = con.parepareStatement(QUERY_INSERT);
            pstmt.setString(1, member.getEmail());
            pstmt.setString(2, member.getPassword());
            pstmt.setString(3, member.getName());
            pstmt.setTimestamp(4, Timestamp.valueOf(member.getRegisterDateTime()));
            return psmt;
        }
    });
}
```

#### 자동 생성 키 값 구하기
- `int update(PreparedStatementCreator psc, KeyHolder generatedKeyHolder)`
```java
private static final String QUERY_INSERT = "INSERT INTO members (email, password, name, regdate) VALUES (?, ?, ?, ?)";

public void insert(final Member member) {
    KeyHolder = keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(new PreparedStetementCreator() {
        @Override
        public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
            PreparedStatement pstmt = con.parepareStatement(QUERY_INSERT, new String[]{"id"});
            pstmt.setString(1, member.getEmail());
            pstmt.setString(2, member.getPassword());
            pstmt.setString(3, member.getName());
            pstmt.setTimestamp(4, Timestamp.valueOf(member.getRegisterDateTime()));
            return psmt;
        }
    }, keyHolder);
    Number keyValue = keyHolder.getKey();
    member.setId(keyValue.longValue());
}
```

#### 예외 번역
> `convertSqlToDataException()` 함수를 찾을 수 없음;;;

- `RuntimeException` <- `DataAccessException` <- `BadSqlGrammerException`, `DuplicateKeyException`, `QueryTimeoutException`
```java
try {
    // JDBC 코드
} catch (SQLException e) {
    throw convertSqlToDataException(e);
}
```

#### 트랜잭션 처리
- 트랜잭션 범위에서 실행할 함수 위에 `@Transactional` 선언 & 스프링 설정 클래스에 `@PlatformTransactionManager` Bean 설정
```java
@Configuration
@EnableTransactionManagement
public class AppCtx {
    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager tm = new DataSourceTransactionManager();
        tm.setDataSource(dataSource());
        return tm;
    }
}

public class ChangePasswordService {
    @Transactional
    public void changePassword(String email, String oldPwd, String new Pwd) {
        // ...
    }
}
```
- 스프링은 `@Transactional` annotation을 이용해서 트랜잭션을 처리하기 위해서 내부적으로 AOP를 사용한다.
- 별도 설정을 추가하지 않으면 `RuntimeException`이 발생하면 트랜잭션을 롤백한다.

#### 로깅 활성화
- `pom.xml`에 `slf4j-api`, `logback-classic` 의존 모듈 추가 필요
- `logback.xml` 추가 필요

---

## ch 09 스프링 MVC 시작하기

스프링 = DI 컨테이너, MVC = 스프링 + 웹

- 스프링 웹 MVC 프로젝트 폴더 구조
```
.
├── pom.xml
└── src/main
    ├── java
    │   ├── HelloController.java
    │   └── config
    │       ├── MvcConfig.java
    │       └── ControllerConfig.java
    └── webapp
        └── WEB-INF
            ├── web.xml
            └── view
                └── hello.jsp
```

#### 설정
- `pom.xml`
    - `<packaging>war</packaging>`
    - javax.servlet-api 모듈
    - javax.servlet.jsp-api 모듈
    - jstl 모듈
    - spring-mvc 모듈
- 로컬 테스트를 위해 톰캣 또는 제티 서버 필요
- 스프링 컨테이너 설정
```java
@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        retistry.jsp("/WEB-INF/view/", ".jsp");
    }
}
```
- `web.xml`
    - `DispatcherServlet` 별칭 등록
    - 스프링 설정 클래스의 타입 등록(`AnnotationConfigWebApplicationContext`)
    - 스프링 설정 경로 등록
    - 웹 요청을 등록한 `DispatcherServlet`이 처리하도록 맵핑 추가
    - 요청 파라미터 인코딩 관련 서블릿 필터 등록

#### tomcat 설정
```bash
$ brew install tomcat

$ which catalina
# /usr/local/bin/catalina

$ catalina version
# Using CATALINA_BASE:   /usr/local/Cellar/tomcat/9.0.13/libexec
# Using CATALINA_HOME:   /usr/local/Cellar/tomcat/9.0.13/libexec
# ...
```

#### 컨트롤러 구현
```java
@Controller
public class HelloController {
    @GetMapping("/hello")
    public String hello(Model model, @RequestParam(value = "foo", required = false) String foo) {
        // model은 Request 객체이며, 아래 코드에서 greeting이란 속성을 동적으로 추가함.
        model.addAttribute("greeting", "Hello " + foo);
    }

    // hello는 뷰 이름. /src/main/webapp/WEB-INF/view/hello.jsp
    return "hello"; 
}
```
- 컨텍스트 경로(`pom.xml` `<artifactId>here</artifactId>`)를 시작 위치로 URL 결정됨. 위 예제의 경우 `GET /here/hello`가 URL임.
- 컨트롤러 Bean 등록
```java
// src/main/java/config/ControllerConfig.java
@Configuration
public class ControllerConfig {
    @Bean
    public HelloController helloController() {
        return new HelloController();
    }
}
```

#### 뷰
```html
<%@ page contentTYpe="text/html; charset=utf-8"%>
<!DOCTYPE html>
...
${greeting} <!-- // JSP Expression Language -->
```

#### 실행하기

---

## ch 10 스프링 MVC 프레임워크 동작 방식
- 스프링 MVC의 핵심 구성 요소. (=)로 그린 박스 2개는 개발자가 직접 구현해야 함
```
+--------+    +-------------------+ 
| Client |    | DispatcherServlet | 
+--------+    +-------------------+ 
    | 1. Web Request    |
    | ----------------> | 2. find matching 
    |                   |    controller     +-----------------+ 
    |                   | ----------------> | <<spring bean>> | 
    |                   |                   | HandlerMapping  |
    |                   |                   +-----------------+
    |                   |                  
    |                   | 3. delegate req   +-----------------+ 4. exec           +=================+
    |                   | ----------------> | <<spring bean>> | ----------------> | <<spring bean>> |
    |                   | <---------------- | HandlerAdapter  | <---------------- | Controller      |
    |                   | 6. ModelAndView   +-----------------+ 5. return         +=================+
    |                   |                   
    |                   | 7. find View      +-----------------+
    |                   | ----------------> | <<spring bean>> |
    |                   | <---------------- | ViewResolver    |
    |                   | 8. View           +-----------------+
    |                   |
    |                   | 9. Make Response  +=================+
    |                   | ----------------> | JSP             |
    | 10. Web Response  |                   +=================+
    | <---------------- |
```

- `DispatcherServlet`은 스프링 컨테이너를 생성하고, 그 컨테이너로부터 필요한 Bean 객체를 구함
```
+-------------------+ <<create>>  +-------------------------+
| DispatcherServlet | ----------> | Spring Container        |
+-------------------+             | (WebApplicationContext) |
          |                       | +---------------------+ |
          |                       | | HandlerMapping      | |
          |                       | +---------------------+ |
          |                       | +---------------------+ |
          | <<use>>               | | HandlerAdapter      | |
          +---------------------> | +---------------------+ |
                                  | +---------------------+ |
                                  | | Controller Bean     | |
                                  | +---------------------+ |
                                  | +---------------------+ |
                                  | | ViewResolver        | |
                                  | +---------------------+ |
                                  +-------------------------+
```

#### DefaultHandler와 HandlerMapping의 우선 순위
- `@EnableWebMvc` 선언은 `RequestMappingHandlerMapping` Bean을 등록하며, `@Controller`, `@GetMapping("/foo")`로 정의한 컨트롤러 함수를 찾아서 웹 요청을 처리함
- ch09의 설정에 따라 `.jsp`를 제외한 모든 요청, 예를 들어 `/index.html`이나 `/css/bootstrap.css`도 `DispatcherServlet`이 처리함.
- 예로 든 정적 파일과 같이 경로와 컨트롤러간의 맵핑이 선언되지 않은 경우에는 `DefaultServletHandlerConfigurer#enable()`가 제공하는 두개의 Bean 객체를 통하여 웹 요청을 처리함
    - `DefaultServletHttpRequestHandler`
    - `SimpleUrlHandlerMapping`

다시 정리하면,

① `RequestMappingHandlerMapping`을 사용해서 요청을 처리할 핸들러를 검색한다.
  - 존재하면 해당 컨트롤러를 이용해서 요청을 처리한다.

② 존재하지 않으면 `SimpleUrlHandlerMapping`을 사용해서 요청을 처리할 핸들러를 검색한다.
  - `DefaultServletHandlerConfigurer#enable()` 메서드가 등록한 `SimpleUrlHandlerMapping`은 "/**" 경로(즉 모든 경로)에 대해 `DefaultServletHttpRequestHandler`를 리턴한다.
  - `DispatcherServlet`은 `DefaultServletHttpRequestHandler`에 처리를 요청한다.
  - `DefaultServletHttpRequestHandler`는 디폴트 서블릿에 처리를 위임한다.

---

## ch11 MVC1: 요청 매핑, 커맨드 객체, 리다이렉트, 폼 태그, 모델

> 대부분의 설정은 개발 초기에 완성된다. 따라서, 웹 애플리케이션을 개발한다는 것은 어떤 **1) 컨트롤러**를 이용해서 어떤 요청 경로를 처리할지 결정하고, 웹 브라우저가 전송한 요청에서 필요한 값을 구하고, 처리 결과를 **2) 뷰(JSP)**를 이용해서 보여주는 것이다.

- 웹 애플리케이션 개발은 다음 코드를 작성하는 일
    - 특정 요청 URL을 처리할 코드
    - 처리 결과를 HTML과 같은 형식으로 응답하는 코드
- 요청 매핑 annotation? 요청한 Url을 처리할 컨트롤러 함수를 매핑함
    - `@RequestMapping("/foo")`
    - `@GetMapping("/foo")`
    - `@PostMapping("/foo")`
    - `@PutMapping("/foo")`
    - `@PatchMapping("/foo")`
    - `@DeleteMapping("/foo")`
- 요청 파라미터 접근? 
    - `HttpServletRequest` 객체에 직접 접근하는 방식
    - `@RequestParam` annotation을 이용하는 방식

```java
@Controller
public class RegisterController {
    @PostMapping("/foo")
    public String someFunc(HttpServletRequest request) {
        String param = request.getParameter("param");
        // ...
    }
}
```
```java
@Controller
public class RegisterClass {
    @PostMapping("/foo")
    public String someFunc(@RequestParam(value = "param", required = false, defaultValue = "false") Boolean param) {
        if (param == false) { }
        // ...
    }
}
```
- 리다이렉트? `redirect:/path`
```java
@GetMapping("/foo2")
public String foo2Func() {
    return "redirect:/foo1"
}
```
- 커맨드 객체를 이용하여 요청 매핑? 컨트롤러 함수의 파라미터로 DTO 객체를 선언하면(HTTP 요청과 DTO의 필드명이 일치해야 함), `HttpServletRequest#getParameter()` 함수를 이용하여 DTO를 셋팅하지 않아도 됨.
```java
private FooService fooSvc;

@PostMapping("/foo")
public String fooFunc(FooRequest fooReq) {
    fooSvc.someFunc(fooReq);
    // ...
}
```
```html
<p>${fooReq.property}</p>
```
- 커맨드 객체와 스프링 폼 연동
```html
<input type="text" name="property" value="${fooReq.property}">
```
- 컨트롤러 구현이 없는 경로 매핑
```java
@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/main").setViewName("main");
    }
}
```
- Model 객체를 통해 컨트롤러에서 뷰에 데이터 전달하기
```java
@Controller
public class SurveyController {
    @GetMapping("/survey")
    public String form(Model model) {
        List<Question> questions = createQuestions();
        model.addAttribute("questions", questions);
        return "survey";
    }
}
```
```html
<!-- // survry.jsp -->
<c:forEach var="q" items="${questions}" varStatus="status">
</c:forEach>
```
- ModelAndView 객체 이용
```java
@Controller
public class SurveyController {
    @GetMapping("/survey")
    public ModelAndView form() {
        List<Question> questions = createQuestions();
        ModelAndView mav = new ModelAndView();
        mav.addObject("questions", questions);
        mav.setViewName("survey");
        return mav;
    }
}
```

---

## ch 12 MVC2: 메시지, 커맨드 객체 검증
### 메시지
- 적용법
    - 문자열을 담을 파일을 준비한다.
    - 메시지 파일에서 값을 읽어오는 `MessageSource` Bean 을 설정한다.
    - JSP 코드에서 `<spring:message>` 태크를 이용해서 메시지를 출력한다.
```bash
# src/main/resources/message/label.properties
email=이메일
password.confirm=비밀번호 확인
register.done=<strong>{0}님</strong>, 환영합니다.
```
```java
@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
    @Bean
    public class MessageSource messageSource() {
        ResourceBundleMessageSource ms = new ResourceBundleMessageSource();
        ms.setBasenames("message.label");
        ms.setDefaultEncoding("UTF-8");
        return ms;
    }
}
```
```html
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<label><spring:message code="email" /></label>
<p><spring:message code="register.done" arguments="${registerRequest.name}" /></p>
```

### 커맨드 객체 유효성 검증
#### `Validator#validate()` 함수를 호출하는 방법
- 적용법
    - `Validator` 클래스를 구현한다.
    - 컨트롤러에서 `Validator#validate()` 함수를 호출한다.
    - 메시지 파일에 에러코드에 따라 출력할 문자열을 정의한다.
    - JSP 코드에서 `<form:errors>` 태크를 이용해서 메시지를 출력한다.
```java
public class RegisterRequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        // aClass 객체가 RegisterRequest 타입으로 변환 가능한지 확인
        return RegisterRequest.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        RegisterRequest regReq =. (RegisterRequest) o;
        if (regReq.getEmail() == null) {
            errors.rejectValue("email", "required");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "required");
        ValidationUtils.rejectIfEmpty(errors, "password", "required");
    }
}
```
```java
@Controller
public class RegisterController {
    @PostMapping(..)
    pulbic String register(RegisterRequest regReq, Errors errors) {
        (new RegisterRequestValidator()).validate(regReq, errors);
        if (errors.hasError()) {
            // return or throw
        }
    }
}
```
```bash
# src/main/resources/message/label.properties
required=필수 항목입니다.
required.name=이름은 필수 항목입니다.
```
```html
<%@ taglib prefix="form" uri=""http://www.springframework.org/tags/form %>
<%@ taglib prefix="spring" uri=""http://www.springframework.org/tags %>
<form:form>
<form:input path="email" />
<form:errors path="email" />
</form:form>
```
- `Errors`
    - `reject(String errorCode, Object[] errorArgs, String defaultMessage)` // 커맨드 객체 자체가 유효하지 않을 때
    - `rejectValue(String field, String errorCode, Object[] errorArgs, String defaultMessage)`
- `ValidationUtils`
    - `rejectIfEmpty(Errors errors, String field, String errorCode, Object[] errorArgs)`
    - `rejectIfEmptyOrWhitespace(Errors errors, String field, String errorCode, Object[] errorArgs)`
- 에러 메시지 적용 우선 순위
    - `required.registerRequest.email`
    - `required.email`
    - `required.String`
    - `required`

#### 전역 범위 Validator를 이용하는 방법
- 적용법
    - 설정 클래스에서 `WebMvcConfigurer#getValidator()` 메서드가 `Validator` 구현 객체를 리턴하도록 구현
    - 전역 범위 `Validator`가 검증할 커맨드 객체에 `@Valid` annotation 적용
```java
@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public Validator getValidator() {
        return new RegisterRequestValidator();
    }
}
```
```java
@Controller
public class RegisterController {
    @PostMapping(..)
    public String register(@Valid RegisterRequest regReq, Errors errors) {
        // Validator.validate() 함수 호출하지 않음
        if (errors.hasError()) {
            // return or throw
        }
    }
}
```

#### Bean Validation을 이용하는 방법
- 적용법
    - 의존 모듈 추가
    - 커맨드 클래스에 검증 annotation 추가
    - 검증할 커맨드 객체에 `@Valid` annotation 적용
    - (선택) 메시지 파일에 에러코드에 따라 출력할 커스텀 문자열을 정의한다.
- `주의` Bean Validation을 이용할 때는 `WebMvcConfigurer#getValidator()`를 Override하면 안됨. 왜냐하면, `@EnableWebMvc` annotation이 `OptionalValidatorFactoryBean`을 전역 Validator로 자동 등록하므로.
```xml
<dependency>
  <groupId>javax.validation</groupId>
  <artifactId>validation-api</artifactId>
  <version>1.1.0.Final</version>
</dependency>

<dependency>
  <groupId>org.hibernate</groupId>
  <artifactId>hibernate-validator</artifactId>
  <version>5.4.2.Final</version>
</dependency>
```
```java
public class RegisterRequest {
    @NotBlank
    @Email
    private String email;
    @Size(min = 6)
    private String password;
}
```
```java
@Controller
public class RegisterController {
    @PostMapping(..)
    public String register(@Valid RegisterRequest regReq, Errors errors) {
        // Validator.validate() 함수 호출하지 않음
        if (errors.hasError()) {
            // return or throw
        }
    }
}
```
```bash
NotBlank=필수 항목입니다. 공백 문자는 허용하지 않습니다.
Email=올바른 이메일 주소를 입력해야 합니다.
Size.password=암호 길이는 6자 이상이어야 합니다.
```
- 에러 메시지 적용 우선 순위
    - `NotBlank.registerRequest.email`
    - `NotBlank.email`
    - `NotBlank`
- Bean Validation의 주요 annotation? `@AssertTrue`, `@AssertFalse`, `@DecimalMax`, `@DecimalMin`, `@Max`, `@Min`, `@Digits`, `@Size`, `@Null`, `@NotNull`, `@Pattern`
- `주의` `NotNull`을 제외한 나머지 annotation은 검사 대상 값이 null이면 유효한 것으로 판단함
- `참고` Bean Validation 2.0

---

## ch13 MVC3: 세션, 인터셉터, 쿠키
#### 세션
- 사용법 두 가지
    - 요청 매핑을 적용한 컨트롤러 메서드에 `HttpSession` 파라미터를 추가한다.
    - 요청 매핑을 적용한 컨트롤러 메서드에 `HttpServletRequest` 파라미터를 추가하고, `HttpServletRequest#getSession()` 함수를 이용한다.
    - 첫 번째 방법은 항상 `HttpSession`을 사용하지만 두 번째 방법은 `getSession()`함수를 호출했을 때만 `HttpSession` 객체를 생성한다.
```java
@PostMapping
public String form(LoginCommand loginCommand, Errors errors, HttpSession session) { }
```
```java
@PostMapping
public String foo(LoginCommand loginCommnad, Errors errors, HttpServletRequest req) {
    HttpSession session = req.getSession();
    session.setAttribute("foo", foo); // 세션 데이터 저장
    Foo foo = (Foo) session.getAttribute("foo"); // 세션 데이터 조회
    session.invalidate(); // 세션 삭제
}
```

#### 인터셉터
프레임워크|기능
---|---
Spring|`Interceptor`, `HandlerInterceptor`

호출 시점|Spring|Laravel
---|---|---
컨트롤러(핸들러) 실행 전|`preHandle`
컨트롤러(핸들러) 실행 후, 뷰 렌더링 전
뷰를 실행한 후|`afterCompletion`

- 적용법
    - `HanlderInterceptor`를 구현한 인터셉터 클래스 구현
    - 스프링 설정 파일에 `WebMvcConfigurer#addInterceptors()` 재정의한 함수 구현
```java
public class AuthCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        resp.sendRedirect(req.getContextPath() + "/login"); // 로그인 페이지로 돌려보내는 예
        // 검사 로직 수행 및 Boolean 반환
    }
}
```
```java
@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
    @Bean
    public AuthCheckInterceptor authCheckInterceptor() {
        return new AuthCheckInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authCheckInterceptor())
            .addPathPatterns("/edit/**")
            // 제외할 Ant 패턴 추가
            .excludePathPatterns("/edit/help/**");
    }
}
```
- `HandlerInterceptor` 인터페이스
    - `boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception`
    - `void postHandle(HttpServletRequest req, HttpServletResponse resp, Object handler, ModelAndView mav) throws Exception`
    - `void afterCompletion(HttpServletRequest req, HttpServletResponse resp, Object handler, Exception e) throws Exception`

#### 쿠키
- 사용법
    - 요청 매핑을 적용한 컨트롤러 메서드에 `@CookieValue` annotation 파라미터 적용하여 `Cookie` 객체를 구하여 쿠키 값에 접근할 수 있음
    - `HttpServletResponse#addCookie()` 메서드를 이용해서 응답 헤더에 쿠키를 내보낼 수 있음
```java
@Controller
@RequestMapping("/login")
public class LoginController {
    @GetMapping
    public String form(LoginCommand loginCommand,
                       @CookieValue(value = "REMEMBER", required = false) Cookie rCookie) {
        if (rCookie != null) {
            // 커맨드 객체에 쿠키에서 읽은 값을 셋팅해서 뷰 렌더링
            loginCommand.setEmail(rCookie.getValue());
            loginCommand.setRememberEmail(true);
        }
        return "login/loginForm";
    }

    @PostMapping
    public String submit(LoginCommand loginCommand, Errors errors, HttpSession session, HttpServletResponse response) {
        Cookie rememberCookie = new Cookie("REMEMBER", loginCommand.getEmail());
        rememberCookie.setPath("/");
        // Set-Cookie 응답 헤더 전송
        rememberCookie.setMaxAge(60 * 60 * 24 * 30); // 30 days
        response.addCookie(rememberCookie);
        return "login/loginSuccess";
    }
}
```

---

## MVC4: 날짜 값 변환, @PathVariable, 예외 처리
#### 날짜 값 변환
- `Long`, `int`등의 기본 데이터 타입 변환은 스프링이 처리해주지만, `LocalDateTime`은 직접 설정해줘야 함
```java
public class ListCommand {
    @DateTimeFormat(pattern = "yyyyMMdd")
    private LocalDateTime from;
    // ...
}
```
- JSTL이 제공하는 날짜 태그는 `LocalDateTime` 형식을 처리하지 못하므로, 애플리케이션에서 뷰로 날짜 형식 데이터를 내 보낼때는 커스텀 태그를 선언해야 함.
```jsp
<!-- // src/main/webapp/WEB-INF/tags/formatDateTime.tag -->
<%@ tag body-content="empty" pageEncoding="utf-8" %>
<%@ tag import="java.time.format.DateTimeFormatter" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="value" required="true" type="java.time.temporal.TemporalAccessor" %>
<%@ attribute name="pattern" type="java.lang.String" %>
<%
    if (pattern == null) pattern = "yyyy-MM-dd";
%>
<%= DateTimeFormatter.ofPattern(pattern).format(value) %>
```
```html
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tf" tagdir="/WEB-INF/tags" %>
<!-- // ... -->
<td>
    <tf:formatDateTime value="${mem.regDateTime}" pattern="yyyy-MM-dd" />
</td>
```

> `WebDataBinder`가 날짜 형식의 문자열을`LocalDateTime` 형식으로 변경해준다. `WebDataBinder`는 `ConversionService`에 날짜 형식 변경을 위임하며, `@EnableWebMvc` 설정이 `DefaultFormattingConversionService` 구현체를 바인딩한다.

#### @PathVariable
```java
@GetMapping("/members/{id}")
public String detail(@PathVariable("id") Long memberId, Model model) { }
```

#### 예외 처리
- 컨트롤러 단위 예외 처리
```java
@Controller
public class MemberDetailController {
    @GetMapping("/members/{id}")
    public String detail(@PathVariable("id") Long memberId, Model model) { }

    @ExceptionHandler(MemberNotFoundException.class)
    public String handleNotFoundException(MemberNotFoundException e) {
        // 로깅 등
        return "member/noMembmer";
    }
}
```
- 전역 예외 처리, 구현후 Bean 등록 필요
```java
@ControllerAdvice("spring")
public class CommonExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException() {
        return "error/commonException";
    }
}
```
- 컨트롤러 단위 예외 처리기 -> 전역 예외 처리기 순으로 적용됨

---

## ch15 간단한 웹 애플리케이션 구조
#### 간단한 웹 애플리케이션의 구성 요소
- **프론트 서블릿** 웹 브라우저의 요청을 받는 창구 역할. 스프링 MVC에서는 `DispatcherServlet`이 프론트 서블릿의 역할을 수행한다.
- **컨트롤러** 애플리케이션이 제공하는 기능과 사용자 요청을 연결하는 매개체로서 기능 제공을 위한 로직을 직접 수행하지는 않는다. 대신 해당 로직을 제공하는 서비스에 그 처리를 위임한다.
    - 클라이언트가 요구한 기능을 실행
    - 응답 결과를 생성하는데 필요한 모델 생성
    - 응답 결과를 생성할 뷰 선택
- **서비스** 사용자에게 비밀번호를 변경 기능을 제공하려면 수정 폼을 제공하고, 로그인 여부를 확인하고, 실제로 비밀번호를 변경해야 한다. **핵심 로직은 비밀번호를 변경하는 것**이다. 예를 들어 웹 폼 대신에 콘솔에서 명령어를 입력해서 비밀번호를 변경할 수도 있다. 여기서 폼이나 콘솔은 사용자와의 상호 작용을 위한 연결 고리에 해당하지, 핵심 로직인 비밀번호 변경 자체는 아니라는 점을 명심하자.
- **DAO** Data Access Object. DB와 애플리케이션 간에 데이터를 이동시켜 주는 역할을 한다.

#### 서비스의 구현
- 예를 들어, 비밀번호 변경 기능은 다음 로직을 서비스에서 수행한다.
    - DB에서 비밀번호를 변경할 모델을 구한다.
    - 존재하지 않으면 예외를 발생시킨다.
    - 모델의 비밀번호 속성을 변경한다.
    - 모델의 변경 내역을 DB에 반영한다.
    - 트랜잭션을 처리한다.
```java
@Transactional
public void changePassword(String email, String oldPassword, String newPassword) {
    Member member = memberDao.selectByEmail(email);
    if (member == null) {
        throw new MemberNotFoundException();
    }

    member.changePassword(oldPassword, newPassword);
    memberDao.update(member);
}
```
- 서비스 클래스의 메서드를 호출할 때, 파라미터 대신 스프링 MVC의 **커맨드 객체**를 이용할 수도 있다. 커맨드 객체를 이용하는 이유는 스프링 MVC가 제공하는 폼 값 바인딩과 검증, 스프링 폼 태그와의 연동 기능을 사용하기 위함이다.
```java
@RequestMapping("/register")
public String register(RegisterCommand registerCommand, Errors errors) {
    // ...
    memberRegisterService.register(registerCommand);
}
```
- 서비스 메서드는 기능 실행 후에 다음과 같은 방식으로 호출한 쪽에 결과를 알려준다.
    - 리턴 값을 이용한 정상 결과
    - 예외를 이용한 비정상 결과

#### 패키지 구성
- 스프링 설정 영역
- 웹 요청 처리 영역
- 기능 제공 영역
    - 서비스
    - DAO
    - 모델

> 컨트롤러-서비스-DAO 구조는 간단한 웹 애플리케이션을 개발하기에는 무리가 없다. 문제는 애플리케이션이 기능이 많아지고 로직이 추가되기 시작할 때 발생한다. 로직이 복잡해지면 컨트롤러-서비스-DAO 구조의 코드도 함께 복잡해지는 경향이 있따. 특정 기능을 분석할 때 시간이 오래 걸리기도 하고, **중요한 로직을 구현한 코드가 DAO, 서비스 등에 흩어지기도 한다. 또한 중복된 쿼리나 중복된 로직 코드가 늘어나기도 한다**.
>
> 웹 애플리케이션이 복잡해지고 커지면서 코드도 함께 복잡해지는 문제를 완화하는 방법 중 하나는 도메인 주도 설계를 적용하는 것이다. **도메인 주도 설계는 컨트롤러-서비스-DAO 구조 대신에 UI-서비스-도메인-인프라의 네 영역으로 애플리케이션을 구성한다**. 여기서 UI는 컨트롤러 영역에 대응하고 인프라는 DAO 영역에 대응한다. **중요한 점은 주요한 도메인 모델과 업무 로직이 서비스 영역이 아닌 도메인 영역에 위치한다는 것이다**. 또한 도메인 영역은 정해진 패턴에 따라 모델을 구현한다. 이를 통해 업무가 복잡해져도 일정 수준의 복잡도로 코드를 유지할 수 있도록 해 준다.

---

## ch16 JSON 응답과 처리(강사자료-댓글컨트롤러 https://github.com/miniplugin/springframework/blob/master/src/main/java/org/edu/controller/ReplyController.java + 안드로이드앱 연동은 강사블로그에서 gson 으로 검색)
- Jackson 의존 모듈 추가
```bash
+----------+       +---------+       +----------+
| JSON Obj | <---> | Jackson | <---> | Java Obj |
+----------+       +---------+       +----------+
```
- `@RestController`로 JSON 응답, Bean 등록 필수
```java
@RestController
public class RestMemberController {
    @GetMapping("/api/members")
    public List<Member> members() {
        return memberDao.selectAll();
    }
}
```
- JSON 응답에서 제외할 필드는 `@JsonIgnore` 선언
```java
public class Member {
    @JsonIgnore
    private String password;
}
```
- ISO_8601 날짜 형식 응답
```java
public class Member {
    @JsonFormat(shape = Shape.STRING)
    private LocalDateTime regDateTime;
}
```

> 스프링 MVC는 자바 객체를 HTTP 응답으로 변환할 때 `HttpMessageConverter` 인터페이스를 이용한다. Jackson을 사용할 때는 `MappingJackson2HttpMessageConverter` 구현체를 사용한다.

- 모든 날짜 필드에 `JsonDate`를 선언할 수 없으므로, 스프링 설정에 전역 설정을 등록하는 것이 편리하다.
```java
@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder
            .json()
            .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .build();
        // 새로 만든 HttpMessageConverter 객체를 0번 인덱스로 추가
        converters.add(0, new MappingJackson2HttpMessageConverter(objectMapper));
    }
}
```

- `@RequestBody`로 JSON 요청 매핑
```java
@RestController
public class RestMemberController {
    public void newMember(@RequestBody @Valid RegisterRequest regReq, HttpServletResponse response) throws IOException {
        // response.setHeader("key", "value");
        // response.setStatus(HttpServletResponse.OK);
    }
}
```
- 요청 객체 검증
```java
@RestController
public class RestMemberController {
    public void newMember(@RequestBody @Valid RegisterRequest regReq, Errors errors, HttpServletResponse response) throws IOException {
        // response.sendError(HttpServletResponse.BAD_REQUEST);
    }
}
```
- `ResponseEntity`로 객체와 응답 코드 돌려주기
```java
// ErrorResponse는 message 필드만 있는 데이터 객체
@RestController
public class RestMemberController {
    @GetMapping("/api/members/{id}")
    public ResponseEntity<Object> member(@PathVariable Long id) {
        Member member = memberDao.selectById(id);
        if (member == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("no member"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(member);
    }
}
```

> 스프링 MVC는 리턴 타입이 `ResponseEntity`이면 ResponseEntity#body에 담긴 객체를 JSON으로 변환한다.

---

### 20200702 강의중 신규로 사용한 스프링 애노테이션 부분 확인
- @ResponseBody 에서 Body와 Header 부분 비교(크롬 > 개발자도구 > 네트워크 탭)
- @Transactional 이론

### 20200702 작업중 미완성 소스 부분 처리
```
	List<String> files = new ArrayList<>();
	files.add("sample1.jpg");
	files.add("sample2.jpg");
	files.add("sample3.jpg");
	String[] filenames = new String[files.size()];
	int cnt = 0;
	for(String fileName : files) {
		filenames[cnt++] = fileName;
	}
	System.out.println(filenames[0] + filenames[1] + filenames[2]);
```

----
# #게시판 페이징 처리(20200706강의예정)
----
### 더미테이터 insert구문으로 등록시 누락되는 번호발생 문제처리(프로시저로 처리)
```
CREATE PROCEDURE dummyInsert() 
BEGIN
	DECLARE i INT DEFAULT 1;
	WHILE i <= 100 DO
		INSERT INTO tbl_board (bno, title, content, writer) VALUES
		(i, '수정된 글입니다.', '수정 테스트', 'user00');
		SET i = i + 1;
	END WHILE;
    -- 실행 CALL dummyInsert;
END
```

```
기존방법 (아래방법으로는 자동증가 AI 필드값에 누락된 번호가 들어갑니다.)
SET FOREIGN_KEY_CHECKS = 0; #제약조건때문에 truncate하지 못할때 실행
truncate TABLE tbl_board; #AI 데이터까지 모두 지우기
-- 초기 더미데이터 1개 입력(아래)
INSERT INTO tbl_board (bno, title, content, writer) VALUES
(1, '수정된 글입니다.', '수정 테스트 ', 'user00');
-- 더미데이터 입력 첫번째 방법(아래)
INSERT INTO tbl_board (title, content, writer)
SELECT title, content, writer FROM TBL_BOARD;
-- 더미데이터 입력 두번째 방법(아래)
INSERT INTO TBL_BOARD SELECT * FROM TBL_BOARD;
```
### 개발환경
Server - Java(Spring 4.3.22)사용
Web - Jsp사용
게시판에서 페이징 리스트를 사용하기위하여

----
### 순서
	1. 쿼리 생성 limit #{startBno}, #{perPageNum}
	2. 현재 페이지번호를 Web으로부터 받아서 DAO 와 DB 사이에 get/set으로 사용할 VO 클래스 추가,
	3. 게시판 게시글 갯수(Count) 값을 가져올것 - 필요한 DAO, Service 클래스 변경 및 추가,
	4. Controller 클래스에서 PageVO클래스 초기값 지정 및 확인 후 jsp와 데이터 주고받는 부분 추가.
	5. jsp페이지에 갯수와 현재 페이지 번호를 가지고 페이징 알고리즘을 활용하여 End페이지 계산 및 Prev,Next버튼 생성
	6. 뷰단에서 리스트 -> 상세보기 -> 수정 -> 쓰기 페이지 이동 후에도 페이징이 유지되게 처리

### 소스코드

####PageVO.java

```
public class PageVO {
	private int perPageNum;//쿼리 공통사용-1페이지당 보여줄 게시물 개수
	private int startBno;  //쿼리에서 사용-페이지에서 보여줄 게시물 시작번호
	private Integer page;  //jsp에서 사용-뷰단에서 선택한 페이지 번호
	private int startPage; //jsp에서 사용-뷰단에서 보여줄 페이지 시작번호
	private int endPage;   //jsp에서 사용-뷰단에서 보여줄 페이지 끝번호
	private int totalCount;//jsp에서 사용-뷰단에서 예를 들어 endPage가 10을 넘을때 계산식에 사용
	private boolean prev;  //jsp에서 사용-뷰단에서 startPage 이전게시물이 존재하는지 검사용
	private boolean next;  //jsp에서 사용-뷰단에서 endPage 다음게시물이 존재하는지 검사용
	
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		calcPage();
	}
	public boolean isPrev() {
		return prev;
	}
	public void setPrev(boolean prev) {
		this.prev = prev;
	}
	public boolean isNext() {
		return next;
	}
	public void setNext(boolean next) {
		this.next = next;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public int getPerPageNum() {
		return perPageNum;
	}
	public void setPerPageNum(int perPageNum) {
		this.perPageNum = perPageNum;
	}
	public int getEndPage() {
		return endPage;
	}
	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}
	public int getStartPage() {
		return startPage;
	}
	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}
	
	public int getStartBno() {
		//DB쿼리에서 사용...시작데이터번호 = (페이지번호 - 1)*페이지당 보여지는 개수.
		startBno = (this.page - 1) * perPageNum;
		return startBno;
	}
	public void setStartBno(int startBno) {
		this.startBno = startBno;
	}
	
	private void calcPage() {
		// page변수는 현재 페이지번호
		int tempEnd = (int)(Math.ceil(page / 10.0) * 10); //ceil함수는 천장함수로 0.9은 -> 1출력, 1.1은 -> 2출력
		// 현재 페이지번호를 기준으로 끝 페이지를 계산한다.(참고 round 는 반올림함수, floor 바닥함수)
		this.startPage = tempEnd - 9;// 시작 페이지 계산
		/**
		 * 디버그 
		System.out.println("디버그 page = " +page);
		System.out.println("tempEnd = "+tempEnd);
		System.out.println("this.totalCount =" +this.totalCount);
		System.out.println("this.startPage =" +this.startPage);
		*/
		if (tempEnd * 10 > this.totalCount) { //현재페이지 번호로 계산한 게시물 개수가 실제 게시물 개수보다 많을경우
			this.endPage = (int) Math.ceil(this.totalCount / 10.0);
		} else {						
			this.endPage = tempEnd;	          //현재페이지 번호로 계산된 게시물 개수가 실제 게시물 개수보다 적거나 같을 경우
		}
		//System.out.println("this.pageEnd = "+this.pageEnd);//디버그
		this.prev = this.startPage != 1;            	//시작페이지가 1보다 크면 무조건 이전페이지가 있음 true
		this.next = this.endPage * 10 < this.totalCount;//현재페이지 번호로 계산된 게시물개수가 실제 게시물 개수보다 작다면 다음페이지가 있음 true
	}
	
}
```

#### @Controller
```
AdminController클래스
	@RequestMapping(value = "/admin/board/list", method = RequestMethod.GET)
	public String boardList(@ModelAttribute("pageVO") PageVO pageVO, Locale locale, Model model) throws Exception {
		//PageVO pageVO = new PageVO();//디버그 jsp와 연동전
		if(pageVO.getPage() == null) {
			pageVO.setPage(1);
		}else {
			pageVO.setPage(pageVO.getPage());
		}
		pageVO.setPerPageNum(10);
		pageVO.setTotalCount(boardService.countBno());
		List<BoardVO> list = boardService.selectBoard(pageVO);
		//모델클래스는 jsp화면으로 boardService에서 셀렉트한 list값을 boardList변수명으로 보낸다.
		model.addAttribute("boardList", list);
		model.addAttribute("pageVO", pageVO);
		return "admin/board/board_list";
	}
```
 
----
Web(jsp)에서 현재 페이지 번호를 Controller(/admin/board/list)에 전달,
Controller는 PageVO 클래스에 접근하여 페이지 계산.
페이지 알고리즘 및 현재 페이지를 기준으로 마지막 페이지 , Prev, Next 계산.
PageVO 클래스에서 계산된 페이지값을 받아서 화면(Web)에 출력 부분입니다.
----

#### board_list.jsp (일부)
```jsp
<ul class="pagination">
   <c:if test="${pageVO.prev}">
      <li class="page-item"><a class="page-link" href='/admin/board/list?page=${pageVO.startPage -1}'>이전</a></li>
   </c:if>
   <c:forEach begin="${pageVO.startPage}" end="${pageVO.endPage}" var="idx">
      <li class='page-item <c:out value="${idx == pageVO.page?'active':''}"/>'>
         <a class="page-link" href='/admin/board/list?page=${idx}'>${idx}</a>
      </li>
   </c:forEach>
   <c:if test="${pageVO.next}">
      <li class="page-item"><a class="page-link" href='/admin/board/list?page=${pageVO.endPage +1}'>다음</a></li>
   </c:if>
</ul>
```

----
# #게시판 검색 처리(위에서 작업한 페이징처리에 VO를 추가하면 됩니다)
----
### 순서
	1. 쿼리 생성 <if test="searchType != null" > <if test="searchType == 'all'.toString()">, #{searchKeyword}
	2. 기존 pageVO에 위 쿼리에 사용된 변수 2개 추가, private String searchType, private String searchKeyword
	3. pageVO 변수추가에 따른 DAO, Service 클래스 변경,
	4. 게시판 리스트 jsp페이지에서 검색폼에 <form>태그 및 <input> name 생성

### 소스코드(아래)

```매퍼쿼리
	<select id="selectBoard" resultType="org.edu.vo.BoardVO">
		select * from tbl_board where 1=1
		<if test="searchType != null" > 
			<if test="searchType == 'all'.toString()">
			  and (   
			  	title like CONCAT('%', #{searchKeyword}, '%') 
			        OR 
			          content like CONCAT('%', #{searchKeyword}, '%') 
			        OR 
			          writer like CONCAT('%', #{searchKeyword}, '%')
			      )
			</if>
		</if> 
		order by bno ASC
		limit #{startBno}, #{perPageNum} 
	</select>
```

```
<!-- 반복된는 조건절을 include 문으로 대체 -->
<sql id="sqlWhere">
	<if test="searchType != null" > 
		<if test="searchType == 'all'.toString()">
		  and (   
		  		title like CONCAT('%', #{searchKeyword}, '%') 
		        OR 
		          content like CONCAT('%', #{searchKeyword}, '%') 
		        OR 
		          writer like CONCAT('%', #{searchKeyword}, '%')
		      )
		</if>
	</if>
</sql>
<!-- include 사용법 -->
<include refid="sqlWhere"></include>
```

----
# #게시판 유효성검사 처리(상단교재 내용에서 'validator' 로 검색, 위에서 작업한 Controller 클래스에 @Valid를 추가하면 됩니다)
----
### 순서(게시판검색 예)
	1. pom.xml 디펜던시 추가
	2. 기존 pageVO에 @NotBlank(message="페이지번호가 공백 입니다."),@Range(min = 0) 추가,
	3. BoardVO 변수 수정에 따른 Controller클래스 변경,
	4. 뷰단에 에러처리에 해당되는 error_valid.jsp페이지 생성

### 소스코드(아래)

```
pom.xml 내용 추가
<!-- 유효성 검사 -->
<dependency>
  <groupId>javax.validation</groupId>
  <artifactId>validation-api</artifactId>
  <version>1.1.0.Final</version>
</dependency>
<dependency>
  <groupId>org.hibernate</groupId>
  <artifactId>hibernate-validator</artifactId>
  <version>5.4.2.Final</version>
</dependency>

```

```
BoardVO.java 내용 수정
@NotNull(message="회원아이디가 공백 입니다.")
@NotBlank(message="회원아이디가 공백 입니다.")
private String user_id;
```

```
Controller 내용 추가
@RequestMapping(value = "/admin/member/write", method = RequestMethod.POST)
	public String memberWrite(@Valid MemberVO memberVO, BindingResult result, Locale locale, RedirectAttributes rdat, Model model, HttpServletRequest request) throws Exception {
	if( result.hasErrors() ) {
		// 에러를 List로 저장
		List<ObjectError> list = result.getAllErrors();
		/*for( ObjectError error : list ) {
			System.out.println("=====디버그=====" + error);
		}*/
		model.addAttribute("exception", list);
		return "/admin/error_valid";
	}else{
		//이전페이지 링크저장 <p><a href='<c:out value="${prevPage}" />'>이전페이지로 돌아가기</a></p>
		String referrer = request.getHeader("Referer");
		request.getSession().setAttribute("prevPage", referrer);
	}
	...
```

```
//error_valid.jsp 추가 기존 ControllerAdvicedException.java 클래스를 이용하면 필요없음.

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="include/header.jsp" %>

  <!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <div class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6">
            <h1 class="m-0 text-dark">에러 페이지</h1>
          </div><!-- /.col -->
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item">에러명</li>
              <li class="breadcrumb-item active">${exception}</li>
            </ol>
          </div><!-- /.col -->
        </div><!-- /.row -->
      </div><!-- /.container-fluid -->
    </div>
    <!-- /.content-header -->
    <!-- Main content -->
    <div class="content">
    <p>에러 상세내역</p>
    <ul>
    <c:forEach items="${exception}" var="stack">
    	<li>${stack.toString()}</li>
    </c:forEach>
    
    
    </ul>
    </div>
    <!-- /.content -->
  </div>
  <!-- /.content-wrapper -->
<%@ include file="include/footer.jsp" %> 
```

```
//ControllerAdvicedException.java 클래스에서 이전페이지 저장하기 부분 추가

package org.edu.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ControllerAdviceException {
	private static final Logger logger = LoggerFactory.getLogger(ControllerAdviceException.class);
	@ExceptionHandler(Exception.class)
	public ModelAndView errorModelAndView(Exception ex, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		//모델앤뷰에서 셋뷰네임은 jsp파일명과 매칭
		modelAndView.setViewName("admin/error_controller");
		modelAndView.addObject("exception", ex);
		//에러시 이전페이지 이동 코딩시작
		HttpSession session = request.getSession();
		String redirectUrl = (String) session.getAttribute("prevPage");
		      if (redirectUrl != null) {
		          session.removeAttribute("prevPage");
		      }
		      logger.info("결과" + redirectUrl);
		      modelAndView.addObject("prevPage", redirectUrl);
		//에러시 이전페이지 이동 코딩끝
		return modelAndView;
	}
}

```

----
# #HSQLDB file 로 사용(기본은 메모리DB 이고, Mysql처럼 고정값을 가지도록 처리)
----
### root-context.xml 파일 내용 추가
```
기존 Hslq소스는 주석처리 후 아래 내용 추가
<!-- HSQLDB FILE 사용 헤로쿠에 올릴때 경로는 /tmp/embeded/edu.db , 로컬PC일때 경로는 c:/egov/workspace/embeded/edu.db  --> 
<bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource"> 
	<property name="driverClassName" value="org.hsqldb.jdbcDriver" />
	<property name="url" value="jdbc:hsqldb:file:c:/egov/workspace/embeded/edu.db" />
	<property name="username" value="sa" />
	<property name="password" value="" />
</bean> 
<!-- CREATE TABLE 초기 1회만 실행 -->
<jdbc:initialize-database data-source="dataSource" ignore-failures="DROPS"> 
	<jdbc:script location="classpath:/db/embeded_edu_dummy.sql" /> 
</jdbc:initialize-database>

embeded_edu_dummy.sql 내용은 기존 코드를 복사해서 수정 후 사용
drop table tbl_attach if exists
drop table tbl_reply if exists
drop table tbl_board if exists
drop table tbl_member if exists
CREATE MEMORY TABLE -> CREATE TABLE 로 변경
```

----
# #스프링 시큐리티는 설정파일 에서 부터 출발
----
### 모든 작업전 회원 가입시 암호화 적용
```
AdminController의 insertMember메서드에 아래 내용 추가
String new_pw = member.getUser_pw();
	if(new_pw != "") {
		//스프링 시큐리티 4.x BCryptPasswordEncoder 암호 사용
		BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder(10);
		String bcryptPassword = bcryptPasswordEncoder.encode(new_pw);
		member.setUser_pw(bcryptPassword);
	}else {
		return "redirect:/admin/member/listAll";
	}
```

### 소스코드(설정파일)
- https://github.com/miniplugin/springframework/blob/master/src/main/webapp/WEB-INF/web.xml
- https://github.com/miniplugin/springframework/blob/master/src/main/webapp/WEB-INF/spring/security-context.xml

### 로그인UI(우선 사용자 홈페이지 메인(header,footer분리)과 로그인.jsp 부터 구현)
- https://github.com/miniplugin/springframework/blob/master/src/main/webapp/WEB-INF/views/login.jsp
- 로그인 POST액션 /login 은 security-context.xml 에서 지정

### 소스코드(로그인세션저장)
```
Controller클래스내용
@RequestMapping(value = "/login_success", method = RequestMethod.GET)
	public String login_success(Locale locale,HttpServletRequest request, RedirectAttributes rdat) {
		logger.info("Welcome login_success! The client locale is {}.", locale);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = "";//anonymousUser
		String level = "";//ROLE_ANONYMOUS
		Boolean enabled = false;
		Object principal = authentication.getPrincipal();
		if (principal instanceof UserDetails) {
			enabled = ((UserDetails)principal).isEnabled();
		}
		HttpSession session = request.getSession();
		if (enabled) {
			Collection<? extends GrantedAuthority>  authorities = authentication.getAuthorities();
			if(authorities.stream().filter(o -> o.getAuthority().equals("ROLE_ANONYMOUS")).findAny().isPresent())
			{level = "ROLE_ANONYMOUS";}
			if(authorities.stream().filter(o -> o.getAuthority().equals("ROLE_USER,")).findAny().isPresent())
			{level = "ROLE_USER,";}
			if(authorities.stream().filter(o -> o.getAuthority().equals("ROLE_ADMIN")).findAny().isPresent())
			{level = "ROLE_ADMIN";}
			username =((UserDetails)principal).getUsername();
			//로그인 세션 저장
			session.setAttribute("session_enabled", enabled);//인증확인
			session.setAttribute("session_username", username);//사용자명
			session.setAttribute("session_level", level);//사용자권한
        	}
		rdat.addFlashAttribute("msg", "success");//result 데이터를 숨겨서 전송
		return "redirect:/";//새로고침 자동 등록 방지를 위해서 아래처럼 처리
	}
```

----
# #댓글(Ajax방식)달기(우선 상단교재 내용에서 'JSON 응답과 처리' 로 검색)
----
