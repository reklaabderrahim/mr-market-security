package fr.mr_market.mr_market_security.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class FullBaseEntity extends BaseEntity {

    @UpdateTimestamp
    @Column(name = "update_date", columnDefinition = "DATE")
    private LocalDateTime updateDate;
}
