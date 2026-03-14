package com.lidigu.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "sections")
public class Section {
    @Id
    @Column(name = "section_id")
    private String sectionId;

    @Column(name = "section_name", unique = true, nullable = false)
    private String sectionName;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "product_sections",
        joinColumns = @JoinColumn(name = "section_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;
}
