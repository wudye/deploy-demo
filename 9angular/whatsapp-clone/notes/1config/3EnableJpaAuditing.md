@EnableJpaAuditing
作用: 启用 JPA 审计功能

设计意图:

自动管理实体的审计字段，如创建时间、修改时间、创建者、修改者等
减少样板代码，避免在每个实体类中手动设置这些字段

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Message {

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;
    
    // JPA 会自动填充这些字段
}

维度	Spring Data JPA 审计	Hibernate 时间戳
配置复杂度	中（需要 @EnableJpaAuditing）	低（无需配置）
性能	略慢（Spring AOP 拦截）	略快（直接回调）
功能完整性	高（支持用户审计）	低（仅时间戳）
可控性	高（可自定义）	低（固定行为）
适用场景	企业应用、需要审计追踪	简单应用、性能敏