### Description
This project is a minimum implementation reproducing a bug in `spring-data-jpa:3.1.2`.

### How to
The project consists of several entity classes modeling a RDS relations. The main entity - `ChildClassEntity` is both inheriting from an abstract class - `SuperClassEntity` and has a relation to `RelatedEntity` via `RelationshipEntity`.

The problem only occurs when querying via the repository method `findAll(Specification, Pageable)` and is demonstrated in the test class `ChildClassRepositoryTest`. Said method generates an SQL count query where it is not taking in consideration the joined table from `SuperClassEntity` resulting in an invalid query and the following exception:
```
org.springframework.dao.InvalidDataAccessResourceUsageException: could not prepare statement [Column "C1_1.INHERITED_PROPERTY" not found; SQL statement:
select distinct count(distinct c1_0.id) from child_class c1_0 join relationship r1_0 on c1_0.id=r1_0.child_id join related r2_0 on r2_0.id=r1_0.related_id where lower(c1_1.inherited_property) like ? escape '' and lower(c1_0.own_property) like ? escape '' and r2_0.name in (?) [42122-200]] [select distinct count(distinct c1_0.id) from child_class c1_0 join relationship r1_0 on c1_0.id=r1_0.child_id join related r2_0 on r2_0.id=r1_0.related_id where lower(c1_1.inherited_property) like ? escape '' and lower(c1_0.own_property) like ? escape '' and r2_0.name in (?)]; SQL [select distinct count(distinct c1_0.id) from child_class c1_0 join relationship r1_0 on c1_0.id=r1_0.child_id join related r2_0 on r2_0.id=r1_0.related_id where lower(c1_1.inherited_property) like ? escape '' and lower(c1_0.own_property) like ? escape '' and r2_0.name in (?)]

	at org.springframework.orm.jpa.vendor.HibernateJpaDialect.convertHibernateAccessException(HibernateJpaDialect.java:256)
	at org.springframework.orm.jpa.vendor.HibernateJpaDialect.translateExceptionIfPossible(HibernateJpaDialect.java:229)
	at org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.translateExceptionIfPossible(AbstractEntityManagerFactoryBean.java:550)
	at org.springframework.dao.support.ChainedPersistenceExceptionTranslator.translateExceptionIfPossible(ChainedPersistenceExceptionTranslator.java:61)
	at org.springframework.dao.support.DataAccessUtils.translateIfNecessary(DataAccessUtils.java:242)
	at org.springframework.dao.support.PersistenceExceptionTranslationInterceptor.invoke(PersistenceExceptionTranslationInterceptor.java:152)
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:184)
	at org.springframework.data.jpa.repository.support.CrudMethodMetadataPostProcessor$CrudMethodMetadataPopulatingMethodInterceptor.invoke(CrudMethodMetadataPostProcessor.java:164)
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:184)
	at org.springframework.aop.interceptor.ExposeInvocationInterceptor.invoke(ExposeInvocationInterceptor.java:97)
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:184)
	at org.springframework.data.repository.core.support.MethodInvocationValidator.invoke(MethodInvocationValidator.java:94)
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:184)
	at org.springframework.aop.framework.JdkDynamicAopProxy.invoke(JdkDynamicAopProxy.java:244)
	at jdk.proxy2/jdk.proxy2.$Proxy112.findAll(Unknown Source)
	at kpentchev.bug.ChildClassRepositoryTest.test(ChildClassRepositoryTest.kt:59)
```