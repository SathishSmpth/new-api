package com.kamatchibotique.application.model.common.description;

import com.kamatchibotique.application.model.common.Auditable;
import com.kamatchibotique.application.model.reference.language.Language;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Description extends Auditable<String> {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "DESCRIPTION_ID")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "description_gen")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "LANGUAGE_ID")
    private Language language;

    @NotEmpty
    @Column(name = "NAME", nullable = false, length = 120)
    private String name;

    @Column(name = "TITLE", length = 100)
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;

    public Description(Language language, String name) {
        this.setLanguage(language);
        this.setName(name);
    }
}
