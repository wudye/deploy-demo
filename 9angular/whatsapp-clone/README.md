# Virtual Thread
# postgresql and mysql application.yml config
# liquibase config
# common entity base class
    @EnableJpaAuditing in main class to enable JPA auditing features
    @EntityListeners(AuditingEntityListener.class) to enable auditing on the entitys
    @CreatedDate to automatically set the creation timestamp
    @LastModifiedDate to automatically set the last modified timestamp
    @CreatedBy to automatically set the creator of the entity
    @LastModifiedBy to automatically set the last modifier of the entity
    AuditingEntityListener.class to listen for entity lifecycle events and populate auditing fields
    @MappedSuperclass is used to define a base class for entities, allowing common fields to be inherited

# scheduled  task  

# cors configuration: servlet + spring security + mvc

# security filter chain with oauth2 resource server and jwt 

# KeycloakJwtAuthenticationConverter 
     put the role AuthenticationToken，Spring Security puts token in SecurityContext

# sse

# improve
 keycloak configuration