## [코드로 배우는 스프링 웹프로젝트] 책(이하 책으로 표기)<br> 스프링프레임웍(이하 스프링으로 표기)을 이용한 웹사이트 만들기 오라클용으로 변경.
***
사용된 스프링 버전: 4.3.16.RELEASE
스프링 라이선스는 Apache 2.0 라이선스를 따릅니다.[웹사이트](https://spring.io/)<br>
부트스트랩/AdminLTE/기타등등<br>책 내에서 사용된 외부 오픈소스의 경우 원 오픈소스의 라이선스 정책을 유지합니다.
[라이센스 보기](https://github.com/spring-projects/spring-framework/blob/master/src/docs/dist/license.txt)
***
>작업일자(아래): 20200430
### 기존 스프링 웹프로젝트를 오라클용으로 변경하고 았습니다.
기존 소스 정보:
- 소스: https://github.com/miniplugin/springframework
- 아래 DB변경은 다음파일에서 가능: src/main/webapp/WEB-INF/spring/root-context.xml
- Mysql용 확인URL: http://edu.paas-ta.org/
- Hsql용 확인URL: https://spring-edu.herokuapp.com/

### 개발환경.
- 개발PC에 오라클 11g EX 설치: https://www.oracle.com/database/technologies/oracle-database-software-downloads.html

- 개발PC에 오라클 SQL Development 설치: https://www.oracle.com/tools/downloads/sqldev-v192-downloads.html (JDK포함버전으로)

- 오라클 설치 후 8080포트변경: Get Started With Oracle Database 11g Express Edition 웹 툴에서 사용하는 포트와 톰캣서버가 충돌 때문에.

- SQL Development 에서 아래 명령어 실행 후 OK

  SELECT DBMS_XDB.GETHTTPPORT() FROM DUAL;
  EXEC DBMS_XDB.SETHTTPPORT(9000);
-(참고) 오라클 DB의 문자설정 확인: select * from v$nls_parameters where parameter like '%CHARACTERSET%';

- 작업소스에서 회원테이블의 level 필드가 오라클에서 예약어이기 때문에 필드명 변경처리 아래 예)

```
<security:jdbc-user-service
                data-source-ref="dataSource"
                users-by-username-query="select user_id as no, user_pw as password, enabled from tbl_member where user_id = ?"
                authorities-by-username-query="select user_id as no, levels as authority from tbl_member where user_id = ?"
            />
```

- 위 스프링 시큐리티 설정 외에도 쿼리 매퍼, VO  등 수정이 필요한 곳 모두 수정.
- 아래 DB변경은 다음파일에서 가능: src/main/webapp/WEB-INF/spring/root-context.xml (오라클용 추가)

```
    </bean>
    <bean class="org.springframework.jdbc.datasource.DriverManagerDataSource" id="dataSource">
        <property name="driverClassName" value="net.sf.log4jdbc.sql.jdbcapi.DriverSpy"/>
        <property name="url" value="jdbc:log4jdbc:oracle:thin:@localhost:1521/XE"/>
        <property name="username" value="XE"/>
        <property name="password" value="apmsetup"/>
    </bean>

```

- boardMapper.xml. memberMapper.xml, replyMapper.xml 의 쿼리를 Mysql|Hsql 용에서 오라클용으로 변경 아래 예)

```

 -- SQL Development 에서 페이징처리에 사용된 limit 명령을 사용할 수 없어서 대체용 으로 아래오 같은 쿼리가 실행 되도록 처리.
 
select * from ( select rownum rn, A.* from tbl_board A where bno > 0 AND rownum <= (0/10+1) * 10 ) where rn > 0/10 * 10;
select * from ( select rownum rn, A.* from tbl_board A where bno > 0 AND rownum <= (10/10+1) * 10 ) where rn > 10/10 * 10;

```

- 페이징 처리 수정(소스 수정 없이 쿼리만 수정)

```

<!--  Mysql 또는 Hsql 공용 쿼리
 <select id="listAll" resultType="BoardVO">
  <![CDATA[  
    select * 
    from tbl_board 
    where bno > 0 
  ]]>  
   <include refid="sqlWhere"></include>
   <![CDATA[    
    order by bno desc
    limit #{pageStart}, #{perPageNum}
  ]]>  
 </select>
 --> 


 <!-- 오라클용 쿼리 -->
 <select id="listAll" resultType="BoardVO">
  <![CDATA[  
    select 
       X.*
     from 
         (
         select
           rownum as rnum, A.* 
         from (
          select * from tbl_board where bno > 0
  ]]>
    <include refid="sqlWhere"></include>
    <![CDATA[
      order by bno desc
    ]]>
  <![CDATA[
          ) A
         where rownum <= (#{pageStart}/10+1) * #{perPageNum}
        ) X
    where x.rnum > (#{pageStart}/10) * #{perPageNum}   
  ]]>
 </select>

```

Ps. 개발환경 구성 ( 아래 sw는 교육용으로 사용하면 free no cost 라이센스임 )

- 라이센스 정보: https://m.blog.naver.com/hanajava/220824719322 

- https://www.oracle.com/downloads/licenses/sqldev-license.html﻿

- 이클립스에서 ojdbc6.jar 외부 라이브러리 등록: 파일은 오라클설치폴더/app/oracle/product/11.2.0/server/jdbc/lib/ojdbc6.jar
