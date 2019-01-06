package com.sopt.dowadog.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sopt.dowadog.enumeration.MailboxTypeEnum;
import com.sopt.dowadog.model.domain.auditing.DateEntity;
import com.sopt.dowadog.model.dto.MailboxDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value={"hibernateLazyInitializer", "handler"})
public class Mailbox extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    private String title;
    private String detail;
    private String type;
    private boolean complete;

    @JsonIgnore
    @ManyToOne
    private User user;

    public MailboxDto getMailboxDto() {
        System.out.println("#########");
        System.out.println(type);
        System.out.println(MailboxTypeEnum.valueOf(type).getValue());
        return MailboxDto.builder()
                    .type(this.type)
                    .imgPath(MailboxTypeEnum.valueOf(type).getValue())
                    .detail(this.detail)
                    .title(this.title)
                    .complete(this.complete)
                    .build();
    }

}
