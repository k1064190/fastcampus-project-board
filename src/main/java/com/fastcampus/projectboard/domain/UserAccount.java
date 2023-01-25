package com.fastcampus.projectboard.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString(callSuper = true)
@Table(indexes = {
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Entity
public class UserAccount extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 유저 번호

    @Column(nullable = false, length = 50, unique = true)
    private String user_id; // 유저 아이디

    @Column(nullable = false, length = 255)
    private String user_password; // 유저 비밀번호

    @Column(length = 100)
    private String email;

    @Column(length = 100)
    private String nickname;

    @Column(length = 255)
    private String memo;

    protected UserAccount() {}

    public static UserAccount of(String user_id, String user_password, String email, String nickname, String memo) {
        return new UserAccount(user_id, user_password, email, nickname, memo);
    }

    private UserAccount(String user_id, String user_password, String email, String nickname, String memo) {
        this.user_id = user_id;
        this.user_password = user_password;
        this.email = email;
        this.nickname = nickname;
        this.memo = memo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccount userAccount)) return false;
        return id != null && id.equals(userAccount.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
