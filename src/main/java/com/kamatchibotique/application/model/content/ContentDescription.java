package com.kamatchibotique.application.model.content;

import com.kamatchibotique.application.constants.SchemaConstant;
import com.kamatchibotique.application.model.reference.language.Language;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CONTENT_DESCRIPTION", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "CONTENT_ID",
                "LANGUAGE_ID"
        })
})
@TableGenerator(name = "description_gen", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "content_description_seq", allocationSize = SchemaConstant.DESCRIPTION_ID_ALLOCATION_SIZE, initialValue = SchemaConstant.DESCRIPTION_ID_START_VALUE)
public class ContentDescription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "DESCRIPTION_ID")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "description_gen")
    private Long id;

    @ManyToOne(targetEntity = Content.class)
    @JoinColumn(name = "CONTENT_ID", nullable = false)
    private Content content;

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

    @Column(name = "SEF_URL", length = 120)
    private String seUrl;

    @Column(name = "META_KEYWORDS")
    private String metatagKeywords;

    @Column(name = "META_TITLE")
    private String metatagTitle;

    @Column(name = "META_DESCRIPTION")
    private String metatagDescription;

    public ContentDescription(String name, Language language) {
        this.setName(name);
        this.setLanguage(language);
        this.setId(0L);
    }
}
